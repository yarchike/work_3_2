package com.yarchike.work_3_1.service


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yarchike.work_3_1.NotifictionHelper

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val recipientId = message.data["recipientId"]
        val title =message.data["title"]

        println(recipientId)
        println(title)
        if (title != null) {
            if (recipientId != null) {
                NotifictionHelper.testNotific(this, title, recipientId)
            }
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }
}