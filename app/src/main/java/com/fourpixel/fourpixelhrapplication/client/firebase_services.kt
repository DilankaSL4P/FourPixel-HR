package com.fourpixel.fourpixelhrapplication.client

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import android.app.Application

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    // Called when a new FCM registration token is generated
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    // Called when a message is received
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")


        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Process the data payload here.
            // For "logged in successful" or "leave applied" type messages,
            // you'll typically find your custom data here.
            val messageType = remoteMessage.data["type"]
            val messageContent = remoteMessage.data["content"] // e.g., "Login successful!"

            Log.d(TAG, "Message type: $messageType, content: $messageContent")

            // Display the in-app notification on the UI thread
            // Important: Services run on a background thread, UI updates need the main thread.
            Handler(Looper.getMainLooper()).post {
                if (messageContent != null && applicationContext != null) {
                    Toast.makeText(
                        applicationContext,
                        messageContent,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            // You could also send a local broadcast here to notify active Activities/Fragments
            // if you want more complex in-app UI updates (e.g., a Snackbar or Dialog).
            // For a Toast, the above is sufficient.

        }

        // Check if message contains a notification payload.
        // If you send a "notification" message from FCM console, this block runs.
        // For "in-app" messages, we usually prefer data messages.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // If your app is in the foreground and you send a notification message,
            // you might want to display it differently here, e.g., as a custom in-app banner.
            // Otherwise, Android handles it by default.
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app's backend server.
        // Your backend needs this token to send targeted push notifications to this specific device.
        // Example: make an API call to your server with the token.
        Log.d(TAG, "sendRegistrationToServer($token)")
    }
}