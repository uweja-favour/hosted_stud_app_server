package com.xapps.selftest.domain.repository

import com.xapps.selftest.domain.model.SelfTestQuiz

interface SelfTestQuizRepository {

    suspend fun save(quiz: SelfTestQuiz): SelfTestQuiz

    suspend fun saveAll(quizzes: List<SelfTestQuiz>): List<SelfTestQuiz>

    suspend fun findById(id: String): SelfTestQuiz?

    suspend fun findAll(): List<SelfTestQuiz>

    suspend fun deleteById(id: String)

    suspend fun delete(quiz: SelfTestQuiz)

    suspend fun deleteAll()
}
