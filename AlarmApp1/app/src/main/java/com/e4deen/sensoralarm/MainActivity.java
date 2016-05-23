package com.e4deen.sensoralarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class MainActivity extends ListActivity {
//public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SensorAlarm_MainActivity";
    SharedPreferences mNumberOfAlarmDataPref;
    int mNumberOfAlarmData;
    Button addAlarmButton, editAlarmButton, deleteAlarmButton, setData, removeData;
    CheckBox activeCheckBox;
    Context mContext;
    public ArrayList<AlarmClass> AlarmDataList;
    public AlarmManager alarmMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        addAlarmButton = (Button)findViewById(R.id.AddAlarm);
        addAlarmButton.setOnClickListener(mClickListener);
        alarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<AlarmClass> alarmDataList = new ArrayList<AlarmClass>();

        mNumberOfAlarmDataPref = getSharedPreferences(AlarmClass.NumberOfAlarmData, 0);
        mNumberOfAlarmData = mNumberOfAlarmDataPref.getInt(AlarmClass.NumberOfAlarmData, 0);

        Log.v(LOG_TAG, "onCreate " + mNumberOfAlarmData);
        if(mNumberOfAlarmData == 0) {

        } else {
            for(int i = 1; i <= mNumberOfAlarmData; i++) {
                String tempAlarmString = new String("Alarm" + Integer.toString(i));
                Log.v(LOG_TAG, "String Test " + tempAlarmString);
                SharedPreferences tempAlarmDataPref = getSharedPreferences(tempAlarmString, 0);
                AlarmClass tempAlarmClass = new AlarmClass();  // 리스트에 추가할 객체
                tempAlarmClass.hour = tempAlarmDataPref.getInt(AlarmClass.Hour, 99);
                tempAlarmClass.min = tempAlarmDataPref.getInt(AlarmClass.Min, 99);
                tempAlarmClass.index = tempAlarmDataPref.getInt(AlarmClass.Index, 0);
                tempAlarmClass.sun = tempAlarmDataPref.getBoolean(AlarmClass.Sun, false);
                tempAlarmClass.mon = tempAlarmDataPref.getBoolean(AlarmClass.Mon, false);
                tempAlarmClass.tue = tempAlarmDataPref.getBoolean(AlarmClass.Tue, false);
                tempAlarmClass.wed = tempAlarmDataPref.getBoolean(AlarmClass.Wed, false);
                tempAlarmClass.thr = tempAlarmDataPref.getBoolean(AlarmClass.Thr, false);
                tempAlarmClass.fri = tempAlarmDataPref.getBoolean(AlarmClass.Fri, false);
                tempAlarmClass.sat = tempAlarmDataPref.getBoolean(AlarmClass.Sat, false);
                tempAlarmClass.activate = tempAlarmDataPref.getBoolean(AlarmClass.Activate, false);
                tempAlarmClass.sensorEnable = tempAlarmDataPref.getBoolean(AlarmClass.SensorEnable, false);
                alarmDataList.add(tempAlarmClass);
            }
        }
        PersonAdapter m_adapter = new PersonAdapter(this, R.layout.alarm_list_layout, alarmDataList); // 어댑터를 생성합니다.
        AlarmDataList = alarmDataList;
        setListAdapter(m_adapter);
        alarmRefresh();
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.AddAlarm:
                    Intent intent = new Intent(mContext, AddAlarmActivity.class);
                    startActivity(intent);
                    Log.i(LOG_TAG, "onClick AddAlarm button click");
                    break;
/*
                case R.id.setData:
                    Log.i(LOG_TAG, "onClick setData button click");
  // add code
                    SharedPreferences tempAlarmDataPref = getSharedPreferences("Alarm1", 0);
                    SharedPreferences.Editor editor = tempAlarmDataPref.edit();
                    editor.putInt(AlarmClass.Hour, 5);
                    editor.putInt(AlarmClass.Min, 10);
                    editor.putBoolean(AlarmClass.Sun, true);
                    editor.putBoolean(AlarmClass.Mon, true);
                    editor.commit();

                    tempAlarmDataPref = getSharedPreferences("Alarm2", 0);
                    editor = tempAlarmDataPref.edit();
                    editor.putInt(AlarmClass.Hour, 15);
                    editor.putInt(AlarmClass.Min, 36);
                    editor.putBoolean(AlarmClass.Sun, true);
                    editor.putBoolean(AlarmClass.Tue, true);
                    editor.putBoolean(AlarmClass.Thr, true);
                    editor.putBoolean(AlarmClass.Sat, true);
                    editor.commit();

                    if(mNumberOfAlarmDataPref == null)
                    mNumberOfAlarmDataPref = getSharedPreferences(AlarmClass.NumberOfAlarmData, 0);

                    SharedPreferences.Editor editor2 = mNumberOfAlarmDataPref.edit();
                    editor2.putInt(AlarmClass.NumberOfAlarmData, 2);
                    editor2.commit();

 // delete code
                    getSharedPreferences("Alarm1", 0).edit().clear().commit();
                    getSharedPreferences("Alarm2", 0).edit().clear().commit();
                    getSharedPreferences(AlarmClass.NumberOfAlarmData, 0).edit().clear().commit();

                    break;
                case R.id.removeData:
                    Log.i(LOG_TAG, "onClick removeData button click");

//                    AlarmClass.removeAlarm(mContext,2);
                    AlarmClass.removePrefData(mContext);
//                    getSharedPreferences("Alarm1", 0).edit().clear().commit();
//                    getSharedPreferences("Alarm2", 0).edit().clear().commit();
//                    getSharedPreferences("Alarm1", 0).edit().clear().commit();
//                    getSharedPreferences("Alarm2", 0).edit().clear().commit();
                    getSharedPreferences(AlarmClass.NumberOfAlarmData, 0).edit().clear().commit();
                    break;
*/
                case R.id.time:
                    int position = (Integer)v.getTag(); // ListVeiw의 Tag 는 position 값을 저장해놓았다.
                    AlarmClass AlarmData = AlarmDataList.get(position);
                    AlarmData.activate = !AlarmData.activate;
                    Log.i(LOG_TAG, "onClick time check button click , position " + position + ", AlarmData index " + AlarmDataList.get(position).index + ", AlarmData.activate " + AlarmData.activate);
                    AlarmData.modifyAlarm(mContext);
                    alarmRefresh();
                    break;
                case R.id.EditAlarm:
                    Intent i = new Intent(mContext, AddAlarmActivity.class);
                    position = (Integer)v.getTag(); // ListVeiw의 Tag 는 position 값을 저장해놓았다.
                    i.putExtra("AlarmData", AlarmDataList.get(position));
                    Log.i(LOG_TAG, "onClick EditAlarm button click , position " + position + ", AlarmData index " + AlarmDataList.get(position).index);
                    startActivity(i);
                    break;
                case R.id.DeleteAlarm:
                    position = (Integer)v.getTag();
                    int alarmIndex = position + 1;
                    AlarmClass.removeAlarm(mContext, alarmIndex);
                    onResume();
                    Log.i(LOG_TAG, "onClick DeleteAlarm button click, position " + position + ", alarmIndex " + alarmIndex);
                    break;
            }
        }
    };

    private class PersonAdapter extends ArrayAdapter<AlarmClass> {

        public ArrayList<AlarmClass> items;

        public PersonAdapter(Context context, int textViewResourceId, ArrayList<AlarmClass> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            //Log.d(LOG_TAG, "getView: position" + position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.alarm_list_layout, null);
                editAlarmButton = (Button)v.findViewById(R.id.EditAlarm);
                editAlarmButton.setTag(position);
                editAlarmButton.setOnClickListener(mClickListener);
                deleteAlarmButton = (Button)v.findViewById(R.id.DeleteAlarm);
                deleteAlarmButton.setTag(position);
                deleteAlarmButton.setOnClickListener(mClickListener);
                activeCheckBox = (CheckBox)v.findViewById(R.id.time);
                activeCheckBox.setOnClickListener(mClickListener);
                activeCheckBox.setTag(position);
            }
            AlarmClass p = items.get(position);
            AlarmDataList = items;

            if (p != null) {
                TextView timeView = (TextView) v.findViewById(R.id.time);
                if (timeView != null){
                    timeView.setText(String.valueOf((p.getHour())) + "시 " + String.valueOf(p.getMin()) + "분");
                    //Log.v(LOG_TAG, "getView() index " + p.index + ", " + p.getHour() + "시 " + p.getMin() + "분");
                }
                CheckBox checkBoxTimeView = (CheckBox) v.findViewById(R.id.time);

                Log.v(LOG_TAG, "getView() index " + p.index + ", " + "activate " + p.activate);
                if (checkBoxTimeView != null) {
                    if(p.activate == true) {
                        checkBoxTimeView.setChecked(true);
                    }
                }

                TextView dateView = (TextView) v.findViewById(R.id.date);
                if (dateView != null){
                    String dateString = new String("         ");
                    if(p.sun == true)
                        dateString = dateString + "일 ";
                    if(p.mon == true)
                        dateString = dateString + "월 ";
                    if(p.tue == true)
                        dateString = dateString + "화 ";
                    if(p.wed == true)
                        dateString = dateString + "수 ";
                    if(p.thr == true)
                        dateString = dateString + "목 ";
                    if(p.fri == true)
                        dateString = dateString + "금 ";
                    if(p.sat == true)
                        dateString = dateString + "토 ";
                    if(p.sun == false && p.mon == false && p.tue == false && p.wed == false && p.thr == false && p.fri == false && p.sat == false)
                        dateString = dateString + "한번만실행 ";

                        dateView.setText(dateString);
                }
            }
            return v;
        }
    }

    void alarmRefresh() {

        Log.v(LOG_TAG, "alarmRefresh()");

        SharedPreferences numberOfActiveAlarmPref;
        int i,index, position, numberOfActiveAlarm;
        numberOfActiveAlarmPref = getSharedPreferences(AlarmClass.NumberOfActiveAlarm, 0);
        numberOfActiveAlarm = numberOfActiveAlarmPref.getInt(AlarmClass.NumberOfActiveAlarm, 0);

        for(i=1; i <= numberOfActiveAlarm; i++) {
            Intent intent = new Intent(this, AlarmingActivity.class);
            PendingIntent pending = PendingIntent.getActivity(mContext, i, intent, 0); // 두번째 인자가 식별자이니 고유번호로 취소할 수 있도록 할것.
            Log.v(LOG_TAG, "alarmRefresh() cancel alram number" + i);
            this.alarmMgr.cancel(pending);
        }

        numberOfActiveAlarm = 0;

        for(index = 1; index <= mNumberOfAlarmData ; index++) {
            position = index - 1;
            AlarmClass AlarmData = AlarmDataList.get(position);

            if (AlarmData.activate == true) {

                Log.e(LOG_TAG, "alarmRefresh() index " + (index - 1) + " is activate.");

                numberOfActiveAlarm = numberOfActiveAlarm + 1;
                final Intent intent = new Intent(mContext, AlarmingActivity.class);
                intent.putExtra("AlarmData", AlarmData);
                final PendingIntent pi = PendingIntent.getActivity(mContext, numberOfActiveAlarm, intent, Intent.FLAG_ACTIVITY_NEW_TASK); // 두번째 인자가 식별자이다. 고유번호를 넣을것
                long intervalTime = 24 * 60 * 60 * 1000;// 24시간
                //long intervalTime = 3 * 1000;// 3초

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, AlarmData.getHour());
                calendar.set(Calendar.MINUTE, AlarmData.getMin());
                //  위처럼 셋팅하고 나면 오늘 날짜 기준으로 시간이 셋팅됨. 이걸 오늘 시간과 비교.

                Log.v(LOG_TAG, "time test setting time : " + new Date(calendar.getTimeInMillis()) + ", current time : " + new Date(System.currentTimeMillis()));

                if(calendar.getTimeInMillis() > System.currentTimeMillis() ) {
                    Log.v(LOG_TAG, "time test calendar time is newer");
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, AlarmData.getHour() + 24); // 셋팅된 시간이 이미 지나갔으므로 다음날 그 시간에 알람이 울리도록 셋팅.
                    Log.v(LOG_TAG, "time test current time time is newer. New setting time - " + new Date(calendar.getTimeInMillis()));
                }

                //alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), intervalTime, pi);
                //alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi);
                Log.v(LOG_TAG, "alarmRefresh() set alram number" + numberOfActiveAlarm);

                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, intervalTime, pi);
            }
        }
        numberOfActiveAlarmPref.edit().putInt(AlarmClass.NumberOfActiveAlarm, numberOfActiveAlarm).commit();
    }
}
