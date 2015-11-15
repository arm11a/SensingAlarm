package lsw.alarmapp1;

import android.bluetooth.BluetoothDevice;

/**
 * Created by user on 2015-11-15.
 */
public class DeviceHolder {
    BluetoothDevice device;
    String additionalData;
    int rssi;

    public DeviceHolder(BluetoothDevice device, String additionalData, int rssi) {
        this.device = device;
        this.additionalData = additionalData;
        this.rssi = rssi;
    }
}

