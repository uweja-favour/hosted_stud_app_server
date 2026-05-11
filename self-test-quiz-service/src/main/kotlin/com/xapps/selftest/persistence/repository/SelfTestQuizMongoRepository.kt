package com.xapps.selftest.persistence.repository

import com.xapps.selftest.persistence.entity.SelfTestQuizDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.Query

@Repository
interface SelfTestQuizMongoRepository :
    CoroutineCrudRepository<SelfTestQuizDocument, String> {

    suspend fun findBySubject(subject: String): List<SelfTestQuizDocument>

    suspend fun findBySubjectAndTopic(subject: String, topic: String): List<SelfTestQuizDocument>

    suspend fun findByTitleContainingIgnoreCase(title: String): List<SelfTestQuizDocument>

    suspend fun findByAttemptsIsNotEmpty(): List<SelfTestQuizDocument>

    @Query($$"{ 'attempts.state.type': { $ne: 'COMPLETED' } }")
    suspend fun findWithUnfinishedAttempts(): List<SelfTestQuizDocument>

    @Query("{ 'attempts.state.type': 'ON_GOING' }")
    suspend fun findWithOnGoingAttempts(): List<SelfTestQuizDocument>
}