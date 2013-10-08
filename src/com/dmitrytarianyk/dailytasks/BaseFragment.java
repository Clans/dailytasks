package com.dmitrytarianyk.dailytasks;

import android.app.Fragment;
import android.widget.Toast;

public class BaseFragment extends Fragment {

    protected String LOGTAG = "DailyTasks";

    protected void showErrorToast() {
        Toast.makeText(getActivity(), "Sorry, an error occurred", Toast.LENGTH_SHORT).show();
    }
}
