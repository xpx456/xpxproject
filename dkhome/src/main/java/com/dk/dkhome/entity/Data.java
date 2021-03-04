package com.dk.dkhome.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Data {

    public String type = "";
    public JSONArray leavels = new JSONArray();
    public JSONArray herts = new JSONArray();
    public JSONArray speeds = new JSONArray();
    public JSONArray rpm = new JSONArray();
    public JSONArray carl = new JSONArray();
    public JSONArray work = new JSONArray();
    public int during = 0;
    public double totaldis = 0;
    public double totalcarl = 0;
    public void initData() {

        try {
            for (int i = 0; i < speeds.length(); i++) {
                during++;
                totaldis += speeds.getDouble(i)/3600;
                totalcarl += carl.getDouble(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("leavel", leavels);
            jsonObject.put("hert", herts);
            jsonObject.put("speed", speeds);
            jsonObject.put("rpm", rpm);
            jsonObject.put("carl", carl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void updata(Course course,String[] data,Device device) {
        leavels.put(device.nowleavel);
        speeds.put(data[0]);
        herts.put(0);
        rpm.put(data[3]);
        carl.put(String.valueOf(EquipData.getCar(Integer.valueOf(data[2]),
                Integer.valueOf(data[3]),device)));
        during++;
        Double sp = Double.valueOf(data[0]);
        totaldis += sp/3600;
        totalcarl += EquipData.getCar(Integer.valueOf(data[2]),
                Integer.valueOf(data[3]),device);
    }
}
