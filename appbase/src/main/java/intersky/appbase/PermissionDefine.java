package intersky.appbase;


import android.Manifest;

import java.util.HashMap;

public class PermissionDefine {

    public static final int READ_EXTERNAL_STORAGE = 400001;
    public static final int WRITE_EXTERNAL_STORAGE = 400002;
    public static final int ACCESS_COARSE_LOCATION = 400003;
    public static final int ACCESS_FINE_LOCATION = 400004;
    public static final int ACCESS_WIFI_STATE = 400005;
    public static final int RECORD_AUDIO = 400006;
    public static final int CALL_PHONE = 400007;
    public static final int READ_CONTACTS = 400008;
    public static final int SEND_SMS = 400009;
    public static final int CAMERA = 400010;
    public static final int READ_PHONE_STATE = 400011;

    public static final HashMap<String,Integer> percode = new HashMap<String,Integer>();
    static {
        percode.put(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE);
        percode.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE);
        percode.put(Manifest.permission.ACCESS_COARSE_LOCATION,ACCESS_COARSE_LOCATION);
        percode.put(Manifest.permission.ACCESS_FINE_LOCATION,ACCESS_FINE_LOCATION);
        percode.put(Manifest.permission.RECORD_AUDIO,RECORD_AUDIO);
        percode.put(Manifest.permission.CALL_PHONE,CALL_PHONE);
        percode.put(Manifest.permission.READ_CONTACTS,READ_CONTACTS);
        percode.put(Manifest.permission.SEND_SMS,SEND_SMS);
        percode.put(Manifest.permission.CAMERA,CAMERA);
        percode.put(Manifest.permission.READ_PHONE_STATE,READ_PHONE_STATE);
    }


}
