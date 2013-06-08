package com.dmitrytarianyk.dailytasks.screens.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import com.dmitrytarianyk.dailytasks.BaseActivity;
import com.dmitrytarianyk.dailytasks.R;
import com.dmitrytarianyk.dailytasks.screens.fragments.TaskListFragment;

public class TaskListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.frag_container, new TaskListFragment()).commit();
        }
    }
}
