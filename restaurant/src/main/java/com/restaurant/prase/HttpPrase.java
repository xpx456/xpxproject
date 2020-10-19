package com.restaurant.prase;

import android.content.Context;

import com.restaurant.entity.Device;
import com.restaurant.entity.Location;
import com.restaurant.view.RestaurantApplication;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class HttpPrase {

    public static void praseLocation(NetObject net, Context context) {
        try {
            String json = net.result;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            RestaurantApplication.mApp.locationHashMap.clear();
            RestaurantApplication.mApp.grops.clear();
            RestaurantApplication.mApp.locations.clear();
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                Location location = new Location();
                location.addressid = jo.getString("eqptLocationId");
                location.address = jo.getString("eqptLocation");
                location.parentid = jo.getString("parentId");
                RestaurantApplication.mApp.locationHashMap.put(location.addressid,location);
                location.locations = RestaurantApplication.mApp.grops.get(location.addressid);
                if(location.locations == null) {
                    location.locations = new ArrayList<Location>();
                    RestaurantApplication.mApp.grops.put(location.addressid,location.locations);
                }
                if(location.parentid.length() > 0 && !location.parentid.equals("0"))
                {
                    ArrayList<Location> locations =  RestaurantApplication.mApp.grops.get(location.parentid);
                    if(locations == null) {
                        locations = new ArrayList<Location>();
                        RestaurantApplication.mApp.grops.put(location.parentid,locations);
                    }
                    locations.add(location);
                }
                else
                {
                    RestaurantApplication.mApp.locations.add(location);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Device praseDevice(NetObject net, Context context)
    {
        try {
            String json = net.result;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            Device device = new Device();
            if(!jo.getString("eqptLocationId").equals("null"))
            device.addressid = jo.getString("eqptLocationId");
            if(jo.has("entEqptLocation"))
            {
                device.address = jo.getString("entEqptLocation");
            }
            else
            {
                if(!jo.getString("eqptLocationId").equals("null"))
                {
                    Location location = RestaurantApplication.mApp.locationHashMap.get(device.addressid);
                    if(location != null)
                    {
                        device.address = location.address;
                    }
                }
            }
            device.cname = jo.getString("entEqptName");
            if(!jo.getString("isAttendanceEqpt").equals("null"))
            device.isattence = jo.getString("isAttendanceEqpt");
            if(!jo.getString("isEntEqpt").equals("null"))
            device.isaccess = jo.getString("isEntEqpt");
            return device;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
