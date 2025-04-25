package com.wergity.autoskillzex;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wergity.autoskillzex.utils.Logcat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    private List<GameData> getInstalledGames() {
        PackageManager pm = this.getPackageManager();
        List<GameData> list = new ArrayList<>();

        for (App.Info info : App.SupportedGames) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(info.packageName, PackageManager.GET_META_DATA);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                String installedVersion = packageInfo.versionName;

                if (applicationInfo == null || installedVersion == null) {
                    continue;
                }

                if (!installedVersion.equals(info.versionName)) {
                    continue;
                }

                GameData gameData = new GameData();
                gameData.version = packageInfo.versionName;
                gameData.icon = applicationInfo.loadIcon(pm);
                gameData.packageName = packageInfo.packageName;
                gameData.label = (String) applicationInfo.loadLabel(pm);

                list.add(gameData);
            } catch (PackageManager.NameNotFoundException nameNotFoundException) {
                Logcat.Warning("Package not found: %s", nameNotFoundException.getMessage());
            }
        }

        return list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Supported Game List");
        setContentView(R.layout.game_activity);
        List<GameData> list = getInstalledGames();

        RecyclerView recyclerView = findViewById(R.id.game_activity_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new GameViewAdapter(this, list, position -> {
            GameData gameData = list.get(position);
            for (App.Info info : App.SupportedGames) {
                if (Objects.equals(info.packageName, gameData.packageName)) {
                    App.SelectedGameInfo = info;
                }
            }

            startActivity(new Intent(this, PostActivity.class));
            finish();
        }));
    }

    public static class GameData {
        public String label;
        public String packageName;
        public String version;
        public Drawable icon;
    }

    public static class GameViewAdapter extends RecyclerView.Adapter<GameViewHolder> {
        private final List<GameData> list;
        private final LayoutInflater inflater;
        private final OnItemClickListener listener;

        public GameViewAdapter(Activity activity, List<GameData> list, OnItemClickListener listener) {
            this.list = list;
            this.inflater = LayoutInflater.from(activity);
            this.listener = listener;
        }

        @NonNull
        @Override
        public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.game_view, parent, false);
            return new GameViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
            GameData gameData = list.get(position);
            holder.bind(gameData);

            holder.itemView.setOnClickListener(view -> listener.onItemClick(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView game_view_label;
        private final AppCompatTextView game_view_package;
        private final AppCompatTextView game_view_version;
        private final AppCompatImageView game_view_logo;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);

            game_view_label = itemView.findViewById(R.id.game_view_label);
            game_view_package = itemView.findViewById(R.id.game_view_package);
            game_view_logo = itemView.findViewById(R.id.game_view_logo);
            game_view_version = itemView.findViewById(R.id.game_view_version);
        }

        public void bind(@NonNull GameData gameData) {
            game_view_label.setText(gameData.label);
            game_view_package.setText(gameData.packageName);
            game_view_logo.setBackground(gameData.icon);
            game_view_version.setText(gameData.version);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
