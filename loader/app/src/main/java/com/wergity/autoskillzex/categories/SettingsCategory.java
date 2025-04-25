package com.wergity.autoskillzex.categories;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.tabs.TabLayout;
import com.wergity.autoskillzex.natives.ESPView;

public class SettingsCategory extends Category {
    private static final int MAX_IMAGE_SCALE      = 10;
    private static final int DEFAULT_IMAGE_SCALE  = 5;

    private static final int MAX_IMAGE_OPACITY     = 255;
    private static final int DEFAULT_IMAGE_OPACITY = 255;

    private int initialImageWidth;
    private int initialImageHeight;
    private AppCompatImageView imageView;

    public SettingsCategory(TabLayout tabLayout) {
        super(tabLayout, 2, "SETTINGS");
    }

    public void setIcon(@NonNull AppCompatImageView appCompatImageView) {
        imageView = appCompatImageView;

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        initialImageWidth = layoutParams.width;
        initialImageHeight = layoutParams.height;

        setIconAlpha(DEFAULT_IMAGE_OPACITY);
    }

    private void setIconAlpha(int iconAlpha) {
        imageView.setImageAlpha(iconAlpha);
    }

    private void setIconScale(float iconScale) {
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        layoutParams.width = (int) (initialImageWidth * iconScale);
        layoutParams.height = (int) (initialImageHeight * iconScale);

        imageView.setLayoutParams(layoutParams);
        imageView.requestLayout();
    }

    @Override
    public void create() {
        super.create();

        addSpinner("Rendering FPS", 1, new String[]{"30", "60", "120"}, position -> {
            switch (position) {
                case 0: ESPView.setFps(30); break;
                case 1: ESPView.setFps(60); break;
                case 2: ESPView.setFps(120); break;
            }
        });
        addText("Set frame rate for overlays", true);
        addSeparator();

        addSwitch("AntiAlias", true, ESPView::setAntiAlias);
        addText("Set AntiAlias for overlays", true);
        addSeparator();

        addSeekBar(0, MAX_IMAGE_SCALE, new SeekBarCallback() {
            private final AppCompatTextView textView = addText("Icon Scale: Default", false);

            @Override
            public void onProgressChanged(int progress) {
                StringBuilder stringBuilder = new StringBuilder("Icon Scale: ");

                if (progress == 0) {
                    stringBuilder.append("Default");
                    progress = DEFAULT_IMAGE_SCALE;
                } else {
                    stringBuilder.append((((progress * 77) / 77) * MAX_IMAGE_SCALE));
                    stringBuilder.append("%");
                }

                textView.setText(stringBuilder.toString());
                setIconScale((float) (progress * 0.2d));
            }
        });
        addSeparator();

        addSeekBar(0, MAX_IMAGE_OPACITY, new SeekBarCallback() {
            private final AppCompatTextView textView = addText("Icon Opacity: Default", false);

            @Override
            public void onProgressChanged(int progress) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Icon Opacity: ");

                if (progress == 0) {
                    stringBuilder.append("Default");
                    progress = DEFAULT_IMAGE_OPACITY;
                } else {
                    stringBuilder.append(((progress * 100) / MAX_IMAGE_OPACITY));
                    stringBuilder.append("%");
                }

                textView.setText(stringBuilder.toString());
                setIconAlpha(progress);
            }
        });
        addSeparator();

        ESPView.setFps(60);
        ESPView.setAntiAlias(true);
    }
}