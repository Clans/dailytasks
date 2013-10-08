package com.dmitrytarianyk.dailytasks.screens.activities;

import android.os.Bundle;

import com.dmitrytarianyk.dailytasks.BaseActivity;
import com.dmitrytarianyk.dailytasks.R;
import com.dmitrytarianyk.dailytasks.screens.fragments.PreferencesFragment;

public class PreferencesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_activity);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.frag_container, new PreferencesFragment()).commit();
        }
    }
}
