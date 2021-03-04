package intersky.apputils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.os.EnvironmentCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class OSUtils {



    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    public static final String ROM_EMUI = "EMUI";
    public static final String ROM_FLYME = "FLYME";
    public static final String ROM_MIUI = "MIUI";
    public static final String ROM_OPPO = "OPPO";
    public static final String ROM_QIKU = "QIKU";
    public static final String ROM_SMARTISAN = "SMARTISAN";
    public static final String ROM_VIVO = "VIVO";
    private static final String TAG = "Rom";
    private static String sName;
    private static String sVersion;

    public static boolean check(String str) {
        if (sName != null) {
            return sName.equals(str);
        }
        CharSequence prop = getProp(KEY_VERSION_MIUI);
        sVersion = (String) prop;
        if (TextUtils.isEmpty(prop)) {
            prop = getProp(KEY_VERSION_EMUI);
            sVersion = (String) prop;
            if (TextUtils.isEmpty(prop)) {
                prop = getProp(KEY_VERSION_OPPO);
                sVersion = (String) prop;
                if (TextUtils.isEmpty(prop)) {
                    prop = getProp(KEY_VERSION_VIVO);
                    sVersion = (String) prop;
                    if (TextUtils.isEmpty(prop)) {
                        prop = getProp(KEY_VERSION_SMARTISAN);
                        sVersion = (String) prop;
                        if (TextUtils.isEmpty(prop)) {
                            sVersion = Build.DISPLAY;
                            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                                sName = ROM_FLYME;
                            } else {
                                sVersion = EnvironmentCompat.MEDIA_UNKNOWN;
                                sName = Build.MANUFACTURER.toUpperCase();
                            }
                        } else {
                            sName = ROM_SMARTISAN;
                        }
                    } else {
                        sName = ROM_VIVO;
                    }
                } else {
                    sName = ROM_OPPO;
                }
            } else {
                sName = ROM_EMUI;
            }
        } else {
            sName = ROM_MIUI;
        }
        return sName.equals(str);
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public static String getProp(String str) {
        BufferedReader bufferedReader;
        Throwable e;
        String str2;
        StringBuilder stringBuilder;
        Throwable th;
        BufferedReader bufferedReader2 = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("getprop ");
            stringBuilder2.append(str);
            bufferedReader = new BufferedReader(new InputStreamReader(runtime.exec(stringBuilder2.toString()).getInputStream()), 1024);
            try {
                String readLine = bufferedReader.readLine();
                bufferedReader.close();
                try {
                    bufferedReader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return readLine;
            } catch (IOException e3) {
                e = e3;
                try {
                    str2 = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to read prop ");
                    stringBuilder.append(str);
                    Log.e(str2, stringBuilder.toString(), e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader2 = bufferedReader;
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            e = e5;
            bufferedReader = null;
            str2 = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to read prop ");
            stringBuilder.append(str);
            Log.e(str2, stringBuilder.toString(), e);
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    public static boolean is360() {
        if (!check(ROM_QIKU)) {
            if (!check("360")) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmui() {
        return check(ROM_EMUI);
    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean isMiui() {
        return check(ROM_MIUI);
    }

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }
    /**
     * 判断是否是华为刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasHWNotchInScreen(Context context) {

        boolean hasNotch = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class hwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method method = hwNotchSizeUtil.getMethod("hasNotchInScreen");
            hasNotch = (Boolean) method.invoke(hwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasNotch;
    }

    /**
     * 获取华为刘海的高宽
     *
     * @param context 上下文对象
     * @return [0]值为刘海宽度int；[1]值为刘海高度
     */
    public static int[] getHWNotchSize(Context context) {
        int[] size = new int[2];
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class hwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method method = hwNotchSizeUtil.getMethod("getNotchSize");
            size = (int[]) method.invoke(hwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

    /**
     * 只适用于判断MIUI 是否有刘海屏
     * SystemProperties.getInt("ro.miui.notch", 0) == 1;
     *
     * @return
     */
    public static boolean hasMIUINotchInScreen() {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("getInt", String.class, String.class);
            return (int) get.invoke(clz, "ro.miui.notch", -1) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取MIUI 的刘海屏高度
     *
     * @param context
     */
    public static int getMIUINotchSize(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

/**
 * Vivo手机刘海屏
 *
 * @param context
 * @return
 */
    /**
     * 是否有刘海
     */
    public static final int VIVO_NOTCH = 0x00000020;
    /**
     * 是否有圆角
     */
    public static final int VIVO_FILLET = 0x00000008;


    /**
     * 是否是Oppo的刘海屏
     *
     * @param context
     * @return
     */
    public static boolean hasOppoNotchInScreen(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }


    public static boolean hasVivoNotchInScreen(Context context) {

        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVoio ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVoio NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVoio Exception");
        } finally {
            return ret;
        }
    }

}
