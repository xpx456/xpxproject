package xpx.map.view.activity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

import intersky.mywidget.CustomScrollView;
import intersky.mywidget.MapContainer;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.mywidget.SecondScrollView;
import xpx.map.entity.MapAddress;
import xpx.map.presenter.LocationPresenter;
import xpx.map.view.adapter.AddressAdapter;

public class LocationActivity extends BaseActivity implements
		LocationSource,AMapLocationListener,PoiSearch.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener,CustomScrollView.OnScrollChangeListener {

	public MapContainer mapContainer;
	public boolean isfirst = true;
	public LinearLayout mListView;
	public MapView mMapView;
	public SearchViewLayout mSearch;
	public SecondScrollView scrollView;
	public LocationPresenter mLocationPresenter = new LocationPresenter(this);
	public PoiSearch poiSearch;
	public OnLocationChangedListener mListener;
	public AMap aMap;
	public String mAddress = "";
	public String mCity = "";
	public boolean isall = false;
	public Marker centerMark;
	public GeocodeSearch geocodeSearch;
	public MapAddress selectAddress;
	public boolean searchIng = false;
	public ArrayList<PoiSearch> searches = new ArrayList<PoiSearch>();
	public LatLng last;
	public View watiView;
	public View back;
	public TextView send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationPresenter.Create();
		mMapView.onCreate(savedInstanceState);
		mLocationPresenter.initMap();
	}

//	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//			// TODO Auto-generated method stub
//			//mLocationPresenter.onItemClick(SignManager.getInstance().mPoiItems.get(arg2));
//		}
//
//	};

	public View.OnClickListener mOnItemClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mLocationPresenter.onItemClick((MapAddress) v.getTag());
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationPresenter.Destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocationPresenter.Resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationPresenter.Pause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}



	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		mLocationPresenter.onLocation(aMapLocation);
	}

	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		mLocationPresenter.onActivate(onLocationChangedListener);
	}

	@Override
	public void deactivate() {
		mLocationPresenter.onDeactivate();
	}

	@Override
	public void onPoiSearched(PoiResult poiResult, int i) {
		mLocationPresenter.doSelect(poiResult);
	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {
		//mLocationPresenter.doSelect(poiResult);
	}


	public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// TODO Auto-generated method stub
			mLocationPresenter.onSearchClick(v, actionId, event);
			return true;
		}
	};

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}


	public AMap.OnCameraChangeListener cameraChangeListener = new AMap.OnCameraChangeListener()
	{

		@Override
		public void onCameraChange(CameraPosition cameraPosition) {
			mLocationPresenter.onCameraMove(cameraPosition);
		}

		@Override
		public void onCameraChangeFinish(CameraPosition cameraPosition) {
			mLocationPresenter.onCameraFinish(cameraPosition);
		}
	};

	public AMap.OnCameraChangeListener cameraChangeListener2 = new AMap.OnCameraChangeListener()
	{

		@Override
		public void onCameraChange(CameraPosition cameraPosition) {
			mLocationPresenter.onCameraMove(cameraPosition);
		}

		@Override
		public void onCameraChangeFinish(CameraPosition cameraPosition) {
			aMap.setOnCameraChangeListener(cameraChangeListener);
		}
	};

	@Override
	public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
		mLocationPresenter.onMarkSearch(regeocodeResult,i);
	}

	@Override
	public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

	}

	@Override
	public void onScrollToStart() {
		scrollView.dosuper = false;
	}

	@Override
	public void onScrollToEnd() {
		mLocationPresenter.onFoot();
	}

	public View.OnClickListener backListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public View.OnClickListener sendListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mLocationPresenter.doSend();
		}
	};
}
