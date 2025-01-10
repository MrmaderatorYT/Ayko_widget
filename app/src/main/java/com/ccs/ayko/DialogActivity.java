package com.ccs.ayko;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Список повідомлень
        List<String> messages = Arrays.asList("Привіт!", "Як справи?", "Що нового?", "Бувай!");

        // Створення AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Повідомлення");
        builder.setItems(messages.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Обробка вибору повідомлення
                String selectedMessage = messages.get(which);
                // Тут можна додати дії при виборі повідомлення
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Закриття активності після натискання OK
                finish();
            }
        });

        // Показ AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}