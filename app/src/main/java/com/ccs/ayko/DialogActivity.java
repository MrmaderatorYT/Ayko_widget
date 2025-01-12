package com.ccs.ayko;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

public class DialogActivity extends AppCompatActivity {

    private static final String TAG = "DialogActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DialogActivity created");

        // Показуємо випадкове повідомлення
        showRandomMessage();
    }

    private void showRandomMessage() {
        List<String> messages = DialogPresets.getRandomMessages();
        String randomMessage = messages.get(new Random().nextInt(messages.size()));
        Log.d(TAG, "Showing random message: " + randomMessage);

        // Створення AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Твоя яндере тян 💕");
        builder.setMessage(randomMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "OK button clicked");
                finish();
            }
        });

        // Додавання кнопки "Ще"
        builder.setNeutralButton("Ще", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Ще button clicked");
                showRandomMessage();
            }
        });

        // Показ AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}