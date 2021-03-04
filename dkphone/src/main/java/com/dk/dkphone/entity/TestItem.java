package com.dk.dkphone.entity;

import com.dk.dkphone.TestManager;
import com.dk.dkphone.view.DkPhoneApplication;

import org.json.JSONArray;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;

public class TestItem {

    public int current = 0;
    public int currentLeavel = 1;
    public String uid;
//    public String oid;
//    public boolean finish = false;
    public int sunleavel = 0;
    public double allcarl = 0;
//    public JSONArray hashopLeave = new JSONArray();
    public JSONArray leavels = new JSONArray();
    public JSONArray speeds = new JSONArray();
    public JSONArray herts = new JSONArray();
    public JSONArray rpm = new JSONArray();
    public JSONArray carl = new JSONArray();
    public String rid = AppUtils.getguid();
    public String date = TimeUtils.getDate();
    public String time = TimeUtils.getTime();
    public Double averageSpeed = 0.0;
    public Double topSpeed = 0.0;
    public Double distence = 0.0;
    public boolean isstart = false;
    public void updateCurrent(String leave,boolean set) {
        if(isstart)
        {
            if(this.current == 0)
            {
                date = TimeUtils.getDate();
                time = TimeUtils.getTime();
            }

            current++;
        }


//        if(current == hashopLeave.length()*DkPadApplication.OPTATION_ITEM_PER_SECOND)
//        {
//            finish = true;
//        }
//        else
//        {
//            if(current%DkPadApplication.OPTATION_ITEM_PER_SECOND == 0)
//            {
//                try {
//                    currentLeavel = hashopLeave.getInt(current/DkPadApplication.OPTATION_ITEM_PER_SECOND);
//                    if(TestManager.testManager != null)
//                    TestManager.testManager.setLeavel(currentLeavel);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            else
//            {
//                if(TestManager.testManager.xpxUsbManager.SPEED_MODE == 2)
//                currentLeavel = Integer.valueOf(leave);
//            }
//        }
        if(TestManager.testManager.xpxUsbManager.SPEED_MODE == 2)
        {

            if(TestManager.testManager.preset == -1 )
            {
                currentLeavel = Integer.valueOf(leave);
            }
            else
            {
                if(TestManager.testManager.preset == Integer.valueOf(leave))
                {
                    currentLeavel = Integer.valueOf(leave);
                    TestManager.testManager.preset = -1;
                }
            }
//            currentLeavel = Integer.valueOf(leave);
        }



        //TestManager.testManager.flagset = false;
    }

    public void updataLeave()
    {

    }

    public void recordData(String[] data) {
        leavels.put(data[2]);
        speeds.put(data[0]);
        herts.put(data[1]);
        rpm.put(data[3]);
        carl.put(getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3])));
        Double sp = Double.valueOf(data[0]);
        if(sp >topSpeed)
        {
            topSpeed = sp;
        }
        sunleavel += Integer.valueOf(data[2]);
        distence += sp*10/36;
        allcarl += getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3]));
        if(current > 0)
        averageSpeed = distence*36/10/current;
    }

    public String praseCurrentTime()
    {
        int s = current%60;
        int m = current/60;
        return String.format("%02d:%02d",m,s);
    }

    public Double getCar(int leavel, int rpm) {
        int a = 100 / DkPhoneApplication.mApp.maxSelect;
        int b = leavel / a;
        if (b > DkPhoneApplication.mApp.maxSelect) {
            b = DkPhoneApplication.mApp.maxSelect;
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
        double k = EquipData.data[c][b] * 2.5;
        return k;
    }
}
