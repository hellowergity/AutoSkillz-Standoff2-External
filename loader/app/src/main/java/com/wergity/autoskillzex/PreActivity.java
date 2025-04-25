package com.wergity.autoskillzex;

import static com.wergity.autoskillzex.App.PERMISSION_REQUEST_CODE;

import static com.wergity.autoskillzex.utils.Helper.RunWithDelay;
import static com.wergity.autoskillzex.utils.Helper.ShowDialog;
import static com.wergity.autoskillzex.utils.Helper.IsAnyGameInstalled;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.topjohnwu.superuser.Shell;
import com.wergity.autoskillzex.utils.Notifications;

public class PreActivity extends AppCompatActivity {
    private static final int MAX_ATTEMPTS = 5;

    private static int attempt = 0;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> initialize()
    );

    private void showOverlayPermissionDialog() {
        String message = "Please grant draw over apps permission for the overlay service.";

        ShowDialog(this, message, (dialog, which) -> {
            dialog.dismiss();

            Intent intent = new Intent();
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);

            activityResultLauncher.launch(intent);
        });
    }

    private void showRootPermissionDialog() {
        String message = "Please grant the superuser permissions for the application to work properly.";
        ShowDialog(this, message, (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
    }

    private boolean checkRequirements() {
        if (!Notifications.GrantPermission(this)) {
            return false;
        }

        if (!Settings.canDrawOverlays(this)) {
            showOverlayPermissionDialog();
            return false;
        }

        if (Boolean.FALSE.equals(Shell.isAppGrantedRoot())) {
            showRootPermissionDialog();
            return false;
        }

        return true;
    }

    private void authenticate(String password) {
        attempt++;

        if (attempt > MAX_ATTEMPTS) {
            Toast.makeText(this, "Maximum number of tries has been reached. Try to re-open the app again.", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            ShowDialog(this, "Internal error occurred", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
        } else {
            startActivity(new Intent(this, GameActivity.class));
            finish();
        }
    }

    private void initialize() {
        if (!checkRequirements()) {
            return;
        }

        findViewById(R.id.pre_activity_auth_button).setOnClickListener(v -> {
            AppCompatEditText editText = findViewById(R.id.pre_activity_password_edittext);
            Editable editable = editText.getText();
            if (editable == null) {
                return;
            }

            String password = editable.toString();
            if (password.isEmpty()) {
                Toast.makeText(this, "Please fill both fields.", Toast.LENGTH_LONG).show();
            } else {
                authenticate(password);
            }
        });

        RunWithDelay(() -> {
            findViewById(R.id.pre_activity_progress_bar).setVisibility(View.GONE);
            findViewById(R.id.pre_activity_auth_layout).setVisibility(View.VISIBLE);
        }, 1500);
    }

    private void showGameNotInstalledDialog() {
        String message = "Couldn't find any supported game installed. Make sure your game is in our support list and it's actually installed with a compatible version.";

        ShowDialog(this, message, (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_activity);

        if (!IsAnyGameInstalled(this)) {
            showGameNotInstalledDialog();
        } else {
            initialize();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            initialize();
        }
    }
}