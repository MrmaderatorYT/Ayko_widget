package com.ccs.ayko;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private static final int NOTIFICATION_LISTENER_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity created");

        // Перевірка та запит дозволу на відображення поверх інших програм
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Log.d(TAG, "Requesting overlay permission");
                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(overlayIntent, OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                Log.d(TAG, "Overlay permission already granted");
                checkAndStartNotificationListener();
            }
        } else {
            Log.d(TAG, "Overlay permission not required, starting FloatingService");
            startFloatingService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Log.d(TAG, "Overlay permission granted");
                    checkAndStartNotificationListener();
                } else {
                    Log.d(TAG, "Overlay permission not granted");
                }
            }
        } else if (requestCode == NOTIFICATION_LISTENER_REQUEST_CODE) {
            Log.d(TAG, "Notification listener permission granted");
            startFloatingService();
        }
    }

    private void checkAndStartNotificationListener() {
        if (!isNotificationServiceEnabled()) {
            Log.d(TAG, "Requesting notification listener permission");
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivityForResult(intent, NOTIFICATION_LISTENER_REQUEST_CODE);
        } else {
            Log.d(TAG, "Notification listener permission already granted");
            startFloatingService();
        }
    }

    private boolean isNotificationServiceEnabled() {
        String enabledNotificationListeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    private void startFloatingService() {
        Log.d(TAG, "Starting FloatingService");
        Intent serviceIntent = new Intent(this, FloatingService.class);
        startService(serviceIntent);
        finish(); // Закриваємо актівіті
    }
}