package com.dk.dkhome.utils;

import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.Device;
import com.dk.dkhome.entity.SportData;
import com.dk.dkhome.entity.UserWeight;
import com.dk.dkhome.view.DkhomeApplication;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.conturypick.DbHelper;

public class WeightManager {

    public static final int STATE_UP = 0;
    public static final int STATE_DOWN = 1;
    public static final int STATE_SAME = 2;
    public static volatile WeightManager weightManager;
    public HashMap<String, UserWeight> dayWeight= new HashMap<String, UserWeight>();
    public UserWeight lastinput;
    public UserWeight lastinput2;
    public int weightstate;
    public static WeightManager init() {
        if (weightManager == null) {
            synchronized (WeightManager.class) {
                if (weightManager == null) {
                    weightManager = new WeightManager();
//                    weightManager.addTest();
                    UserWeight[] data = DBHelper.getInstance(DkhomeApplication.mApp).scanUserWeight(DkhomeApplication.mApp.mAccount,weightManager);
                    weightManager.lastinput = data[0];
                    weightManager.lastinput2 = data[1];
                } else {
                    UserWeight[] data = DBHelper.getInstance(DkhomeApplication.mApp).scanUserWeight(DkhomeApplication.mApp.mAccount,weightManager);
                    weightManager.lastinput = data[0];
                    weightManager.lastinput2 = data[1];
                }
            }
        }
        return weightManager;
    }

//    public void addTest()
//    {
//        UserWeight userWeight = new UserWeight();
//        userWeight.uid = DkhomeApplication.mApp.mAccount.uid;
//        userWeight.date = "2021-02-01";
//        userWeight.weight = "70";
//        DBHelper.getInstance(DkhomeApplication.mApp).addUserWeight(userWeight);
//        userWeight.id = AppUtils.getguid();
//        userWeight.date = "2021-02-02";
//        userWeight.weight = "77";
//        DBHelper.getInstance(DkhomeApplication.mApp).addUserWeight(userWeight);
//        userWeight.id = AppUtils.getguid();
//        userWeight.date = "2021-02-03";
//        userWeight.weight = "72";
//        DBHelper.getInstance(DkhomeApplication.mApp).addUserWeight(userWeight);
//    }

    public void addWeight(int weight) {
        UserWeight userWeight = dayWeight.get(TimeUtils.getDate());
        if(userWeight == null)
        {
            userWeight = new UserWeight();
            userWeight.date = TimeUtils.getDate();
            userWeight.uid = DkhomeApplication.mApp.mAccount.uid;
            userWeight.weight = String.valueOf(weight);
        }
        dayWeight.put(userWeight.date,userWeight);
        DBHelper.getInstance(DkhomeApplication.mApp).addUserWeight(userWeight);
        if(userWeight.date.equals(lastinput.date))
        {
            if(weightManager.lastinput2 == null)
            {
                weightManager.weightstate = WeightManager.STATE_SAME;
            }
            else
            {
                if(Integer.valueOf(userWeight.weight) > Integer.valueOf(lastinput2.weight) )
                {
                    weightManager.weightstate = WeightManager.STATE_UP;
                }
                else if(Integer.valueOf(userWeight.weight) < Integer.valueOf(lastinput2.weight) )
                {
                    weightManager.weightstate = WeightManager.STATE_DOWN;
                }
                else
                {
                    weightManager.weightstate = WeightManager.STATE_SAME;
                }
            }
        }
        else
        {
            if(Integer.valueOf(userWeight.weight) > Integer.valueOf(lastinput.weight) )
            {
                weightManager.weightstate = WeightManager.STATE_UP;
            }
            else if(Integer.valueOf(userWeight.weight) < Integer.valueOf(lastinput.weight) )
            {
                weightManager.weightstate = WeightManager.STATE_DOWN;
            }
            else
            {
                weightManager.weightstate = WeightManager.STATE_SAME;
            }
        }
        lastinput = userWeight;
    }

}
