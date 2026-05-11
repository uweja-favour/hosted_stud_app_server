package com.xapps.classroom.application.useronline

import com.xapps.classroom.application.useronline.policy.UserOnlinePolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.stereotype.Component

@Component
class UserOnlineOrchestrator(
    private val policies: List<UserOnlinePolicy>
) {

    suspend fun handleUserOnline(userId: String) = supervisorScope {
        for (policy in policies) {
            launch(Dispatchers.IO) { policy.execute(userId) }
        }
    }
}