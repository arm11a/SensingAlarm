package lsw.alarmapp1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    static MediaPlayer mPlayer;
    public String MediaPath = new String("/sdcard/bell.mp3");
//    public String MediaPath = new String("/sdcard/Music/test.mp3");
    private static final String LOG_TAG = "SensingAlarm_AlarmActivity";

    Button StopAlarmButton;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        Log.i(LOG_TAG, "onCreate AlarmActivity");

        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StopAlarmButton = (Button) findViewById(R.id.StopAlarm);
        StopAlarmButton.setOnClickListener(mClickListener);

        try {
            mPlayer = new MediaPlayer();
            //mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(MediaPath);
            mPlayer.prepare();
            mPlayer.start();
            Log.v(LOG_TAG, "media player start");
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume()");
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.StopAlarm:
                    if(mPlayer.isPlaying()) {
                        mPlayer.stop();
//                        mPlayer.release();
                    }
                    //Intent Service = new Intent(mContext, SensorReceiverService.class);
                    //startService(Service);
                    Log.i(LOG_TAG, "onClick() start SensorReceiverService");
                    startService(new Intent(mContext, SensorReceiverService.class));
                    Log.i(LOG_TAG, "onClick() StopAlarm button click");



                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()");
    }
}
