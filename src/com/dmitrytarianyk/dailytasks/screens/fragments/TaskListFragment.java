package com.dmitrytarianyk.dailytasks.screens.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dmitrytarianyk.dailytasks.BaseFragment;
import com.dmitrytarianyk.dailytasks.R;
import com.dmitrytarianyk.dailytasks.objects.Event;
import com.dmitrytarianyk.dailytasks.objects.EventCollection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskListFragment extends BaseFragment {

    @SuppressLint("InlinedApi")
    public static final String[] EVENT_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };

    private static final int PROJECTION_ID_INDEX = 0;
//    private static final int REQUEST_ACCOUNT = 4;

    private View mView;
    private Activity mActivity;
    private EventCollection eventCollection;
    private long calID;
    private Account[] accounts;
    private EventsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.task_list_fragment, container, false);
        return mView;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        AccountManager am = AccountManager.get(mActivity);
        accounts = am.getAccountsByType("com.google");

        // TODO: maintain account choose option
        // Either with custom dialog or with AccountPicker

        /*Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, REQUEST_ACCOUNT);*/

        loadEvents();
    }

    private void loadEvents() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = mActivity.getContentResolver();
                String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                        + Calendars.OWNER_ACCOUNT + " = ?))";
                String[] selectionArgs = new String[] {accounts[0].name, "com.google", accounts[0].name};
                // Submit the query and get a Cursor object back.
                Cursor cur = cr.query(Calendars.CONTENT_URI, EVENT_PROJECTION, selection, selectionArgs, null);

                while (cur != null && cur.moveToNext()) {
                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                }

                Cursor cursor = mActivity.getContentResolver().query(Events.CONTENT_URI, new String[]{Events.CALENDAR_ID,
                        Events.TITLE, Events.DESCRIPTION, Events.DTSTART, Events.DTEND, Events.RRULE},
                        Events.CALENDAR_ID + " = " + calID + " AND " + Events.DTSTART + " BETWEEN " + getDtStart() +
                                " AND " + getDtEnd(), null, Events.DTSTART + " ASC");

                /*Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
                ContentUris.appendId(builder, curMonthStart);
                ContentUris.appendId(builder, nextMonthStart);
                Cursor cursor = mActivity.getContentResolver().query(builder.build(),
                        new String[]{"title", "begin", "end", "allDay"}, null, null, "startDay ASC, startMinute ASC");

                while (cursor.moveToNext()) {
                    final String title = cursor.getString(0);
                    final Date begin = new Date(cursor.getLong(1));
                    final Date end = new Date(cursor.getLong(2));
                    final Boolean allDay = !cursor.getString(3).equals("0");

                    System.out.println("Title: " + title + " Begin: " + begin + " End: " + end +
                            " All Day: " + allDay);
                }*/

                eventCollection = new EventCollection();
                while (cursor != null && cursor.moveToNext()) {
                    Event event = new Event();
                    event.setTitle(cursor.getString(1));
                    event.setDescription(cursor.getString(2));
                    event.setStartDate(getDate(cursor.getLong(3)));
                    event.setEndDate(getDate(cursor.getLong(4)));

                    eventCollection.addEvent(event);
                }

                updateUi();
            }
        }).start();
    }

    /**
     *
     * @return One week earlier from now in millis
     */
    private long getDtStart() {
        return new Date().getTime() - DateUtils.WEEK_IN_MILLIS;
    }

    /**
     *
     * @return One week later from now in millis
     */
    private long getDtEnd() {
        return new Date().getTime() + DateUtils.WEEK_IN_MILLIS;
    }

    /*private long getCurrentMonthStart() {
        //get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of the month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Log.i(LOGTAG, "Start of the current month: " + cal.getTime());

        return cal.getTimeInMillis();
    }

    private long getNextMonthStart() {
        //get today and clear time of day
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        // get start of the month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);

        Log.i(LOGTAG, "Start of the next month: " + cal.getTime());

        return cal.getTimeInMillis();
    }*/

    private String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void updateUi() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView list = (ListView) mView.findViewById(R.id.list);

                if (list.getAdapter() == null) {
                    adapter = new EventsAdapter(eventCollection);
                    list.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class EventsAdapter extends BaseAdapter {

        private EventCollection events;

        private EventsAdapter(EventCollection events) {
            this.events = events;
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public Object getItem(int i) {
            return events.getEvent(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(android.R.layout.simple_list_item_1, null);
            }

            Event event = (Event) getItem(position);

            TextView title = (TextView) view.findViewById(android.R.id.text1);
            title.setText(event.getTitle());

            return view;
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACCOUNT && resultCode == Activity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        }
    }*/
}
