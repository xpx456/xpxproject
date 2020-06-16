package xpx.map.handler;

import android.location.Address;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import xpx.map.MapManager;
import xpx.map.entity.MapAddress;
import xpx.map.view.activity.BigMapActivity;
import xpx.map.view.activity.LocationActivity;

//00
public class BigMapHandler extends Handler
{

    public static final int EVENT_SET_ADDRESS = 3040000;
    public static final int EVENT_UPDATA_LIST= 3040001;
    public static final int EVENT_SET_ADDRESS_OUT_SIDE = 3040002;
    public static final int EVENT_START_SEARCH = 3040003;
    public static final int EVENT_ON_FOOT= 3040004;
    public BigMapActivity theActivity;

    public BigMapHandler(BigMapActivity activity)
    {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {


        }
    }
}
