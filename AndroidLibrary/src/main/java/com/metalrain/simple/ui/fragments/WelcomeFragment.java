package com.metalrain.simple.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metalrain.simple.R;

/**
 * Created by Adam on 1/12/2015.
 */
public class WelcomeFragment extends Fragment {
    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container,false);
    }
}
