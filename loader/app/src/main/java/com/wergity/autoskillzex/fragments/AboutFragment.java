package com.wergity.autoskillzex.fragments;

import static com.wergity.autoskillzex.utils.Helper.OpenURL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wergity.autoskillzex.R;

public class AboutFragment extends Fragment {
    private View view;

    private void setupClick(int index, String url) {
        view.findViewById(index).setOnClickListener(view -> OpenURL(requireActivity(), url));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.about_fragment, container, false);

            setupClick(R.id.about_telegram, "https://t.me/wergity_channel");
            setupClick(R.id.about_github, "https://github.com/hellowergity");
        }

        return view;
    }
}
