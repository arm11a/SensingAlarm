package lsw.alarmapp1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2015-11-18.
 */
public class DevicePairingActivity extends AppCompatActivity {

    BLEScan mBLEScan;
    Handler mHandler;
    private static final String LOG_TAG = "SensingAlarm_DevicePairing";
    TextView deviceNameView, addressView, rssiView, currentPairedDevice;
    Button ScanDevice, StopScan;
    private DeviceListAdapter mAdapter;
    ListView mBTdeviceListView;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String PairedDeviceStorage = "PairedDeviceStorage";
    public String mPairedDeviceName;
    SharedPreferences mPairedDeviceStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pairing);

        mPairedDeviceStorage = getSharedPreferences(PairedDeviceStorage, 0);
        mPairedDeviceName = mPairedDeviceStorage.getString("PairedDeviceKey", ""); //temporary uses the device address.
        currentPairedDevice = (TextView)findViewById(R.id.CurrentPairedDevice);

        if(mPairedDeviceName.equals("")) {
            currentPairedDevice.setText("CurrentPairingDevice : None");
            Toast.makeText(this, "Sensing Device is not paired. Please pairs the sensing device.", Toast.LENGTH_SHORT).show();
        } else {
            currentPairedDevice.setText("CurrentPairingDevice : " + mPairedDeviceName);
        }
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        deviceNameView = (TextView)findViewById(R.id.DeviceName_DevicePairing);
        addressView = (TextView)findViewById(R.id.DeviceAddress_DevicePairing);
        rssiView = (TextView)findViewById(R.id.DeviceRssi_DevicePairing);

        ScanDevice = (Button)findViewById(R.id.ScanDevice);
        ScanDevice.setOnClickListener(mClickListener);
        StopScan = (Button)findViewById(R.id.StopScan);
        StopScan.setOnClickListener(mClickListener);

        mBTdeviceListView = (ListView)findViewById(R.id.DeviceListView);
        mAdapter = new DeviceListAdapter(DevicePairingActivity.this);
        mBTdeviceListView.setAdapter(mAdapter);
        mBTdeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 SharedPreferences.Editor editor = mPairedDeviceStorage.edit();
                 editor.putString("PairedDeviceKey", mAdapter.mDeviceHolderList.get(position).device.getAddress());
                 editor.commit();
                 currentPairedDevice.setText("CurrentPairingDevice : " + mAdapter.mDeviceHolderList.get(position).device.getAddress());
                 Toast.makeText(getApplicationContext(), "Pairing with " + mAdapter.mDeviceHolderList.get(position).device.getAddress(), Toast.LENGTH_SHORT).show();
//                 Log.d(LOG_TAG, "onItemClick " + mAdapter.mDeviceHolderList.get(position).device.getAddress());
//                 Log.d(LOG_TAG, "onItemClick stored PairedDeviceValue : " + mPairedDeviceStorage.getString("PairedDeviceKey",""));
             }
        }
        );

        mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d(LOG_TAG, "handleMessage what " + msg.what);

            switch(msg.what) {
                case BLEScan.BLESCAN_DEVICE_DETECTED:
                    DeviceHolder deviceHolder = (DeviceHolder)msg.obj;

                    Log.d(LOG_TAG, "handleMessage device name " + deviceHolder.device.getName());
                    mAdapter.addItem(deviceHolder);
                    listUpdate();
                    /*
                    deviceNameView.setText("Searched Device name : " + deviceHolder.device.getName());
                    addressView.setText("Searched Device Address : " + deviceHolder.device.getAddress());
                    rssiView.setText("Searched Device Rssi : " + Integer.toString(deviceHolder.rssi));
                    */
                    break;
            }
        }
    };

    mBLEScan = new BLEScan(getApplicationContext(), mHandler);

    }

    public void listUpdate(){
        Log.d(LOG_TAG, "listUpdate is called");
        mAdapter.notifyDataSetChanged(); //​리스트뷰 값들의 변화가 있을때 아이템들을 다시 배치 할 때 사용되는 메소드입니다.
    };

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ScanDevice:
                    mBLEScan.scanLeDevice(true);
                    Log.i(LOG_TAG, "onClick ScanDevice button click");
                    break;
                case R.id.StopScan:
                    mBLEScan.scanLeDevice(false);
                    Log.i(LOG_TAG, "onClick StopScan button click");
                    break;
            }
        }
    };

    /*        layout_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GoIntent(position);
            }
        });
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu1).setVisible(true);
        menu.findItem(R.id.menu2).setVisible(true);
        menu.findItem(R.id.menu3).setVisible(true);
        return true;
    }

}
