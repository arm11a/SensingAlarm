package com.e4deen.sensoralarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2016-03-01.
 */
public class AlarmingActivity extends Activity {

    private static final String LOG_TAG = "SensorAlarm_AlarmingActivity";
    AlarmClass AlarmData;
    Context mContext;
    TextView mCurTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarming);

        Log.v(LOG_TAG, "onCreate()");
        mContext = getApplicationContext();

        Bundle b = getIntent().getExtras();
        AlarmClass AlarmData = (AlarmClass)b.getParcelable("AlarmData");

        if(AlarmData != null) {
            setTitle("알람");

            if(!(checkAlarmingCondition(AlarmData))) {
                Log.v(LOG_TAG, "onCreate() Alarming condition isn't satisfied.");
                finish();
                return;
            }

            if(checkAlarmOneShotCondition(AlarmData)) {
                AlarmData.activate = false;
                AlarmData.modifyAlarm(mContext);
            }
            Log.v(LOG_TAG, "onCreate() index " + AlarmData.index + ", Alarm time " + AlarmData.hour +":" + AlarmData.min);
        } else {
            Log.e(LOG_TAG, "onCreate() intent hasn't AlarmData . This is wrong operation");
            finish();
            return;
        }
        mCurTimeTextView =  (TextView)findViewById(R.id.TimeDisplay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume()");
/*
        Date rightNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh:mm:ss dd.MM.yyyy");
        String dateString = formatter.format(rightNow);
        mCurTimeTextView.setText(dateString);
*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()");
    }

    boolean checkAlarmingCondition(AlarmClass AlarmData) {
        Calendar calendar = Calendar.getInstance();
        int Day = calendar.get(Calendar.DAY_OF_WEEK);

        if( AlarmData.mon == true && Day == Calendar.MONDAY)
            return true;
        if( AlarmData.tue == true && Day == Calendar.TUESDAY)
            return true;
        if( AlarmData.wed == true && Day == Calendar.WEDNESDAY)
            return true;
        if( AlarmData.thr == true && Day == Calendar.THURSDAY)
            return true;
        if( AlarmData.fri == true && Day == Calendar.FRIDAY)
            return true;
        if( AlarmData.sat == true && Day == Calendar.SATURDAY)
            return true;
        if( AlarmData.sun == true && Day == Calendar.SUNDAY)
            return true;

        return false;
    }

    boolean checkAlarmOneShotCondition(AlarmClass AlarmData) {
        if( AlarmData.mon == false &&AlarmData.tue == false && AlarmData.wed == false && AlarmData.thr == false && AlarmData.fri == false && AlarmData.sat == false && AlarmData.sun == false ) {
            return true;
        } else {
            return false;
        }
    }
}
