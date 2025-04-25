package com.wergity.autoskillzex.utils;

import android.util.Log;

import com.topjohnwu.superuser.Shell;

import java.util.Locale;

public class Logcat {
    public static final String TAG = "KittenWare";

    public static String Read() {
        Shell.Result result = Shell.cmd("logcat -d -t 1000 -s \"" + Logcat.TAG + "\"").exec();
        if (result.isSuccess()) {
            return String.join("\n", result.getOut());
        }

        return "Failed to read logs...";
    }

    public static void Clear() {
        Shell.cmd("logcat -c").submit();
    }

    public static void Info(String fmt, Object... args) {
        Log.i(TAG, String.format(Locale.ENGLISH, fmt, args));
    }

    public static void Warning(String fmt, Object... args) {
        Log.w(TAG, String.format(Locale.ENGLISH, fmt, args));
    }

    public static void Error(String fmt, Object... args) {
        Log.e(TAG, String.format(Locale.ENGLISH, fmt, args));
    }
}
