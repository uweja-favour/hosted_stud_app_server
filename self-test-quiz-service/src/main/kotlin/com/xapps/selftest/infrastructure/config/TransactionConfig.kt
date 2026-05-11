//package com.xapps.selftest.infrastructure.config
//
//import io.r2dbc.spi.ConnectionFactory
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.transaction.ReactiveTransactionManager
//import org.springframework.transaction.reactive.TransactionalOperator
//import org.springframework.r2dbc.connection.R2dbcTransactionManager
//
//@Configuration
//class TransactionConfig(
//    private val connectionFactory: ConnectionFactory
//) {
//
//    // Transaction manager
//    @Bean
//    fun reactiveTransactionManager(): ReactiveTransactionManager =
//        R2dbcTransactionManager(connectionFactory)
//
//    // Transactional operator for coroutine-friendly transactions
//    @Bean
//    fun transactionalOperator(txManager: ReactiveTransactionManager): TransactionalOperator =
//        TransactionalOperator.create(txManager)
//}
