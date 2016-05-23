package lsw.alarmapp1;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by user on 2015-11-21.
 */
public class DeviceListAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Activity m_activity;
    public ArrayList<DeviceHolder> mDeviceHolderList;
    private static final String LOG_TAG = "SensingAlarm_DeviceListAdapter";

    public DeviceListAdapter(Activity act) {
        this.m_activity = act;
        mDeviceHolderList = new ArrayList<DeviceHolder>();
        mInflater = (LayoutInflater)m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        int size = 0;
        if(mDeviceHolderList != null) {
            size = mDeviceHolderList.size();
            Log.d(LOG_TAG, "getCount : " + size);
        } else {
            size = 0;
            Log.d(LOG_TAG, "DeviceHolderList is empty(null). getCount : " + size);
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return mDeviceHolderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(DeviceHolder deviceHolder) {
            Log.d(LOG_TAG, "addItem() device name : " + deviceHolder.device.getName());
            mDeviceHolderList.add(deviceHolder);
    }

    public boolean existCheck(DeviceHolder deviceHolder) {

        //int i =0;

        for(int i = 0; i < mDeviceHolderList.size(); i++) {
            boolean nameCheck =false;
            boolean addrCheck = false;

//            if ((mDeviceHolderList.get(i).device.getName() != null) && !(mDeviceHolderList.get(i).device.getName().equals(""))) {
            if ((mDeviceHolderList.get(i).device.getName() != null) && (deviceHolder.device.getName() != null)) {
                if (0 == mDeviceHolderList.get(i).device.getName().compareTo(deviceHolder.device.getName()))
                    nameCheck = true;
            } else {
                nameCheck = true; // if name is null, names aren't comparable. So default nameCheck is true.
            }

            if ((mDeviceHolderList.get(i).device.getAddress() != null) && (deviceHolder.device.getAddress() != null)) {
                if (0 == mDeviceHolderList.get(i).additionalData.compareTo(deviceHolder.additionalData))
                    addrCheck = true;
            } else {
                addrCheck = true; // if addrCheck is null, addrs aren't comparable. So default nameCheck is true.
                                  // but this case may be bug case.
            }

//            if (mDeviceHolderList.get(i).rssi == deviceHolder.rssi) // rssi value is continuously changing, so can't compare.
//                rssiCheck = true;

            if(nameCheck == true && addrCheck == true) {
                Log.d(LOG_TAG, "existCheck() deviceHolder is already exist to position " + i);
                return true;
            } else {
                Log.d(LOG_TAG, "existCheck() nameCheck " + nameCheck + " addCheck " + addrCheck);
            }
        }
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG,"getView is called position " + position);
        if(convertView == null){
            int res = 0;
            res = lsw.alarmapp1.R.layout.bt_device_list_item;
            convertView = mInflater.inflate(res, parent, false);
        }

        TextView deviceName = (TextView)convertView.findViewById(R.id.DeviceName_DevicePairing);
        TextView deviceAddress = (TextView)convertView.findViewById(R.id.DeviceAddress_DevicePairing);
        TextView deviceRssi = (TextView)convertView.findViewById(R.id.DeviceRssi_DevicePairing);
        LinearLayout layout_view =  (LinearLayout)convertView.findViewById(R.id.device_list_item);

//        if ((mDeviceHolderList.get(position).device.getName() != null) && !(mDeviceHolderList.get(position).device.getName().equals("")))
            deviceName.setText("Searched device name : " + mDeviceHolderList.get(position).device.getName());

//        if ((mDeviceHolderList.get(position).device.getAddress() != null) && !(mDeviceHolderList.get(position).device.getAddress().equals("")))
            deviceAddress.setText("Searched device address : " + mDeviceHolderList.get(position).device.getAddress());


        deviceRssi.setText("Searched device rssi :" +  mDeviceHolderList.get(position).rssi);

  /*  버튼에 이벤트처리를 하기위해선 setTag를 이용해서 사용할 수 있습니다.
       *   Button btn 가 있다면, btn.setTag(position)을 활용해서 각 버튼들
       *   이벤트처리를 할 수 있습니다.
   */

/*        layout_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GoIntent(position);
            }
        });
*/
        return convertView;
    }

    //설명 4:
    public void GoIntent(int a){
//        Intent intent = new Intent(m_activity, "가고싶은 클래스".class);
        //putExtra 로 선택한 아이템의 정보를 인텐트로 넘겨 줄 수 있다.
//        intent.putExtra("TITLE", arr.get(a).Title);
//        intent.putExtra("EXPLAIN", arr.get(a).Content);
//        m_activity.startActivity(intent);
    }


}

