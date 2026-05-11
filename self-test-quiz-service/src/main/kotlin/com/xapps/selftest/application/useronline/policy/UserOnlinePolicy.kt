package com.xapps.selftest.application.useronline.policy

interface UserOnlinePolicy {
    suspend fun execute(userId: String)
}