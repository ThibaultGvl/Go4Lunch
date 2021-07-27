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
import com.example.go4lunch.viewmodel.users.UserCRUD;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Worker extends androidx.work.Worker {

    private final String mNotificationCanal = "password";

    private final String mNotificationName = "Time To Lunch !";

    String message;

    private final int mId = 26;

    public Worker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        UserCRUD.getUser(uid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                User currentUser = documentSnapshot.toObject(User.class);
                String username = Objects.requireNonNull(currentUser).getUsername();
                String restaurantName = currentUser.getRestaurantName();
                String restaurantAddress = currentUser.getRestaurantAddress();
                List<User> names = new ArrayList<>();
        UserCRUD.getUsers().addOnSuccessListener(documentsSnapshot -> {
            if (documentsSnapshot != null) {
                for (DocumentSnapshot documentSnapshot1 : documentsSnapshot) {
                    User user = documentSnapshot1.toObject(User.class);
                    if (Objects.requireNonNull(user).getRestaurant().equals(currentUser.getRestaurant())) {
                        names.add(user);
                    }
                }
            }
        });
                String users = names.toString();


                if (names.size() != 0) {
                    message = getApplicationContext().getString(R.string.notif_pt_1) + username +
                              getApplicationContext().getString(R.string.notif_pt_2) +
                              restaurantName +
                              getApplicationContext().getString(R.string.notif_pt_3) +
                              restaurantAddress +
                              getApplicationContext().getString(R.string.notif_pt_4) + users;
                }
                else if (currentUser.getRestaurant() == null ||
                        currentUser.getRestaurant().equals("")) {
                    message = getApplicationContext().getString(R.string.notif_pt_1) + username +
                            getApplicationContext().getString(R.string.message_restaurant_null);
                }
                else {
                    message = getApplicationContext().getString(R.string.notif_pt_1) + username +
                              getApplicationContext().getString(R.string.notif_pt_2) + restaurantName +
                              getApplicationContext().getString(R.string.notif_pt_3) + restaurantAddress;
                }
            }
            sendVisualNotification(message);
        });
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
