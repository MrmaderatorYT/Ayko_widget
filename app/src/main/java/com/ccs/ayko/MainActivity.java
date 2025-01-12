package com.ccs.ayko;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private static final int ACCESSIBILITY_PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Перевірка та запит дозволу на відображення поверх інших програм
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasOverlayPermission()) {
                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(overlayIntent, OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                checkAndStartAccessibilityService();
            }
        } else {
            startFloatingService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (hasOverlayPermission()) {
                    checkAndStartAccessibilityService();
                } else {
                    // Дозвіл не надано
                }
            }
        } else if (requestCode == ACCESSIBILITY_PERMISSION_REQUEST_CODE) {
            if (hasAccessibilityPermission()) {
                startFloatingService();
            } else {
                // Дозвіл не надано
            }
        }
    }

    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true; // Для версій нижче Android 6.0 дозвіл не потрібен
    }

    private boolean hasAccessibilityPermission() {
        String accessibilityService = getPackageName() + "/.MyAccessibilityService";
        int accessibilityEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        if (accessibilityEnabled == 1) {
            String enabledServices = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            return enabledServices != null && enabledServices.contains(accessibilityService);
        }
        return false;
    }

    private void checkAndStartAccessibilityService() {
        if (hasAccessibilityPermission()) {
            startFloatingService();
        } else {
            // Запит на увімкнення AccessibilityService
            Intent accessibilityIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(accessibilityIntent, ACCESSIBILITY_PERMISSION_REQUEST_CODE);
        }
    }

    private void startFloatingService() {
        Intent serviceIntent = new Intent(this, FloatingService.class);
        startService(serviceIntent);
        finish(); // Закриваємо актівіті
    }
}