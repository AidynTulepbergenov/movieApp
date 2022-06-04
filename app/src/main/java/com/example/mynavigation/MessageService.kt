package com.example.mynavigation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.mynavigation.presentation.view.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "MovieApp"
const val channelName = "MyProject"

class MessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FIREBASE_TOKEN", "Refreshed token: $token")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            showMessage(message)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String?, content: String?): RemoteViews {
        val remoteViews = RemoteViews("com.example.mynavigation", R.layout.notification)
        remoteViews.setTextViewText(R.id.notification_title, title)
        remoteViews.setTextViewText(R.id.notification_body, content)
        remoteViews.setImageViewResource(R.id.logo, R.drawable.ic_log)
        return remoteViews
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showMessage(message: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val data: Map<String, String> = message.data
        val title = data["title"]
        val content = data["content"]

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_log)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(500, 200, 500, 100))
                .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, content))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }
}