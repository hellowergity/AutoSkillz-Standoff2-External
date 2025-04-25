package com.wergity.autoskillzex;

import static com.wergity.autoskillzex.utils.Helper.ShowDialog;
import static com.wergity.autoskillzex.utils.Helper.OpenURL;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import com.wergity.autoskillzex.fragments.AboutFragment;
import com.wergity.autoskillzex.fragments.HomeFragment;
import com.wergity.autoskillzex.fragments.LogsFragment;
import com.wergity.autoskillzex.fragments.StatusFragment;
import com.wergity.autoskillzex.utils.Logcat;

public class PostActivity extends AppCompatActivity {
    private static final int HOME_TITLE   = R.string.home;
    private static final int STATUS_TITLE = R.string.cheat_status;
    private static final int LOGCAT_TITLE = R.string.logcat;
    private static final int ABOUT_TITLE  = R.string.about;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private final HomeFragment homeFragment = new HomeFragment();
    private final StatusFragment statusFragment = new StatusFragment();
    private final LogsFragment logsFragment = new LogsFragment();
    private final AboutFragment aboutFragment = new AboutFragment();

    private void showSubscriptionDialog() {
        String message = "By subscribing to the channel, you will support me and will be able to play with new cheats & cracks";

        ShowDialog(this, message, (dialog, which) -> {
            dialog.dismiss();
            OpenURL(this, "https://t.me/wergity_channel");

            changeFragment(HOME_TITLE, homeFragment);
            navigationView.setCheckedItem(R.id.drawer_home);
        });
    }

    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void changeFragment(int title, Fragment fragment) {
        setTitle(title);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    private void initNavigationView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.post_activity_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.NULL,
                R.string.NULL
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.post_activity_navigation);
        navigationView.setNavigationItemSelectedListener(item -> {
            final int itemId = item.getItemId();

            if (itemId == R.id.drawer_home) {
                changeFragment(HOME_TITLE, homeFragment);
            } else if (itemId == R.id.drawer_status) {
                changeFragment(STATUS_TITLE, statusFragment);
            } else if (itemId == R.id.drawer_logcat) {
                changeFragment(LOGCAT_TITLE, logsFragment);
            } else if (itemId == R.id.drawer_about) {
                changeFragment(ABOUT_TITLE, aboutFragment);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        Logcat.Clear();
        initNavigationView();
        setupBackPressedHandler();
        showSubscriptionDialog();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initNavigationView();
    }
}
