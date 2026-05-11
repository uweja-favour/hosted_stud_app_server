package com.xapps.notification.repository

import com.xapps.studentapplication.entity.NotificationEntity
import org.springframework.data.jpa.repository.CoroutineCrudRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface NotificationRepository : CoroutineCrudRepository<NotificationEntity, Long> {

    @Query("SELECT n FROM NotificationEntity n WHERE n.userId = :userId AND n.read = false ORDER BY n.timestamp DESC")
    fun findUnreadByUserIdOrderByTimestampDesc(@Param("userId") userId: String): List<NotificationEntity>

    @Transactional
    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.userId = :userId")
    fun deleteAllUserNotifications(userId: String)

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.read = true WHERE n.userId = :userId AND n.id IN :ids")
    fun markAllAsRead(@Param("userId") userId: String, @Param("ids") ids: List<Long>): Int

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.read = true WHERE n.id = :notificationId AND n.userId = :userId")
    fun markAsRead(@Param("notificationId") notificationId: Long, @Param("userId") userId: String): Int

}