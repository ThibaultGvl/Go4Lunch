package com.example.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.example.go4lunch.view.activity.ConnexionActivity;
import com.example.go4lunch.view.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class NotificationsService extends FirebaseMessagingService {

    private final int mNotificationId = 26;

    private User mUser = new User("093094", "Name", "email", "picture", "restaurant", null, "Chez oim", "3 chemin de l'hélianthe");

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            this.sendVisualNotification(remoteMessage.getNotification());
            Log.e("TAG", String.valueOf(remoteMessage.getNotification()));
        }
    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {

        Intent intent = new Intent(this, ConnexionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 26, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "password";
        String message = "You'll eat at" + mUser.getRestaurantName() + " which is at" + mUser.getRestaurantAddress() + " with" + "Workmates";

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch)
                        .setContentTitle(notification.getTitle())
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Time To Lunch !";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(String.valueOf(R.string.app_name), mNotificationId, notificationBuilder.build());
    }
}
