package xpx.map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;

import intersky.apputils.Onlocation;
import xpx.map.entity.MapAddress;
import xpx.map.view.activity.BigMapActivity;
import xpx.map.view.activity.LocationActivity;

public class MapManager {

    public LatLonPoint mLatLonPoint = new LatLonPoint(0,0);
    public ArrayList<MapAddress> mPoiItems = new ArrayList<MapAddress>();
    public MapFunctions mapFunctions;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private ArrayList<Onlocation> loactionList = new ArrayList<Onlocation>();
    public AMapLocation lastAMapLocation = null;
    public Context context;
    public volatile static MapManager mapManager = null;

    public static MapManager init(MapFunctions signFunctions,Context context) {

        if (mapManager == null) {
            synchronized (MapManager.class) {
                if (mapManager == null) {
                    mapManager = new MapManager(signFunctions,context);
                    mapManager.setMap();
                }
                else
                {
                    mapManager.context = context;
                    mapManager.mapFunctions = signFunctions;
                }
            }
        }
        return mapManager;
    }

    public MapManager(MapFunctions signFunctions,Context context) {
        this.mapFunctions = signFunctions;
        this.context = context;
    }

    public void initLastlocation()
    {
        if(lastAMapLocation == null)
        {
            startLocation();
        }
    }

    public static MapManager getInstance()
    {
        return mapManager;
    }


    public void startBigMap(Context context,String json)
    {
        startLocation();
        Intent intent = new Intent(context,
                BigMapActivity.class);
        intent.putExtra("json",json);
        context.startActivity(intent);
    }

    public void startSelectMap(Context context,String action,String path)
    {
        startLocation();
        Intent intent = new Intent(context, LocationActivity.class);
        intent.setAction(action);
        intent.putExtra("selectaddress",false);
        intent.putExtra("path",path);
        context.startActivity(intent);
    }

    public void startSelectLocationAddress(Context context,String action)
    {
        startLocation();
        Intent intent = new Intent(context, LocationActivity.class);
        intent.setAction(action);
        intent.putExtra("selectaddress",true);
        context.startActivity(intent);
    }


    public void setMap() {
        mlocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(onLocationChanged);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(4000);
        mLocationOption.setOnceLocation(false);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
        mlocationClient.stopLocation();
    }

    public void startLocation()
    {
        if(mlocationClient.isStarted() == true)
        {
            mlocationClient.startLocation();
        }

    }

    public void stopLocation()
    {
        if(mlocationClient.isStarted() == true)
        {
            mlocationClient.stopLocation();
        }
    }

    public interface MapFunctions {
        void sendContact(Context context,Intent intent);
    }

    public AMapLocationListener onLocationChanged = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    for(int i = 0; i < loactionList.size(); i++)
                    {
                        if(loactionList.get(i) != null)
                            loactionList.get(i).location(amapLocation);
                    }
                    lastAMapLocation = amapLocation;
                    if(loactionList.size() == 0)
                    {
                        stopLocation();
                    }
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "locationsss Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());

                }
            }
        }
    };

    public void addLocation(Onlocation onlocation) {
        loactionList.add(onlocation);
        if(loactionList.size() > 0 )
        {
            startLocation();
        }
    }

    public void removeLocation(Onlocation onlocation) {
        loactionList.remove(onlocation);
        if(loactionList.size() == 0 )
        {
            stopLocation();
        }
    }
}
