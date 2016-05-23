package com.e4deen.sensoralarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by user on 2016-02-14.
 */
public class AddAlarmActivity extends Activity {

    private static final String LOG_TAG = "SensorAlarm_AddAlarmActivity";
    CheckedTextView SensorEnableCheckBox;
    Button button_mon, button_tue, button_wed, button_thr, button_fri, button_sat, button_sun, AddAlarmOkButton, AddAlarmCancelButton;
    TimePicker AlarmTimePicker;
    AlarmClass AlarmSetting;
    Context mContext;
    boolean modifyMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        mContext = getApplicationContext();
        AlarmSetting = new AlarmClass();

        if(getIntent().getExtras() != null) { // 알람 수정 의경우 Main Activity 에서 "AlarmData" 를 가지고 넘어온다.
            setTitle("알람 수정");
            modifyMode = true;
            AlarmSetting = getIntent().getExtras().getParcelable("AlarmData");
            Log.v(LOG_TAG, "onCreate modify mode AlarmData index " + AlarmSetting.index + ", Alarm time " + AlarmSetting.hour +":" + AlarmSetting.min);
        }
        initializeView();
    }

    public void initializeView() {
        SensorEnableCheckBox = (CheckedTextView)findViewById(R.id.SensorEnable);
        AddAlarmOkButton = (Button)findViewById(R.id.AddAlarmOk);
        AddAlarmCancelButton = (Button)findViewById(R.id.AddAlarmCancel);
        SensorEnableCheckBox.setOnClickListener(mClickListener);
        AddAlarmOkButton.setOnClickListener(mClickListener);
        AddAlarmCancelButton.setOnClickListener(mClickListener);

        button_mon = (Button)findViewById(R.id.set_mon);
        button_tue = (Button)findViewById(R.id.set_tue);
        button_wed = (Button)findViewById(R.id.set_wed);
        button_thr = (Button)findViewById(R.id.set_thr);
        button_fri = (Button)findViewById(R.id.set_fri);
        button_sat = (Button)findViewById(R.id.set_sat);
        button_sun = (Button)findViewById(R.id.set_sun);

        button_mon.setOnClickListener(mClickListener);
        button_tue.setOnClickListener(mClickListener);
        button_wed.setOnClickListener(mClickListener);
        button_thr.setOnClickListener(mClickListener);
        button_fri.setOnClickListener(mClickListener);
        button_sat.setOnClickListener(mClickListener);
        button_sun.setOnClickListener(mClickListener);

        AlarmTimePicker = (TimePicker)findViewById(R.id.time_picker);
        //AlarmTimePicker.setOnTimeChangedListener(this);

        if(AlarmSetting.mon == true)
            button_mon.setEnabled(true);
        if(AlarmSetting.tue == true)
            button_tue.setEnabled(true);
        if(AlarmSetting.wed == true)
            button_wed.setEnabled(true);
        if(AlarmSetting.thr == true)
            button_thr.setEnabled(true);
        if(AlarmSetting.fri == true)
            button_fri.setEnabled(true);
        if(AlarmSetting.sat == true)
            button_sat.setEnabled(true);
        if(AlarmSetting.sun == true)
            button_sun.setEnabled(true);

        if(AlarmSetting.sensorEnable == true)
            SensorEnableCheckBox.setEnabled(true);

        if(AlarmSetting.hour != 99)
            AlarmTimePicker.setCurrentHour(AlarmSetting.hour);
        if(AlarmSetting.min != 99)
        AlarmTimePicker.setCurrentMinute(AlarmSetting.min);

    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.SensorEnable:
                    if(SensorEnableCheckBox.isChecked()) {
                        AlarmSetting.sensorEnable = false;
                        SensorEnableCheckBox.setChecked(false);
                    }
                    else {
                        AlarmSetting.sensorEnable = true;
                        SensorEnableCheckBox.setChecked(true);
                    }
                    Log.i(LOG_TAG, "onClick SensorEnableCheckBox click, " + SensorEnableCheckBox.isChecked());
                    break;

                case R.id.AddAlarmOk:
                    AlarmSetting.activate = true;
                    AlarmSetting.hour = AlarmTimePicker.getCurrentHour();
                    AlarmSetting.min = AlarmTimePicker.getCurrentMinute();

                    if (modifyMode == true)
                        AlarmSetting.modifyAlarm(mContext);
                    else
                        AlarmSetting.saveAlarm(mContext);

                    Log.i(LOG_TAG, "onClick AddAlarmOk button click, " + AlarmSetting.hour + ":" + AlarmSetting.min + ", index " + AlarmSetting.index);
                    finish();
                    break;

                case R.id.AddAlarmCancel:
                    Log.i(LOG_TAG, "onClick AddAlarmCancel button click,");
                    finish();
                    break;

                case R.id.set_mon:
                    if(AlarmSetting.mon == false) {
                        AlarmSetting.mon = true;
                        button_mon.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.mon = false;
                        button_mon.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_mon button click,");
                    break;
                case R.id.set_tue:
                    if(AlarmSetting.tue == false) {
                        AlarmSetting.tue = true;
                        button_tue.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.tue = false;
                        button_tue.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_tue button click,");
                    break;
                case R.id.set_wed:
                    if(AlarmSetting.wed == false) {
                        AlarmSetting.wed = true;
                        button_wed.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.wed = false;
                        button_wed.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_wed button click,");
                    break;
                case R.id.set_thr:
                    if(AlarmSetting.thr == false) {
                        AlarmSetting.thr = true;
                        button_thr.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.thr = false;
                        button_thr.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_thr button click,");
                    break;
                case R.id.set_fri:
                    if(AlarmSetting.fri == false) {
                        AlarmSetting.fri = true;
                        button_fri.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.fri = false;
                        button_fri.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_fri button click,");
                    break;
                case R.id.set_sat:
                    if(AlarmSetting.sat == false) {
                        AlarmSetting.sat = true;
                        button_sat.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.sat = false;
                        button_sat.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_sat button click,");
                    break;
                case R.id.set_sun:
                    if(AlarmSetting.sun == false) {
                        AlarmSetting.sun = true;
                        button_sun.setBackgroundColor(0xCA77D5F2);
                    } else {
                        AlarmSetting.sun = false;
                        button_sun.setBackgroundColor(0xffcccccc);
                    }
                    Log.i(LOG_TAG, "onClick set_sun button click,");
                    break;

            }
        }
    };

/*
    //시각 설정 클래스의 상태변화 리스너

    public void onTimeChanged (TimePicker view, int hourOfDay, int minute) {
//        mCalendar.set (mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
        Log.i(LOG_TAG, "onTimeChanged");
    }
*/

}
