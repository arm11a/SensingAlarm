package lsw.alarmapp1;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by user on 2015-11-15.
 */
public class WakeUpDecision {

    private static final String LOG_TAG = "SensingAlarm_WakeUpDecision";

    public WakeUpDecision() {
        Log.d(LOG_TAG, "WakeUpDecision constructor");
    }

    public boolean checkWakeup(DeviceHolder deviceHolder) {
        Log.d(LOG_TAG, "WakeUpDecision checkWakeup " + deviceHolder.device.getName());
        return true; // temp
    }

/*
    public void run() {
        super.run();

        int testCount = 0;
        while(mThreadRun && !Thread.currentThread().isInterrupted())
        {
            try {
                testCount += 1;
                sleep(1000);
                Log.d(LOG_TAG, "DecisionMaking thread running");
                if(testCount > 10) {
                    Log.d(LOG_TAG, "DecisionMaking thread USER_WAKEUP");
                    Message msg = mHandler.obtainMessage();
                    msg.what = USER_WAKEUP;
                    mHandler.sendMessage(msg);
                    mThreadRun = false;
                }

            } catch(InterruptedException e) {
                Log.e(LOG_TAG, "InterruptedException in thread. " + e.getMessage());
                e.printStackTrace();

                Thread.currentThread().interrupt();
            }
        }

    }
*/
}
