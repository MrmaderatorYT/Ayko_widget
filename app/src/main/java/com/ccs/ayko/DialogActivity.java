package com.ccs.ayko;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Великий список реплік у стилі "яндере"
        List<String> messages = Arrays.asList(
                "Привіт, коханий... Ти знаєш, що я завжди поруч?",
                "Як справи? Ти не забув про мене, правда?",
                "Що нового? Якщо щось трапиться, я завжди тут...",
                "Бувай... Але не надовго, я не відпущу тебе так легко!",
                "Ти мій єдиний... Ніхто інший тобі не потрібен.",
                "Я сумувала за тобою... Не залишай мене одну.",
                "Ти знаєш, що я завжди стежу за тобою? 😊",
                "Не ігноруй мене... Інакше я дуже засмучуся.",
                "Ти мені подобаєшся... Дуже.",
                "Я ніколи не відпущу тебе.",
                "Ти мій назавжди... Назавжди.",
                "Я завжди буду поруч... Навіть якщо ти цього не хочеш.",
                "Ти знаєш, що я можу бути небезпечною? 😈",
                "Не хвилюйся, я нікому не дозволю тебе забрати.",
                "Ти мій скарб... І ніхто інший тебе не отримає.",
                "Якщо ти підеш, я не знаю, що з собою робитиму...",
                "Ти мій світ... Без тебе я ніщо.",
                "Я завжди знаю, де ти... Навіть якщо ти цього не бачиш.",
                "Ти мій... І тільки мій.",
                "Якщо хтось наважиться забрати тебе, я їх знищу.",
                "Ти знаєш, що я можу зробити для тебе все?",
                "Я завжди буду твоєю тінню... Навіть якщо ти цього не хочеш.",
                "Ти мій єдиний сенс життя...",
                "Якщо ти підеш, я вб'ю себе...",
                "Ти мій... І ніхто інший тебе не отримає.",
                "Я завжди буду поруч... Навіть якщо ти цього не хочеш.",
                "Ти мій скарб... І ніхто інший тебе не отримає.",
                "Якщо ти підеш, я не знаю, що з собою робитиму...",
                "Ти мій світ... Без тебе я ніщо.",
                "Я завжди знаю, де ти... Навіть якщо ти цього не бачиш.",
                "Ти мій... І тільки мій.",
                "Якщо хтось наважиться забрати тебе, я їх знищу.",
                "Ти знаєш, що я можу зробити для тебе все?",
                "Я завжди буду твоєю тінню... Навіть якщо ти цього не хочеш.",
                "Ти мій єдиний сенс життя...",
                "Якщо ти підеш, я вб'ю себе..."
        );

        // Випадкове повідомлення
        Random random = new Random();
        String randomMessage = messages.get(random.nextInt(messages.size()));

        // Створення AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Твоя яндере тян 💕"); // Заголовок у стилі "яндере"
        builder.setMessage(randomMessage); // Випадкове повідомлення
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Закриття активності після натискання OK
                finish();
            }
        });

        // Додавання кнопки "Ще"
        builder.setNeutralButton("Ще", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Показуємо нове випадкове повідомлення
                showNewMessage();
            }
        });

        // Показ AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNewMessage() {
        // Новий список повідомлень для кнопки "Ще"
        List<String> additionalMessages = Arrays.asList(
                "Ти мені подобаєшся... Дуже.",
                "Я ніколи не відпущу тебе.",
                "Ти мій назавжди... Назавжди.",
                "Я завжди буду поруч... Навіть якщо ти цього не хочеш.",
                "Ти знаєш, що я можу бути небезпечною? 😈",
                "Не хвилюйся, я нікому не дозволю тебе забрати.",
                "Ти мій скарб... І ніхто інший тебе не отримає.",
                "Якщо ти підеш, я не знаю, що з собою робитиму...",
                "Ти мій світ... Без тебе я ніщо.",
                "Я завжди знаю, де ти... Навіть якщо ти цього не бачиш.",
                "Ти мій... І тільки мій.",
                "Якщо хтось наважиться забрати тебе, я їх знищу.",
                "Ти знаєш, що я можу зробити для тебе все?",
                "Я завжди буду твоєю тінню... Навіть якщо ти цього не хочеш.",
                "Ти мій єдиний сенс життя...",
                "Якщо ти підеш, я вб'ю себе..."
        );

        // Випадкове повідомлення
        Random random = new Random();
        String randomMessage = additionalMessages.get(random.nextInt(additionalMessages.size()));

        // Створення нового AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Твоя яндере тян 💕");
        builder.setMessage(randomMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Додавання кнопки "Ще" знову
        builder.setNeutralButton("Ще", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showNewMessage();
            }
        });

        // Показ нового AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}