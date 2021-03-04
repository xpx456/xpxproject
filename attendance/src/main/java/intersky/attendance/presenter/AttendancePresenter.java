package intersky.attendance.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import intersky.appbase.entity.Contacts;
import intersky.apputils.Onlocation;
import intersky.attendance.AttendanceManager;
import intersky.attendance.R;
import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.entity.Attendance;
import intersky.attendance.handler.AttendanceHandler;
import intersky.attendance.receive.AttendanceReceiver;
import intersky.attendance.view.activity.AttendanceActivity;
import intersky.attendance.view.activity.SetAttendanceActivity;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class AttendancePresenter implements Presenter {

    public AttendanceActivity mAttendanceActivity;
    public AttendanceHandler mAttendanceHandler;

    public AttendancePresenter(AttendanceActivity mAttendanceActivity) {
        this.mAttendanceActivity = mAttendanceActivity;
        this.mAttendanceHandler = new AttendanceHandler(mAttendanceActivity);
        mAttendanceActivity.setBaseReceiver(new AttendanceReceiver(mAttendanceHandler));
    }


    public void selectAddress(String key) {
        mAttendanceActivity.mQuery = new PoiSearch.Query(key, "", mAttendanceActivity.mCity);
        mAttendanceActivity.mQuery.setPageSize(30);
        mAttendanceActivity.mQuery.setPageNum(0);
        mAttendanceActivity.poiSearch = new PoiSearch(mAttendanceActivity, mAttendanceActivity.mQuery);
        mAttendanceActivity.poiSearch.setOnPoiSearchListener(mAttendanceActivity);
        mAttendanceActivity.poiSearch.setBound(new PoiSearch.SearchBound(mAttendanceActivity.mLatLonPoint, 200));//
        mAttendanceActivity.poiSearch.searchPOIAsyn();

    }

    public void doSelect(PoiResult arg0) {

        if (arg0.getPois().size() > 0) {
            Message msg = new Message();
            msg.obj = arg0.getPois().get(0).getSnippet();
            msg.what = AttendanceHandler.EVENT_UPDATA_LIST;
            mAttendanceHandler.sendMessage(msg);
        }
    }


    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mAttendanceActivity.setContentView(R.layout.activity_work_attendance);
        ImageView back = mAttendanceActivity.findViewById(R.id.back);
        back.setOnClickListener(mAttendanceActivity.mBackListener);
//        mAttendanceActivity.mMapView = (MapView) mAttendanceActivity.findViewById(R.id.detial_cion);
        mAttendanceActivity.mHead = (TextView) mAttendanceActivity.findViewById(R.id.conversation_img);
        mAttendanceActivity.mAddresstext = mAttendanceActivity.findViewById(R.id.signtitle);
        mAttendanceActivity.mName = (TextView) mAttendanceActivity.findViewById(R.id.conversation_title);
        mAttendanceActivity.mDepartMent = (TextView) mAttendanceActivity.findViewById(R.id.conversation_subject);
        mAttendanceActivity.mDete = (TextView) mAttendanceActivity.findViewById(R.id.date_text);
        mAttendanceActivity.attdenceList = (LinearLayout) mAttendanceActivity.findViewById(R.id.work_attendance_List);
        mAttendanceActivity.mDetebtn = (RelativeLayout) mAttendanceActivity.findViewById(R.id.datebtn);
        mAttendanceActivity.mSign = (RelativeLayout) mAttendanceActivity.findViewById(R.id.sign);
        mAttendanceActivity.mSign.setOnClickListener(mAttendanceActivity.signListener);
        mAttendanceActivity.mSignTitle = (TextView) mAttendanceActivity.findViewById(R.id.signtitle1);
        mAttendanceActivity.signTime = (TextView) mAttendanceActivity.findViewById(R.id.signtime2);
        mAttendanceActivity.mshada = (RelativeLayout) mAttendanceActivity.findViewById(R.id.shade);
        mAttendanceActivity.mbglayer = (ScrollView) mAttendanceActivity.findViewById(R.id.bglayer);
        mAttendanceActivity.mbglayer1 = (RelativeLayout) mAttendanceActivity.findViewById(R.id.bglayer1);
        mAttendanceActivity.mbglayer2 = (RelativeLayout) mAttendanceActivity.findViewById(R.id.work_attendance);
        mAttendanceActivity.upData = (RelativeLayout) mAttendanceActivity.findViewById(R.id.updata);
        String name = AttendanceManager.getInstance().oaUtils.mAccount.mRealName;
        if (name.length() == 0) {
            name = AttendanceManager.getInstance().oaUtils.mAccount.mAccountId;
        }
        mAttendanceActivity.mName.setText(name);

        if (!AttendanceManager.getInstance().oaUtils.mAccount.mOrgName.equals("(Root)")) {
            if(AttendanceManager.getInstance().oaUtils.mAccount.mOrgName.length() == 0)
            {
                Contacts contacts = AttendanceManager.getInstance().oaUtils.mContactManager.mOrganization.mAllContactsDepartMap.get(AttendanceManager.getInstance().oaUtils.mAccount.mOrgId);
                if(contacts != null)
                mAttendanceActivity.mDepartMent.setText(contacts.getName());
            }
            else
            mAttendanceActivity.mDepartMent.setText(AttendanceManager.getInstance().oaUtils.mAccount.mOrgName);
        } else {
            mAttendanceActivity.mDepartMent.setText("");
        }

        AppUtils.setContactCycleHead(mAttendanceActivity.mHead,name);
        mAttendanceActivity.mDete.setText(TimeUtils.getDate2());
        mAttendanceActivity.mDeteString = TimeUtils.getDate();
        mAttendanceActivity.mDetebtn.setOnClickListener(mAttendanceActivity.dateListener);
        mAttendanceActivity.mWorkAttendances.add(new Attendance(0));
        mAttendanceActivity.mSignTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_shang2));
        if (mAttendanceHandler != null)
            mAttendanceHandler.sendEmptyMessage(AttendanceHandler.EVENT_SAVE_UPDATA_TIME);
        ToolBarHelper.setRightBtnText(mAttendanceActivity.mActionBar, mAttendanceActivity.mMoreListenter, " · · ·", true);
        setNoSign();
        mAttendanceActivity.waitDialog.show();
        AttdenanceAsks.getWorkAttdenaceList(mAttendanceHandler, mAttendanceActivity, AttendanceManager.getInstance().getSetUserid(), mAttendanceActivity.mDeteString);
        if (AttendanceManager.getInstance().oaUtils.mAccount.mAccountId.equals("忻盼贤")) {
            TextView title = mAttendanceActivity.findViewById(R.id.save);
            title.setVisibility(View.VISIBLE);
            title.setOnClickListener(mAttendanceActivity.signexListener);
        }
        mAttendanceActivity.upData.setOnClickListener(mAttendanceActivity.uplistListener);
        if(AttendanceManager.getInstance().mapManager.lastAMapLocation != null)
        {
            onLocation(AttendanceManager.getInstance().mapManager.lastAMapLocation);
        }
        AttendanceManager.getInstance().mapManager.addLocation(onlocation);

    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub
        //mAttendanceActivity.mMapView.onResume();
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
        //mAttendanceActivity.mMapView.onPause();
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        //mAttendanceActivity.mMapView.onDestroy();
        AttendanceManager.getInstance().mapManager.removeLocation(onlocation);
//        AttendanceManager.getInstance().mapManager.stopLocation();
    }

    public void showTimeDialog() {
        AppUtils.creatDataPicker(mAttendanceActivity, mAttendanceActivity.mDeteString, mAttendanceActivity.getString(R.string.keyword_signtime), mOnDaySetListener);
    }


    public void doSign() {

        if (mAttendanceActivity.bingSign == false) {
            mAttendanceActivity.bingSign = true;
            mAttendanceActivity.waitDialog.show();
            AttdenanceAsks.doSign(mAttendanceHandler, mAttendanceActivity, mAttendanceActivity.mAddress, mAttendanceActivity.mLatLonPoint.getLatitude(), mAttendanceActivity.mLatLonPoint.getLongitude());
        } else {
            mAttendanceActivity.waitDialog.show();
        }


    }

    public void doSignex() {

        if (mAttendanceActivity.bingSign == false) {
            mAttendanceActivity.bingSign = true;
            mAttendanceActivity.waitDialog.show();
            AttdenanceAsks.doSign(mAttendanceHandler, mAttendanceActivity, "世纪大道北段333号华东城2号楼13层",
                    Double.valueOf("29.865589"), Double.valueOf("121.604997"));
        } else {
            mAttendanceActivity.waitDialog.show();
        }


    }

    public void showDialog(final Attendance mWorkAttendance) {
        AppUtils.creatDialogTowButton(mAttendanceActivity, mAttendanceActivity.getString(R.string.attdence_update), mAttendanceActivity.getString(R.string.attdence_update1),
                mAttendanceActivity.getString(R.string.button_word_cancle), mAttendanceActivity.getString(R.string.button_word_ok), null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        AttdenanceAsks.doUpdateSign(mAttendanceHandler, mAttendanceActivity, mAttendanceActivity.mAddress, mWorkAttendance, mAttendanceActivity.mLatLonPoint.getLatitude(), mAttendanceActivity.mLatLonPoint.getLongitude());
                    }
                });
    }

    public void showSig(NetObject net) {
        String json = net.result;
        try {
            if (AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if (AppUtils.success(json) == false) {
                AppUtils.showMessage(mAttendanceActivity, AppUtils.getfailmessage(json));
                return;
            }
            JSONObject object = new JSONObject(json);
            JSONObject ob = object.getJSONObject("data");
            String timet = ob.getString("attence_time");
            timet = timet.substring(11, 16);
            int a = Integer.valueOf(String.valueOf(ob.get("status")));
            View popupWindowView = LayoutInflater.from(mAttendanceActivity).inflate(R.layout.buttom_window13, null);
            RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
            lsyer.setFocusable(true);
            lsyer.setFocusableInTouchMode(true);
            mAttendanceActivity.popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            popupWindowView.setFocusable(true);
            popupWindowView.setFocusableInTouchMode(true);
            mAttendanceActivity.popupWindow.setAnimationStyle(R.style.PopupAnimation);
            popupWindowView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return false;
                }
            });
            lsyer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //doclose();

                }
            });
            ColorDrawable dw = new ColorDrawable(0x0000000);
            mAttendanceActivity.popupWindow.setBackgroundDrawable(dw);
            mAttendanceActivity.mclose = (TextView) popupWindowView.findViewById(R.id.btn);
            mAttendanceActivity.mclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAttendanceActivity.popupWindow.dismiss();

                }
            });
            TextView time = (TextView) popupWindowView.findViewById(R.id.time);
            TextView title = (TextView) popupWindowView.findViewById(R.id.title1);
            if (a == 0 || a == 2) {
                title.setText(mAttendanceActivity.getString(R.string.on));
            } else {
                title.setText(mAttendanceActivity.getString(R.string.off));
            }
            time.setText(timet);
            mAttendanceActivity.popupWindow.showAtLocation(mAttendanceActivity.findViewById(R.id.work_attendance),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                JSONObject object = new JSONObject(json);
                if (object.has("message")) {
                    AppUtils.showMessage(mAttendanceActivity, object.getString("message"));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }


    }

    public void initListView() {
        if (mAttendanceActivity.mDeteString.equals(TimeUtils.getDate()) == true && AttendanceManager.getInstance().getSetUserid().equals(AttendanceManager.getInstance().oaUtils.mAccount.mRecordId) == true) {
            if (mAttendanceActivity.mWorkAttendances.size() == 0) {
                Attendance mWorkAttendanceModel1 = new Attendance(0);
                mWorkAttendanceModel1.setmStatus(0);
                mAttendanceActivity.mWorkAttendances.add(mWorkAttendanceModel1);
                mAttendanceActivity.mSignTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_shang2));
            } else if (mAttendanceActivity.mWorkAttendances.get(mAttendanceActivity.mWorkAttendances.size() - 1).getmStatus() == 0
                    || mAttendanceActivity.mWorkAttendances.get(mAttendanceActivity.mWorkAttendances.size() - 1).getmStatus() == 2) {
                Attendance mWorkAttendanceModel1 = new Attendance(0);
                mWorkAttendanceModel1.setmStatus(1);
                mAttendanceActivity.mWorkAttendances.add(mWorkAttendanceModel1);
                mAttendanceActivity.mSignTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_xia2));
            } else if (mAttendanceActivity.mWorkAttendances.get(mAttendanceActivity.mWorkAttendances.size() - 1).getmStatus() == 1
                    || mAttendanceActivity.mWorkAttendances.get(mAttendanceActivity.mWorkAttendances.size() - 1).getmStatus() == 3) {
                Attendance mWorkAttendanceModel1 = new Attendance(0);
                mWorkAttendanceModel1.setmStatus(0);
                mAttendanceActivity.mWorkAttendances.add(mWorkAttendanceModel1);
                mAttendanceActivity.mSignTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_shang2));
            }
        }
    }

    public void initAttendanceView() {
        mAttendanceActivity.attdenceList.removeAllViews();
        LayoutInflater mInflater = (LayoutInflater) mAttendanceActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < mAttendanceActivity.mWorkAttendances.size(); i++) {
            Attendance mWorkAttendance = mAttendanceActivity.mWorkAttendances.get(i);
            View convertView;
            if (mWorkAttendance.getType() == 1) {
                convertView = mInflater.inflate(R.layout.work_attdence_item, null);
                TextView cicle = (TextView) convertView.findViewById(R.id.cyclework1);
                TextView mAddress = (TextView) convertView.findViewById(R.id.signaddress);
                TextView mTitle = (TextView) convertView.findViewById(R.id.signtime);
                TextView subtext = (TextView) convertView.findViewById(R.id.subtext);
                subtext.setText(mWorkAttendance.getSubText());
                if (mWorkAttendance.getmStatus() == 0 || mWorkAttendance.getmStatus() == 2) {
                    cicle.setText(mAttendanceActivity.getString(R.string.on));
                    mTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_shang) + ": " + mWorkAttendance.getmATime().substring(11, 16));
                } else {
                    cicle.setText(mAttendanceActivity.getString(R.string.off));
                    mTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_xia) + ": " + mWorkAttendance.getmATime().substring(11, 16));
                }
                if (mWorkAttendance.getmUserId().equals(AttendanceManager.getInstance().oaUtils.mAccount.mRecordId)
                        && mAttendanceActivity.mDete.getText().toString().equals(TimeUtils.getDate2()) && mAttendanceActivity.mWorkAttendances.size() > 1) {
                    if (mAttendanceActivity.mWorkAttendances.size() - 2 == i) {
                        TextView updata = (TextView) convertView.findViewById(R.id.updata);
                        updata.setVisibility(View.VISIBLE);
                        updata.setTag(mWorkAttendance);
                        updata.setOnClickListener(mAttendanceActivity.signupListener);
                    }

                }
                if(i == 0)
                {
                    RelativeLayout line1 = convertView.findViewById(R.id.line1);
                    line1.setVisibility(View.INVISIBLE);
                }
                if(i == mAttendanceActivity.mWorkAttendances.size()-1)
                {
                    RelativeLayout line1 = convertView.findViewById(R.id.line);
                    line1.setVisibility(View.INVISIBLE);
                }
                mAddress.setText(mWorkAttendance.getmAddress());
            } else {
                convertView = mInflater.inflate(R.layout.work_attdence_item_end, null);
                if (mWorkAttendance.getmStatus() == 0 && mWorkAttendance.getmStatus() == 0) {
                    TextView cicle = (TextView) convertView.findViewById(R.id.cyclework1);
                    TextView mTitle = (TextView) convertView.findViewById(R.id.signtime);
                    cicle.setText(mAttendanceActivity.getString(R.string.on));
                    mTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_shang1));
                } else {
                    TextView cicle = (TextView) convertView.findViewById(R.id.cyclework1);
                    TextView mTitle = (TextView) convertView.findViewById(R.id.signtime);
                    cicle.setText("下");
                    mTitle.setText(mAttendanceActivity.getString(R.string.message_work_attdence_xia1));
                }

                if(i == 0)
                {
                    RelativeLayout line1 = convertView.findViewById(R.id.line);
                    line1.setVisibility(View.INVISIBLE);
                }
            }
            mAttendanceActivity.attdenceList.addView(convertView);
        }
    }

    public void setuser(Intent intent) {
        AttendanceManager.getInstance().setContact = intent.getParcelableExtra("contacts");

        if (mAttendanceActivity.mDeteString.equals(TimeUtils.getDate()) == true && AttendanceManager.getInstance().getSetUserid().equals(AttendanceManager.getInstance().oaUtils.mAccount.mAccountId)) {
            mAttendanceActivity.mSign.setVisibility(View.VISIBLE);
//            mAttendanceActivity.mbglayer.setBackgroundColor(Color.rgb(215, 238, 252));
//            mAttendanceActivity.mbglayer1.setBackgroundColor(Color.rgb(215, 238, 252));
//            mAttendanceActivity.mbglayer2.setBackgroundColor(Color.rgb(215, 238, 252));
        } else {
            mAttendanceActivity.mSign.setVisibility(View.GONE);
//            mAttendanceActivity.mbglayer.setBackgroundColor(Color.rgb(255, 255, 255));
//            mAttendanceActivity.mbglayer1.setBackgroundColor(Color.rgb(255, 255, 255));
//            mAttendanceActivity.mbglayer2.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        String name = AttendanceManager.getInstance().setContact.mRName;
        if (name.length() == 0) {
            name = AttendanceManager.getInstance().setContact.mName;
        }
        mAttendanceActivity.mName.setText(name);

        if (!AttendanceManager.getInstance().setContact.mDepartMent.equals("(Root)")) {
            if(AttendanceManager.getInstance().setContact.mDepartMent.length() == 0)
            {
                Contacts contacts = AttendanceManager.getInstance().oaUtils.mContactManager.mOrganization.mAllContactsDepartMap.get(AttendanceManager.getInstance().oaUtils.mAccount.mOrgId);
                if(contacts != null)
                    mAttendanceActivity.mDepartMent.setText(contacts.getName());
            }
            else
            mAttendanceActivity.mDepartMent.setText(AttendanceManager.getInstance().setContact.mDepartMent);
        } else {
            mAttendanceActivity.mDepartMent.setText("");
        }
        mAttendanceActivity.mWorkAttendances.clear();
        mAttendanceActivity.attdenceList.removeAllViews();
        AttdenanceAsks.getWorkAttdenaceList(mAttendanceHandler, mAttendanceActivity, AttendanceManager.getInstance().getSetUserid(), mAttendanceActivity.mDeteString);
        ToolBarHelper.setTitle(mAttendanceActivity.mActionBar, AttendanceManager.getInstance().setContact.mName + mAttendanceActivity.getString(R.string.xml_attdacne_title_top));


    }

    public void getGoogleAddress(AMapLocation amapLocation) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(mAttendanceActivity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(amapLocation.getLatitude(), amapLocation.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zipCode = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryCode();
            Message msg = new Message();
            msg.what = AttendanceHandler.EVENT_SET_ADDRESS_OUT_SIDE;
            msg.obj = address + "," + state + "," + country;
            if (mAttendanceHandler != null)
                mAttendanceHandler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Onlocation onlocation = new Onlocation() {
        @Override
        public void location(AMapLocation amapLocation) {
            onLocation(amapLocation);
        }
    };

    public void onLocation(AMapLocation amapLocation) {
        if (amapLocation != null
                && amapLocation.getErrorCode() == 0) {
            mAttendanceActivity.mLatLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
            CoordinateConverter converter = new CoordinateConverter(mAttendanceActivity);
            boolean isAMapDataAvailable = converter.isAMapDataAvailable(amapLocation.getLatitude(), amapLocation.getLongitude());
            if (isAMapDataAvailable) {
                Message msg = new Message();
                msg.what = AttendanceHandler.EVENT_SET_ADDRESS;
                msg.obj = amapLocation;
                if (mAttendanceHandler != null)
                    mAttendanceHandler.sendMessage(msg);

            } else {
                getGoogleAddress(amapLocation);
            }


        } else {
            String errText = mAttendanceActivity.getString(R.string.location_fail)+"," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }

//    public void onActivate(LocationSource.OnLocationChangedListener listener) {
//        mAttendanceActivity.mListener = listener;
//        if (mAttendanceActivity.mlocationClient == null) {
//            mAttendanceActivity.mlocationClient = new AMapLocationClient(mAttendanceActivity);
//            mAttendanceActivity.mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mAttendanceActivity.mlocationClient.setLocationListener(mAttendanceActivity);
//            //设置为高精度定位模式
//            mAttendanceActivity.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位参数
//            mAttendanceActivity.mLocationOption.setInterval(2000);
//            mAttendanceActivity.mLocationOption.setOnceLocation(false);
//            mAttendanceActivity.mlocationClient.setLocationOption(mAttendanceActivity.mLocationOption);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            mAttendanceActivity.mlocationClient.startLocation();
//        }
//    }
//
//    public void onDeactivate() {
//        mAttendanceActivity.mListener = null;
//        if (mAttendanceActivity.mlocationClient != null) {
//            mAttendanceActivity.mlocationClient.stopLocation();
//            mAttendanceActivity.mlocationClient.onDestroy();
//        }
//        mAttendanceActivity.mlocationClient = null;
//    }

    public void showMore() {

        if (AttendanceManager.getInstance().oaUtils.mAccount.mCloundAdminId.equals(AttendanceManager.getInstance().oaUtils.mAccount.mRecordId)) {
            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem item = new MenuItem();
            item.btnName = mAttendanceActivity.getString(R.string.keyword_mywattdence);
            item.mListener = mAttendanceActivity.mShowMy;
            items.add(item);
            item = new MenuItem();
            item.btnName = mAttendanceActivity.getString(R.string.keyword_otherwattdence);
            item.mListener = mAttendanceActivity.mShowOther;
            items.add(item);
            item = new MenuItem();
            item.btnName = mAttendanceActivity.getString(R.string.keyword_setwattdence);
            item.mListener = mAttendanceActivity.mShowSet;
            items.add(item);
            mAttendanceActivity.popupWindow1 = AppUtils.creatButtomMenu(mAttendanceActivity,  mAttendanceActivity.mshada, items, mAttendanceActivity.findViewById(R.id.work_attendance));
        } else {

            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem item = new MenuItem();
            item.btnName = mAttendanceActivity.getString(R.string.keyword_mywattdence);
            item.mListener = mAttendanceActivity.mShowMy;
            items.add(item);
            item = new MenuItem();
            item.btnName = mAttendanceActivity.getString(R.string.keyword_otherwattdence);
            item.mListener = mAttendanceActivity.mShowOther;
            items.add(item);
            mAttendanceActivity.popupWindow1 = AppUtils.creatButtomMenu(mAttendanceActivity, mAttendanceActivity.mshada, items, mAttendanceActivity.findViewById(R.id.work_attendance));
        }
    }

    public void showSet() {

        Intent intent = new Intent(mAttendanceActivity, SetAttendanceActivity.class);
        intent.putExtra("x",mAttendanceActivity.mLatLonPoint.getLatitude());
        intent.putExtra("y",mAttendanceActivity.mLatLonPoint.getLongitude());
        mAttendanceActivity.startActivity(intent);
        mAttendanceActivity.popupWindow1.dismiss();
    }


    public void showMy() {
        if (mAttendanceActivity.mDeteString.equals(TimeUtils.getDate()) == true) {
            mAttendanceActivity.mSign.setVisibility(View.VISIBLE);
//            mAttendanceActivity.mbglayer.setBackgroundColor(Color.rgb(215, 238, 252));
//            mAttendanceActivity.mbglayer1.setBackgroundColor(Color.rgb(215, 238, 252));
//            mAttendanceActivity.mbglayer2.setBackgroundColor(Color.rgb(215, 238, 252));
        } else {
            mAttendanceActivity.mSign.setVisibility(View.GONE);
//            mAttendanceActivity.mbglayer.setBackgroundColor(Color.rgb(255, 255, 255));
//            mAttendanceActivity.mbglayer1.setBackgroundColor(Color.rgb(255, 255, 255));
//            mAttendanceActivity.mbglayer2.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        AttendanceManager.getInstance().setContact = null;
        String name = AttendanceManager.getInstance().oaUtils.mAccount.mRealName;
        if (name.length() == 0) {
            name = AttendanceManager.getInstance().oaUtils.mAccount.mAccountId;
        }
        mAttendanceActivity.mName.setText(name);

        if (!AttendanceManager.getInstance().oaUtils.mAccount.mOrgName.equals("(Root)")) {
            mAttendanceActivity.mDepartMent.setText(AttendanceManager.getInstance().oaUtils.mAccount.mOrgName);
        } else {
            mAttendanceActivity.mDepartMent.setText("");
        }
        mAttendanceActivity.mHead.setText(AppUtils.getContactHead(name));
        mAttendanceActivity.popupWindow1.dismiss();

        ToolBarHelper.setTitle(mAttendanceActivity.mActionBar, mAttendanceActivity.getString(R.string.keyword_workattdence));
        mAttendanceActivity.mWorkAttendances.clear();
        mAttendanceActivity.attdenceList.removeAllViews();
        AttdenanceAsks.getWorkAttdenaceList(mAttendanceHandler,mAttendanceActivity,AttendanceManager.getInstance().getSetUserid(),mAttendanceActivity.mDeteString);
    }

    public void showOther() {
        mAttendanceActivity.popupWindow1.dismiss();
        AttendanceManager.getInstance().oaUtils.mContactManager.setUnderlineContacts(mAttendanceActivity,"", AttendanceActivity.ACTION_LEAVE_SET_CONTACTS);
    }


    public void dodismiss() {
        mAttendanceActivity.popupWindow1.dismiss();
    }

    public void setCanSign() {
        mAttendanceActivity.mSign.setBackgroundResource(R.drawable.shape_bg_round2);
        mAttendanceActivity.mSign.setEnabled(true);
    }

    private void setNoSign() {
        mAttendanceActivity.mSign.setBackgroundResource(R.drawable.shape_bg_round_gray);
        mAttendanceActivity.mSign.setEnabled(false);
    }


    public DoubleDatePickerDialog.OnDateSetListener mOnDaySetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
            int count = TimeUtils.measureDayCount4(textString, TimeUtils.getDate());
            if(count >= 0)
            {
                mAttendanceActivity.mDeteString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
                mAttendanceActivity.mDete.setText(String.format("%04d.%02d.%02d", startYear, startMonthOfYear + 1, startDayOfMonth));
                if (mAttendanceActivity.mDeteString.equals(TimeUtils.getDate()) == true && AttendanceManager.getInstance().getSetUserid().equals(AttendanceManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    mAttendanceActivity.mSign.setVisibility(View.VISIBLE);
//                    mAttendanceActivity.mbglayer.setBackgroundColor(Color.rgb(215, 238, 252));
//                    mAttendanceActivity.mbglayer1.setBackgroundColor(Color.rgb(215, 238, 252));
//                    mAttendanceActivity.mbglayer2.setBackgroundColor(Color.rgb(215, 238, 252));
                } else {
                    mAttendanceActivity.mSign.setVisibility(View.GONE);
//                    mAttendanceActivity.mbglayer.setBackgroundColor(Color.rgb(255, 255, 255));
//                    mAttendanceActivity.mbglayer1.setBackgroundColor(Color.rgb(255, 255, 255));
//                    mAttendanceActivity.mbglayer2.setBackgroundColor(Color.rgb(255, 255, 255));
                }


                mAttendanceActivity.mWorkAttendances.clear();
                mAttendanceActivity.mAttendancePresenter.initAttendanceView();
                AttdenanceAsks.getWorkAttdenaceList(mAttendanceHandler, mAttendanceActivity, AttendanceManager.getInstance().getSetUserid(), mAttendanceActivity.mDeteString);
            }
            else{
                AppUtils.showMessage(mAttendanceActivity,mAttendanceActivity.getString(R.string.address_eraly));
            }
        }
    };
}
