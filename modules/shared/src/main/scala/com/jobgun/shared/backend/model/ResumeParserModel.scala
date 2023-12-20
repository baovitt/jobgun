package com.jobgun.shared.backend
package model

// Tapir Imports:
import sttp.tapir.TapirFile

// PDFBox Imports:
import org.apache.pdfbox.preflight.parser.PreflightParser
import org.apache.pdfbox.text.PDFTextStripper

// POI Imports:
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.extractor.XWPFWordExtractor

// ZIO Imports:
import zio.{ZIO, IO}

object ResumeParserModel:

  def parsePDF(resume: TapirFile): IO[Throwable, String] = ZIO.attempt {
    val parser = new PreflightParser(resume)
    val text = PDFTextStripper().getText(parser.parse)
    text
  }

  def parseWord(resume: TapirFile): IO[Throwable, String] = ZIO.attempt {
    val doc = new XWPFDocument(OPCPackage.open(resume))
    XWPFWordExtractor(doc).getText
  }

end ResumeParserModel
