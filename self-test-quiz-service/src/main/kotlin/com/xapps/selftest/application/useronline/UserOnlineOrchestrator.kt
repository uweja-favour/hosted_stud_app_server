package com.xapps.selftest.application.useronline

import com.xapps.selftest.application.useronline.policy.UserOnlinePolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class UserOnlineOrchestrator(
    private val policies: List<UserOnlinePolicy>
) {

    suspend fun handleUserOnline(userId: String) = coroutineScope {
        for (policy in policies) {
            launch {
                policy.execute(userId)
            }
        }
    }
}