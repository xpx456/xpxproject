package intersky.sign.presenter;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

import intersky.appbase.Presenter;
import intersky.sign.R;
import intersky.sign.view.activity.StatisticsDetialActivity;
import xpx.com.toolbar.utils.ToolBarHelper;

public class StatisticsDetialPresenter implements Presenter {

	private StatisticsDetialActivity mStatisticsDetialActivity;
	
	public StatisticsDetialPresenter(StatisticsDetialActivity mStatisticsDetialActivity)
	{
		this.mStatisticsDetialActivity = mStatisticsDetialActivity;
	
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
		mStatisticsDetialActivity.setContentView(R.layout.activity_statistics_detial);
		mStatisticsDetialActivity.mAttendanceModel = mStatisticsDetialActivity.getIntent().getParcelableExtra("sign");
		RelativeLayout mLinearLayout = (RelativeLayout) mStatisticsDetialActivity.findViewById(R.id.statisticsdetial);
		mLinearLayout.setOnTouchListener(mStatisticsDetialActivity);
		mLinearLayout.setLongClickable(true);
		mStatisticsDetialActivity.mGestureDetector = new GestureDetector((OnGestureListener) mStatisticsDetialActivity);
		ToolBarHelper.setTitle(mStatisticsDetialActivity.mActionBar, mStatisticsDetialActivity.getString(R.string.keyword_statistics_detoal));
		mStatisticsDetialActivity.mMapView = (MapView) mStatisticsDetialActivity.findViewById(R.id.map_view);
	}

	public void initMap() {
		mStatisticsDetialActivity.aMap = mStatisticsDetialActivity.mMapView.getMap();
		MyLocationStyle myLocationStyle;
		myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		myLocationStyle.strokeColor(R.color.trans);//设置定位蓝点精度圆圈的边框颜色的方法。
		myLocationStyle.radiusFillColor(R.color.trans);//设置定位蓝点精度圆圈的填充颜色的方法

		mStatisticsDetialActivity.aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		mStatisticsDetialActivity.aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mStatisticsDetialActivity.aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mStatisticsDetialActivity.aMap.getUiSettings().setZoomControlsEnabled(false);
		mStatisticsDetialActivity.aMap.addTileOverlay(new TileOverlayOptions()
				.tileProvider(tileProvider)
				.diskCacheEnabled(true)
				.diskCacheDir("/storage/emulated/0/demo/cache")
				.diskCacheSize(100000)
				.memoryCacheEnabled(true)
				.memCacheSize(100000));
		mStatisticsDetialActivity.aMap.animateCamera(CameraUpdateFactory.newCameraPosition(
				new CameraPosition(new LatLng(mStatisticsDetialActivity.mAttendanceModel.mAltitude
						,mStatisticsDetialActivity.mAttendanceModel.mLongitude), 0, 0, 0)));
		mStatisticsDetialActivity.aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		mStatisticsDetialActivity.aMap.addMarker(new MarkerOptions().position(new LatLng(mStatisticsDetialActivity.mAttendanceModel.mAltitude
				,mStatisticsDetialActivity.mAttendanceModel.mLongitude)));
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
		
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		
	}



}
