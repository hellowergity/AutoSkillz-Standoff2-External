package com.wergity.autoskillzex.natives;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.topjohnwu.superuser.Shell;
import com.wergity.autoskillzex.App;
import com.wergity.autoskillzex.floating.OverlayService;
import com.wergity.autoskillzex.fragments.StatusFragment;
import com.wergity.autoskillzex.utils.Helper;
import com.wergity.autoskillzex.utils.Logcat;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class Daemon {
    public static native void signal(ESPView a, Canvas b);
    public static native void signal(int a, int b, int c);

    static {
        System.loadLibrary("Cat");
    }

    @NonNull
    private static String getDeviceName() {
        String MANUFACTURER = Build.MANUFACTURER;
        String MODEL = Build.MODEL;

        if (MODEL.startsWith(MANUFACTURER)) {
            return MANUFACTURER + MODEL.replace(MANUFACTURER, "");
        }

        return MANUFACTURER + " " + MODEL;
    }

    private static void StartGame() {
        String packageName = App.SelectedGameInfo.packageName;

        PackageManager pm = App.instance.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
            App.instance.startActivity(intent);
        }
    }

    public static void ShowError() {
        Helper.Run(() -> {
            Toast.makeText(App.instance, "Injection failed, check logcat.", Toast.LENGTH_LONG).show();
            WeakReference<StatusFragment> weakReference = StatusFragment.weakReference;
            if (weakReference == null) {
                return;
            }

            StatusFragment statusFragment = weakReference.get();
            if (statusFragment != null) {
                statusFragment.stopCheats();
            }
        });
    }

    public static void Stop() {
        Shell.cmd("killall " + App.SelectedGameInfo.libraryName).exec();
    }

    private static void Start() {
        Stop();
        Helper.RunWithDelay(Daemon::StartGame, 500);

        File file = new File(App.instance.getApplicationInfo().nativeLibraryDir, App.SelectedGameInfo.libraryName);
        if (!file.exists()) {
            Logcat.Error("%s not found.", file);
            ShowError();
            return;
        }

        String path = file.getAbsolutePath();
        if (!Shell.cmd("chmod 777 " + path).exec().isSuccess()) {
            Logcat.Error("Failed set \"executable\" rights to daemon.");
            ShowError();
            return;
        }

        Shell.cmd(String.format(Locale.ENGLISH, "nohup %s &", path)).submit(out -> {
            if (!out.isSuccess()) {
                ShowError();
            }
        });
    }

    public static void Start(@NonNull Activity activity) {
        Logcat.Info("[ ====== New injection for %s ====== ]", App.SelectedGameInfo.packageName);

        Logcat.Info("Device: %s", getDeviceName());
        Logcat.Info("SDK: %s", Build.VERSION.SDK_INT);
        Logcat.Info("ARCH: %s", System.getProperty("os.arch"));

        Helper.RunNewThread(Daemon::Start);
        activity.startForegroundService(new Intent(activity, OverlayService.class));
    }
}