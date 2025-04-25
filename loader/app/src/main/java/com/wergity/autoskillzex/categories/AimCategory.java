package com.wergity.autoskillzex.categories;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.tabs.TabLayout;

public class AimCategory extends Category {
    private static final int AIMBOT            = 0;
    private static final int SILENT            = 1;
    private static final int SMOOTHNESS        = 2;
    private static final int SHOW_FOV          = 3;
    private static final int AIM_FOV_WIDTH     = 4;
    private static final int AIM_FOV_HEIGHT    = 5;
    private static final int VISIBILITY_CHECKS = 6;
    private static final int IGNORE_OFF_SCREEN = 7;
    private static final int AIM_RANGE         = 8;
    private static final int TARGET_SELECTION  = 9;

    public AimCategory(TabLayout tabLayout) {
        super(tabLayout, 0, "AIM");
    }

    @Override
    public void create() {
        super.create();

        addSwitch("Aimbot", false, isChecked -> signal(AIMBOT, isChecked));
        addText("Enable Auto Aiming.", true);
        addSeparator();

        addSwitch("Silent", false, isChecked -> signal(SILENT, isChecked));
        addText("Redirect bullets to the aimbot target.", true);
        addSeparator();

        addSeekBar(0, 100, new SeekBarCallback() {
            private final AppCompatTextView textView = addText("Smoothness: Off", false);

            @Override
            public void onProgressChanged(int progress) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Smoothness: ");

                if (progress == 0) {
                    stringBuilder.append("Off");
                } else {
                    stringBuilder.append(((progress * 100) / 100));
                    stringBuilder.append("%");
                }

                textView.setText(stringBuilder.toString());
                AimCategory.this.signal(SMOOTHNESS, progress);
            }
        });
        addText("Adjust the smoothness percentage.", true);
        addSeparator();

        addSwitch("Show FOV", false, isChecked -> signal(SHOW_FOV, isChecked));
        addSeekBar(0, 100, new SeekBarCallback() {
            private final AppCompatTextView textView = addText("AimFov Width: Off", false);
            @Override
            public void onProgressChanged(int progress) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("AimFov Width: ");

                if (progress == 0) {
                    stringBuilder.append("Off");
                } else {
                    stringBuilder.append(((progress * 100) / 100));
                    stringBuilder.append("%");
                }

                textView.setText(stringBuilder.toString());
                AimCategory.this.signal(AIM_FOV_WIDTH, progress);
            }
        });
        addSeekBar(0, 100, new SeekBarCallback() {
            private final AppCompatTextView textView = addText("AimFov Height: Off", false);
            @Override
            public void onProgressChanged(int progress) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("AimFov Height: ");

                if (progress == 0) {
                    stringBuilder.append("Off");
                } else {
                    stringBuilder.append(((progress * 100) / 100));
                    stringBuilder.append("%");
                }

                textView.setText(stringBuilder.toString());
                AimCategory.this.signal(AIM_FOV_HEIGHT, progress);
            }
        });
        addText("Limit aimbot to work within FOV range on screen", true);
        addSeparator();

        addSwitch("Visibility Checks", false, isChecked -> signal(VISIBILITY_CHECKS, isChecked));
        addText("Add visibility checks to the aimbot.", true);
        addSeparator();

        addSwitch("Ignore Off-Screen", false, isChecked -> signal(IGNORE_OFF_SCREEN, isChecked));
        addText("Make aimbot ignore the off-screen targets.", true);
        addSeparator();

        addSeekBar(0, 100, new SeekBarCallback() {
            private final AppCompatTextView textView = addText("Aim Range: Off", false);

            @Override
            public void onProgressChanged(int progress) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Aim Range: ");

                if (progress == 0) {
                    stringBuilder.append("Off");
                } else {
                    stringBuilder.append(((progress * 100) / 100));
                    stringBuilder.append("%");
                }

                textView.setText(stringBuilder.toString());
                AimCategory.this.signal(AIM_RANGE, progress);
            }
        });
        addText("Limit aimbot within a range.", true);
        addSeparator();

        addSpinner("Target Selection", 0, new String[] {
                "Closest in angle",
                "Closest in distance"
        }, position -> this.signal(TARGET_SELECTION, position));
        addText("Set target selection whether closest in distance or angle.", true);
        addSeparator();
    }
}