package lsw.alarmapp1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import adparser.AdElement;
import adparser.AdParser;

/**
 * Created by user on 2015-11-09.
 */
public class BLEScan {

    private static final String LOG_TAG = "SensingAlarm_BLEScan";

    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 1000*60*60; // 1000 이 1초
    boolean mScanning;
    public BluetoothAdapter mBluetoothAdapter;
    Context mContext;
    ArrayList<DeviceHolder> mDeviceHolderList;

    final static int BLESCAN_DEVICE_DETECTED = 0;
    final static int BLESCAN_SCAN_END = 1;


    public BLEScan(Context context,Handler handler) {
        Log.d(LOG_TAG, "onCreate BLEScan");
//        mHandler = new Handler();
        mHandler = handler;
        mContext = context;
        final BluetoothManager bluetoothManager =
                (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "This device don't have bluetooth adapter", Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }

    }

public void scanLeDevice(final boolean enable, long scanPeriod) {
            if (enable) {
                Log.d(LOG_TAG, "scanLeDevice scanPeriod +" + scanPeriod/1000 + " second");
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        Log.d(LOG_TAG, "scanLeDevice postDelay");
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        Message msg = mHandler.obtainMessage();
                        msg.what = BLESCAN_SCAN_END;
//                        msg.obj = deviceHolder;
                        mHandler.sendMessage(msg);
                    }
//                }, SCAN_PERIOD);
//                }, scanPeriod);
                }, 10 * 1000);

                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
                Log.d(LOG_TAG, "scanLeDevice " + enable );
        }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    String deviceName = device.getName();
                    StringBuffer b = new StringBuffer();
                    int byteCtr = 0;
                    for( int i = 0 ; i < scanRecord.length ; ++i ) {
                        if( byteCtr > 0 )
                            b.append( " ");
                        b.append( Integer.toHexString( ((int)scanRecord[i]) & 0xFF));
                        ++byteCtr;
                        if( byteCtr == 8 ) {
                            Log.d(LOG_TAG, new String(b));
                            byteCtr = 0;
                            b = new StringBuffer();
                        }
                    }
                    ArrayList<AdElement> ads = AdParser.parseAdData(scanRecord);
                    StringBuffer sb = new StringBuffer();
                    for( int i = 0 ; i < ads.size() ; ++i ) {
                        AdElement e = ads.get(i); // ads.get(0) 일때 "flags:LE General Discoverable Mode,BR/EDR Not Supported" 정도로 긴 값을 갖는다.
                        if( i > 0 ) {
                            sb.append(";");
                        }
                        //Log.d(LOG_TAG, "parsing test i " + i + ", " + e.toString());
                        sb.append(e.toString());
                    }
                    String additionalData = new String(sb);
                    Log.d(LOG_TAG, "additionalData: " + additionalData);
                    DeviceHolder deviceHolder = new DeviceHolder(device,additionalData,rssi,scanRecord);

                    //mContext.runOnUiThread(new DeviceAddTask(deviceHolder));
                    Thread t = new Thread(new DeviceAddTask(deviceHolder));
                    t.start();
                }
            };


    class DeviceAddTask implements Runnable {
        DeviceHolder deviceHolder;

        public DeviceAddTask( DeviceHolder deviceHolder ) {
            this.deviceHolder = deviceHolder;
        }

        public void run() {
            Log.d(LOG_TAG, "DeviceAddTask recoDevice " + deviceHolder.device.getName());
                    Message msg = mHandler.obtainMessage();
                    msg.what = BLESCAN_DEVICE_DETECTED;
                    msg.obj = deviceHolder;
                    mHandler.sendMessage(msg);
                    Log.d(LOG_TAG, "DeviceAddTask device Name " + deviceHolder.device.getName());
                    Log.d(LOG_TAG, "DeviceAddTask address  " + deviceHolder.device.getAddress());
                    Log.d(LOG_TAG, "DeviceAddTask rssi  " + Integer.toString(deviceHolder.rssi));
                    Log.d(LOG_TAG, "DeviceAddTask scanRecord " + deviceHolder.scanRecord[0] + " / " +deviceHolder.scanRecord[1] + " / " + deviceHolder.scanRecord[2] + " / " +  deviceHolder.scanRecord[3] + " / " +  deviceHolder.scanRecord[4] + " / " +  deviceHolder.scanRecord[5] + " / " +  deviceHolder.scanRecord[6] + " / " +  deviceHolder.scanRecord[7]);
        }
    }
}
