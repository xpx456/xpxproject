package intersky.sign.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import intersky.appbase.BaseActivity;
import intersky.mywidget.MyRelativeLayout;
import intersky.sign.SignManager;
import intersky.sign.presenter.SignPresenter;


@SuppressLint("ClickableViewAccessibility")
public class SignActivity extends BaseActivity implements OnGestureListener, OnTouchListener,
		LocationSource,PoiSearch.OnPoiSearchListener {
	
	public TextView dateText;
	public TextView btnChange;
	public TextView dateText1;
	public TextView dateText2;
	public TextView dateText3;
	public RelativeLayout datelayer;
	public TextView detialContent;
	public TextView signcount;
	public EditText mEditText;
	public ImageView btnSign;
	public ImageView dateIcon;
	public MapView mMapView;
	public MyRelativeLayout mMapLayer;
//	public AMapLocationClientOption mLocationOption = null;
//	public AMapLocationClient mlocationClient;
	public LatLonPoint mLatLonPoint = new LatLonPoint(0,0);
	public PoiSearch poiSearch;
	public PoiSearch.Query mQuery;
	public SignPresenter mSignPresenter = new SignPresenter(this);
	public OnLocationChangedListener mListener;
	public AMap aMap;
	public String mAddress = "";
	public TextView addarssname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSignPresenter.Create();
		mMapView.onCreate(savedInstanceState);
		mSignPresenter.initMap();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSignPresenter.Destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSignPresenter.Resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSignPresenter.Pause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	public View.OnClickListener mBackListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	public View.OnClickListener mStatisticsListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSignPresenter.doStatstics();
		}
	};

	public View.OnClickListener mSignListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			mSignPresenter.doSign();
		}
	};

	public View.OnClickListener mDateListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			mSignPresenter.showDateDialog();
		}
	};


	public View.OnClickListener mSelectAddressListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SignManager.getInstance().mapManager.startSelectLocationAddress(mSignPresenter.mSignActivity,SignManager.ACTION_AMPA_SET_ADDTESS);
		}
	};

//	@Override
//	public void onLocationChanged(AMapLocation arg0) {
//		// TODO Auto-generated method stub
//		mSignPresenter.onLocation(arg0);
//	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		// TODO Auto-generated method stub
		mSignPresenter.onActivate(arg0);
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mSignPresenter.onDeactivate();
	}


	@Override
	public void onPoiSearched(PoiResult poiResult, int i) {
//		mSignPresenter.doSelect(poiResult);
	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {

	}
}
