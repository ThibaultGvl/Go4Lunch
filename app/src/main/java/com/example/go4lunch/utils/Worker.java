package com.example.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.example.go4lunch.view.activity.ConnexionActivity;
import com.example.go4lunch.view.activity.MainActivity;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.TimeUnit;

public class Worker extends androidx.work.Worker {

    private final String mNotificationCanal = "password";

    private final String mNotificationName = "Time To Lunch !";

    private final int mId = 26;

    public Worker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String message = getInputData().getString("message");
        sendVisualNotification(message);
        return Result.success();
    }

    private void sendVisualNotification(String message) {

        Intent intent = new Intent(this.getApplicationContext(), ConnexionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), mId,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), mNotificationCanal)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch)
                        .setContentTitle
                                (this.getApplicationContext().getString(R.string.notification_name))
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) this.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(this.getApplicationContext()
                    .getString(R.string.notification_channel_id), mNotificationName,
                    importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(mNotificationName, mId, notificationBuilder.build());
        }
    }
}
