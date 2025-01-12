package com.ccs.ayko;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.content.Intent;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName(); // Отримуємо ім'я пакета додатка
        String notificationText = sbn.getNotification().tickerText != null ? sbn.getNotification().tickerText.toString() : "";

        // Перевіряємо, чи це повідомлення з месенджера
        if (packageName.contains("whatsapp") || packageName.contains("telegram") || packageName.contains("messenger")) {
            String message = "Хто це тобі пише? Мені не подобається...";
            showReaction(message);
        }
    }

    private void showReaction(String message) {
        Intent intent = new Intent(this, FloatingService.class);
        intent.putExtra("message", message);
        startService(intent);
    }
}