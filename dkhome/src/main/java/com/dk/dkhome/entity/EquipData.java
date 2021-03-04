package com.dk.dkhome.entity;

import android.os.Message;

import com.dk.dkhome.R;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.DkhomeApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.select.entity.CustomSelect;
import intersky.select.entity.Select;
import xpx.usb.XpxUsbManager;

public class EquipData {


    public static volatile EquipData equipData;
    public ArrayList<Select> selects = new ArrayList<>();
    public ArrayList<CustomSelect> names = new ArrayList<>();
    public static EquipData init() {

        if (equipData == null) {
            synchronized (EquipData.class) {
                if (equipData == null) {
                    equipData = new EquipData();
                    equipData.selects.clear();
                    Select select = new Select("1","DK20N02");
                    equipData.selects.add(select);
                    select = new Select("2","DK20W02");
                    equipData.selects.add(select);
                    select = new Select("3","DKTEST");
                    equipData.selects.add(select);
                    CustomSelect customSelect;
                    customSelect = new CustomSelect("1", DkhomeApplication.mApp.getString(R.string.device_type_elliptical_machine));
                    equipData.names.add(customSelect);
                    customSelect = new CustomSelect("2", DkhomeApplication.mApp.getString(R.string.device_type_rowing_machine));
                    equipData.names.add(customSelect);
                    customSelect = new CustomSelect("3", DkhomeApplication.mApp.getString(R.string.device_type_exercise_bike));
                    equipData.names.add(customSelect);
                    customSelect = new CustomSelect("4", DkhomeApplication.mApp.getString(R.string.device_type_spinning_bike));
                    equipData.names.add(customSelect);
                }
                else
                {
                    equipData.selects.clear();
                    Select select = new Select("1","DK20N02");
                    equipData.selects.add(select);
                    select = new Select("2","DK20W02");
                    equipData.selects.add(select);
                    select = new Select("3","DKTEST");
                    equipData.selects.add(select);
                    CustomSelect customSelect;
                    customSelect = new CustomSelect("1", DkhomeApplication.mApp.getString(R.string.device_type_elliptical_machine));
                    equipData.names.add(customSelect);
                    customSelect = new CustomSelect("2", DkhomeApplication.mApp.getString(R.string.device_type_rowing_machine));
                    equipData.names.add(customSelect);
                    customSelect = new CustomSelect("3", DkhomeApplication.mApp.getString(R.string.device_type_exercise_bike));
                    equipData.names.add(customSelect);
                    customSelect = new CustomSelect("4", DkhomeApplication.mApp.getString(R.string.device_type_spinning_bike));
                    equipData.names.add(customSelect);
                }
            }
        }
        return equipData;
    }

    public static EquipData getInstance() {
        return equipData;
    }

    public EquipData() {
    }


    public static byte [] set = {0x25, 0x03, 0x12, 0x01, (byte) 0xa0};
    public static byte [] stop = {0x25, 0x09, 0x00, 0x01, (byte) 0xa0};
    public static byte [] check = {0x25, 0x08, 0x00, 0x01, (byte) 0xa0};
    public static final int DK20N02  = 1;
    public static final int DK20W02  = 2;
    public static final int DKTEST  = 3;

    public static int[][] data = {{2,2,2,2,2,2,2, 3,3,3, 4,4,4,4,4, 5,5,5,5,5, 6,6,6,6, 7,7,7,7,7, 8,8,8},
            {4,5,6,7,8, 9,10,11,12,13, 14,15,16,17,18, 19, 20,21,22,23, 24,25,26,27,28,29,30,31,32,33,34,35},
            {8,12,14,16,17,19,21,23,25,27,29,31,33,34,36,38,40,42,44,46,48,50,52,54,56,58,60,62,64,66,68,71},
            {13,17,21,25,29,32,36,38,42,45,48,52,56,58,62,64,68,73,76,79,83,86,89,93,97,100,104,107,111,115,119,124},
            {18,24,30,36,40,45,50,56,62,67,73,79,84,89,95,100,106,111,115,120,126,131,136,142,147,153,159,165,171,178,184,188},
            {25,33,41,50,57,64,71,78,85,92,100,107,114,121,128,135,142,150,157,164,171,178,185,192,200,208,217,225,234,242,251,260},
            {30,41,52,64,73,84,94,102,112,121,132,142,150,160,168,175,186,195,203,211,222,230,239,249,258,268,280,290,302,313,323,335},
            {35,50,64,80,89,100,114,128,140,150,163,174,184,194,204,215,228,241,251,263,274,284,295,308,320,333,345,361,373,385,398,412},
            {46,64,83,100,115,130,145,158,174,185,201,218,232,243,256,268,286,298,310,324,337,349,367,384,395,415,431,450,464,480,495,512},
            {50,75,100,121,135,150,172,185,200,221,241,258,272,292,308,321,341,357,370,384,406,418,434,448,465,482,503,525,540,555,574,597},
            {61,85,107,138,152,176,197,214,236,252,278,300,316,334,351,369,394,410,426,446,465,481,505,520,541,559,582,608,623,644,658,675},
            {68,96,122,152,171,194,217,235,260,275,301,322,338,362,382,400,424,449,454,476,500,518,532,550,569,589,615,640,657,676,697,730}};


    public static int getBaseCarl(User user) {
        int w = user.lastweight;
        int t = user.tall;
        int y = Integer.valueOf(user.age);
        if(user.sex == 0)
        {
            return (int) (10*w+6.25*t-5*y+5);
        }
        else
        {
            return (int) (10*w+6.25*t-5*y-161);
        }
    }

    public static int goalCarl(int weight,int day) {
        return 7700*weight/day;
    }

    public static int weightCarl(int weight) {
        return 7700*weight;
    }

    public static String getDeviceName(int type)
    {
        String name = "";
        switch (type)
        {
            case DK20N02:
                name = "DK20N02";
                break;
            case DK20W02:
                name = "DK20W02";
                break;
            case DKTEST:
                name = "DKTEST";
                break;
        }
        return name;
    }

    public static int setLeave(int leavel,Device device)
    {
        if(device.deviceForm == DKTEST)
        {
            return leavel;
        }
        int a = 100/ device.maxleave;
        int b = leavel/a;
        if(b > device.maxleave)
        {
            b = device.maxleave;
        }
        return b;

    }

    public static byte[] setLeaveCmd(int leave,Device device)
    {
        if(device.deviceForm == DK20N02 || device.deviceForm == DK20W02)
        {
            BigInteger target = new BigInteger(String.valueOf(leave));
            long b = Long.parseLong(String.valueOf(leave));
            String a = Long.toHexString(b);
            if(a.length() == 1)
            {
                a = "0"+a;
            }
            String com = "2503"+a+"00a0";
            try {
                return AppUtils.hex2byte(com);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        else{
            BigInteger target = new BigInteger(String.valueOf(leave));
            long b = Long.parseLong(String.valueOf(leave));
            String a = Long.toHexString(b);
            if(a.length() == 1)
            {
                a = "0"+a;
            }
            String com = "250300"+a+"a0";
            try {
                return AppUtils.hex2byte(com);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static void praseData(byte[] data, Device device, TestManager testManager)
    {
        if(device.deviceForm == DK20N02 || device.deviceForm == DK20W02)
        {
            praseData2(data,testManager);
        }
        else if(device.deviceForm == DKTEST)
        {
            praseData3(data,testManager);
        }
    }

    public static void praseData2(byte[] data, TestManager testManager) {
        if (data[0] == 0x55) {
            if (data.length >= 11) {
                if (data[10] == 0x3f) {

                    if (data[1] == 0x25) {
                        String v1 = String.format("%02x", data[3]);
                        String v2 = String.format("%02x", data[2]);

                        String l1 = String.format("%02x", data[5]);
                        String l2 = String.format("%02x", data[4]);

                        String r1 = String.format("%02x", data[9]);
                        String r2 = String.format("%02x", data[8]);

                        String s = String.format("%02x", data[6]);
                        int d = Integer.valueOf(l1 + l2, 16);
                        int sate = Integer.valueOf(s, 16);
                        int v = Integer.valueOf(v1 + v2, 16);
                        int r = Integer.valueOf(r1 + r2, 16);
                        String satue = Integer.toBinaryString(sate);
                        double rv = 0;
                        rv = v / 10;

                        String[] dataget = new String[8];
                        DecimalFormat df = new DecimalFormat("#.00");
                        dataget[0] = String.valueOf(df.format(rv));
                        dataget[1] = "0";
                        dataget[2] = "0";
                        dataget[3] = String.valueOf(r);
                        dataget[4] = String.valueOf(v);
                        dataget[5] = "0";
                        if (data[1] == 0x15) {
                            dataget[6] = "1";
                        } else {
                            dataget[6] = "0";
                        }
                        dataget[7] = satue;
                        if (satue.startsWith("1")) {
                            if (testManager.change == false) {
                                testManager.change = true;
                                testManager.changecurrent = System.currentTimeMillis();
                            }
                        } else {
                            testManager.change = false;
                        }

                        if ((System.currentTimeMillis() - testManager.changecurrent) > XpxUsbManager.CHANGE_TIME && testManager.change == true) {
                            dataget[5] = "1";
                            if (testManager.deviceerror = false)
                                testManager.deviceerror = true;
                        }

                        if (testManager.testHandler != null) {
                            if (testManager.getData != null) {
                                Message message = new Message();
                                message.what = TestManager.GET_DATA;
                                message.obj = dataget;
                                testManager.testHandler.sendMessage(message);
                            }
                        }
                    }
                    if (data[1] == 0x15) {

                    }
                }
            }
        }
    }


    public static void praseData3(byte[] data, TestManager testManager) {


        if (data[0] == 0x55) {
            if (data.length >= 11) {
                if (data[10] == 0x3f) {

                    if (data[1] == 0x25 || data[1] == 0x15) {
                        String v1 = String.format("%02x", data[3]);
                        String v2 = String.format("%02x", data[2]);

                        String l1 = String.format("%02x", data[5]);
                        String l2 = String.format("%02x", data[4]);

                        String r1 = String.format("%02x", data[9]);
                        String r2 = String.format("%02x", data[8]);

                        String p1 = String.format("%02x", data[7]);

                        String s = String.format("%02x", data[6]);
                        int d = Integer.valueOf(l1 + l2, 16);
                        int sate = Integer.valueOf(s, 16);
                        int v = Integer.valueOf(v1 + v2, 16);
                        int r = Integer.valueOf(r1 + r2, 16);
                        int p = Integer.valueOf(p1, 16);

                        String satue = Integer.toBinaryString(sate);
                        double rv = 0;
                        rv = v / 10;

                        String[] dataget = new String[8];
                        DecimalFormat df = new DecimalFormat("#.00");
                        dataget[0] = String.valueOf(df.format(rv));
                        dataget[1] = "0";
                        dataget[2] = String.valueOf(p);
                        dataget[3] = String.valueOf(r);
                        dataget[4] = String.valueOf(v);
                        dataget[5] = "0";
                        if (data[1] == 0x15) {
                            dataget[6] = "1";
                        } else {
                            dataget[6] = "0";
                        }
                        dataget[7] = satue;
                        if (satue.startsWith("1")) {
                            if (testManager.change == false) {
                                testManager.change = true;
                                testManager.changecurrent = System.currentTimeMillis();
                            }
                        } else {
                            testManager.change = false;
                        }

                        if ((System.currentTimeMillis() - testManager.changecurrent) > XpxUsbManager.CHANGE_TIME && testManager.change == true) {
                            dataget[5] = "1";
                            if (testManager.deviceerror = false)
                                testManager.deviceerror = true;
                        }

                        if (testManager.getData != null) {
                            Message message = new Message();
                            message.what = TestManager.GET_DATA;
                            message.obj = dataget;
                            testManager.testHandler.sendMessage(message);
                        }


                    }
                    if (data[1] == 0x25) {

                    }
                }
            }
        }
    }

    public static void praseStringData(Course testItem, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            testItem.leavels = jsonObject.getJSONArray("leavel");
            testItem.herts = jsonObject.getJSONArray("hert");
            testItem.speeds = jsonObject.getJSONArray("speed");
            testItem.rpm = jsonObject.getJSONArray("rpm");
            testItem.carl = jsonObject.getJSONArray("carl");
            double car = 0;
            for(int i = 0 ; i < testItem.carl.length() ; i++)
            {
                car += testItem.carl.getDouble(i);
            }
            testItem.totalCarl =  car;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//
    public static String initStringData(Course testItem) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("leavel", testItem.leavels);
            jsonObject.put("hert", testItem.herts);
            jsonObject.put("speed", testItem.speeds);
            jsonObject.put("rpm", testItem.rpm);
            jsonObject.put("carl", testItem.carl);
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static String getCourseJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray ja = new JSONArray();
            jsonObject.put("data", ja);
            JSONObject jo = new JSONObject();
            jo.put("cid","df4299e8-7ce2-3276-c3a3-0de9e7be9d54");
            jo.put("cname","动感单车43期");
            jo.put("img","http://www.intersky.com.cn/app/android.version/df4299e8-7ce2-3276-c3a3-0de9e7be9d54.png");
            jo.put("video","http://www.intersky.com.cn/app/android.version/testvidoe.avi");
            jo.put("time","49:23");
            ja.put(jo);
            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static double getCar(int leavel, int rpm, Device device) {
        if(rpm < 1){
            return 0;
        }
        int a = 100 / device.maxleave;
        int b = leavel / a;
        if (b > device.maxleave) {
            b = device.maxleave;
        }
        b = b - 1;
        if (b < 0) {
            b = 0;
        }

        int c = rpm / 10 - 1;
        if (c < 0) {
            c = 0;
        } else if (c > 11) {
            c = 11;
        }
        double k = EquipData.data[c][b] * 2.5/1000;
        return k;
    }


    public static double getWork(int leavel, int rpm, Device device) {
        if(rpm < 1){
            return 0;
        }
        int a = 100 / device.maxleave;
        int b = leavel / a;
        if (b > device.maxleave) {
            b = device.maxleave;
        }
        b = b - 1;
        if (b < 0) {
            b = 0;
        }

        int c = rpm / 10 - 1;
        if (c < 0) {
            c = 0;
        } else if (c > 11) {
            c = 11;
        }
        double k = EquipData.data[c][b];
        return k;
    }
}
