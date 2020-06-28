package com.finger.receicer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.finger.FingerManger;

public class UsbReceiver extends BroadcastReceiver {

    public FingerManger fingerManger;

    public UsbReceiver(FingerManger fingerManger){
        this.fingerManger = fingerManger;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (FingerManger.ACTION_USB_PERMISSION.equals(intent.getAction()))
        {
            synchronized (this)
            {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                {
                    fingerManger.openDevice();
                    Toast.makeText(fingerManger.context, "USB已授权", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(fingerManger.context, "USB未授权", Toast.LENGTH_SHORT).show();
                    //mTxtReport.setText("USB未授权");
                }
            }
        }
    }
}
