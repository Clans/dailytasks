package com.dmitrytarianyk.dailytasks.screens.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;

import com.dmitrytarianyk.dailytasks.BaseActivity;
import com.dmitrytarianyk.dailytasks.R;
import com.dmitrytarianyk.dailytasks.screens.fragments.EditEventFragment;

public class EditEventActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.frag_container, new EditEventFragment()).commit();
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setCustomView(R.layout.actionbar_done_discard);
        }

        findViewById(R.id.actionbar_discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
