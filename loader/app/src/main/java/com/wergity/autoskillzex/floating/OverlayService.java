package com.wergity.autoskillzex.floating;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.wergity.autoskillzex.natives.ESPView;
import com.wergity.autoskillzex.utils.Notifications;

import java.lang.ref.WeakReference;

public class OverlayService extends Service {
    private static WeakReference<OverlayService> weakReference;

    private ESPView espView;
    private WindowManager windowManager;

    public static void ForceStop() {
        if (weakReference == null) {
            return;
        }

        OverlayService overlayService = weakReference.get();
        if (overlayService != null) {
            overlayService.stopForeground(true);
            overlayService.stopSelf();
        }
    }

    @NonNull
    private WindowManager.LayoutParams getLayoutParams() {
        int flags = FLAG_NOT_FOCUSABLE
                | FLAG_LAYOUT_NO_LIMITS
                | FLAG_NOT_TOUCH_MODAL
                | FLAG_NOT_TOUCHABLE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT,
                TYPE_APPLICATION_OVERLAY,
                flags,
                PixelFormat.TRANSLUCENT
        );

        params.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        params.packageName = getPackageName();

        return params;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notifications.Create(this);
        weakReference = new WeakReference<>(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        espView = new ESPView(this);
        windowManager.addView(espView, getLayoutParams());

        startForegroundService(new Intent(this, MenuService.class));
    }

    @Override
    public void onDestroy() {
        if (espView != null) {
            if (!espView.thread.isInterrupted()) {
                espView.thread.interrupt();
            }

            windowManager.removeView(espView);
            espView = null;
        }

        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
}