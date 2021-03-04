package xpx.video;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

import androidx.annotation.NonNull;

import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Capturer;
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


    @SuppressLint("MissingPermission")
    public VideoCapturer createCameraCapturer(boolean isFront, String name) {

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

        CameraManager manager  = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId: manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                //默认打开后置摄像头
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT)
                    continue;


                manager.openCamera(cameraId, stateCallback, null);
                break;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            camera.close();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };
}
