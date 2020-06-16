package xpx.map.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;

import java.util.Map;

import intersky.appbase.BaseActivity;
import intersky.mywidget.MapContainer;
import xpx.map.MapManager;
import xpx.map.entity.MapAddress;
import xpx.map.presenter.BigMapPresenter;

public class BigMapActivity extends BaseActivity {

	public MapView mMapView;
	public BigMapPresenter mBigMapPresenter = new BigMapPresenter(this);
	public AMap aMap;
	public Marker centerMark;
	public TextView address;
	public RelativeLayout shade;
	public TextView name;
	public ImageView startmap;
	public LatLng latLng;
	public ImageView back;
	public ImageView more;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBigMapPresenter.Create();
		mMapView.onCreate(savedInstanceState);
		mBigMapPresenter.initMap();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBigMapPresenter.Destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBigMapPresenter.Resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBigMapPresenter.Pause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}



	public View.OnClickListener backListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public View.OnClickListener moreListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mBigMapPresenter.more();
		}
	};

	public View.OnClickListener startListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mBigMapPresenter.startMap();
		}
	};

	public View.OnClickListener sendListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			mBigMapPresenter.doSendContact();
		}
	};
}
