package com.lamdah.medicinereminder;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FullScreenAlarmActivity extends Activity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_alarm);

        TextView pillNameTextView = findViewById(R.id.alarm_pill_name);
        Button stopAlarmButton = findViewById(R.id.alarm_stop_button);

        String pillName = getIntent().getStringExtra("pillName");
        pillNameTextView.setText("Time to take " + pillName);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mediaPlayer = MediaPlayer.create(this, notification); // Replace R.raw.alarm_sound with the desired ringtone
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        stopAlarmButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
