package xpx.video;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import org.webrtc.Camera1Enumerator;
import org.webrtc.VideoCapturer;

import java.util.ArrayList;
import java.util.HashMap;

public class XpxCameraManager {

    public static volatile XpxCameraManager mXpxCameraManager;
    public Context context;
    public CameraHandler cameraHandler;
    public static XpxCameraManager init(Context context)
    {
        if (mXpxCameraManager == null) {
            synchronized (XpxCameraManager.class) {
                if (mXpxCameraManager == null) {
                    mXpxCameraManager = new XpxCameraManager(context);
                    mXpxCameraManager.cameraHandler = new CameraHandler(mXpxCameraManager);
                } else {

                }
            }
        }
        return mXpxCameraManager;
    }


    public XpxCameraManager(Context context)
    {
        this.context = context;
    }


    public VideoCapturer createCameraCapturer(boolean isFront,String name) {
        Camera1Enumerator enumerator = new Camera1Enumerator(false);
        final String[] deviceNames = enumerator.getDeviceNames();
        boolean hascamera1 = false;
        ArrayList<String> camera1names = new ArrayList<String>();
        boolean front = isFront;
        for(int i = 0 ; i < deviceNames.length ; i++)
        {
            if(deviceNames[i].contains(name))
            {
                hascamera1 = true;
                camera1names.add(deviceNames[i]);
            }
        }
        if(hascamera1)
        {
            for(String deviceName : camera1names)
            {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        else
        {
            for (String deviceName : deviceNames) {
                if (front ? enumerator.isFrontFacing(deviceName) : enumerator.isBackFacing(deviceName)) {
                    VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                    if (videoCapturer != null) {
                        return videoCapturer;
                    }
                }
            }
            if(isFront == true)
            {
                front = false;
            }
            else
            {
                front = true;
            }
            for (String deviceName : deviceNames) {
                if (front ? enumerator.isFrontFacing(deviceName) : enumerator.isBackFacing(deviceName)) {
                    VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                    if (videoCapturer != null) {
                        return videoCapturer;
                    }
                }
            }
        }
        return null;
    }
}
