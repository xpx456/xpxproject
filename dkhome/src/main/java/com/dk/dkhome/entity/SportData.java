package com.dk.dkhome.entity;

import com.dk.dkhome.R;
import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.view.DkhomeApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;

public class SportData {

    public String id = AppUtils.getguid();
    public String time = "";
    public String json = "";
    public String userid = "";
    public int baseCarl = 0;
    public Data elliptical = new Data();
    public Data exercise = new Data();
    public Data spinning = new Data();
    public Data rowing = new Data();


    public void initjson() {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject ellipticaljo = jsonObject.getJSONObject("elliptical");
            JSONObject rowingjo = jsonObject.getJSONObject("rowing");
            JSONObject exercisejo = jsonObject.getJSONObject("exercise");
            JSONObject spinningjo = jsonObject.getJSONObject("spinning");
            elliptical.leavels = ellipticaljo.getJSONArray("leavel");
            elliptical.herts = ellipticaljo.getJSONArray("hert");
            elliptical.speeds = ellipticaljo.getJSONArray("speed");
            elliptical.rpm = ellipticaljo.getJSONArray("rpm");
            elliptical.carl = ellipticaljo.getJSONArray("carl");
            elliptical.initData();
            rowing.leavels = rowingjo.getJSONArray("leavel");
            rowing.herts = rowingjo.getJSONArray("hert");
            rowing.speeds = rowingjo.getJSONArray("speed");
            rowing.rpm = rowingjo.getJSONArray("rpm");
            rowing.carl = rowingjo.getJSONArray("carl");
            rowing.initData();
            spinning.leavels = spinningjo.getJSONArray("leavel");
            spinning.herts = spinningjo.getJSONArray("hert");
            spinning.speeds = spinningjo.getJSONArray("speed");
            spinning.rpm = spinningjo.getJSONArray("rpm");
            spinning.carl = spinningjo.getJSONArray("carl");
            spinning.initData();
            exercise.leavels = exercisejo.getJSONArray("leavel");
            exercise.herts = exercisejo.getJSONArray("hert");
            exercise.speeds = exercisejo.getJSONArray("speed");
            exercise.rpm = exercisejo.getJSONArray("rpm");
            exercise.carl = exercisejo.getJSONArray("carl");
            exercise.initData();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String praseDataJson() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("elliptical",elliptical.getJson());
            jsonObject.put("rowing",rowing.getJson());
            jsonObject.put("spinning",spinning.getJson());
            jsonObject.put("exercise",exercise.getJson());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public void updata(String[] data,Course course,Device device) {
        if(device.typeName.equals(DkhomeApplication.mApp.getString(R.string.device_type_elliptical_machine)))
        {
            elliptical.updata(course,data,device);
        }
        else if(device.typeName.equals(DkhomeApplication.mApp.getString(R.string.device_type_rowing_machine)))
        {
            rowing.updata(course,data,device);
        }
        else if(device.typeName.equals(DkhomeApplication.mApp.getString(R.string.device_type_spinning_bike)))
        {
            spinning.updata(course,data,device);
        }
        else if(device.typeName.equals(DkhomeApplication.mApp.getString(R.string.device_type_exercise_bike)))
        {
            exercise.updata(course,data,device);
        }
    }

    public double gettotalCarl()
    {
        return elliptical.totalcarl+rowing.totalcarl+spinning.totalcarl+exercise.totalcarl;
    }

    public int gettotalTime()
    {
        return elliptical.during+rowing.during+spinning.during+exercise.during;
    }

    public double gettotalDis()
    {
        return elliptical.totaldis+rowing.totaldis+spinning.totaldis+exercise.totaldis;
    }


    public void addData() {

    }
}
