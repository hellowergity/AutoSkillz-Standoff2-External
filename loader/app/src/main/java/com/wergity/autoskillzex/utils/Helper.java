package com.wergity.autoskillzex.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.wergity.autoskillzex.App;

public class Helper {
    public static void ShowDialog(Activity activity, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("OK", listener);
        builder.setCancelable(false);

        builder.setTitle("AutoSkillzEx");
        builder.setMessage(message);

        builder.show();
    }

    public static void OpenURL(@NonNull Activity activity, String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public static void Run(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void RunWithDelay(Runnable runnable, long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delay);
    }

    public static void RunNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static boolean IsAnyGameInstalled(@NonNull Activity activity) {
        PackageManager packageManager = activity.getPackageManager();

        for (App.Info info : App.SupportedGames) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(info.packageName, PackageManager.GET_META_DATA);
                String installedVersion = packageInfo.versionName;

                if (installedVersion != null && installedVersion.equals(info.versionName)) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Logcat.Warning("Package not found: %s.", nameNotFoundException.getMessage());
            }
        }

        return false;
    }
}
