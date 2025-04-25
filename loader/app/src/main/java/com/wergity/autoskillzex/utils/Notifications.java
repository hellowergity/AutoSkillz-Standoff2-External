package com.wergity.autoskillzex.utils;

import static com.wergity.autoskillzex.App.PERMISSION_REQUEST_CODE;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.wergity.autoskillzex.R;

public class Notifications {
    private static final String NAME        = "AutoSkillzEx";
    private static final String DESCRIPTION = "I fuck your ass :(";

    private static final int CODE = 0x1337;
    private static final int ICON = R.mipmap.ic_launcher;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static boolean hasNotificationPermission(Activity activity) {
        int result = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static void requestNotificationPermission(Activity activity) {
        String[] permissions = new String[] {
            android.Manifest.permission.POST_NOTIFICATIONS
        };

        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
    }

    public static boolean GrantPermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || hasNotificationPermission(activity)) {
            return true;
        }

        requestNotificationPermission(activity);
        return false;
    }

    public static void Create(Service service) {
        NotificationManager notificationManager = service.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            NotificationChannel channel = notificationManager.getNotificationChannel(NAME);

            if (channel == null) {
                channel = new NotificationChannel(NAME, NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(DESCRIPTION);

                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification.Builder builder = new Notification.Builder(service, NAME);
        builder.setSmallIcon(ICON);
        builder.setContentTitle(NAME);
        builder.setContentText(DESCRIPTION);
        builder.setWhen(System.currentTimeMillis());

        Notification notification = builder.build();
        service.startForeground(CODE, notification);
    }
}