package intersky.sign.handler;

import android.content.Intent;
import android.location.Address;
import android.os.Handler;
import android.os.Message;


import com.amap.api.location.AMapLocation;

import java.util.List;

import intersky.sign.SignManager;
import intersky.sign.view.activity.SignActivity;

//02
public class SignHandler extends Handler
{
   public static final int EVENT_SET_ADDRESS_SELECT = 3230200;
    public static final int EVENT_UP_COUNT = 3230201;
    public static final int EVENT_SET_ADDRESS = 3230202;
    public static final int EVENT_SET_ADDRESS_OUT_SIDE = 3230203;

    public SignActivity theActivity;

    public SignHandler(SignActivity activity)
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
                    AMapLocation mAMapLocation = (AMapLocation) msg.obj;
                    if(!theActivity.mAddress.equals(mAMapLocation.getPoiName()+","+mAMapLocation.getAddress()))
                    {
                        if(theActivity.mLatLonPoint.getLatitude() == SignManager.getInstance().mLatLonPoint.getLatitude()
                                && theActivity.mLatLonPoint.getLongitude() == SignManager.getInstance().mLatLonPoint.getLongitude())
                        {
                            if(SignManager.getInstance().addressname.length() != 0)
                            {
                                theActivity.mAddress = SignManager.getInstance().addressname+","+SignManager.getInstance().addressdetial;
                                theActivity.addarssname.setText(SignManager.getInstance().addressname);
                            }
                            else
                            {
                                theActivity.mAddress = mAMapLocation.getPoiName()+","+mAMapLocation.getAddress();
                                theActivity.addarssname.setText(mAMapLocation.getPoiName());
                            }
                        }
                        else
                        {
                            theActivity.mAddress = mAMapLocation.getPoiName()+","+mAMapLocation.getAddress();
                            theActivity.addarssname.setText(mAMapLocation.getPoiName());
                        }

                        //theActivity.detialContent.setText(theActivity.getString(R.string.xml_attendanceat)+":"+theActivity.mAddress);
                    }
                    break;
                case EVENT_SET_ADDRESS_OUT_SIDE:
                    List<Address> addresses  = (List<Address>) msg.obj;
                    if(addresses.size() > 0)
                    {
                        Address mAddress = addresses.get(0);
                        if(!theActivity.mAddress.equals(mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea()))
                        {
                            if(theActivity.mLatLonPoint.getLatitude() == SignManager.getInstance().mLatLonPoint.getLatitude()
                                    && theActivity.mLatLonPoint.getLongitude() == SignManager.getInstance().mLatLonPoint.getLongitude())
                            {
                                if(SignManager.getInstance().addressname.length() != 0)
                                {
                                    theActivity.mAddress = SignManager.getInstance().addressname+","+SignManager.getInstance().addressdetial;
                                    if(theActivity.mAddress == null)
                                    {
                                        theActivity.mAddress = "";
                                    }
                                    theActivity.addarssname.setText(SignManager.getInstance().addressname);
                                    theActivity.daddressetial.setText(mAddress.getAdminArea());
                                }
                                else
                                {
                                    theActivity.mAddress =mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea();
                                    if(theActivity.mAddress == null)
                                    {
                                        theActivity.mAddress = "";
                                    }
                                    theActivity.addarssname.setText(mAddress.getLocality()+","+mAddress.getAdminArea());
                                    theActivity.daddressetial.setText(mAddress.getAdminArea());
                                }
                            }
                            else
                            {
                                theActivity.mAddress =mAddress.getAddressLine(0)+","+ mAddress.getLocality()+","+mAddress.getAdminArea();
                                theActivity.addarssname.setText(mAddress.getLocality()+","+mAddress.getAdminArea());
                                theActivity.daddressetial.setText(mAddress.getAdminArea());
                            }

                            //theActivity.detialContent.setText(theActivity.getString(R.string.xml_attendanceat)+":"+theActivity.mAddress);
                        }
                    }
                    break;
                case EVENT_SET_ADDRESS_SELECT:
                    Intent intent = (Intent) msg.obj;
                    if(!theActivity.mAddress.equals(intent.getStringExtra("city")))
                    {
                        theActivity.mAddress = intent.getStringExtra("city")+","+intent.getStringExtra("addressdetial");
                        theActivity.daddressetial.setText(intent.getStringExtra("addressdetial"));
                        theActivity.addarssname.setText(intent.getStringExtra("city"));
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}