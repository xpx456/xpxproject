package com.dk.dkpad.entity;

import com.dk.dkpad.TestManager;
import com.dk.dkpad.view.DkPadApplication;


import org.json.JSONArray;
import org.json.JSONException;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;

public class TestItem {

    public int current = 0;
    public int currentLeavel = 1;
    public String uid;
    public String oid;
    public boolean finish = false;
    public int sunleavel = 0;
    public JSONArray hashopLeave = new JSONArray();
    public JSONArray leavels = new JSONArray();
    public JSONArray speeds = new JSONArray();
    public JSONArray herts = new JSONArray();
    public JSONArray rpm = new JSONArray();
    public String rid = AppUtils.getguid();
    public String date = TimeUtils.getDate();
    public String time = TimeUtils.getTime();
    public Double averageSpeed = 0.0;
    public Double topSpeed = 0.0;
    public Double distence = 0.0;
    public boolean isstart = false;
    public void updateLeave() {

        if(this.current == 0)
        {
            date = TimeUtils.getDate();
            time = TimeUtils.getTime();
        }

        current++;

        if(current == hashopLeave.length()* DkPadApplication.OPTATION_ITEM_PER_SECOND)
        {
            finish = true;
        }
        else
        {
            if(current%DkPadApplication.OPTATION_ITEM_PER_SECOND == 0)
            {
                try {
                    currentLeavel = hashopLeave.getInt(current/DkPadApplication.OPTATION_ITEM_PER_SECOND);
                    if(TestManager.testManager != null)
                    TestManager.testManager.setLeavel(currentLeavel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void recordData(String[] data) {
        leavels.put(data[2]);
        speeds.put(data[0]);
        herts.put(data[1]);
        rpm.put(data[3]);
        Double sp = Double.valueOf(data[0]);
        if(sp >topSpeed)
        {
            topSpeed = sp;
        }
        sunleavel += Integer.valueOf(data[2]);
        distence += sp*10/36;
        if(current > 0)
        averageSpeed = distence*36/10/current;
    }

    public String praseCurrentTime()
    {
        int s = current%60;
        int m = current/60;
        return String.format("%02d:%02d",m,s);
    }

}
