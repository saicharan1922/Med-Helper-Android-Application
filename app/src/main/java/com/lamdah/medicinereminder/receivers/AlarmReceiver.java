package com.lamdah.medicinereminder.receivers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lamdah.medicinereminder.FullScreenAlarmActivity;
import com.lamdah.medicinereminder.R;

public class AlarmReceiver extends BroadcastReceiver {

    private String TAG = "RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        String pillName = intent.getStringExtra("pillName");
        int pillId = intent.getIntExtra("pillId", -1);
        Intent i = new Intent(context, FullScreenAlarmActivity.class);
        i.putExtra("pillName", pillName);
        i.putExtra("pillId", pillId);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

}
