package com.pw.parent.data

import kotlinx.coroutines.flow.Flow

class ParentAppRepository(private val parentAppDao: ParentAppDao) {

    val allOfflineDrafts: Flow<List<OfflineDraft>> = parentAppDao.getAllOfflineDrafts()
    val allNotifications: Flow<List<NotificationItem>> = parentAppDao.getAllNotifications()

    suspend fun saveOfflineDraft(content: String, recipient: String = "General") {
        parentAppDao.insertOfflineDraft(OfflineDraft(content = content, recipient = recipient))
    }

    suspend fun deleteOfflineDraft(draft: OfflineDraft) {
        parentAppDao.deleteOfflineDraft(draft)
    }

    suspend fun clearAllDrafts() {
        parentAppDao.clearAllDrafts()
    }

    suspend fun saveNotification(title: String, message: String) {
        parentAppDao.insertNotification(NotificationItem(title = title, message = message))
    }

    suspend fun markNotificationAsRead(id: Int) {
        parentAppDao.markNotificationAsRead(id)
    }

    suspend fun deleteNotification(id: Int) {
        parentAppDao.deleteNotification(id)
    }

    suspend fun clearAllNotifications() {
        parentAppDao.clearAllNotifications()
    }
}
