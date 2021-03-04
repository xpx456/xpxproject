package xpx.video;

import android.bluetooth.BluetoothDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Message;

public class CameraHandler extends Handler {

    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 40001;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 40002;
    public static final int PERMISSION_REQUEST_WRITE_SETTINGS = 40003;
    public static final int PERMISSION_REQUEST_WRITE_SECURE_SETTINGS = 40004;
    public static final int EVENT_FIND_DEVICE = 300004;
    public static final int EVENT_DISCOVER_SERVEICE = 300005;
    public static final int EVENT_CONCTACT_DEVICE = 300006;
    public static final int EVENT_REBOND = 300007;

    public XpxCameraManager mXpxCameraManager;

    public CameraHandler(XpxCameraManager mXpxCameraManager) {
        this.mXpxCameraManager = mXpxCameraManager;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {

        }
    }
}
