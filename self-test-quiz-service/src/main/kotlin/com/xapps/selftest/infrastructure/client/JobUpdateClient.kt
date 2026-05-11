package com.xapps.selftest.infrastructure.client

import com.xapps.dto.SseJobSubscriptionRequest
import com.xapps.dto.SseJobUpdateDto
import com.xapps.selftest.infrastructure.client.http_client.HttpClientFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.serialization.json.Json
import org.springframework.http.MediaType
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Component
class JobUpdateClient(
    private val webClient: WebClient,
    private val json: Json
) {

    val webClient2 = WebClient.builder()
        .exchangeStrategies(
            ExchangeStrategies.builder()
                .codecs { configurer ->
                    configurer.defaultCodecs().kotlinSerializationJsonDecoder(
                        KotlinSerializationJsonDecoder(json)
                    )
                }
                .build()
        )
        .baseUrl("http://localhost:8083/api/question_generator")
        .build()

    val hc = HttpClientFactory.getClient()

    suspend fun streamUpdates(
        subscription: SseJobSubscriptionRequest,
        authHeader: String?
    ): Flow<SseJobUpdateDto> {

        val request = webClient2.post()
            .uri("$BASE_URL/stream")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(subscription)

        authHeader?.let {
            request.header("Authorization", it)
        }

        return request
            .retrieve()
            .bodyToFlux(SseJobUpdateDto::class.java)
            .asFlow()
    }

    private companion object {
        const val BASE_URL =
            "http://localhost:8083/api/question_generator"
    }
}
