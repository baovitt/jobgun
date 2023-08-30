package com.jobgun.shared.model.api

import com.jobgun.shared.domain.requests.JobRequest
import com.jobgun.shared.domain.responses.JobResponse
import com.jobgun.shared.utils.LRUCache

import sttp.model.StatusCode

import zio.IO

trait JobsApi:
    def searchJobsWithResume(
        request: JobRequest.JobSearchWithResumeRequest
    ): IO[StatusCode, JobResponse.JobSearchFromResumeResponse]

    def searchJobsWithEmbedding(
        request: JobRequest.JobSearchWithEmbeddingRequest
    ): IO[StatusCode, JobResponse.JobSearchFromEmbeddingResponse]
end JobsApi