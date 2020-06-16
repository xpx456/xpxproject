package intersky.sign.handler;

import android.location.Address;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import intersky.sign.SignManager;
import intersky.sign.view.activity.AddressSelectActivity;

//00
public class AddressSelectHandler extends Handler
{

    public static final int EVENT_SET_ADDRESS = 3230000;
    public static final int EVENT_UPDATA_LIST= 3230001;
    public static final int EVENT_SET_ADDRESS_OUT_SIDE = 3230002;

    public AddressSelectActivity theActivity;

    public AddressSelectHandler(AddressSelectActivity activity)
    {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg)
    {
        if (theActivity != null)
        {
            switch (msg.what)
            {
                case EVENT_SET_ADDRESS:
                    if(!theActivity.mAddress.equals(((AMapLocation)msg.obj).getAddress()))
                    {
                        if(theActivity.mAddress.length() == 0)
                        {
                            theActivity.isall = false;
                            theActivity.page = 0;
                            SignManager.getInstance().mPoiItems.clear();
                            theActivity.mAddress = ((AMapLocation)msg.obj).getAddress();
                            theActivity.mCity = ((AMapLocation)msg.obj).getCityCode();
                            if(theActivity.isall == false)
                                theActivity.mAddressSelectPresenter.selectAddress("");
                        }
                        theActivity.mCity = ((AMapLocation)msg.obj).getCityCode();
                        theActivity.mAddress = ((AMapLocation)msg.obj).getAddress();
                    }
                    break;
                case EVENT_SET_ADDRESS_OUT_SIDE:
                    List<Address> addresses  = (List<Address>) msg.obj;
                    if(addresses.size() > 0)
                    {
                        Address mAddress = addresses.get(0);
                        if(theActivity.mAddress.length() == 0)
                        {
                            theActivity.mAddress = mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea();
                            theActivity.mCity = mAddress.getLocality();
                            if(theActivity.isall == false)
                                theActivity.mAddressSelectPresenter.selectAddress("");
                        }
                        theActivity.mCity = mAddress.getLocality();
                        theActivity.mAddress = mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea();
                    }
                    break;
                case EVENT_UPDATA_LIST:
                    if(((ArrayList<PoiItem>)msg.obj).size() == 30)
                    {
                        theActivity.page++;
                    }
                    else
                    {
                        theActivity.isall = true;
                    }
                    SignManager.getInstance().mPoiItems.addAll((ArrayList<PoiItem>)msg.obj);
                    theActivity.mAddressAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
