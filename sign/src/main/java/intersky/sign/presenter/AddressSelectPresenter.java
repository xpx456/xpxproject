package intersky.sign.presenter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import intersky.appbase.Presenter;
import intersky.apputils.Onlocation;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.handler.AddressSelectHandler;
import intersky.sign.view.activity.AddressSelectActivity;
import intersky.sign.view.adapter.AddressAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class AddressSelectPresenter implements Presenter {

	public AddressSelectActivity mAddressSelectActivity;
    public AddressSelectHandler mAddressSelectHandler;

	public AddressSelectPresenter(AddressSelectActivity mAddressSelectActivity)
	{
		this.mAddressSelectActivity = mAddressSelectActivity;
		this.mAddressSelectHandler = new AddressSelectHandler(mAddressSelectActivity);
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
		mAddressSelectActivity.setContentView(R.layout.activity_address_select);
		mAddressSelectActivity.mPullToRefreshView = (PullToRefreshView) mAddressSelectActivity.findViewById(R.id.mail_pull_refresh_view);
		mAddressSelectActivity.mSearch = (SearchViewLayout) mAddressSelectActivity.findViewById(R.id.search);
		mAddressSelectActivity.mMapView = (MapView) mAddressSelectActivity.findViewById(R.id.detial_cion);
		mAddressSelectActivity.mSearch.mSetOnSearchListener(mAddressSelectActivity.mOnEditorActionListener);
		ToolBarHelper.setTitle(mAddressSelectActivity.mActionBar, mAddressSelectActivity.getString(R.string.keyword_selectaddress));
		mAddressSelectActivity.mListView = (ListView) mAddressSelectActivity.findViewById(R.id.address_List);
		mAddressSelectActivity.mListView.setOnItemClickListener(mAddressSelectActivity.mOnItemClickListener);
		mAddressSelectActivity.mAddressAdapter = new AddressAdapter(mAddressSelectActivity, SignManager.getInstance().mPoiItems);
		mAddressSelectActivity.mListView.setAdapter(mAddressSelectActivity.mAddressAdapter);
		mAddressSelectActivity.mPullToRefreshView.setOnHeaderRefreshListener(mAddressSelectActivity);
		mAddressSelectActivity.mPullToRefreshView.setOnFooterRefreshListener(mAddressSelectActivity);
		SignManager.getInstance().mapManager.addLocation(onlocation);
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
		mAddressSelectActivity.mMapView.onResume();
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		mAddressSelectActivity.mMapView.onPause();
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mAddressSelectActivity.mMapView.onDestroy();
		mAddressSelectHandler = null;
		SignManager.getInstance().mapManager.removeLocation(onlocation);
	}


	public void onSearchClick(TextView v, int actionId, KeyEvent event)
	{
		if (actionId == EditorInfo.IME_ACTION_SEARCH)
		{
			//if (mAddressSelectActivity.mSearch.getText().toString().length() > 0)
			{
				doSearch();
			}
		}
	}

	public void doSearch()
	{
		mAddressSelectActivity.isall = false;
		mAddressSelectActivity.page = 0;
		SignManager.getInstance().mPoiItems.clear();
		selectAddress(mAddressSelectActivity.mSearch.getText().toString());
	}
	
	public void onItemClick(PoiItem mPoiItem)
	{
		Intent intent = new Intent();
		intent.setAction(SignManager.ACTION_AMPA_SET_ADDTESS);
		intent.putExtra("city", mPoiItem.getTitle());
		intent.putExtra("addressdetial", mPoiItem.getSnippet());
		SignManager.getInstance().addressname = mPoiItem.getTitle();
		SignManager.getInstance().addressdetial = mPoiItem.getSnippet();
		mAddressSelectActivity.sendBroadcast(intent);
		mAddressSelectActivity.finish();
	}

	public void initMap() {
		mAddressSelectActivity.waitDialog.show();
		mAddressSelectActivity.aMap = mAddressSelectActivity.mMapView.getMap();
		MyLocationStyle myLocationStyle;
		myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		myLocationStyle.strokeColor(R.color.trans);//设置定位蓝点精度圆圈的边框颜色的方法。
		myLocationStyle.radiusFillColor(R.color.trans);//设置定位蓝点精度圆圈的填充颜色的方法
		mAddressSelectActivity.aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		mAddressSelectActivity.aMap.setLocationSource(mAddressSelectActivity);// 设置定位监听
		mAddressSelectActivity.aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mAddressSelectActivity.aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mAddressSelectActivity.aMap.getUiSettings().setZoomControlsEnabled(false);
		mAddressSelectActivity.aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		mAddressSelectActivity.aMap.getUiSettings().setAllGesturesEnabled(false);
		((ViewGroup) mAddressSelectActivity.mMapView.getChildAt(0)).removeViewAt(2);
        mAddressSelectActivity.aMap.addTileOverlay(new TileOverlayOptions()
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
        Geocoder geocoder = new Geocoder(mAddressSelectActivity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(amapLocation.getLatitude(), amapLocation.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zipCode = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryCode();
            Message msg = new Message();
            msg.what = AddressSelectHandler.EVENT_SET_ADDRESS_OUT_SIDE;
            msg.obj = addresses;
			if(mAddressSelectHandler != null)
            mAddressSelectHandler.sendMessage(msg);
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

		if (mAddressSelectActivity.mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mAddressSelectActivity.mListener.onLocationChanged(amapLocation);
				SignManager.getInstance().mLatLonPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());

                CoordinateConverter converter  = new CoordinateConverter(mAddressSelectActivity);
                boolean isAMapDataAvailable = converter.isAMapDataAvailable(amapLocation.getLatitude(),amapLocation.getLongitude());
                if(isAMapDataAvailable)
                {
                    Message msg = new Message();
                    msg.what = AddressSelectHandler.EVENT_SET_ADDRESS;
                    msg.obj = amapLocation;
                    if(mAddressSelectActivity.mAddress.length() == 0)
					{
						if(mAddressSelectHandler != null)
							mAddressSelectHandler.sendMessage(msg);
					}

                    if(mAddressSelectActivity.isfirst)
                    {
                        mAddressSelectActivity.isfirst = false;
                        mAddressSelectActivity.waitDialog.hide();
//                        selectAddress("");
                    }
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
		mAddressSelectActivity.mListener = listener;
		if(SignManager.getInstance().mapManager.lastAMapLocation != null)
		{
			mAddressSelectActivity.mListener.onLocationChanged(SignManager.getInstance().mapManager.lastAMapLocation);
		}
	}

	public void onDeactivate()
	{
		mAddressSelectActivity.mListener = null;
	}

	public void selectAddress(String key)
	{
		mAddressSelectActivity.mQuery = new PoiSearch.Query(key,"",mAddressSelectActivity.mCity);
		mAddressSelectActivity.mQuery.setPageSize(30);
		mAddressSelectActivity.mQuery.setPageNum(mAddressSelectActivity.page);
		mAddressSelectActivity.poiSearch = new PoiSearch(mAddressSelectActivity, mAddressSelectActivity.mQuery);
		mAddressSelectActivity.poiSearch.setOnPoiSearchListener(mAddressSelectActivity);
				mAddressSelectActivity.poiSearch.setBound(new PoiSearch.SearchBound(SignManager.getInstance().mLatLonPoint, 200));//
		mAddressSelectActivity.poiSearch.searchPOIAsyn();

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
        msg.what = AddressSelectHandler.EVENT_UPDATA_LIST;
		if(mAddressSelectHandler != null)
        mAddressSelectHandler.sendMessage(msg);
    }

	public void doSelect(PoiResult arg0)
	{

		Message msg = new Message();
		msg.obj =  arg0.getPois();
		msg.what = AddressSelectHandler.EVENT_UPDATA_LIST;
		if(mAddressSelectHandler != null)
		mAddressSelectHandler.sendMessage(msg);
	}

	public void onHead()
	{
		mAddressSelectActivity.isall = false;
		mAddressSelectActivity.page = 0;
		SignManager.getInstance().mPoiItems.clear();
		mAddressSelectActivity.mPullToRefreshView.onHeaderRefreshComplete();
		selectAddress(mAddressSelectActivity.mSearch.getText().toString());
	}

	public void onFoot()
	{
		mAddressSelectActivity.mPullToRefreshView.onFooterRefreshComplete();
		if(mAddressSelectActivity.isall == false)
		selectAddress(mAddressSelectActivity.mSearch.getText().toString());
	}


}
