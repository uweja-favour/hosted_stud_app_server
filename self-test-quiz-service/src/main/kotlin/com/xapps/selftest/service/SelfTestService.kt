//package com.xapps.selftest.service
//
//import com.xapps.platform.core.string.generateUniqueId
//import com.xapps.questions.contracts.dto.EmptyResponse
//import com.xapps.questions.contracts.dto.JobDto
//import com.xapps.questions.contracts.dto.JobStatus
//import com.xapps.questions.contracts.dto.JobTask
//import com.xapps.questions.contracts.events_.QuestionsRequestedEvent
//import com.xapps.questions.contracts.events_.QuizDeliveredEvent
//import com.xapps.questions.contracts.self_test_generation.dto.SelfTestQuestionDto
//import com.xapps.questions.contracts.model.QuizId
//import com.xapps.questions.contracts.model.QuizType
//import com.xapps.questions.contracts.question_generation.JobId
//import com.xapps.questions.contracts.self_test_generation.dto.SelfTestSetupDto
//import com.xapps.selftest.core.service.BaseService
//import com.xapps.selftest.domain.factory.SelfTestQuizFactory
//import com.xapps.selftest.domain.factory.UserPendingQuizFactory
//import com.xapps.selftest.domain.model.PendingQuizStatus
//import com.xapps.selftest.domain.model.SelfTestQuiz
//import com.xapps.selftest.domain.repository.SelfTestQuizRepository
//import com.xapps.selftest.domain.repository.UserPendingQuizRepository
//import com.xapps.selftest.events_.QuestionsRequestedEventPublisher
//import com.xapps.selftest.events_.QuizDeliveredEventPublisher
//import kotlinx.coroutines.flow.toList
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.stereotype.Service
//
//@Service
//class SelfTestService(
//    private val selfTestQuizRepository: SelfTestQuizRepository,
//    private val pendingQuizRepository: UserPendingQuizRepository,
//    private val questionsRequestedEventPublisher: QuestionsRequestedEventPublisher,
//    private val selfTestQuizFactory: SelfTestQuizFactory,
//    private val userPendingQuizFactory: UserPendingQuizFactory,
//    private val quizDeliveredEventPublisher: QuizDeliveredEventPublisher
//) : BaseService() {
//
//    override val logger: Logger = LoggerFactory.getLogger(javaClass)
//
//    suspend fun requestQuestions(
//        userId: String,
//        setup: SelfTestSetupDto
//    ): JobDto {
//        val jobId = JobId.of(generateUniqueId())
//        val event = QuestionsRequestedEvent(
//            jobId = jobId,
//            userId = userId,
//            title = setup.title,
//            subject = setup.subject,
//            questionCount = setup.questionCount,
//            allocations = setup.allocations,
//            files = setup.files
//        )
//
//        questionsRequestedEventPublisher.publishQuestionsRequested(event)
//
//        return JobDto(
//            jobId = jobId,
//            status = JobStatus.Queued,
//            task = JobTask.SELF_TEST
//        )
//    }
//
//    suspend fun completeQuestionsGeneration(
//        userId: String,
//        quizId: QuizId,
//        title: String,
//        subject: String,
//        questions: List<SelfTestQuestionDto>
//    ) {
//        val quiz = selfTestQuizFactory.create(
//            quizId = quizId,
//            subject = subject,
//            title = title,
//            questions = questions
//        )
//
//        val pendingQuiz = userPendingQuizFactory.create(
//            userId = userId,
//            quizId = quizId
//        )
//
//        selfTestQuizRepository.save(quiz)
//        pendingQuizRepository.save(pendingQuiz)
//    }
//
//    suspend fun handleUserOnlineEvent(
//        userId: String
//    ) {
//        val pendingQuizzesIds = pendingQuizRepository.findAllByUserIdAndStatus(
//            userId = userId,
//            PendingQuizStatus.PENDING
//        )
//        .toList()
//        .map { it.quizId }
//
//        val event = QuizDeliveredEvent(
//            userId = userId,
//            quizIds = pendingQuizzesIds,
//            quizType = QuizType.SELF_TEST
//        )
//
//        quizDeliveredEventPublisher.publishQuizDelivered(event)
//    }
//
//    suspend fun fetchQuizById(quizId: QuizId): SelfTestQuiz =
//        selfTestQuizRepository.findById(quizId) ?: error("Cannot find quiz with ID=$quizId")
//
//    suspend fun acknowledgePendingQuiz(quizId: QuizId): EmptyResponse {
//        return EmptyResponse()
//    }
//}
