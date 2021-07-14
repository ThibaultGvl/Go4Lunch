package com.example.go4lunch.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.example.go4lunch.view.activity.ConnexionActivity;
import com.example.go4lunch.view.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.valueOf;

public class NotificationsService extends FirebaseMessagingService {

    private final String mNotificationCanal = "password";

    private final String mNotificationName = "Time To Lunch !";

    private final int mId = 26;

    private final User mUser = MainActivity.getUserInformations();

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            this.sendVisualNotification(notification);
        }
    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {

        Intent intent = new Intent(this, ConnexionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, mId,
                intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = mNotificationCanal;
        String message = "Hello" + mUser.getUsername() + " it's Time to Lunch you'll eat at " +
                mUser.getRestaurantName() + " at " + mUser.getRestaurantAddress() + "with"
                + mUser.getRestaurant();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch)
                        .setContentTitle(notification.getTitle())
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = mNotificationName;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.notification_channel_id), channelName,
                    importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(mNotificationName, mId, notificationBuilder.build());
        }
    }
}
