package com.xapps.selftest.infrastructure.client

import com.xapps.dto.FetchJobRequest
import com.xapps.dto.job.JobDTO
import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.dto.CreateSelfTestQuizRequest
import com.xapps.selftest.infrastructure.client.http_client.HttpClientFactory
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.springframework.stereotype.Component

@Component
class QuestionGeneratorClient {

    private val client = HttpClientFactory.getClient()

    suspend fun generateQuestions(setup: CreateSelfTestQuizRequest): JobDTO =
        post("/generate_self_test_questions", setup)

    suspend fun fetchJob(jobId: JobId): JobDTO =
        post("/job", FetchJobRequest(jobId))

    private suspend inline fun <reified R> post(path: String, body: Any): R {
        val response = client.post("$BASE_URL$path") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        if (response.status.value !in 200..299) {
            throw IllegalStateException("Question generator returned ${response.status}")
        }

        return response.body<R>()
    }

    private companion object {
        private const val BASE_URL = "http://localhost:8083/api/question_generator"
    }
}
