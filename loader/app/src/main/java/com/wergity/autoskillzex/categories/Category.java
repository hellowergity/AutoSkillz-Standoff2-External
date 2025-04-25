package com.wergity.autoskillzex.categories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.tabs.TabLayout;
import com.wergity.autoskillzex.R;
import com.wergity.autoskillzex.natives.Daemon;

public class Category {
    private static final int SEPARATOR_COLOR   = 0xFF5F5F5F;
    private static final int ACTIVATED_COLOR   = 0x4C0000FF;
    private static final int DEACTIVATED_COLOR = 0x4CFFFFFF;
    private static final int PROGRESS_COLOR    = 0x4CFF0000;

    private final int index;

    private final Context context;
    private final Typeface typeface;

    public LinearLayout layout;
    public final TabLayout.Tab tab;

    public Category(@NonNull TabLayout tabLayout, int idx, String name) {
        index = idx;
        context = tabLayout.getContext();

        tab = tabLayout.newTab();
        tab.setText(name);

        tabLayout.addTab(tab);
        typeface = ResourcesCompat.getFont(context, R.font.font);
    }

    @NonNull
    private LinearLayout.LayoutParams getLayoutParams() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    public AppCompatTextView addText(String text, boolean isDescription) {
        AppCompatTextView textView = new AppCompatTextView(context);

        textView.setText(text);
        textView.setTypeface(typeface);
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(getLayoutParams());

        if (isDescription) {
            textView.setTextSize(0, textView.getTextSize() * 0.9f);
        }

        layout.addView(textView);
        return textView;
    }

    public void addSpace(int height) {
        LinearLayout.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;

        View spaceView = new View(context);
        spaceView.setLayoutParams(layoutParams);
        spaceView.setBackgroundColor(Color.TRANSPARENT);

        layout.addView(spaceView);
    }

    public void addSeparator() {
        addSpace(7);

        LinearLayout.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = 8;

        View lineView = new View(context);
        lineView.setBackgroundColor(SEPARATOR_COLOR);
        lineView.setLayoutParams(layoutParams);
        lineView.setAlpha(0.8f);

        layout.addView(lineView);
        addSpace(7);
    }

    public void addSeekBar(int progress, int max, SeekBarCallback seekBarCallback) {
        AppCompatSeekBar seekBar = new AppCompatSeekBar(context);
        seekBar.setLayoutParams(getLayoutParams());
        seekBar.setProgress(progress);
        seekBar.setAlpha(0.7f);
        seekBar.setMax(max);

        Drawable thumbDrawable = seekBar.getThumb();
        Drawable progressDrawable = seekBar.getProgressDrawable();

        thumbDrawable = DrawableCompat.wrap(thumbDrawable).mutate();
        progressDrawable = DrawableCompat.wrap(progressDrawable).mutate();

        DrawableCompat.setTint(thumbDrawable, Color.RED);
        DrawableCompat.setTint(progressDrawable, PROGRESS_COLOR);

        seekBar.setThumb(thumbDrawable);
        seekBar.setProgressDrawable(progressDrawable);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarCallback.onProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        layout.addView(seekBar);
    }

    private void setSwitchSelectionColor(@NonNull SwitchCompat switchCompat) {
        int thumbColor = switchCompat.isChecked() ? Color.RED : Color.WHITE;
        int trackColor = switchCompat.isChecked() ? ACTIVATED_COLOR : DEACTIVATED_COLOR;

        Drawable thumbDrawable = switchCompat.getThumbDrawable();
        Drawable trackDrawable = switchCompat.getTrackDrawable();

        thumbDrawable = DrawableCompat.wrap(thumbDrawable).mutate();
        trackDrawable = DrawableCompat.wrap(trackDrawable).mutate();

        DrawableCompat.setTint(thumbDrawable, thumbColor);
        DrawableCompat.setTint(trackDrawable, trackColor);

        switchCompat.setThumbDrawable(thumbDrawable);
        switchCompat.setTrackDrawable(trackDrawable);
    }

    public void addSwitch(String text, boolean checked, SwitchCallback switchCallback) {
        SwitchCompat switchCompat = new SwitchCompat(context);
        switchCompat.setLayoutParams(getLayoutParams());
        switchCompat.setTypeface(typeface);
        switchCompat.setTextColor(Color.WHITE);
        switchCompat.setChecked(checked);
        switchCompat.setText(text);

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setSwitchSelectionColor(switchCompat);
            switchCallback.onCheckedChanged(isChecked);
        });

        setSwitchSelectionColor(switchCompat);
        layout.addView(switchCompat);
    }

    public void setSpinnerSelectionColor(AdapterView<?> adapterView) {
        TextView textView;
        if (adapterView == null || (textView = (TextView) adapterView.getSelectedView()) == null) {
            return;
        }

        textView.setTextColor(Color.WHITE);
    }

    public void addSpinner(String text, int selection, String[] array, SpinnerCallback spinnerCallback) {
        addText(text, false);

        SpinnerArrayAdapter spinnerArrayAdapter = new SpinnerArrayAdapter(context, array);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AppCompatSpinner spinner = new AppCompatSpinner(context);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setLayoutParams(getLayoutParams());
        spinner.setSelection(selection, true);
        spinner.setPopupBackgroundDrawable(new ColorDrawable(Color.RED));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCallback.onItemSelected(position);
                setSpinnerSelectionColor(parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setSpinnerSelectionColor(parent);
            }
        });

        setSpinnerSelectionColor(spinner);
        layout.addView(spinner);
    }

    private class SpinnerArrayAdapter extends ArrayAdapter<String> {
        public SpinnerArrayAdapter(Context context, String[] strArr) {
            super(context, android.R.layout.simple_spinner_item, strArr);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View dropDownView = super.getDropDownView(position, convertView, parent);
            ((TextView) dropDownView).setTypeface(typeface);
            return dropDownView;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ((TextView) view).setTypeface(typeface);
            return view;
        }
    }

    void signal(int idx, boolean value) {
        Daemon.signal(index, idx, value ? 1 : 0);
    }

    void signal(int idx, int value) {
        Daemon.signal(index, idx, value);
    }

    public void create() {
        layout = new LinearLayout(context);
        layout.setLayoutParams(getLayoutParams());
        layout.setOrientation(LinearLayout.VERTICAL);
    }

    public interface SeekBarCallback {
        void onProgressChanged(int progress);
    }

    public interface SwitchCallback {
        void onCheckedChanged(boolean isChecked);
    }

    public interface SpinnerCallback {
        void onItemSelected(int position);
    }
}