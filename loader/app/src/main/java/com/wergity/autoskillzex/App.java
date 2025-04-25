package com.wergity.autoskillzex;

import android.app.Application;

public class App extends Application {
    public static App instance;

    public static Info SelectedGameInfo;
    public static Info[] SupportedGames = new Info[] {
        new Info("com.axlebolt.standoff2", "0.33.1", "libHelloKittySO.so"),
        new Info("com.SKITLSE.StandRise",  "1.3.0",  "libHelloKittySR.so")
    };

    public static final int PERMISSION_REQUEST_CODE = 0x1337;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static class Info {
        public String packageName;
        public String versionName;
        public String libraryName;

        public Info(String packageName, String versionName, String libraryName) {
            this.packageName = packageName;
            this.versionName = versionName;
            this.libraryName = libraryName;
        }
    }
}