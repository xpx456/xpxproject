package intersky.sign.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiResult;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.Onlocation;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyRelativeLayout;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.entity.Sign;
import intersky.sign.handler.SignHandler;
import intersky.sign.receive.SignReceiver;
import intersky.sign.view.activity.AddressSelectActivity;
import intersky.sign.view.activity.SignActivity;
import intersky.sign.view.activity.SignDetialActivity;
import intersky.sign.view.activity.StatisticsActivity;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapManager;

@SuppressLint("SimpleDateFormat")
public class SignPresenter implements Presenter {

	public SignActivity mSignActivity;
	public SignHandler mSignHandler;
	public SignPresenter(SignActivity mSignActivity) {
		this.mSignActivity = mSignActivity;
		this.mSignHandler = new SignHandler(mSignActivity);
		mSignActivity.setBaseReceiver(new SignReceiver(mSignHandler));
	}

	public TileProvider tileProvider = new UrlTileProvider(256, 256) {
		public URL getTileUrl(int x, int y, int zoom) {
			try {
				return new URL(String.format("http://mt2.google.cn/vt/lyrs=m@198&hl=zh-CN&gl=cn&src=app&x=%d&y=%d&z=%d&s=", x, y, zoom));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mSignActivity.setContentView(R.layout.activity_sign);
		RelativeLayout mLinearLayout = (RelativeLayout) mSignActivity.findViewById(R.id.activity_work_sign);
		mLinearLayout.setOnTouchListener(mSignActivity);
		mLinearLayout.setLongClickable(true);
		ToolBarHelper.setTitle(mSignActivity.mActionBar, mSignActivity.getString(R.string.keyword_attendance));
		ToolBarHelper.setRightBtnText(mSignActivity.mActionBar, mSignActivity.mStatisticsListener, mSignActivity.getString(R.string.keyword_statistics));
		mSignActivity.dateText = (TextView) mSignActivity.findViewById(R.id.date_text);
		mSignActivity.btnChange = (TextView) mSignActivity.findViewById(R.id.btn_change);
		mSignActivity.dateIcon = (ImageView) mSignActivity.findViewById(R.id.date_cion);
		mSignActivity.mMapLayer = (MyRelativeLayout) mSignActivity.findViewById(R.id.layermap);
		mSignActivity.mEditText = (EditText) mSignActivity.findViewById(R.id.detial_content_edit);
		mSignActivity.signcount = (TextView) mSignActivity.findViewById(R.id.sign_text);
		mSignActivity.detialContent = (TextView) mSignActivity.findViewById(R.id.detial_content);
		mSignActivity.btnSign = (ImageView) mSignActivity.findViewById(R.id.sign_btn);
		mSignActivity.mMapView = (MapView) mSignActivity.findViewById(R.id.detial_cion);
		mSignActivity.dateText1 = (TextView) mSignActivity.findViewById(R.id.sign_text1);
		mSignActivity.dateText2 = (TextView) mSignActivity.findViewById(R.id.sign_text2);
		mSignActivity.dateText3 = (TextView) mSignActivity.findViewById(R.id.sign_text3);
		mSignActivity.datelayer = (RelativeLayout) mSignActivity.findViewById(R.id.textline);
		mSignActivity.addarssname = (TextView) mSignActivity.findViewById(R.id.address_name);
		mSignActivity.mMapLayer.setOnClickListener(mSignActivity.mSelectAddressListener);
		mSignActivity.btnChange.setOnClickListener(mSignActivity.mSelectAddressListener);
		mSignActivity.btnSign.setOnClickListener(mSignActivity.mSignListener);
		mSignActivity.dateText.setText(TimeUtils.getDate());
		mSignActivity.dateIcon.setOnClickListener(mSignActivity.mDateListener);
		SignManager.getInstance().mapManager.addLocation(onlocation);
	}


	public void initMap() {
		mSignActivity.aMap = mSignActivity.mMapView.getMap();
		MyLocationStyle myLocationStyle;
		myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		mSignActivity.aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		myLocationStyle.strokeColor(R.color.trans);//设置定位蓝点精度圆圈的边框颜色的方法。
		myLocationStyle.radiusFillColor(R.color.trans);//设置定位蓝点精度圆圈的填充颜色的方法
		mSignActivity.aMap.setLocationSource(mSignActivity);// 设置定位监听
		mSignActivity.aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mSignActivity.aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mSignActivity.aMap.getUiSettings().setZoomControlsEnabled(false);
		mSignActivity.aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		mSignActivity.aMap.getUiSettings().setAllGesturesEnabled(false);
		mSignActivity.aMap.addTileOverlay(new TileOverlayOptions()
				.tileProvider(tileProvider)
				.diskCacheEnabled(true)
				.diskCacheDir("/storage/emulated/0/demo/cache")
				.diskCacheSize(100000)
				.memoryCacheEnabled(true)
				.memCacheSize(100000));
		initHitView();
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
		mSignActivity.mMapView.onResume();
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		mSignActivity.mMapView.onPause();
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mSignActivity.mMapView.onDestroy();
		mSignHandler = null;
		SignManager.getInstance().mapManager.removeLocation(onlocation);
//		SignManager.getInstance().mapManager.stopLocation();
	}



	public void doSelect(PoiResult arg0)
	{
		SignManager.getInstance().mPoiItems.clear();
		SignManager.getInstance().mPoiItems.addAll(arg0.getPois());
		Intent intent = new Intent(mSignActivity, AddressSelectActivity.class);
		mSignActivity.startActivity(intent);
	}

	public void selectAddress()
	{
		Intent intent = new Intent(mSignActivity,AddressSelectActivity.class);
		mSignActivity.startActivity(intent);
	}


	public void showDateDialog() {
		AppUtils.creatDataPicker(mSignActivity,mSignActivity.dateText.getText().toString(), mSignActivity.getString(R.string.keyword_dete),mOnDaySetListener);
	}

	
	public void doSign() {
		Intent intent = new Intent(mSignActivity, SignDetialActivity.class);
		Sign sign = new Sign();
		sign.mAddress = mSignActivity.mAddress;
		sign.mDate = mSignActivity.dateText.getText().toString();
		sign.mAltitude = mSignActivity.mLatLonPoint.getLatitude();
		sign.mLongitude = mSignActivity.mLatLonPoint.getLongitude();
		sign.mReason = mSignActivity.mEditText.getText().toString();
		intent.putExtra("sign",sign);
		mSignActivity.startActivity(intent);
	}
	
	public void doStatstics()
	{
		Intent intent = new Intent(mSignActivity, StatisticsActivity.class);
		mSignActivity.startActivity(intent);
	}

	public void getGoogleAddress(AMapLocation amapLocation)
	{
		List<Address> addresses;
		Geocoder geocoder = new Geocoder(mSignActivity, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(amapLocation.getLatitude(), amapLocation.getLongitude(), 1);
			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			String zipCode = addresses.get(0).getPostalCode();
			String country = addresses.get(0).getCountryCode();
			Message msg = new Message();
			msg.what = SignHandler.EVENT_SET_ADDRESS_OUT_SIDE;
			msg.obj = addresses;
			mSignHandler.sendMessage(msg);
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
		if (mSignActivity.mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {

				mSignActivity.mListener.onLocationChanged(amapLocation);
				if(mSignActivity.mLatLonPoint.getLongitude() == amapLocation.getLongitude()
						&& mSignActivity.mLatLonPoint.getLatitude() == amapLocation.getLatitude())
				{
					return;
				}
				else
				{
					mSignActivity.mListener.onLocationChanged(amapLocation);
					CoordinateConverter converter  = new CoordinateConverter(mSignActivity);
					boolean isAMapDataAvailable = converter.isAMapDataAvailable(amapLocation.getLatitude(),amapLocation.getLongitude());
					mSignActivity.mLatLonPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
					if(isAMapDataAvailable)
					{

						Message msg = new Message();
						msg.what = SignHandler.EVENT_SET_ADDRESS;
						//msg.obj = amapLocation.getAddress();
						msg.obj = amapLocation;
						if(mSignActivity.mAddress.length() == 0)
							mSignHandler.sendMessage(msg);
					}
					else
					{
						getGoogleAddress(amapLocation);
					}

				}


			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}
	
	public void onActivate(LocationSource.OnLocationChangedListener listener)
	{
		mSignActivity.mListener = listener;
		if(SignManager.getInstance().mapManager.lastAMapLocation != null)
		{
			mSignActivity.mListener.onLocationChanged(SignManager.getInstance().mapManager.lastAMapLocation);
		}
	}
	
	public void onDeactivate()
	{
		mSignActivity.mListener = null;
	}

	public void initHitView() {
		if(SignManager.getInstance().signHit == 0)
		{
			mSignActivity.datelayer.setVisibility(View.INVISIBLE);
			mSignActivity.signcount.setVisibility(View.VISIBLE);
			mSignActivity.signcount.setText(mSignActivity.getString(R.string.xml_sign_none));
		}
		else
		{
			mSignActivity.datelayer.setVisibility(View.VISIBLE);
			mSignActivity.signcount.setVisibility(View.INVISIBLE);
			mSignActivity.dateText2.setText(String.valueOf(SignManager.getInstance().signHit));
			mSignActivity.dateText2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
			mSignActivity.dateText2.getPaint().setAntiAlias(true);//抗锯齿
		}
	}


	public DoubleDatePickerDialog.OnDateSetListener mOnDaySetListener = new DoubleDatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
			String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
			mSignActivity.dateText.setText(textString);
		}
	};
}
