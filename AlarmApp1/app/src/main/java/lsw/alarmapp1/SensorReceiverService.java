package lsw.alarmapp1;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;


/**
 * Created by user on 2015-11-15.
 */
public class SensorReceiverService extends Service {

    private static final String LOG_TAG = "SensingAlarm_SensorReceiverService";
    BLEScan mBLEScan;
    Handler mHandler;
    WakeUpDecision mWakeUpDecision;
    int mReceivedPacket = -1;
    private static final int REQUEST_ENABLE_BT = 1;
    TextView deviceNameView, addressView, rssiView;
    ArrayList<DeviceHolder> mDeviceHolderList = new ArrayList<DeviceHolder>();
    //private BluetoothAdapter mBluetoothAdapter;
    public static final String AutoSnoopPeriodStorage = "AutoSnoopPeriodStorage";
    public String mPairedDeviceName;
    public String mAutoSnoopPeriod;
    SharedPreferences mPairedDeviceStorage;
    SharedPreferences mAutoSnoopPeriodStorage;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(LOG_TAG, "onStartCommand()");
        mPairedDeviceStorage = getSharedPreferences(DevicePairingActivity.PairedDeviceStorage, 0);
        mPairedDeviceName = mPairedDeviceStorage.getString("PairedDeviceKey", ""); //temporary uses the device address.
        Log.v(LOG_TAG, "onStartCommand() mPairedDeviceName : " + mPairedDeviceName);

        mAutoSnoopPeriodStorage = getSharedPreferences(AutoSnoopPeriodStorage, 0);
        mAutoSnoopPeriod = mPairedDeviceStorage.getString("AutoSnoopPeriodKey", "5"); //temporary uses the default period. need to make Auto snoop period setting menu.
        Log.v(LOG_TAG, "onStartCommand() mAutoSnoopPeriod : " + mAutoSnoopPeriod);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(LOG_TAG, "handleMessage what" + msg.what);

                switch(msg.what) {
                    case BLEScan.BLESCAN_DEVICE_DETECTED:
                        DeviceHolder deviceHolder = (DeviceHolder)msg.obj;
                        Log.d(LOG_TAG, "handleMessage device name " + deviceHolder.device.getName() + deviceHolder.device.getAddress() + Integer.toString(deviceHolder.rssi) + "packet sequence " + deviceHolder.scanRecord[13]);
                        if(mReceivedPacket < deviceHolder.scanRecord[13]) {
                            mReceivedPacket = deviceHolder.scanRecord[13];
                            Log.d(LOG_TAG, "handleMessage " +  mReceivedPacket);
                        }
//                        deviceNameView.setText("device name : " + deviceHolder.device.getName());
//                        addressView.setText("address : " + deviceHolder.device.getAddress());
//                        rssiView.setText("rssi : " + Integer.toString(deviceHolder.rssi));

//                        mWakeUpDecision.addDetection(deviceHolder);
                        break;
                    case WakeUpDecision.USER_WAKEUP:
                        Log.d(LOG_TAG, "handleMessage USER_WAKEUP");
                        break;

                }
            }
        };

        mBLEScan = new BLEScan(getApplicationContext(), mHandler);

/* // 폰의 BT 가 꺼져있을 경우 켜는 루틴. 서비스 안에서 액티비티가 호출 안되고 있음. 나중에 다시 손봐야함.
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBLEScan.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            startActivity(enableBtIntent,REQUEST_ENABLE_BT);
        }
*/
        //parseLong(mAutoSnoopPeriod);
        long periodTime = parseLong(mAutoSnoopPeriod) * 1000 * 60; // 1000 이 1초. 여기서는 default 5분간 Scan한다(default Auto Snoop Period)
        Log.d(LOG_TAG, "onStartCommand() periodTime " + periodTime);
        mBLEScan.scanLeDevice(true, periodTime);
        return super.onStartCommand(intent, flags, startId);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "onCreate()");
        super.onCreate();

        /*
        deviceNameView = (TextView)findViewById(R.id.DeviceName);
        addressView = (TextView)findViewById(R.id.Address);
        rssiView = (TextView)findViewById(R.id.Rssi);
*/
        /*
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(LOG_TAG, "handleMessage what" + msg.what);

                switch(msg.what) {
                    case BLEScan.BLESCAN_DEVICE_DETECTED:
                        DeviceHolder deviceHolder = (DeviceHolder)msg.obj;
                        Log.d(LOG_TAG, "handleMessage device name " + deviceHolder.device.getName());
                        deviceNameView.setText("device name : " + deviceHolder.device.getName());
                        addressView.setText("address : " + deviceHolder.device.getAddress());
                        rssiView.setText("rssi : " + Integer.toString(deviceHolder.rssi));

                        mWakeUpDecision.addDetection(deviceHolder);
                        break;
                    case WakeUpDecision.USER_WAKEUP:
                        if(mPlayer.isPlaying()) {
                            mPlayer.stop();
//                            mPlayer.release();
                        }

                        Log.d(LOG_TAG, "handleMessage USER_WAKEUP");
                        break;

                }
            }
        };

        mBLEScan = new BLEScan(getApplicationContext(), mHandler);
        mWakeUpDecision = new WakeUpDecision(mHandler);
        mWakeUpDecision.start();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBLEScan.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        mBLEScan.scanLeDevice(true);
*/
    }







    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        super.onDestroy();
//        mWakeUpDecision.mThreadRun = false;
    }
}