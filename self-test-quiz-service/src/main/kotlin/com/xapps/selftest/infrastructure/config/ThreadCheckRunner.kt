package com.xapps.selftest.infrastructure.config

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ThreadCheckRunner : CommandLineRunner {
    private val logger = LoggerFactory.getLogger("VIRTUAL_THREAD_MONITOR")

    override fun run(vararg args: String) {
        val threads = Thread.getAllStackTraces().keys
        val vthreads = threads.count { it.isVirtual }
        val total = threads.size
        logger.info("🧵 Total threads: $total, Virtual threads: $vthreads")
    }
}