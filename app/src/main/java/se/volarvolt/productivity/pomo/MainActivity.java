package se.volarvolt.productivity.pomo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMinutes;
    private Button buttonStart;
    private Button buttonStop;
    private TextView textViewCountdown;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMinutes = findViewById(R.id.editTextMinutes);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        textViewCountdown = findViewById(R.id.textViewCountdown);

        mediaPlayer = MediaPlayer.create(this, R.raw.gong_sound); // Put your gong sound file in the raw folder

        buttonStart.setOnClickListener(v -> {
            long minutes = Integer.parseInt(editTextMinutes.getText().toString());
            startTimer(minutes * 60000); // Convert minutes to milliseconds
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
                mediaPlayer.start();
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
}
