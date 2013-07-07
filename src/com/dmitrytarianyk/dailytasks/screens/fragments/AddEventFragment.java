package com.dmitrytarianyk.dailytasks.screens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmitrytarianyk.dailytasks.BaseFragment;
import com.dmitrytarianyk.dailytasks.R;

public class AddEventFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_event_fragment, container, false);
    }
}
