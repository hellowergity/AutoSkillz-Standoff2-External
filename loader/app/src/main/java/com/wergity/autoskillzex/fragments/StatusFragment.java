package com.wergity.autoskillzex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.wergity.autoskillzex.R;
import com.wergity.autoskillzex.floating.MenuService;
import com.wergity.autoskillzex.floating.OverlayService;
import com.wergity.autoskillzex.natives.Daemon;

import java.lang.ref.WeakReference;

public class StatusFragment extends Fragment {
    private static final int IDLE_COLOR       = 0xFFFFA500;
    private static final int RUNNING_COLOR    = 0xFF00C800;
    private static final int STOPPED_COLOR    = 0xFFC80000;
    private static final int UNDETECTED_COLOR = 0xFFFFA500;
    private static final int DETECTED_COLOR   = 0xFFC80000;

    private boolean isSelected;

    private View view;
    private CheatStatus cheatStatus;
    private AppCompatTextView status;
    private FragmentActivity activity;

    public static WeakReference<StatusFragment> weakReference;

    private final MenuProvider menuProvider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.status_menu, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.status_start) {
                startCheats();
                return true;
            } else if (itemId == R.id.status_stop) {
                stopCheats();
                return true;
            }

            return false;
        }
    };

    private void showSnackbar(String message) {
        if (isSelected) {
            Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetStatus(@NonNull CheatStatus cheatStatus) {
        switch (cheatStatus) {
            case IDLE:
                status.setTextColor(IDLE_COLOR);
                status.setText("Idle");
                break;

            case RUNNING:
                status.setTextColor(RUNNING_COLOR);
                status.setText("Running");
                break;

            case STOPPED:
                status.setTextColor(STOPPED_COLOR);
                status.setText("Stopped");
                break;

            case UNDETECTED:
                status.setTextColor(UNDETECTED_COLOR);
                status.setText("Undetected");
                break;

            case DETECTED:
                status.setTextColor(DETECTED_COLOR);
                status.setText("Detected");
                break;
        }

        this.cheatStatus = cheatStatus;
    }

    private void startCheats() {
        if (cheatStatus == CheatStatus.RUNNING) {
            showSnackbar("Cheat is already running.");
        } else {
            SetStatus(CheatStatus.RUNNING);

            OverlayService.ForceStop();
            MenuService.ForceStop();

            Daemon.Start(activity);
        }
    }

    public void stopCheats() {
        OverlayService.ForceStop();
        MenuService.ForceStop();
        Daemon.Stop();

        if (cheatStatus == CheatStatus.RUNNING) {
            SetStatus(CheatStatus.STOPPED);
            showSnackbar("Cheat has been stopped.");
        } else {
            showSnackbar("Cheat is not running.");
        }
    }

    private void selectConfig() {
        String title = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setMessage("By wergity and his cat <3");

        builder.setNeutralButton("Reset", (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton("Export", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Import", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = requireActivity();
        weakReference = new WeakReference<>(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        isSelected = true;
        activity.addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onStop() {
        super.onStop();
        isSelected = false;
        activity.removeMenuProvider(menuProvider);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.status_fragment, container, false);
            view.findViewById(R.id.status_config).setOnClickListener(view -> selectConfig());
            status = view.findViewById(R.id.status_text);
        }

        return view;
    }

    public enum CheatStatus {
        IDLE,
        RUNNING,
        STOPPED,
        UNDETECTED,
        DETECTED
    }
}
