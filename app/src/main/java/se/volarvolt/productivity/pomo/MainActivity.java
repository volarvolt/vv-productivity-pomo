package se.volarvolt.productivity.pomo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMinutes;
    private Button buttonStart;
    private Button buttonStop;
    private TextView textViewCountdown;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMinutes = findViewById(R.id.editTextMinutes);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        textViewCountdown = findViewById(R.id.textViewCountdown);

        buttonStart.setOnClickListener(v -> {
            try {
                long minutes = Integer.parseInt(editTextMinutes.getText().toString());
                startTimer(minutes * 60000); // Convert minutes to milliseconds
            } catch (NumberFormatException nfe) {
                // nothing;
            }
        });

        buttonStop.setOnClickListener(v -> stopTimer());
    }

    private void startTimer(long milliseconds) {
        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                updateCountdown(secondsRemaining);
            }

            @Override
            public void onFinish() {
                stopTimer();
                emitNotification();
            }
        };

        countDownTimer.start();
        disableControls();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            enableControls();
            updateCountdown(0); // Reset countdown display
        }
    }

    private void updateCountdown(long secondsRemaining) {
        textViewCountdown.setText(String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
    }

    private void disableControls() {
        editTextMinutes.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
    }

    private void enableControls() {
        editTextMinutes.setEnabled(true);
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
    }

    private void emitNotification() {
        // Create a notification channel
        NotificationChannel channel = new NotificationChannel("default", "Timer Notifications", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.setLightColor(Color.MAGENTA);

        // Get the notification manager
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        // Create the notification channel
        notificationManager.createNotificationChannel(channel);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                .setContentTitle("Timer Expired")
                .setContentText("Your timer has expired.")
                .setSmallIcon(R.drawable.ic_notification) // Icon for the notification
                .setColor(Color.MAGENTA) // Notification color
                .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority
                .setAutoCancel(true); // Dismiss notification when tapped

        // Show the notification
        notificationManager.notify(0, builder.build());
    }

    private void setVersion(String version) {
        TextView textViewVersion = findViewById(R.id.textViewVersion);
        textViewVersion.setText(version);
    }
}
