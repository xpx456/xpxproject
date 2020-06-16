package intersky.sign.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import intersky.appbase.BaseActivity;
import intersky.sign.entity.Sign;
import intersky.sign.presenter.StatisticsDetialPresenter;


@SuppressLint("ClickableViewAccessibility")
public class StatisticsDetialActivity extends BaseActivity implements OnGestureListener, OnTouchListener {

	public GestureDetector mGestureDetector;
	public MapView mMapView;
	public AMap aMap;
	public Sign mAttendanceModel;
	public StatisticsDetialPresenter mStatisticsDetialPresenter = new StatisticsDetialPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStatisticsDetialPresenter.Create();
		mMapView.onCreate(savedInstanceState);
		mStatisticsDetialPresenter.initMap();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mStatisticsDetialPresenter.Destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mStatisticsDetialPresenter.Resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mStatisticsDetialPresenter.Pause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

}
