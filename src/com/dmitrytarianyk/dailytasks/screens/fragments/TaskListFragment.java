package com.dmitrytarianyk.dailytasks.screens.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dmitrytarianyk.dailytasks.BaseFragment;
import com.dmitrytarianyk.dailytasks.R;
import com.dmitrytarianyk.dailytasks.database.TasksDataSource;
import com.dmitrytarianyk.dailytasks.objects.Event;
import com.dmitrytarianyk.dailytasks.objects.EventCollection;
import com.dmitrytarianyk.dailytasks.screens.activities.EditEventActivity;
import com.dmitrytarianyk.dailytasks.screens.activities.PreferencesActivity;
import com.dmitrytarianyk.dailytasks.util.PreferenceHelper;

import java.util.Calendar;

public class TaskListFragment extends BaseFragment {

    private View mView;
    private Activity mActivity;
    private EventCollection eventCollection;
    private long mCalId;
    private EventsAdapter adapter;
    private TasksDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.task_list_fragment, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        dataSource = new TasksDataSource(getActivity());
        dataSource.open();

        loadEvents();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tasks_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(getActivity(), EditEventActivity.class));
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }

    private void loadEvents() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCalId = PreferenceHelper.getCalendarId();

                Cursor cursor = mActivity.getContentResolver().query(Events.CONTENT_URI,
                        new String[]{Events.CALENDAR_ID, Events.TITLE, Events.DESCRIPTION,
                                Events.DTSTART, Events.DTEND, Events.RRULE},
                        Events.CALENDAR_ID + " = " + mCalId + " AND " + Events.DTSTART + " BETWEEN "
                                + getDtStart() + " AND " + getDtEnd(), null, Events.DTSTART + " ASC");

                // TODO: get all day events
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

                eventCollection = EventCollection.fromCursor(cursor);
                eventCollection.addAll(dataSource.getAllTasks());
                updateUi();
            }
        }).start();
    }

    /**
     *
     * @return One week earlier from now in millis
     */
    private long getDtStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime().getTime() - DateUtils.WEEK_IN_MILLIS;
    }

    /**
     *
     * @return One week later from now in millis
     */
    private long getDtEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime().getTime() + DateUtils.WEEK_IN_MILLIS;
    }

    private String getTime(long milliSeconds) {
        return DateUtils.formatDateTime(getActivity(), milliSeconds, DateUtils.FORMAT_SHOW_TIME);
    }

    private void updateUi() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (eventCollection.size() == 0) {
                    showEmptyView();
                    return;
                }

                hideEmptyView();

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

    private void showEmptyView() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView list = (ListView) mView.findViewById(R.id.list);
                final TextView empty = (TextView) mView.findViewById(R.id.empty);

                if (list != null) {
                    list.setVisibility(View.GONE);
                }

                if (empty != null) {
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void hideEmptyView() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView list = (ListView) mView.findViewById(R.id.list);
                TextView empty = (TextView) mView.findViewById(R.id.empty);

                if (list != null) {
                    list.setVisibility(View.VISIBLE);
                }

                if (empty != null) {
                    empty.setVisibility(View.GONE);
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
                view = LayoutInflater.from(mActivity).inflate(R.layout.list_item_event, null);
            }

            Event event = (Event) getItem(position);

            TextView time = (TextView) view.findViewById(R.id.time);
            TextView title = (TextView) view.findViewById(R.id.title);

            time.setText(getTime(event.getStartMillis()));
            title.setText(event.getTitle());

            return view;
        }
    }
}
