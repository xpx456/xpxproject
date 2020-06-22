package intersky.apputils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.amap.api.services.core.LatLonPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    public static final int PERMISSION_REQUEST_TEL = 20100;

    public static String getguid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase();

    }

    public static Dialog createLoadingDialogBlue(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog_blue, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.text);// 提示文字
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        // 加载动画
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

    public static void creatDialogTowButton(Context mContext, String message, String title, String btnname1, String btnname2, String btnname3,
                                            DialogInterface.OnClickListener btnlistener1, DialogInterface.OnClickListener btnlistener2, DialogInterface.OnClickListener btnlistener3)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setNeutralButton(btnname2,btnlistener2);
        builder.setPositiveButton(btnname3, btnlistener3);
        builder.show();
    }

    public static void creatDialogTowButton(Context mContext,String message,String title,String btnname1,String btnname2,
                                            DialogInterface.OnClickListener btnlistener1,DialogInterface.OnClickListener btnlistener2)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setPositiveButton(btnname2, btnlistener2);
        builder.show();
    }

    public static void creatDialogTowButtonEdit(Context mContext,String message,String title,String btnname1,String btnname2,
                                                EditDialogListener btnlistener1,EditDialogListener btnlistener2,String hit)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setTitle(title);
        final EditText et = new EditText(mContext);
        et.getText().append(hit);
        builder.setView(et);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setPositiveButton(btnname2, btnlistener2);
        if(btnlistener1 != null)
        {
            btnlistener1.setEditText(et);
        }
        if(btnlistener2 != null)
        {
            btnlistener2.setEditText(et);
        }
        builder.show();

    }


    public static PopupWindow creatPopView(Context mContext, int lid, int oid, View location, InitView initView,  DestoryView destoryView)
    {
        View popupWindowView = LayoutInflater.from(mContext).inflate(lid, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(oid);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        PopupWindow popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();
            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow1.getContentView().setTag(destoryView);
        popupWindow1.setBackgroundDrawable(dw);
        popupWindow1.setOnDismissListener(new MyDismiss(destoryView,popupWindowView));
        popupWindow1.showAtLocation(location,
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        initView.initView(popupWindowView);
        return popupWindow1;
    }

    public static PopupWindow creatDialogTowButtonView(Context mContext,final RelativeLayout mRelativeLayout,View view,View location)
    {
        View popupWindowView = view;
        PopupWindow popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow1.setBackgroundDrawable(dw);
        mRelativeLayout.setVisibility(View.VISIBLE);
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow1.setWindowLayoutType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        popupWindow1.showAtLocation(location,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        return popupWindow1;
    }

    public static PopupWindow creatButtomMenu(Context mContext, final RelativeLayout mRelativeLayout, ArrayList<MenuItem> mMenuItems, View location) {
        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        PopupWindow popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow1.setBackgroundDrawable(dw);
        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
        View view;
        Button btn;
        if(mMenuItems.size() == 1)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn2, null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mMenuItems.get(0).btnName);
            btn.setTag(mMenuItems.get(0).item);
            btn.setOnClickListener(mMenuItems.get(0).mListener);
            linelayer.addView(view);
        }
        else
        {

            for(int i = 0 ; i < mMenuItems.size() ; i++)
            {

                if(i == 0 )
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn1, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                    linelayer.addView(LayoutInflater.from(mContext).inflate(R.layout.buttom_line, null));
                }
                else if(i == mMenuItems.size()-1)
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn3, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                else
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn4, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                    linelayer.addView(LayoutInflater.from(mContext).inflate(R.layout.buttom_line, null));
                }
            }
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.emptylayer, null);
        linelayer.addView(view);
        view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn2, null);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setText(mContext.getString(R.string.cancle));
        final PopupWindow finalPopupWindow1 = popupWindow1;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow1.dismiss();
            }
        });
        linelayer.addView(view);

        mRelativeLayout.setVisibility(View.VISIBLE);
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        popupWindow1.showAtLocation(location,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        return popupWindow1;
    }

    public static PopupWindow creatPopMenu(Context mContext, ArrayList<MenuItem> mMenuItems,View location,int x,int y,int btnx,int btny) {
        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.pop_menu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
//        lsyer.setFocusable(true);
//        lsyer.setFocusableInTouchMode(true);
        PopupWindow popupWindow1 = new PopupWindow(popupWindowView, btnx, btny*mMenuItems.size(), true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        popupWindow1.setAnimationStyle(R.style.popAnimation2);
        final PopupWindow finalPopupWindow = popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow1.setBackgroundDrawable(dw);
        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
        View view;
        Button btn;
        for(int i = 0 ; i < mMenuItems.size() ; i++)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.popmenu_menu_btn, null);
            btn = (Button) view.findViewById(R.id.btn);
            if(mMenuItems.get(i).select)
            {
                btn.setBackgroundColor(Color.rgb(255,248,237));
            }
            else
            {
                btn.setBackgroundColor(Color.rgb(255,255,255));
            }
            btn.setText(mMenuItems.get(i).btnName);
            btn.setTag(mMenuItems.get(i).item);
            btn.setOnClickListener(mMenuItems.get(i).mListener);
            linelayer.addView(view);
        }
        final PopupWindow finalPopupWindow1 = popupWindow1;
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        popupWindow1.showAtLocation(location,
                Gravity.NO_GRAVITY ,  x,y);
        return popupWindow1;
    }

    public static void creatDataPicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {
        int theme = R.style.Theme_AppCompat_DayNight_Dialog;

        new DoubleDatePickerDialog(mContext, theme, mCollback,title,time,true).show();
    }

    public static void creatMonthPicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {

        new DoubleDatePickerDialog(mContext, 2, mCollback,title,time,false).show();
    }

    public static void creatDataAndTimePicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {
        int theme = 2;

        new DoubleDatePickerDialog(mContext, theme, mCollback,title,time).show();
    }

    public static void creatTimePicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {

        new DoubleDatePickerDialog(mContext,  mCollback,title,time).show();
    }

    public static final DisplayMetrics getWindowInfo(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);

        return metric;
    }

    public static void showMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void getPermission(String permission, Activity context, int repuestid, Handler mHandler)
    {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{permission},
                        repuestid);
            }
            else
            {
                if(mHandler != null)
                mHandler.sendEmptyMessage(repuestid);
            }
        } else {
            if(mHandler != null)
            mHandler.sendEmptyMessage(repuestid);
        }
    }

    public static boolean strlen(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }

        int len = 0;

        char c;
        for (int i = str.length() - 1; i >= 0; i--) {
            c = str.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                // 字母, 数字
                len++;
            } else {
                if (Character.isLetter(c)) { // 中文
                    len += 2;
                } else { // 符号或控制字符
                    len++;
                }
            }
        }

        if (len > 2) {
            return true;
        } else {
            return false;
        }
    }

    public static void measureHit(TextView mview , int hit)
    {
        if (hit < 100) {
            mview.setText(String.valueOf(hit));
            mview.setVisibility(View.VISIBLE);
            if (hit == 0) {
                mview.setVisibility(View.INVISIBLE);
            }
        } else {
            mview.setVisibility(View.VISIBLE);
            mview.setText("99+");
        }
    }

    public static void setHit(int hit,TextView textView) {
        if(hit > 99) {
            textView.setText("99+");
            textView.setVisibility(View.VISIBLE);
        }
        else if(hit > 0 && hit <= 99)
        {
            textView.setText(String.valueOf(hit));
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public static int getHit(int hit) {
        if(hit > 99) {
            return 99;
        }
        else
        {
            return  hit;
        }
    }

    public static void sendSampleBroadCast(Context context , String action) {
        Intent intent = new Intent(action);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public static String measureToken(String result) {
        JSONObject mobj = null;
        try {
            mobj = new JSONObject(result);
            if (mobj.has("token")) {
                return mobj.getString("token");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean success(String result) {
        JSONObject mobj = null;
        try {
            mobj = new JSONObject(result);

            if (mobj.has("status")) {
                if(mobj.getString("status").equals("500"))
                {
                    return false;
                }
            }

            if(mobj.has("code"))
            {
                if(mobj.getInt("code") == 0|| mobj.getInt("code") == 1)
                {
                    return true;
                } else {

                    return false;
                }
            }

            if(mobj.has("isSucess"))
            {
                if(mobj.getBoolean("isSucess") == true)
                {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean oasuccess(String result) {
        JSONObject mobj = null;
        try {

            if (mobj.has("status")) {
                if(mobj.getString("status").equals("500"))
                {
                    return false;
                }
            }

            mobj = new JSONObject(result);
            if(mobj.has("code"))
            {
                if(mobj.getInt("code") == 0)
                {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getfailmessage(String json) {
        String message = "操作失败";
        if (json != null) {
            try {
                JSONObject object = new JSONObject(json);
                if (object.has("message"))
                    message = object.getString("message");
                if (object.has("msg"))
                    message = object.getString("msg");
//                if (object.has("data"))
//                {
//                    JSONObject jo = object.getJSONObject("data");
//                    message = jo.getString("msg");
//                }


            } catch (JSONException e) {
                e.printStackTrace();
                return message;
            }
            return message;
        } else {
            return message;
        }

    }

    public static int tokenresult(String json) {

        try {
            JSONObject mobj = new JSONObject(json);
            if (mobj.has("code")) {
                if(mobj.getString("code").equals("-1"))
                {
                    return -1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Calendar getCalendar(String time)
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {

            c.setTime(format.parse(formateTime(time)));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c;
    }

    public static String formateTime(String time) {
        if(time.length() < 18)
        {
            for(int i = time.length() ; i < 18 ; i++)
            {
                if(i == 10)
                {
                    time += " ";
                }
                else if(i == 13 || i == 16)
                {
                    time += ":";
                }
                else
                {
                    time += "0";
                }
            }
            return time;
        }
        else
        {
            return time;
        }
    }

    public static String getSizeText(long size) {
        String s1 = "";
        if (size / 1024 / 1024 > 0) {
            if (size / 1024 % 1024 * 100 / 1024 > 9) {
                s1 = String.valueOf(size / 1024 / 1024) + "." + String.valueOf(size / 1024 % 1024 * 100 / 1024) + "M";
            } else {
                s1 = String.valueOf(size / 1024 / 1024) + "." + "0" + String.valueOf(size / 1024 % 1024 * 100 / 1024)
                        + "M";
            }
        } else if (size / 1024 > 0) {
            if (size % 1024 * 100 / 1024 > 9) {
                s1 = String.valueOf(size / 1024) + "." + String.valueOf(size % 1024 * 100 / 1024) + "K";
            } else {
                s1 = String.valueOf(size / 1024) + "." + "0" + String.valueOf(size % 1024 * 100 / 1024) + "K";
            }
        } else if (size > 0) {
            s1 = String.valueOf(size) + "B";
        } else {
            s1 = "0B";
        }
        return s1;
    }

    public static int setContactCycleHead(TextView mhead, String name,int color) {
        String s;
        int colorhead = color;
        if(name.length() > 0)
        {
            if(name.length() > 2)
            {
                s = name.substring(0,2);
                if(s.substring(0, 1).toCharArray()[0]>='a'&&s.substring(0, 1).toCharArray()[0]<='z'){
                    s=(s.substring(0,1).toUpperCase()+s.substring(1,2));
                }
                if(AppUtils.strlen(s))
                {
                    mhead.setText(name.substring(0,1));
                }
                else
                {
                    mhead.setText(name.substring(0,2));
                }
            }
            else
            {
                s = name.toString();
                if(s.substring(0, 1).toCharArray()[0]>='a'&&s.substring(0, 1).toCharArray()[0]<='z'){
                    s=(s.substring(0,1).toUpperCase());
                }
                if(AppUtils.strlen(s))
                {
                    mhead.setText(name.substring(0,1));
                }
                else
                {
                    mhead.setText(name);
                }
            }
        }
        if(colorhead == -1)
        {
            Random random = new Random();
            int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
            colorhead = ranColor;
        }
        GradientDrawable myGrad = (GradientDrawable)mhead.getBackground();
        myGrad.setColor(colorhead);
        return colorhead;
    }

    public static String getuser(String word) {
        String mword = "";
        if (word.length() > 2 && word != null) {
            mword = word.substring(1, word.length() - 2);
        }

        return mword;
    }
//716249 958086
    //2821662 3800730

    public static boolean chatShowTime(String begin, String end) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(end);
            Date d2 = df.parse(begin);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            if (diff > 60 * 1000 * 3) {
                return true;
            } else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static int daysBetween(String smdate, String bdate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void sendBroadcast(Context context,String action, Parcelable mParcelable) {
        Intent intent = new Intent(action);
        intent.putExtra("item",mParcelable);
        context.sendBroadcast(intent);
    }

    public static String getContactHead(String name) {
        String s = "";
        if (name.toString().length() > 0) {
            if (name.length() > 2) {
                s = name.substring(0, 2);
                if (AppUtils.strlen(s)) {
                    return name.substring(0, 1);
                } else {
                    return name.substring(0, 2);
                }
            } else {
                s = name;
                if (AppUtils.strlen(s)) {
                   return name.substring(0, 1);
                } else {
                    return name;
                }
            }
        }
        return "";
    }

    public static String getNumber(String str) {
        String number = "";
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) >= 48 && str.charAt(i) <= 57) || str.charAt(i) == 46 || str.charAt(i) == 44) {
                number += str.charAt(i);
            }
        }
        return number;
    }

    public static boolean checkFreeSize(long size) {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocksLong() * (double) stat
                .getBlockSizeLong()) / 1024 / 1024;
        double addMB = size / 1024 / 1024;
        if (addMB > sdFreeMB) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        List<ActivityManager.RunningTaskInfo> task_info = activityManager
                .getRunningTasks(20);
        String className = "";
        for (int i = 0; i < task_info.size(); i++) {
            if (packageName.equals(task_info.get(i).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

//    private boolean isExistMainActivity(Context context,Class<?> activity) {
//        Intent intent = new Intent(context, activity);
//        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
//        boolean flag = false;
//        if (cmpName != null) {
//            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
//            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
//                if (taskInfo.baseActivity.equals(cmpName)) {
//                    flag = true;
//                    break;
//                }
//            }
//        }
//        return flag;
//    }




    public static Bitmap decodeBitmap(String mfile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        File imfiel = new File(mfile);


        if (imfiel.length() > 6 * 1024 * 1024) {
            options.inSampleSize = 8;
        } else if (imfiel.length() > 4 * 1024 * 1024) {
            options.inSampleSize = 6;
        } else if (imfiel.length() > 3 * 1024 * 1024) {
            options.inSampleSize = 4;
        } else if (imfiel.length() > 2 * 1024 * 1024) {
            options.inSampleSize = 3;
        } else if (imfiel.length() > 1024 * 1024) {
            options.inSampleSize = 2;
        }


        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mfile, options);
        return bitmap;
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        } else {
            int scaledWidth = widthOrg > heightOrg ? heightOrg : widthOrg;
            int scaledHeight = widthOrg > heightOrg ? heightOrg : widthOrg;
//            Bitmap scaledBitmap;
//
//            try{
//                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
//            }
//            catch(Exception e){
//                return null;
//            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = widthOrg > heightOrg ? (widthOrg - heightOrg) / 2 : 0;
            int yTopLeft = widthOrg > heightOrg ? 0 : (heightOrg - widthOrg) / 2;
            try {
                result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, scaledWidth, scaledWidth);
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static String getLangue(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.contains("en")){
            return  "$00000409";

        }else {
            return "$00000804";
        }
    }

    public static int getsutebarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public static int getActionBarHeight(Context context)
    {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static String getFileAbsolutePath(Activity context, Uri fileUri) {
        if (context == null || fileUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, fileUri)) {
            if (isExternalStorageDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(fileUri)) {
                String id = DocumentsContract.getDocumentId(fileUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(fileUri)) {
                String docId = DocumentsContract.getDocumentId(fileUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(fileUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(fileUri))
                return fileUri.getLastPathSegment();
            return getDataColumn(context, fileUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(fileUri.getScheme())) {
            return fileUri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String addString(String base,String add,String slip) {
        String a = base;
        if(a.length() > 0 )
        {
            a += slip+add;
        }
        else
        {
            a += add;
        }
        return a;
    }

    public static String addStringBefore(String base,String add,String slip,int max) {

        String[] list = base.split(slip);
        if(list.length >= max)
        {
            String a = add;
            for(int i = 0 ; i < 5 ; i++)
            {
                if(!list[i].equals(add))
                {
                    a += ","+list[i];
                }
            }
            return a;
        }
        else
        {
            String a = base;
            if(a.length() > 0 )
            {
                a = add+slip+a;
            }
            else
            {
                a = add;
            }
            return a;
        }


    }

    public static String measureStringBefore(String base,String add,String slip) {
        String a = add;
        String[] list = base.split(slip);
        for(int i = 0 ; i < list.length ; i++)
        {
            if(!list[i].equals(add))
            {
                a += ","+list[i];
            }
        }
        return a;
    }

    public static boolean isMobileNum(String telNum){
        Pattern p = Pattern.compile("^(1[3|4|5|7|8][0-9])\\d{8}$");
        Matcher m = p.matcher(telNum);
        return true;
    }

    public static void del(Context context, String telnumber) {
        if (telnumber.length() > 0) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telnumber));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            context.startActivity(intent);
        } else {
            AppUtils.showMessage(context, "号码为空");
        }
    }

    public static PermissionResult checkTel(Activity context,String telnumber)
    {
        PermissionResult mPermissionRepuest = new TelPremissionResult(context,telnumber);
        Handler handler = new PermissionHandler(context,telnumber);
        AppUtils.getPermission(Manifest.permission.CALL_PHONE,context, AppUtils.PERMISSION_REQUEST_TEL,handler);
        return  mPermissionRepuest;
    }

    //255,187,8
    public static SpannableStringBuilder highlight(String text, String target,int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        target = "(?i)" + target;//忽略大小写

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(color);// 需要重复！颜色自行修改
            spannable.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static String md5Decode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static boolean check(String word,String wordslist) {
        String[] words = wordslist.split(",");
        boolean has = false;
        for(int i = 0 ; i < words.length ; i++)
        {
            if(words[i].equals(word))
            {
                has = true;
                break;
            }
        }
        return has;
    }

    public static String getMac(Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(context, "6.0以下", Toast.LENGTH_SHORT).show();
            strMac = getLocalMacAddressFromWifiInfo(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(context, "6.0以上7.0以下", Toast.LENGTH_SHORT).show();
            strMac = getMacAddress(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!TextUtils.isEmpty(getMacAddress())) {
                Log.e("=====", "7.0以上1");
                Toast.makeText(context, "7.0以上1", Toast.LENGTH_SHORT).show();
                strMac = getMacAddress();
                return strMac;
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                Log.e("=====", "7.0以上2");
                Toast.makeText(context, "7.0以上2", Toast.LENGTH_SHORT).show();
                strMac = getMachineHardwareAddress();
                return strMac;
            } else {
                Log.e("=====", "7.0以上3");
                Toast.makeText(context, "7.0以上3", Toast.LENGTH_SHORT).show();
                strMac = getLocalMacAddressFromBusybox();
                return strMac;
            }
        }

        return "02:00:00:00:00:00";
    }


    /**
     * 根据wifi信息获取本地mac
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac =  winfo.getMacAddress();
        return mac;
    }

    public static String getMacAddress(Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }

        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress:" + e.toString());
            }

        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress0:" + e.toString());
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            Log.e("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:"
                    + "access wifi state is enabled");
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

/**
 * android 7.0及以上 （2）扫描各个网络接口获取mac地址
 *
 */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            //"\\u只是代号，请根据具体所需添加相应的符号"
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }


    private static final String ONEPLUS6 = "ONEPLUS A6000";

    /**
     * 获取刘海的高度
     *
     * @param context
     * @return
     */
    public static int obtainCutoutHeight(Context context) {
        // 7.0以下不会有刘海，避免不必要的反射性能损耗
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return 0;
        }
         /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // ------------------------------ Android P 版本 及以上 的统一适配 ------------------------------
            val displayCutout: DisplayCutout? = rootWindowInsets.displayCutout
            if (displayCutout != null) {
                L.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.safeInsetTop)
                return displayCutout.safeInsetTop
            }
            return 0
        }*/

        if (OSUtils.isEmui() && OSUtils.hasHWNotchInScreen(context)) {
            return OSUtils.getHWNotchSize(context)[1];
        }

        if (OSUtils.isMiui() && OSUtils.hasMIUINotchInScreen()) {
            return OSUtils.getMIUINotchSize(context);
        }

        // oppo固定80px
        if (OSUtils.hasOppoNotchInScreen(context)) {
            return 80;
        }

        if (OSUtils.hasVivoNotchInScreen(context)) {
            return obtainStatusBarHeight(context);
        }

        if (ONEPLUS6.equals(Build.MODEL)) {
            return obtainStatusBarHeight(context);
        }

        return 0;
    }

    public static int obtainStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = 0;
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static String getAndriodid(Context context) {
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return ANDROID_ID;
    }

    public static String getUid(Context context) {
        String uid = getAndriodid(context)+android.os.Build.SERIAL;
        return uid;
    }

    public static String getAppUnicode(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences("BASE", 0);
        String code = sharedPre.getString("BASE_CODE", "");
        SharedPreferences.Editor e = sharedPre.edit();
        if(code.length() == 0)
        {
            code = AppUtils.getguid();
            e.putString("BASE_CODE",code);
        }
        e.commit();
        return code;
    }


    public static boolean getAppFirst(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences("BASE", 0);
        boolean code = sharedPre.getBoolean("FIRST_USE", true);
        return code;
    }
    private final static double a = 6378245.0;
    private final static double pi = 3.14159265358979324;
    private final static double ee = 0.00669342162296594626;

    //gcj-02  to  wgs-84
    public static LatLonPoint toWGS84Point(double latitude, double longitude) {
        LatLonPoint dev = calDev(latitude, longitude);
        double retLat = latitude - dev.getLatitude();
        double retLon = longitude - dev.getLongitude();
        dev = calDev(retLat, retLon);
        retLat = latitude - dev.getLatitude();
        retLon = longitude - dev.getLongitude();

        return new LatLonPoint(retLat, retLon);

    }

    //wsg84 to  gcj02
    public static LatLonPoint toGCJ02Piont(double latitude, double longitude) {
        LatLonPoint dev = calDev(latitude, longitude);
        double retLat = latitude - dev.getLatitude();
        double retLon = longitude - dev.getLongitude();
        return new LatLonPoint(retLat, retLon);

    }


    private static LatLonPoint calDev(double wgLat, double wgLon) {
        if (isOutofChina(wgLat, wgLon)) {
            return new LatLonPoint(0, 0);
        }
        double dLat = calLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = calLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new LatLonPoint(dLat, dLon);
    }

    private static double calLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;

        return ret;
    }

    private static double calLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        ;

        return ret;

    }


    private static boolean isOutofChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }

        return false;
    }

    public interface InitView
    {
        void initView(View view);
    }

    public interface DestoryView
    {
        void destoryView(View view);
    }

    private static class MyDismiss implements PopupWindow.OnDismissListener{

        public DestoryView destoryView;
        public View view;

        public MyDismiss(DestoryView destoryView,View view)
        {
            this.destoryView = destoryView;
            this.view = view;
        }

        @Override
        public void onDismiss() {
            destoryView.destoryView(view);
        }
    }

}
