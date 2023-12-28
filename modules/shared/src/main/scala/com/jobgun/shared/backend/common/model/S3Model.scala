package com.jobgun.shared.backend
package common.model

// ZIO Imports:
import zio.*
import zio.stream.{ZSink, ZStream}
import zio.connect.s3.S3Connector
import zio.connect.s3.S3Connector.{CopyObject, MoveObject}
import zio.aws.core.AwsError
import zio.aws.s3.S3
import zio.aws.s3.model.*
import zio.aws.s3.model.primitives.*
import zio.aws.s3.model.DeleteObjectsResponse.ReadOnly
import zio.aws.s3.model.primitives.{BucketName, ObjectKey}

// Java Imports:
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.UUID

// STTP Imports:
import sttp.tapir.TapirFile

final case class S3Model(s3: S3):
  import S3Model.*

  def putObject(bucket: BucketName, key: ObjectKey, contentType: String)(using
      trace: Trace
  ): ZSink[Any, AwsError, Byte, Nothing, Unit] =
    ZSink
      .foreachChunk[Any, AwsError, Byte] { content =>
        s3.putObject(
          request = PutObjectRequest(
            bucket = bucket,
            key = key,
            contentLength = Some(ContentLength(content.length.toLong)),
            contentType = Some(ContentType(contentType)),
            grantRead = Some(GrantRead("public-read"))
          ),
          body = ZStream.fromChunk(content).rechunk(1024)
        )
      }

  def deleteObject(bucket: BucketName, objectKey: ObjectKey)(using
      trace: Trace
  ): IO[AwsError, ReadOnly] =
    s3.deleteObjects(
      DeleteObjectsRequest(
        bucket = bucket,
        delete = Delete(List(ObjectIdentifier(objectKey)))
      )
    )

  def putFile(
      resume: TapirFile,
      fileLocation: FileLocation
  ): ZIO[Any, AwsError, Unit] =
    val contentType = fileLocation.fileExtension match
      case "pdf"  => "application/pdf"
      case "docx" => "application/msword"

    val fullFileName =
      s"${fileLocation.fileName.toString()}.${fileLocation.fileExtension}"

    ZStream.fromFile(resume, 1024 * 1024 * 25).orDie >>> putObject(
      fileLocation.bucket,
      fileLocation.folder.objectKey(fullFileName),
      contentType
    )

end S3Model

object S3Model:
  val layer: ZLayer[S3, Nothing, S3Model] =
    ZLayer.fromZIO(ZIO.service[S3].map(S3Model(_)))

  final val resumesBucket = BucketName("jobgun-resumes")

  sealed trait S3Folder:
    val folderName: String
    def objectKey(fileName: String): ObjectKey =
      ObjectKey(s"$folderName/$fileName")
  end S3Folder

  object S3Folder:
    case object ProfileFolder extends S3Folder:
      override val folderName = "profile"

    case object RecruiterFolder extends S3Folder:
      override val folderName = "recruit"
  end S3Folder

  final case class FileLocation(
      bucket: BucketName,
      fileName: UUID,
      fileExtension: String,
      folder: S3Folder
  )

  object FileLocation:
    def applyZIO(
        bucket: BucketName,
        fileExtension: String,
        folder: S3Folder
    ): Task[FileLocation] =
      for resumeType <- ZIO.attempt {
          fileExtension match
            case e: ("pdf" | "docx") => e
        }
      yield FileLocation(bucket, UUID.randomUUID(), resumeType, folder)
  end FileLocation
end S3Model
