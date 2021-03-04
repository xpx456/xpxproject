package com.dk.dkhome.utils;

import android.content.SharedPreferences;

import com.dk.dkhome.R;
import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.Device;
import com.dk.dkhome.entity.Food;
import com.dk.dkhome.entity.FoodType;
import com.dk.dkhome.entity.SportData;
import com.dk.dkhome.entity.UserDefine;
import com.dk.dkhome.view.DkhomeApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import intersky.apputils.TimeUtils;

public class SportDataManager {


    public static volatile SportDataManager sportDataManager;
    public HashMap<String,ArrayList<Course>> dayPlans = new HashMap<String,ArrayList<Course>>();
    public HashMap<String, Course> allPlans = new HashMap<String, Course>();
    public HashMap<String, SportData> dayData= new HashMap<String, SportData>();
    public SportData today;
    public static SportDataManager init() {
        if (sportDataManager == null) {
            synchronized (SportDataManager.class) {
                if (sportDataManager == null) {
                    sportDataManager = new SportDataManager();
                    DBHelper.getInstance(DkhomeApplication.mApp).scanPlans(sportDataManager.dayPlans,sportDataManager.allPlans);
                    sportDataManager.today =DBHelper.getInstance(DkhomeApplication.mApp).scanData(sportDataManager.dayData);
                } else {
                    DBHelper.getInstance(DkhomeApplication.mApp).scanPlans(sportDataManager.dayPlans,sportDataManager.allPlans);
                    sportDataManager.today =DBHelper.getInstance(DkhomeApplication.mApp).scanData(sportDataManager.dayData);
                }
            }
        }
        return sportDataManager;
    }

    public Course getSameCourse(Course course) {
        ArrayList<Course> courses = dayPlans.get(TimeUtils.getDate());
        if(courses != null)
        {
            for(int i = 0 ; i < courses.size() ; i++)
            {
                Course course1 = courses.get(i);
                if(course1.url.equals(course.url) && course.url.length() > 0)
                {
                    return course1;
                }
                else if(course1.path.equals(course.path) && course.path.length() > 0) {
                    return course1;
                }
            }
        }
        return null;
    }

    public void updataData(String[] data, Course course, Device device) {
        if(today == null)
        {
            today =DBHelper.getInstance(DkhomeApplication.mApp).scanData(sportDataManager.dayData);
        }
        today.updata(data,course,device);
        DBHelper.getInstance(DkhomeApplication.mApp).updataData(today);
    }

    public double getAllToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.gettotalCarl()+sportData.baseCarl;
        }
        return carl;
    }

    public double getToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.gettotalCarl();
        }
        return carl;
    }

    public double getBToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.baseCarl;
        }
        return carl;
    }

    public int getTotalDuring() {
        int during = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            during += sportData.gettotalTime();
        }
        return during;
    }

    public double getEllipticalToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.exercise.totalcarl;
        }
        return carl;
    }

    public double getRowingToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.rowing.totalcarl;
        }
        return carl;
    }


    public double getSpinningToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.spinning.totalcarl;
        }
        return carl;
    }

    public double getExerciseToalCarl() {
        double carl = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            carl += sportData.exercise.totalcarl;
        }
        return carl;
    }

    public double getTotalDis() {
        double dis = 0;
        for(Map.Entry<String,SportData> temp: dayData.entrySet())
        {
            SportData sportData = temp.getValue();
            dis += sportData.gettotalDis();
        }
        return dis;
    }
}
