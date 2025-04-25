package com.wergity.autoskillzex.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import com.wergity.autoskillzex.App;
import com.wergity.autoskillzex.R;
import com.wergity.autoskillzex.utils.Helper;
import com.wergity.autoskillzex.utils.Logcat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

public class LogsFragment extends Fragment {
    private View view;

    private AppCompatTextView text;
    private ProgressBar progressBar;
    private FragmentActivity activity;

    private static final int DELAY_MILLIS = 1000;
    private static final String LOGCAT_FILENAME = String.format(Locale.ENGLISH, "KittenWare_%s.log", App.SelectedGameInfo.packageName);

    private final MenuProvider menuProvider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.logcat_menu, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            final int itemId = menuItem.getItemId();

            if (itemId == R.id.logcat_refresh) {
                showSnackbar("Reports have been reloaded");
                refreshLogs();
            } else if (itemId == R.id.logcat_save) {
                saveLogs();
            }

            return false;
        }
    };

    private void showSnackbar(String message) {
        Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private void initialize() {
        Helper.RunWithDelay(() -> {
            progressBar.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(Logcat.Read());
        }, DELAY_MILLIS);
    }

    private void clearLogs() {
        Logcat.Clear();
        refreshLogs();
    }

    private void refreshLogs() {
        progressBar.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
        text.setText(null);

        initialize();
    }

    private void saveLogs() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File outputFile = new File(downloadsDir, LOGCAT_FILENAME);

        try {
            if (!downloadsDir.exists() && !downloadsDir.mkdir()) {
                showSnackbar("Failed to create downloads directory");
                return;
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            bufferedWriter.append(text.getText());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception exception) {
            Logcat.Error("LogsFragment.saveLogs: %s", exception);
        }

        showSnackbar("Reports file has been saved to " + outputFile.getAbsolutePath());
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onStop() {
        super.onStop();
        activity.removeMenuProvider(menuProvider);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            activity = requireActivity();

            view = inflater.inflate(R.layout.logcat_fragment, container, false);
            view.findViewById(R.id.logcat_clear).setOnClickListener(view -> clearLogs());

            text = view.findViewById(R.id.logcat_text);
            text.setTextIsSelectable(true);

            progressBar = view.findViewById(R.id.logcat_progress);
        }

        return view;
    }
}
