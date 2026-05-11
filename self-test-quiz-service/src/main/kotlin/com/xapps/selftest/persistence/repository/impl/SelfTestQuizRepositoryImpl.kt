package com.xapps.selftest.persistence.repository.impl

import com.xapps.selftest.domain.model.SelfTestQuiz
import com.xapps.selftest.domain.repository.SelfTestQuizRepository
import com.xapps.selftest.persistence.mapper.SelfTestQuizMapper
import com.xapps.selftest.persistence.repository.SelfTestQuizMongoRepository
import com.xapps.selftest.persistence.saveAllUpserting
import com.xapps.selftest.persistence.saveUpserting
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class SelfTestQuizRepositoryImpl(
    private val mongo: SelfTestQuizMongoRepository,
    private val mapper: SelfTestQuizMapper
): SelfTestQuizRepository {

    override suspend fun save(quiz: SelfTestQuiz): SelfTestQuiz {
        mongo.saveUpserting(mapper.toDocument(quiz))
        return quiz
    }

    override suspend fun saveAll(quizzes: List<SelfTestQuiz>): List<SelfTestQuiz> {
        mongo.saveAllUpserting(quizzes.map { mapper.toDocument(it) })
        return quizzes
    }

    override suspend fun findById(id: String): SelfTestQuiz? {
        return mongo.findById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun findAll(): List<SelfTestQuiz> {
        return mongo.findAll().toList().map { mapper.toDomain(it) }
    }

    override suspend fun deleteById(id: String) {
        mongo.deleteById(id)
    }

    override suspend fun delete(quiz: SelfTestQuiz) {
        mongo.deleteById(quiz.id)
    }

    override suspend fun deleteAll() {
        mongo.deleteAll()
    }
}