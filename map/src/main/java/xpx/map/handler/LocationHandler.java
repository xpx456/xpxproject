package xpx.map.handler;

import android.app.DownloadManager;
import android.location.Address;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import xpx.map.MapManager;
import xpx.map.entity.MapAddress;
import xpx.map.view.activity.LocationActivity;

//01
public class LocationHandler extends Handler
{

    public static final int EVENT_SET_ADDRESS = 3040100;
    public static final int EVENT_UPDATA_LIST= 3040101;
    public static final int EVENT_SET_ADDRESS_OUT_SIDE = 3040102;
    public static final int EVENT_START_SEARCH = 3040103;
    public static final int EVENT_ON_FOOT= 3040104;
    public LocationActivity theActivity;

    public LocationHandler(LocationActivity activity)
    {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what) {
            case EVENT_UPDATA_LIST:
                ArrayList<PoiItem> items = (ArrayList<PoiItem>) msg.obj;
                if(theActivity.poiSearch != null)
                {
                    PoiSearch.Query query = theActivity.poiSearch.getQuery();
                    int child = theActivity.mListView.getChildCount();
                    int a = query.getPageNum();
                    if(query.getPageNum() == 1)
                    {
                        MapManager.getInstance().mPoiItems.clear();
                        theActivity.mListView.removeAllViews();
                    }
                    else
                    {
                        theActivity.mListView.removeView(theActivity.watiView);
                    }
                    if(items.size() == 30)
                    {
                        query.setPageNum(query.getPageNum()+1);
                    }
                    else
                    {
                        theActivity.poiSearch = null;
                    }
                }
                else{
                    MapManager.getInstance().mPoiItems.clear();
                    theActivity.mListView.removeAllViews();
                }
                ArrayList<MapAddress> addresses = new ArrayList<MapAddress>();
                for(int i = 0 ; i < items.size() ; i++)
                {
                    MapAddress mapAddress = new MapAddress(items.get(i));
                    addresses.add(mapAddress);
                    MapManager.getInstance().mPoiItems.add(mapAddress);
                }
                theActivity.mLocationPresenter.addListView(addresses);
                theActivity.waitDialog.hide();
                theActivity.mLocationPresenter.hidWait();
                break;
            case EVENT_SET_ADDRESS:
                if(!theActivity.mAddress.equals(((AMapLocation)msg.obj).getAddress()))
                {
                    if(theActivity.mAddress.length() == 0)
                    {
                        theActivity.mAddress = ((AMapLocation)msg.obj).getAddress();
                        theActivity.mCity = ((AMapLocation)msg.obj).getCityCode();
                        if(theActivity.getIntent().getBooleanExtra("selectaddress",false) == false)
                        {
                            AMapLocation aMapLocation = MapManager.getInstance().lastAMapLocation;
                            if(aMapLocation.getAddress().equals(((AMapLocation)msg.obj).getAddress()) ||
                                    (aMapLocation.getLatitude() == ((AMapLocation)msg.obj).getLatitude() && aMapLocation.getLongitude() == ((AMapLocation)msg.obj).getLongitude()))
                            {

                            }
                            else
                            {
                                theActivity.mLocationPresenter.selectAddress(theActivity.mSearch.getText());
                            }
                        }
                        else
                        {
                            theActivity.mLocationPresenter.selectAddress2(theActivity.mSearch.getText());
                        }

                    }
                    theActivity.mCity = ((AMapLocation)msg.obj).getCityCode();
                    theActivity.mAddress = ((AMapLocation)msg.obj).getAddress();
                }
                break;
            case EVENT_SET_ADDRESS_OUT_SIDE:
                List<Address> addresses1  = (List<Address>) msg.obj;
                if(addresses1.size() > 0)
                {
                    Address mAddress = addresses1.get(0);
                    if(theActivity.mAddress.length() == 0)
                    {
                        theActivity.mAddress = mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea();
                        theActivity.mCity = mAddress.getLocality();
                        theActivity.poiSearch = null;
                        theActivity.mLocationPresenter.selectAddressOut(addresses1);

                    }
                    theActivity.mCity = mAddress.getLocality();
                    theActivity.mAddress = mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea();
                }
                break;
            case EVENT_START_SEARCH:
                theActivity.mLocationPresenter.startSearch();
                break;
            case EVENT_ON_FOOT:
                theActivity.mLocationPresenter.doOnFoot();
                break;

        }
    }
}
