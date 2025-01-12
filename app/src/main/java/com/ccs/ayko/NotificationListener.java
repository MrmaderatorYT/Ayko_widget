package com.ccs.ayko;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.content.Intent;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName(); // Отримуємо ім'я пакета додатка
        String notificationText = sbn.getNotification().tickerText != null ? sbn.getNotification().tickerText.toString() : "";

        Log.d(TAG, "Notification received from: " + packageName);
        Log.d(TAG, "Notification text: " + notificationText);

        // Перевіряємо, чи це повідомлення з месенджера
        if (packageName.contains("whatsapp") || packageName.contains("telegram") || packageName.contains("messenger")) {
            String message = "Хто це тобі пише? Мені не подобається...";
            Log.d(TAG, "Triggering reaction for message: " + message);
            showReaction(message);
        }
    }

    private void showReaction(String message) {
        Intent intent = new Intent(this, FloatingService.class);
        intent.putExtra("message", message);
        startService(intent);
    }
}