package intersky.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;
import intersky.scan.view.activity.MipcaCaptureActivity;
import intersky.scan.view.activity.MipcaCapturePadActivity;

public class ScanUtils {

    public static final int SCAN_FINISH = 2260002;
    public static final String ACTION_SCAN_FINISH = "ACTION_SCAN_FINISH";
    private volatile static ScanUtils mScanUtils;
    public Context context;
    public static ScanUtils init(Context context) {


        if (mScanUtils == null) {
            synchronized (ScanUtils.class) {
                if (mScanUtils == null) {
                    mScanUtils = new ScanUtils(context);
                }
                else
                {
                    mScanUtils.context = context;
                }
            }
        }
        return mScanUtils;
    }

    public ScanUtils(Context context) {

        this.context = context;

    }

    public static ScanUtils getInstance() {
        return mScanUtils;
    }

    public void startScan(Activity context,String className) {

        Intent intent = new Intent(context, MipcaCaptureActivity.class);
        if(className.length() > 0)
        intent.putExtra("class",className);
        intent.setAction(ScanUtils.ACTION_SCAN_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        if(className.length() == 0)
//        {
//            context.startActivityForResult(intent,ScanUtils.SCAN_FINISH);
//        }
//        else
//        {
//            context.startActivity(intent);
//        }
        context.startActivity(intent);
    }

    public PermissionResult checkStartScan(Activity context,String className) {
        PermissionResult mPermissionRepuest = new ScanPremissionResult(context,className);
        Handler handler = new PermissionHandler(context,className);
        AppUtils.getPermission(Manifest.permission.CAMERA,context, PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA,handler);
        return  mPermissionRepuest;
    }


    public PermissionResult checkStartScan(Activity context,String className,Handler handler,PermissionResult mPermissionRepuest) {
        AppUtils.getPermission(Manifest.permission.CAMERA,context, PermissionCode.PERMISSION_REQUEST_CAMERA_CAMERA,handler);
        return  mPermissionRepuest;
    }

    public void startScan(Activity context) {
        Intent intent = new Intent(context, MipcaCaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivityForResult(intent,ScanUtils.SCAN_FINISH);
    }

    public void startScanPad(Activity context,String action) {
        Intent intent = new Intent(context, MipcaCapturePadActivity.class);
        intent.setAction(action);
        context.startActivity(intent);
    }

    public void createQRImage(String url, ImageView imageView)
    {

    }
}
