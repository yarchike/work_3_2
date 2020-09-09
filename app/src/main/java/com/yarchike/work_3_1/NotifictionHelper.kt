package com.yarchike.work_3_1
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yarchike.work_3_1.dto.AttachmentType

import java.util.*

object NotifictionHelper {
    private val UPLOAD_CHANEL_ID = "upload_chanel_id"
    private var channelCreated = false
    private var lastNotificationId: Int? = null

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Загрузка медиа"
            val descriptionText = "Notifies when media upload during post creation"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(UPLOAD_CHANEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun comeBackNotification(context: Context) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createBuilder(
                context,
                "Понравилось ли вам у нас?",
                "Дорогой пользователь, возвращайтесь к нам скорее",
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            createBuilder(
                context,
                "Понравилось ли вам у нас?",
                "Дорогой пользователь, возвращайтесь к нам скорее"
            )
        }
        showNotification(context, builder)

    }

    fun mediaUploaded(type: AttachmentType, context: Context) {
        createNotificationChannelIfNotCreated(context)
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createBuilder(
                context,
                "Медиа загружено",
                "Ваш ${type.name.toLowerCase()} успешно загружен.",
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            createBuilder(
                context,
                "Медиа загружено",
                "Ваш ${type.name.toLowerCase()} успешно загружен."
            )
        }
        showNotification(context, builder)
    }
    fun testNotific(context: Context, title: String, content: String){
        val builder = createBuilder(context = context, title = title, content = content, priority = NotificationManager.IMPORTANCE_HIGH)
        showNotification(context, builder)
    }

    private fun showNotification(
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random().nextInt(100000)
            lastNotificationId = notificationId
            notify(notificationId, builder.build())
        }
    }

    @TargetApi(24)
    private fun createBuilder(
        context: Context,
        title: String,
        content: String,
        priority: Int
    ): NotificationCompat.Builder {
        val builder = createBuilder(context, title, content)
        builder.priority = priority
        return builder
    }

    private fun createBuilder(
        context: Context,
        title: String,
        content: String
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, UPLOAD_CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder
    }

    private fun createNotificationChannelIfNotCreated(context: Context) {
        if (!channelCreated) {
            createNotificationChannel(context)
            channelCreated = true
        }
    }
}
