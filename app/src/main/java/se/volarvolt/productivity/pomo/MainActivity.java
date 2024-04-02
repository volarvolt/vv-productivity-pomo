package se.volarvolt.productivity.pomo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    private Button[] buttonStart;
    private Button buttonStop;
    private TextView textViewCountdown;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = new Button[6];
        buttonStart[0] = findViewById(R.id.button5min);
        buttonStart[1] = findViewById(R.id.button10min);
        buttonStart[2] = findViewById(R.id.button15min);
        buttonStart[3] = findViewById(R.id.button20min);
        buttonStart[4] = findViewById(R.id.button25min);
        buttonStart[5] = findViewById(R.id.button30min);
        buttonStop = findViewById(R.id.buttonStop);
        textViewCountdown = findViewById(R.id.textViewCountdown);

        buttonStop.setOnClickListener(v -> stopTimer());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Clear notifications when the app is brought to the foreground
        NotificationManagerCompat.from(this).cancelAll(); // Clear all notifications
    }

    public void startTimer(View view) {
        // Retrieve the argument from the clicked button's tag
        String tag = view.getTag().toString();
        long minutes = Long.parseLong(tag);

        // Call the startTimer method with the retrieved argument
        startTimer(minutes * 60000); // Convert minutes to milliseconds
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
        for (Button button : buttonStart) {
            button.setEnabled(false);
        }
        buttonStop.setEnabled(true);
    }

    private void enableControls() {
        for (Button button : buttonStart) {
            button.setEnabled(true);
        }
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
