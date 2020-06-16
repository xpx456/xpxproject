package xpx.map.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.interfaces.MapCameraMessage;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.Onlocation;
import intersky.mywidget.CustomScrollView;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapManager;
import xpx.map.R;
import xpx.map.entity.MapAddress;
import xpx.map.handler.LocationHandler;
import xpx.map.view.activity.LocationActivity;

public class LocationPresenter implements Presenter {

	public LocationActivity mLocationActivity;
    public LocationHandler mLocationHandler;

	public LocationPresenter(LocationActivity mLocationActivity)
	{
		this.mLocationActivity = mLocationActivity;
		this.mLocationHandler = new LocationHandler(mLocationActivity);
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
		ToolBarHelper.setSutColor(mLocationActivity, Color.argb(0, 255, 255, 255));
		mLocationActivity.setContentView(R.layout.activity_location_select);
		CustomScrollView scollView = mLocationActivity.findViewById(R.id.sc);
		scollView.setOnScrollChangeListener(onScrollChangeListener);
		mLocationActivity.back = mLocationActivity.findViewById(R.id.back);
		mLocationActivity.send = mLocationActivity.findViewById(R.id.btn_send);
		mLocationActivity.mapContainer = mLocationActivity.findViewById(R.id.detial_cion_l);
		mLocationActivity.mapContainer.setScrollView(scollView);
		mLocationActivity.mSearch = (SearchViewLayout) mLocationActivity.findViewById(R.id.search);
		mLocationActivity.mMapView = (MapView) mLocationActivity.findViewById(R.id.detial_cion);
		mLocationActivity.mSearch.mSetOnSearchListener(mLocationActivity.mOnEditorActionListener);
		mLocationActivity.mListView = (LinearLayout) mLocationActivity.findViewById(R.id.address_List);
		mLocationActivity.scrollView = mLocationActivity.findViewById(R.id.address_view);
		mLocationActivity.scrollView.setOnScrollChangeListener(mLocationActivity);
		mLocationActivity.mToolBarHelper.hidToolbar(mLocationActivity, (RelativeLayout) mLocationActivity.findViewById(R.id.buttomaciton));
		mLocationActivity.measureStatubar(mLocationActivity, (RelativeLayout) mLocationActivity.findViewById(R.id.stutebar));
		mLocationActivity.geocodeSearch = new GeocodeSearch(mLocationActivity);
		mLocationActivity.geocodeSearch.setOnGeocodeSearchListener(mLocationActivity);
		mLocationActivity.watiView = mLocationActivity.getLayoutInflater().inflate(R.layout.address_wait, null);
		mLocationActivity.back.setOnClickListener(mLocationActivity.backListener);
		mLocationActivity.send.setOnClickListener(mLocationActivity.sendListener);
		if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false) == true)
		{
			mLocationActivity.send.setVisibility(View.INVISIBLE);
		}
		addListView(MapManager.getInstance().mPoiItems);
		MapManager.getInstance().addLocation(onlocation);
		showSend();
		startSearch();
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
		mLocationActivity.mMapView.onResume();
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		mLocationActivity.mMapView.onPause();
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mLocationActivity.mMapView.onDestroy();
		mLocationHandler.removeMessages(LocationHandler.EVENT_START_SEARCH);
		mLocationHandler = null;
		MapManager.getInstance().removeLocation(onlocation);
		MapManager.getInstance().stopLocation();
	}

	public void showSend() {
		if(mLocationActivity.selectAddress == null)
		{
			mLocationActivity.send.setBackgroundResource(R.drawable.shape_im_btn_send_none);
			mLocationActivity.send.setTextColor(0x66999999);
			mLocationActivity.send.setEnabled(false);
		}
		else
		{
			mLocationActivity.send.setBackgroundResource(R.drawable.shape_im_btn_send);
			mLocationActivity.send.setTextColor(0xFFFFFFFF);
			mLocationActivity.send.setEnabled(true);
		}
	}

	public CustomScrollView.OnScrollChangeListener onScrollChangeListener = new CustomScrollView.OnScrollChangeListener()
	{

		@Override
		public void onScrollToStart() {

		}

		@Override
		public void onScrollToEnd() {
			mLocationActivity.scrollView.dosuper = true;
		}
	};

	public void addView(MapAddress mPoiItem) {
		View convertView = mLocationActivity.getLayoutInflater().inflate(R.layout.address_map_item, null);
		TextView mAddress = (TextView) convertView.findViewById(R.id.item_address);
		TextView mTitle = (TextView) convertView.findViewById(R.id.item_title);
		mTitle.setText(mPoiItem.poiItem.getTitle());
		mAddress.setText(mPoiItem.poiItem.getSnippet());
		convertView.setTag(mPoiItem);
		convertView.setOnClickListener(mLocationActivity.mOnItemClickListener);
		mPoiItem.view = convertView.findViewById(R.id.selecticon);
		mLocationActivity.mListView.addView(convertView);
	}

	public void updataAdressView(MapAddress mPoiItem) {
		if(mPoiItem.view != null)
		{
			if(mPoiItem.select)
			{
				mPoiItem.view.setVisibility(View.VISIBLE);
			}
			else
			{
				mPoiItem.view.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void addListView(ArrayList<MapAddress> items) {
		for(int i = 0 ; i < items.size() ; i++) {
			addView(items.get(i));
		}
	}

	public void onSearchClick(TextView v, int actionId, KeyEvent event)
	{
		if (actionId == EditorInfo.IME_ACTION_SEARCH)
		{
			if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false) == false)
			{
				doSearch();
			}
			else
			{
				selectAddress(mLocationActivity.mSearch.getText().toString());
			}
		}
	}

	public void doSearch()
	{
		if(mLocationActivity.searchIng == false)
		{
			if(mLocationActivity.mSearch.getText().length() > 0)
				selectAddress(mLocationActivity.mSearch.getText().toString());
			else
			{
				if(mLocationActivity.last != null)
				{
					selectAddress(mLocationActivity.last);
				}
			}
		}
	}

	public void onItemClick(MapAddress mPoiItem)
	{
		if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false) == false)
		{
			if(mPoiItem.select == false)
			{
				mPoiItem.select = true;
				if(mLocationActivity.selectAddress != null)
				{
					mLocationActivity.selectAddress.select = false;
					updataAdressView(mLocationActivity.selectAddress);
				}
				mLocationActivity.selectAddress = mPoiItem;
				mLocationActivity.aMap.setOnCameraChangeListener(mLocationActivity.cameraChangeListener2);
				LatLng latLng = new LatLng(mLocationActivity.selectAddress.poiItem.getLatLonPoint().getLatitude()
						,mLocationActivity.selectAddress.poiItem.getLatLonPoint().getLongitude());
				mLocationActivity.aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
				updataAdressView(mPoiItem);
			}
			else
			{
				mPoiItem.select = false;
				updataAdressView(mPoiItem);
				mLocationActivity.selectAddress = null;
			}
			showSend();
		}
		else{
			Intent intent = new Intent();
			intent.setAction(mLocationActivity.getIntent().getAction());
			intent.putExtra("city", mPoiItem.poiItem.getTitle());
			intent.putExtra("addressdetial", mPoiItem.poiItem.getSnippet());
			mLocationActivity.sendBroadcast(intent);
			mLocationActivity.finish();
		}
	}

	public void initMap() {
		//mLocationActivity.waitDialog.show();
		mLocationActivity.aMap = mLocationActivity.mMapView.getMap();
		if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false) == true)
		{
			MyLocationStyle myLocationStyle;
			myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
			myLocationStyle.strokeColor(R.color.trans);//设置定位蓝点精度圆圈的边框颜色的方法。
			myLocationStyle.radiusFillColor(R.color.trans);//设置定位蓝点精度圆圈的填充颜色的方法
			myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
			mLocationActivity.aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
			mLocationActivity.aMap.setLocationSource(mLocationActivity);// 设置定位监听
			mLocationActivity.aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		}
		else{
			if(MapManager.getInstance().lastAMapLocation != null)
			{
				AMapLocation aMapLocation = MapManager.getInstance().lastAMapLocation;
				MarkerOptions markerOptions = new MarkerOptions();
				LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
				markerOptions.position(latLng);
				markerOptions.draggable(true);
				mLocationActivity.centerMark = mLocationActivity.aMap.addMarker(markerOptions);
				mLocationActivity.aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
			}
			mLocationActivity.aMap.setMyLocationEnabled(false);
		}

		mLocationActivity.aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mLocationActivity.aMap.getUiSettings().setZoomControlsEnabled(false);
		mLocationActivity.aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		mLocationActivity.aMap.getUiSettings().setAllGesturesEnabled(true);
		mLocationActivity.aMap.getUiSettings().setScaleControlsEnabled(false);
		mLocationActivity.aMap.setOnCameraChangeListener(mLocationActivity.cameraChangeListener);
        mLocationActivity.aMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider)
                .diskCacheEnabled(true)
                .diskCacheDir("/storage/emulated/0/demo/cache")
                .diskCacheSize(100000)
                .memoryCacheEnabled(true)
                .memCacheSize(100000));
	}


    public void getGoogleAddress(AMapLocation amapLocation)
    {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(mLocationActivity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(amapLocation.getLatitude(), amapLocation.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zipCode = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryCode();
            Message msg = new Message();
            msg.what = LocationHandler.EVENT_SET_ADDRESS_OUT_SIDE;
            msg.obj = addresses;
			if(mLocationHandler != null)
            mLocationHandler.sendMessage(msg);
            selectAddressOut(addresses);
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

		if (mLocationActivity.mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mLocationActivity.mListener.onLocationChanged(amapLocation);
				MapManager.getInstance().mLatLonPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());

                CoordinateConverter converter  = new CoordinateConverter(mLocationActivity);
                boolean isAMapDataAvailable = converter.isAMapDataAvailable(amapLocation.getLatitude(),amapLocation.getLongitude());
                if(isAMapDataAvailable)
                {
                    Message msg = new Message();
                    msg.what = LocationHandler.EVENT_SET_ADDRESS;
                    msg.obj = amapLocation;
//                    if(mLocationActivity.mAddress.length() == 0)
//					{
//						if(mLocationHandler != null)
//							mLocationHandler.sendMessage(msg);
//					}
//					mLocationHandler.sendEmptyMessage()
					if(mLocationHandler != null)
						mLocationHandler.sendMessage(msg);
                }
                else
                {

						getGoogleAddress(amapLocation);
                }

			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}

	public void onActivate(LocationSource.OnLocationChangedListener listener)
	{
		if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false)== true)
		{
			mLocationActivity.mListener = listener;
			if(MapManager.getInstance().lastAMapLocation != null)
			{
				AMapLocation aMapLocation = MapManager.getInstance().lastAMapLocation;
				MarkerOptions markerOptions = new MarkerOptions();
				LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
				markerOptions.position(latLng);
				markerOptions.draggable(true);

//				{
//					mLocationActivity.centerMark = mLocationActivity.aMap.addMarker(markerOptions);
//				}

				mLocationActivity.mListener.onLocationChanged(MapManager.getInstance().lastAMapLocation);
			}
		}

	}

	public void onDeactivate()
	{
		mLocationActivity.mListener = null;
	}

	public void selectAddress(String key)
	{
		PoiSearch.Query query = new PoiSearch.Query(key,"","");
		query.setPageSize(30);
		query.setPageNum(1);
		PoiSearch poiSearch = new PoiSearch(mLocationActivity, query);
		poiSearch.setOnPoiSearchListener(mLocationActivity);
		mLocationActivity.searches.add(poiSearch);
	}

	public void selectAddress2(String key)
	{
		PoiSearch.Query query = new PoiSearch.Query(key,"",mLocationActivity.mCity);
		query.setPageSize(30);
		query.setPageNum(1);
		PoiSearch poiSearch = new PoiSearch(mLocationActivity, query);
		poiSearch.setOnPoiSearchListener(mLocationActivity);
		if(MapManager.getInstance().lastAMapLocation != null)
		{
			LatLonPoint latLng = new LatLonPoint(MapManager.getInstance().lastAMapLocation.getLatitude(),
					MapManager.getInstance().lastAMapLocation.getLongitude());
			poiSearch.setBound(new PoiSearch.SearchBound(latLng, 200));//
		}
		mLocationActivity.searches.add(poiSearch);
	}

	public void selectAddress(LatLng latLng)
	{
		PoiSearch.Query query = new PoiSearch.Query("","","");
		query.setPageSize(30);
		query.setPageNum(1);
		PoiSearch poiSearch = new PoiSearch(mLocationActivity, query);
		poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
				latLng.longitude), 1000));
		poiSearch.setOnPoiSearchListener(mLocationActivity);
		mLocationActivity.searches.add(poiSearch);

	}

    public void selectAddressOut( List<Address> addresses)
    {
        ArrayList<PoiItem> b = new ArrayList();
        for(int i = 0 ; i < addresses.size() ; i++)
        {
            Address mAddres = addresses.get(i);
            LatLonPoint mLatLonPoint = new LatLonPoint(mAddres.getLatitude(),mAddres.getLongitude());
            PoiItem mPoiItem = new PoiItem("",mLatLonPoint,mAddres.getLocality()+","+mAddres.getAdminArea()
                    ,mAddres.getAddressLine(0)+","+ mAddres.getLocality()+","+mAddres.getAdminArea());
            b.add(mPoiItem);
        }
        Message msg = new Message();
        msg.obj =  b;
        msg.what = LocationHandler.EVENT_UPDATA_LIST;
		if(mLocationHandler != null)
        mLocationHandler.sendMessageDelayed(msg,500);
    }

	public void doSelect(PoiResult arg0)
	{
		Message msg = new Message();
		msg.obj =  arg0.getPois();
		msg.what = LocationHandler.EVENT_UPDATA_LIST;
		if(mLocationHandler != null)
		mLocationHandler.sendMessage(msg);
	}

	public void onFoot()
	{
		mLocationHandler.sendEmptyMessage(LocationHandler.EVENT_ON_FOOT);
	}

	public void doOnFoot() {
		if(mLocationActivity.poiSearch != null && mLocationActivity.searchIng == false)
		{
			mLocationActivity.searches.add(mLocationActivity.poiSearch);
		}
	}

	public void onCameraMove(CameraPosition cameraPosition)
	{
		if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false) == false)
		mLocationActivity.centerMark.setPosition(cameraPosition.target);

	}

	public void onCameraFinish(CameraPosition cameraPosition) {

		if(mLocationActivity.getIntent().getBooleanExtra("selectaddress",false)== false)
		{
			if(mLocationActivity.last == null)
			{
				mLocationActivity.last = cameraPosition.target;
				selectAddress(cameraPosition.target);
			}
			else
			{
				if(mLocationActivity.last.latitude != cameraPosition.target.latitude ||
						mLocationActivity.last.longitude != cameraPosition.target.longitude)
				{
					mLocationActivity.last = cameraPosition.target;
					selectAddress(cameraPosition.target);
				}
			}
		}


	}

	public void onMarkSearch(RegeocodeResult result, int rCode) {

	}

	public void showWait() {

		if(mLocationActivity.searchIng == false)
		{
			mLocationActivity.searchIng = true;
			if(mLocationActivity.mListView.getChildCount() == 0)
			{
				mLocationActivity.waitDialog.show();
			}
			else
			{
				mLocationActivity.mListView.addView(mLocationActivity.watiView);
			}
		}

	}

	public void hidWait()
	{
		if(mLocationActivity.searchIng == true)
		{
			mLocationActivity.searchIng = false;
		}

	}

	public void startSearch() {
		if(mLocationActivity.searchIng == false)
		{
			if(mLocationActivity.searches.size() > 0)
			{
				showWait();
				PoiSearch poiSearch = mLocationActivity.searches.get(0);
				mLocationActivity.searches.remove(0);
				mLocationActivity.poiSearch = poiSearch;
				mLocationActivity.poiSearch.searchPOIAsyn();
			}
			mLocationHandler.sendEmptyMessageDelayed(LocationHandler.EVENT_START_SEARCH,100);
		}
		else
			mLocationHandler.sendEmptyMessageDelayed(LocationHandler.EVENT_START_SEARCH,500);
	}

	public void doSend() {
		mLocationActivity.waitDialog.show();
		mLocationActivity.aMap.getMapScreenShot(screenShotListener);
	}

	public AMap.OnMapScreenShotListener screenShotListener = new AMap.OnMapScreenShotListener() {

		@Override
		public void onMapScreenShot(Bitmap bitmap) {
			int cropwidth = bitmap.getWidth()*2/3;
			int cropheight = cropwidth/3;
			BitmapCache.saveBitmap(BitmapCache.cropBitmapCenter(bitmap,cropwidth,cropheight),mLocationActivity.getIntent().getStringExtra("path"));
			mLocationActivity.waitDialog.hide();
			Intent intent = new Intent();
			intent.setAction(mLocationActivity.getIntent().getAction());
			intent.putExtra("locationname", mLocationActivity.selectAddress.poiItem.getTitle());
			intent.putExtra("locationaddress", mLocationActivity.selectAddress.poiItem.getSnippet());
			intent.putExtra("longitude", mLocationActivity.selectAddress.poiItem.getLatLonPoint().getLongitude());
			intent.putExtra("latitude", mLocationActivity.selectAddress.poiItem.getLatLonPoint().getLatitude());
			intent.putExtra("path", mLocationActivity.getIntent().getStringExtra("path"));
			mLocationActivity.sendBroadcast(intent);
			mLocationActivity.finish();
		}
	};
}
