package com.mysaasa.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysaasa.ui.views.ContactView;

/**
 * Created by Adam on 4/3/2016.
 */
public class ContactsFragment extends Fragment {
    public ContactsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new ContactView(getActivity());
    }
}
