package lsw.alarmapp1;

import android.bluetooth.BluetoothDevice;

/**
 * Created by user on 2015-11-15.
 */
public class DeviceHolder {
    BluetoothDevice device;
    String additionalData;
    int rssi;
    byte scanRecord[] = new byte[62];

    DeviceHolder(DeviceHolder deviceHolder) {
        this.device = deviceHolder.device;
        this.additionalData = deviceHolder.additionalData;
        this.rssi = deviceHolder.rssi;
        System.arraycopy(deviceHolder.scanRecord,0,this.scanRecord,0,this.scanRecord.length);
    }

    DeviceHolder(BluetoothDevice device, String additionalData, int rssi, byte[] scanRecord) {
        this.device = device;
        this.additionalData = additionalData;
        this.rssi = rssi;
        System.arraycopy(scanRecord,0,this.scanRecord,0,this.scanRecord.length);
    }
}

