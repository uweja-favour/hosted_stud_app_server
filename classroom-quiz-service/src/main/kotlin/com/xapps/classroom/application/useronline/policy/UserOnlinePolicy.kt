package com.xapps.classroom.application.useronline.policy

interface UserOnlinePolicy {
    suspend fun execute(userId: String)
}