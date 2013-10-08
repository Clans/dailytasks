package com.dmitrytarianyk.dailytasks.screens.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmitrytarianyk.dailytasks.BaseFragment;
import com.dmitrytarianyk.dailytasks.R;
import com.dmitrytarianyk.dailytasks.util.PreferenceHelper;

public class PreferencesFragment extends BaseFragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.preferences_fragment, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUi();
    }

    private void updateUi() {
        mView.findViewById(R.id.pref_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoogleCalendarsOnDevice();
            }
        });
    }

    private void getGoogleCalendarsOnDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection =
                        new String[]{
                                CalendarContract.Calendars._ID,
                                CalendarContract.Calendars.NAME,
                                CalendarContract.Calendars.ACCOUNT_NAME,
                                CalendarContract.Calendars.ACCOUNT_TYPE};
                Cursor calCursor = getActivity().getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                        projection, CalendarContract.Calendars.VISIBLE + " = 1", null, CalendarContract.Calendars._ID + " ASC");

                if (calCursor.moveToFirst()) {
                    do {
                        long id = calCursor.getLong(0);
                        String displayName = calCursor.getString(1);
                        System.out.println("id = " + id);
                        System.out.println("displayName = " + displayName);
                    } while (calCursor.moveToNext());
                }

                showCalendarPickerDialog(calCursor);
            }
        }).start();
    }

    private void showCalendarPickerDialog(final Cursor calCursor) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose your calendar")
                        .setSingleChoiceItems(calCursor, -1, "name", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (calCursor.moveToPosition(which)) {
                                    long calId = calCursor.getLong(calCursor.getColumnIndex(BaseColumns._ID));
                                    PreferenceHelper.setCalendarId(calId);
                                } else {
                                    showErrorToast();
                                }
                            }
                        })
                        .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: send local broadcast
                            }
                        }).show();
            }
        });
    }
}
