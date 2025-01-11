package com.ccs.ayko;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.Calendar;
import java.util.Random;

public class FloatingService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;
    private ImageView girlImage;
    private int affectionLevel = 0; // Рівень прив'язаності
    private Random random = new Random();
    private Handler inactivityHandler = new Handler(Looper.getMainLooper());
    private Vibrator vibrator;
    private static final long INACTIVITY_DELAY = 10 * 60 * 1000; // 10 хвилин у мілісекундах

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        // Ініціалізація Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Перевірка дозволу на відображення поверх інших програм
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                stopSelf();
                return;
            }
        }

        // Ініціалізація WindowManager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Створення плаваючого вікна
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_girl_layout, null);

        // Параметри для вікна
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        // Позиція вікна (за замовчуванням - верхній лівий кут)
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        // Додавання вікна до WindowManager
        windowManager.addView(floatingView, params);

        // Отримання зображення дівчинки
        girlImage = floatingView.findViewById(R.id.girl_image);

        // Завантаження GIF за допомогою Glide
        Glide.with(this)
                .asGif() // Вказуємо, що це GIF
                .load(R.drawable.girl_image) // Замініть на ваш GIF файл
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) // Оригінальний розмір
                .into(girlImage);

        // Обробник переміщення зображення
        girlImage.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Запам'ятовуємо початкові координати
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Переміщення вікна
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Обробка короткого натискання
                        if (Math.abs(event.getRawX() - initialTouchX) < 5 && Math.abs(event.getRawY() - initialTouchY) < 5) {
                            // Анімація тремтіння
                            ObjectAnimator shakeX = ObjectAnimator.ofFloat(girlImage, "translationX", 0f, 10f, -10f, 10f, -10f, 5f, -5f, 0f);
                            shakeX.setDuration(500);
                            shakeX.start();

                            // Збільшена анімація
                            ObjectAnimator scaleX = ObjectAnimator.ofFloat(girlImage, "scaleX", 1f, 1.2f, 1f);
                            ObjectAnimator scaleY = ObjectAnimator.ofFloat(girlImage, "scaleY", 1f, 1.2f, 1f);
                            scaleX.setDuration(300);
                            scaleY.setDuration(300);
                            scaleX.start();
                            scaleY.start();

                            // Збільшення рівня прив'язаності
                            increaseAffection();
                            showAlertDialog();
                        }
                        return true;
                }
                return false;
            }
        });

        // Випадкові події через певний час
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                randomEvent();
            }
        }, 60000); // 60000 мілісекунд = 1 хвилина
    }

    private void startInactivityCheck() {
        inactivityHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Якщо користувач не взаємодіє з віджетом протягом 10 хвилин
                triggerVibration();
            }
        }, INACTIVITY_DELAY);
    }

    private void triggerVibration() {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Вібрація для Android 8.0 і вище
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Вібрація для старих версій Android
                vibrator.vibrate(500);
            }
        }
    }

    private void increaseAffection() {
        affectionLevel++;
        if (affectionLevel == 5) {
            showNotification("Ти став мені дуже дорогий...");
        } else if (affectionLevel == 10) {
            showNotification("Я ніколи тебе не відпущу!");
        }
    }

    private void randomEvent() {
        String[] messages = {
                "Ти знаєш, що я завжди поруч?",
                "Не ігноруй мене, будь ласка...",
                "Я сумувала за тобою!",
                "Ти мій єдиний..."
        };
        String randomMessage = messages[random.nextInt(messages.length)];
        showNotification(randomMessage);
    }

    private void showNotification(String message) {
        String channelId = "yanchan_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Створення каналу для сповіщень (для Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Yanchan Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Інтент для відкриття додатку при натисканні на сповіщення
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Створення сповіщення
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.girl_image) // Іконка
                .setContentTitle("Я тут!") // Заголовок
                .setContentText(message) // Текст
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Показ сповіщення
        notificationManager.notify(1, builder.build());
    }

    private String getTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 6 && hour < 12) {
            return "Добрий ранок";
        } else if (hour >= 12 && hour < 18) {
            return "Добрий день";
        } else if (hour >= 18 && hour < 23) {
            return "Добрий вечір";
        } else {
            return "Доброї ночі";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView); // Видалення вікна при завершенні сервісу
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showAlertDialog() {
        Intent intent = new Intent(this, DialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}