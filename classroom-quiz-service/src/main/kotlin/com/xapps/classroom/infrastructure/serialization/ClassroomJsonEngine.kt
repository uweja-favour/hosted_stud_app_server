@file: OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)

package com.xapps.classroom.infrastructure.serialization

import com.xapps.classroom.domain.model.ClassroomSessionState
import com.xapps.classroom.domain.model.participant.question.ParticipantFibQuestion
import com.xapps.classroom.domain.model.participant.question.ParticipantMcQuestion
import com.xapps.classroom.domain.model.participant.question.ParticipantMsQuestion
import com.xapps.classroom.domain.model.participant.question.ParticipantQuestion
import com.xapps.classroom.domain.model.participant.question.ParticipantTfQuestion
import com.xapps.messaging.kafka.events.ClassroomQuizStateRefreshEvent
import com.xapps.platform.serialization_core.BaseJsonEngine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.time.ExperimentalTime

object ClassroomJsonEngine {

    val module =
        SerializersModule {

            polymorphic(ClassroomSessionState::class) {
                subclass(ClassroomSessionState.PreLobby::class)
                subclass(ClassroomSessionState.Lobby::class)
                subclass(ClassroomSessionState.Ongoing::class)
                subclass(ClassroomSessionState.SubmissionOpen::class)
                subclass(ClassroomSessionState.Closed::class)
            }

            polymorphic(ParticipantQuestion::class) {
                subclass(ParticipantMcQuestion::class)
                subclass(ParticipantMsQuestion::class)
                subclass(ParticipantTfQuestion::class)
                subclass(ParticipantFibQuestion::class)
            }

            // register other contextual serializers as needed via startup registration
        }

    val json: Json =
        Json {
            serializersModule = module + BaseJsonEngine.baseModule

            // LENIENT + RESILIENT settings
            ignoreUnknownKeys = true        // extra fields won't fail decoding
            isLenient = true                // accept slightly malformed JSON (e.g., single quotes)
            coerceInputValues = true        // missing/nullable values can be coerced rather than failing
            allowSpecialFloatingPointValues = true
            encodeDefaults = false
            prettyPrint = false
            explicitNulls = false
            classDiscriminator = "type"
        }


    inline fun <reified T> safeDecode(
        jsonString: String,
        deserializer: KSerializer<T>,
        context: String = "JsonEngine.decode"
    ): T? {
        return try {
            json.decodeFromString(deserializer, jsonString)
        } catch (ex: Throwable) {
            JsonLog.error(context, "safeDecode failed for ${T::class.simpleName}", "payload" to JsonLog.truncate(jsonString), cause = ex)
            null
        }
    }

    /**
     * Safe encode → logs and returns "{}" on failure.
     */
    inline fun <reified T> safeEncode(
        data: T,
        serializer: KSerializer<T>,
        context: String = "JsonEngine.encode"
    ): String {
        return try {
            json.encodeToString(serializer, data)
        } catch (ex: Exception) {
            JsonLog.error("JsonEncode", "Failed to encode ${T::class.simpleName}. Context=$context", cause = ex)
            "{}"
        }
    }
}
