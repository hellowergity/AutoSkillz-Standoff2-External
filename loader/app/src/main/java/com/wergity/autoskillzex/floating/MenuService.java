package com.wergity.autoskillzex.floating;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.tabs.TabLayout;
import com.wergity.autoskillzex.R;
import com.wergity.autoskillzex.categories.AimCategory;
import com.wergity.autoskillzex.categories.SettingsCategory;
import com.wergity.autoskillzex.categories.VisualsCategory;
import com.wergity.autoskillzex.fragments.StatusFragment;
import com.wergity.autoskillzex.utils.Notifications;

import java.lang.ref.WeakReference;

public class MenuService extends Service {
    private LinearLayout iconLayout;
    private Animation iconAnimation;
    private AppCompatImageView iconImage;
    private WindowManager.LayoutParams iconParams;

    private FrameLayout closeLayout;
    private AppCompatImageView closeImage;

    private LinearLayout menuLayout;
    private TabLayout menuTabLayout;
    private ScrollView menuScrollView;
    private LinearLayout menuFunctionList;

    private boolean isIconAnimation;
    private WindowManager windowManager;
    private static WeakReference<MenuService> weakReference;

    public static void ForceStop() {
        if (weakReference == null) {
            return;
        }

        MenuService menuService = weakReference.get();
        if (menuService != null) {
            menuService.stopForeground(true);
            menuService.stopSelf();
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    private void setupIcon() {
        LayoutInflater inflater = LayoutInflater.from(this);
        iconLayout = (LinearLayout) inflater.inflate(R.layout.icon_floating_view, null);
        iconImage = iconLayout.findViewById(R.id.icon_floating);
        iconLayout.setOnTouchListener(new IconTouchListener());

        closeLayout = (FrameLayout) inflater.inflate(R.layout.close_floating_view, null);
        closeImage = closeLayout.findViewById(R.id.close_icon);
        closeLayout.setVisibility(View.GONE);

        iconParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        iconParams.gravity = Gravity.TOP | Gravity.START;
        iconParams.x = 50;
        iconParams.y = 100;

        setupIconAnimation();
        windowManager.addView(iconLayout, iconParams);
    }

    private void setupIconAnimation() {
        iconAnimation = AnimationUtils.loadAnimation(this, R.anim.open_icon_anim);
        iconAnimation.setInterpolator(input -> (float) (Math.pow(2.718281828459045d, ((double) (-input)) / 0.2d) * -1.0d * Math.cos(20.0d * ((double) input)) + 1.0d)); // WTF?

        iconAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                isIconAnimation = false;

                if (menuLayout.getVisibility() != View.VISIBLE) {
                    menuLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                isIconAnimation = true;
            }

            @Override
            public void onAnimationStart(Animation animation) {
                isIconAnimation = true;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void setupMenu() {
        LayoutInflater inflater = LayoutInflater.from(this);
        menuLayout = (LinearLayout) inflater.inflate(R.layout.menu_floating_view, null);
        menuLayout.setVisibility(View.GONE);

        menuTabLayout = menuLayout.findViewById(R.id.menu_floating_tabs);
        menuScrollView = menuLayout.findViewById(R.id.menu_floating_scrollview);
        menuFunctionList = menuLayout.findViewById(R.id.menu_floating_function_list);

        AppCompatButton closeButton = menuLayout.findViewById(R.id.menu_floating_close);
        closeButton.setOnClickListener(view -> menuLayout.setVisibility(View.GONE));

        setupMenuTabs();

        WindowManager.LayoutParams menuParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        menuParams.packageName = getPackageName();
        windowManager.addView(closeLayout, menuParams);
        windowManager.addView(menuLayout, menuParams);
    }

    private void setupMenuTabs() {
        AimCategory aimCategory = new AimCategory(menuTabLayout);
        aimCategory.create();

        VisualsCategory visualsCategory = new VisualsCategory(menuTabLayout);
        visualsCategory.create();

        SettingsCategory settingsCategory = new SettingsCategory(menuTabLayout);
        settingsCategory.setIcon(iconImage);
        settingsCategory.create();

        menuTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == aimCategory.tab) {
                    menuFunctionList.addView(aimCategory.layout);
                } else if (tab == visualsCategory.tab) {
                    menuFunctionList.addView(visualsCategory.layout);
                } else if (tab == settingsCategory.tab) {
                    menuFunctionList.addView(settingsCategory.layout);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                menuFunctionList.removeAllViews();
                menuScrollView.scrollTo(0, 0);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        menuFunctionList.addView(aimCategory.layout);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notifications.Create(this);
        weakReference = new WeakReference<>(this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        setupIcon();
        setupMenu();
    }

    @Override
    public void onDestroy() {
        if (iconLayout != null) {
            windowManager.removeView(iconLayout);
        }

        if (closeLayout != null) {
            windowManager.removeView(closeLayout);
        }

        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private final class IconTouchListener implements View.OnTouchListener {
        private int paramsX;
        private int paramsY;

        private float initialTouchX;
        private float initialTouchY;

        private final int[] iconPosition = { 0, 0 };
        private final int[] closePosition = { 0, 0 };

        private boolean isOpenMenu(@NonNull MotionEvent motionEvent) {
            if (Math.abs(initialTouchX - motionEvent.getRawX()) >= 10) {
                return false;
            }

            if (Math.abs(initialTouchY - motionEvent.getRawY()) >= 10) {
                return false;
            }

            if (menuLayout.getVisibility() == View.VISIBLE) {
                return false;
            }

            return !isIconAnimation;
        }

        private boolean isIconOverCloseButton() {
            int[] iconPosition = new int[2];
            iconLayout.getLocationOnScreen(iconPosition);

            int[] closePosition = new int[2];
            closeImage.getLocationOnScreen(closePosition);

            float iconWidth = iconLayout.getWidth();
            float iconHeight = iconLayout.getHeight();

            return iconPosition[0] >= closePosition[0] - iconWidth &&
                    iconPosition[0] <= closePosition[0] + iconWidth &&
                    iconPosition[1] >= closePosition[1] - iconHeight &&
                    iconPosition[1] <= closePosition[1] + iconHeight;
        }

        private void vibrate() {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }

        private void forceStop() {
            WeakReference<StatusFragment> weakReference = StatusFragment.weakReference;
            if (weakReference == null) {
                return;
            }

            StatusFragment statusFragment = weakReference.get();
            if (statusFragment != null) {
                statusFragment.stopCheats();
            }
        }

        private void handleActionDown(@NonNull MotionEvent motionEvent) {
            paramsX = iconParams.x;
            paramsY = iconParams.y;

            initialTouchX = motionEvent.getRawX();
            initialTouchY = motionEvent.getRawY();
        }

        private void handleActionUp(MotionEvent motionEvent) {
            if (isOpenMenu(motionEvent)) {
                iconImage.startAnimation(iconAnimation);
            }

            if (isIconOverCloseButton()) {
                forceStop();
                vibrate();
            }

            closeLayout.setVisibility(View.GONE);
        }

        private void handleActionMove(@NonNull MotionEvent motionEvent) {
            iconParams.x = paramsX + ((int) (motionEvent.getRawX() - initialTouchX));
            iconParams.y = paramsY + ((int) (motionEvent.getRawY() - initialTouchY));
            windowManager.updateViewLayout(iconLayout, iconParams);

            iconLayout.getLocationOnScreen(iconPosition);
            closeImage.getLocationOnScreen(closePosition);

            if (iconLayout.getVisibility() == View.VISIBLE) {
                if ((float) (motionEvent.getEventTime() - motionEvent.getDownTime()) >= 100.0f) {
                    closeLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
            return switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN -> {
                    handleActionDown(motionEvent);
                    yield true;
                }

                case MotionEvent.ACTION_UP -> {
                    handleActionUp(motionEvent);
                    yield true;
                }

                case MotionEvent.ACTION_MOVE -> {
                    handleActionMove(motionEvent);
                    yield true;
                }

                case MotionEvent.ACTION_CANCEL -> {
                    closeLayout.setVisibility(View.GONE);
                    yield true;
                }

                default -> false;
            };
        }
    }
}
