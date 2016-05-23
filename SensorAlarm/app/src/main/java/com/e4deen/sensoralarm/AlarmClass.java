package com.e4deen.sensoralarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by user on 2016-02-13.
 */
public class AlarmClass implements Parcelable {

    public int hour, min = 99;
    public int index = 0; // index 는 1 부터 시작한다.
    public boolean sun, mon, tue, wed, thr, fri, sat, activate, sensorEnable = false;

    private static final String LOG_TAG = "SensorAlarm_AlarmClass";

    static final String NumberOfAlarmData = "NumberOfAlarmData";
    static final String NumberOfActiveAlarm = "NumberOfActiveAlarm";
    static final String Activate = "Activate";
    static final String SensorEnable = "SensorEnable";
    static final String Index = "Index";
    static final String Mon = "Monday";
    static final String Tue = "Tuesday";
    static final String Wed = "Wednesday";
    static final String Thr = "Thursday";
    static final String Fri = "Friday";
    static final String Sat = "Saterday";
    static final String Sun = "Sunday";
    static final String Hour = "Hour";
    static final String Min = "Minute";

    public AlarmClass(int _Hour, int _Min){
        this.hour = _Hour;
        this.min = _Min;
    }

    public AlarmClass(){
    }

    public AlarmClass(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public void saveAlarm(Context mContext) {
        SharedPreferences numberOfAlarmDataPref, alarmDataPref;
        int mNumberOfAlarmData;
        numberOfAlarmDataPref = mContext.getSharedPreferences(NumberOfAlarmData, 0);
        mNumberOfAlarmData = numberOfAlarmDataPref.getInt(NumberOfAlarmData, 0);
        mNumberOfAlarmData = mNumberOfAlarmData + 1;
        numberOfAlarmDataPref.edit().putInt(NumberOfAlarmData, mNumberOfAlarmData).commit();
        index =  mNumberOfAlarmData;
        Log.v(LOG_TAG, "saveAlarm mNumberOfAlarmData " + mNumberOfAlarmData + ", index " + index + ", Alarm time " + hour + ":" + min);

        String Alarm = new String("Alarm" + Integer.toString(mNumberOfAlarmData));
        alarmDataPref = mContext.getSharedPreferences(Alarm, 0);
        SharedPreferences.Editor AlarmEditor = alarmDataPref.edit();
        AlarmEditor.putInt(Hour, hour);
        AlarmEditor.putInt(Min, min);
        AlarmEditor.putInt(Index, index);
        AlarmEditor.putBoolean(Mon, mon);
        AlarmEditor.putBoolean(Tue, tue);
        AlarmEditor.putBoolean(Wed, wed);
        AlarmEditor.putBoolean(Thr, thr);
        AlarmEditor.putBoolean(Fri, fri);
        AlarmEditor.putBoolean(Sat, sat);
        AlarmEditor.putBoolean(Sun, sun);
        AlarmEditor.putBoolean(Activate, activate);
        AlarmEditor.putBoolean(SensorEnable, sensorEnable);
        AlarmEditor.commit();

        Log.v(LOG_TAG, "saveAlarm index " + index);
    }

    public void modifyAlarm(Context mContext) {
        SharedPreferences numberOfAlarmDataPref, alarmDataPref;

        String Alarm = new String("Alarm" + Integer.toString(index));

        Log.i(LOG_TAG, "modifyAlarm hour " + hour + ":" + min + "Alarm number " + Alarm + ", index " + index);
        alarmDataPref = mContext.getSharedPreferences(Alarm, 0);
        SharedPreferences.Editor AlarmEditor = alarmDataPref.edit();
        AlarmEditor.putInt(Hour, hour);
        AlarmEditor.putInt(Min, min);
        AlarmEditor.putInt(Index, index);
        AlarmEditor.putBoolean(Mon, mon);
        AlarmEditor.putBoolean(Tue, tue);
        AlarmEditor.putBoolean(Wed, wed);
        AlarmEditor.putBoolean(Thr, thr);
        AlarmEditor.putBoolean(Fri, fri);
        AlarmEditor.putBoolean(Sat, sat);
        AlarmEditor.putBoolean(Sun, sun);
        AlarmEditor.putBoolean(Activate, activate);
        AlarmEditor.putBoolean(SensorEnable, sensorEnable);
        AlarmEditor.commit();

        Log.v(LOG_TAG, "modifyAlarm Alarm number " + Alarm + ", activate " + activate);
    }

    public static void removeAlarm(Context mContext,int index) {
        SharedPreferences numberOfAlarmDataPref, alarmDataPref;
        ArrayList<AlarmClass> AlarmList = new ArrayList<AlarmClass>();
        int mNumberOfAlarmData;

        numberOfAlarmDataPref = mContext.getSharedPreferences(NumberOfAlarmData, 0);
        mNumberOfAlarmData = numberOfAlarmDataPref.getInt(NumberOfAlarmData, 0);

        if(mNumberOfAlarmData != 0 ) {
            AlarmList = getAlarmList(mContext);
            removePrefData(mContext);
            Log.v(LOG_TAG, "removeAlarm() before remove mNumberOfAlarmData " + mNumberOfAlarmData + ", AlarmList size " + AlarmList.size());
            AlarmList.remove(index - 1); //  인자로 받은 index 는 1부터 시작이고, AlarmList 는 0부터 시작이다. But AlarmList.size() 는 0개는 0, 1개는 1 이다.
            Log.v(LOG_TAG, "removeAlarm() after remove mNumberOfAlarmData " + mNumberOfAlarmData + ", AlarmList size " + AlarmList.size());
            numberOfAlarmDataPref.edit().putInt(NumberOfAlarmData, 0).commit();
            for(int i = 0; i < AlarmList.size(); i++) {
                AlarmList.get(i).saveAlarm(mContext); // index 는 1부터 시작이고 position 은 0 부터 시작이다. i는 position 이다.
            }

            Log.v(LOG_TAG, "removeAlarm() index " + index + ", AlarmList size: " + AlarmList.size());
        } else {
            Log.v(LOG_TAG, "removeAlarm() Alarm data already is empty. mNumberOfAlarmData " + mNumberOfAlarmData);
        }
    }

    public static void removePrefData(Context mContext) {
        SharedPreferences numberOfAlarmDataPref, alarmDataPref;
        int numberOfAlarmData;
        numberOfAlarmDataPref = mContext.getSharedPreferences(NumberOfAlarmData, 0);
        numberOfAlarmData = numberOfAlarmDataPref.getInt(NumberOfAlarmData, 0);
        for (int i = 1; i <= numberOfAlarmData; i++) {
            String Alarm = new String("Alarm" + Integer.toString(i));
            alarmDataPref = mContext.getSharedPreferences(Alarm, 0);
            alarmDataPref.edit().clear().commit();
        }
        numberOfAlarmDataPref.edit().putInt(NumberOfAlarmData, 0).commit();

        numberOfAlarmData = numberOfAlarmDataPref.getInt(NumberOfAlarmData, 0);
        Log.v(LOG_TAG, "removePrefData() numberOfAlarmData " + numberOfAlarmData );
    }

    public static ArrayList<AlarmClass> getAlarmList(Context mContext) {
        SharedPreferences numberOfAlarmDataPref, alarmDataPref;
        ArrayList<AlarmClass> AlarmList = new ArrayList<AlarmClass>();
        int numberOfAlarmData;

        numberOfAlarmDataPref = mContext.getSharedPreferences(NumberOfAlarmData, 0);
        numberOfAlarmData = numberOfAlarmDataPref.getInt(NumberOfAlarmData, 0);

        if(numberOfAlarmData > 0) { // 저장되어 있는 alarm data 가 0개이면 return 할 data 가 없다.
            for (int i = 1; i <= numberOfAlarmData; i++) {
                String Alarm = new String("Alarm" + Integer.toString(i));
                AlarmClass alarmData = new AlarmClass();
                alarmDataPref = mContext.getSharedPreferences(Alarm, 0);
                copyFromPrefToVariable(alarmDataPref, alarmData);
                AlarmList.add(alarmData);
            }
        }
        return AlarmList;
    }
    //copy from preferences to alarmClass
    public static void copyFromPrefToVariable(SharedPreferences preferences, AlarmClass alarmClass) {

        alarmClass.hour = preferences.getInt(Hour, 0);
        alarmClass.min = preferences.getInt(Min, 0);
        alarmClass.index = preferences.getInt(Index, 0);
        alarmClass.activate = preferences.getBoolean(Activate, false);
        alarmClass.sensorEnable = preferences.getBoolean(SensorEnable, false);
        alarmClass.mon = preferences.getBoolean(Mon, false);
        alarmClass.tue = preferences.getBoolean(Tue, false);
        alarmClass.wed = preferences.getBoolean(Wed, false);
        alarmClass.thr = preferences.getBoolean(Thr, false);
        alarmClass.fri = preferences.getBoolean(Fri, false);
        alarmClass.sat = preferences.getBoolean(Sat, false);
        alarmClass.sun = preferences.getBoolean(Sun, false);
    }

    public void writeToParcel(Parcel dest, int flags) {

        Log.e(LOG_TAG, "writeToParcel() sun " + sun + ", mon " + mon + ", tue " + tue + ", wed " + wed + ", thr " + thr);
        boolean booleanArray[] = new boolean[] {sun, mon, tue, wed, thr, fri, sat, activate, sensorEnable};

        dest.writeInt(hour);
        dest.writeInt(min);
        dest.writeInt(index);
        dest.writeBooleanArray(booleanArray);
    }

    private void readFromParcel(Parcel in){
        Log.e(LOG_TAG, "readFromParcel()" );
        boolean booleanArray[] = new boolean[9];

        hour = in.readInt();
        min = in.readInt();
        index = in.readInt();
        in.readBooleanArray(booleanArray);

        sun = booleanArray[0];
        mon = booleanArray[1];
        tue = booleanArray[2];
        wed = booleanArray[3];
        thr = booleanArray[4];
        fri = booleanArray[5];
        sat = booleanArray[6];
        activate = booleanArray[7];
        sensorEnable = booleanArray[8];
    }

/*    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlarmClass createFromParcel(Parcel in) {
            return new AlarmClass(in);
        }

        public AlarmClass[] newArray(int size) {
            return new AlarmClass[size];
        }
    };
*/

    public static final Parcelable.Creator<AlarmClass > CREATOR = new Parcelable.Creator<AlarmClass >() {
        public AlarmClass createFromParcel(Parcel source) {
            Log.e(LOG_TAG, "createFromParcel()" );
            return new AlarmClass(source);
        }

        public AlarmClass[] newArray(int size) {
            Log.e(LOG_TAG, "newArray()" );
            return new AlarmClass[size];
        }
    };
///////////////////////////////////////////////
/*
public static final Parcelable.Creator<BookData> CREATOR = new Parcelable.Creator<BookData>() {
    public BookData createFromParcel(Parcel in) {
        return new BookData(in);
    }

    public BookData[] newArray(int size) {
        return new BookData[size];
    }
};
*/
///////////////////////////////////////////////




}
