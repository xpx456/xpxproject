package xpx.map.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.TileProvider;
import com.amap.api.maps2d.model.UrlTileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.MenuItem;
import xpx.com.toolbar.utils.ToolBarHelper;
import xpx.map.MapManager;
import xpx.map.MapUtils;
import xpx.map.R;
import xpx.map.handler.BigMapHandler;
import xpx.map.view.activity.BigMapActivity;

public class BigMapPresenter implements Presenter {

	public BigMapActivity mLocationActivity;
    public BigMapHandler mLocationHandler;

	public BigMapPresenter(BigMapActivity mLocationActivity)
	{
		this.mLocationActivity = mLocationActivity;
		this.mLocationHandler = new BigMapHandler(mLocationActivity);
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
		mLocationActivity.setContentView(R.layout.activity_big_map);
		mLocationActivity.shade = mLocationActivity.findViewById(R.id.shade);
		mLocationActivity.back = mLocationActivity.findViewById(R.id.back);
		mLocationActivity.more = mLocationActivity.findViewById(R.id.more);
		mLocationActivity.address = mLocationActivity.findViewById(R.id.item_address);
		mLocationActivity.name = mLocationActivity.findViewById(R.id.item_title);
		try {
			JSONObject jsonObject = new JSONObject(mLocationActivity.getIntent().getStringExtra("json"));
			mLocationActivity.name.setText(jsonObject.getString("locationName"));
			mLocationActivity.address.setText(jsonObject.getString("locationAddress"));
			mLocationActivity.latLng = new LatLng(jsonObject.getDouble("latitude"),jsonObject.getDouble("longitude"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mLocationActivity.startmap = mLocationActivity.findViewById(R.id.startmap);
		mLocationActivity.mMapView = (MapView) mLocationActivity.findViewById(R.id.detial_cion);
		mLocationActivity.back.setOnClickListener(mLocationActivity.backListener);
		mLocationActivity.more.setOnClickListener(mLocationActivity.moreListener);
		mLocationActivity.startmap.setOnClickListener(mLocationActivity.startListener);
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
		MapManager.getInstance().stopLocation();
	}

	public void initMap() {
		//mLocationActivity.waitDialog.show();
		mLocationActivity.aMap = mLocationActivity.mMapView.getMap();
//		MyLocationStyle myLocationStyle;
//		myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//		myLocationStyle.strokeColor(R.color.trans);//设置定位蓝点精度圆圈的边框颜色的方法。
//		myLocationStyle.radiusFillColor(R.color.trans);//设置定位蓝点精度圆圈的填充颜色的方法
//		mLocationActivity.aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		mLocationActivity.aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mLocationActivity.aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mLocationActivity.aMap.getUiSettings().setZoomControlsEnabled(false);
		MarkerOptions markerOptions = new MarkerOptions();
		LatLng latLng = new LatLng(mLocationActivity.latLng.latitude,mLocationActivity.latLng.longitude);
		markerOptions.position(latLng);
		mLocationActivity.centerMark = mLocationActivity.aMap.addMarker(markerOptions);
		mLocationActivity.aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLocationActivity.latLng));
		mLocationActivity.aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
		mLocationActivity.aMap.getUiSettings().setAllGesturesEnabled(true);
		mLocationActivity.aMap.getUiSettings().setScaleControlsEnabled(false);
        mLocationActivity.aMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider)
                .diskCacheEnabled(true)
                .diskCacheDir("/storage/emulated/0/demo/cache")
                .diskCacheSize(100000)
                .memoryCacheEnabled(true)
                .memCacheSize(100000));
	}

	public void more() {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		MenuItem menuItem = new MenuItem();
		menuItem.btnName = mLocationActivity.getString(R.string.button_word_sendim);
		menuItem.mListener =mLocationActivity.sendListener;
		menuItems.add(menuItem);
		AppUtils.creatButtomMenu(mLocationActivity,mLocationActivity.shade,menuItems,mLocationActivity.findViewById(R.id.addressselect));
	}

	public void startMap() {
        try {
            AMapLocation aMapLocation = MapManager.getInstance().lastAMapLocation;
            if(aMapLocation != null)
            {
                JSONObject jsonObject = new JSONObject(mLocationActivity.getIntent().getStringExtra("json"));
                MapUtils.showMapLocation(mLocationActivity,mLocationActivity.shade,mLocationActivity.findViewById(R.id.addressselect)
                        ,mLocationActivity.getString(R.string.app_name),aMapLocation.getAoiName(),aMapLocation.getLatitude(),aMapLocation.getLongitude(),
                        jsonObject.getString("locationName"),jsonObject.getDouble("latitude"),jsonObject.getDouble("longitude"));
            }
            else
            {
                AppUtils.showMessage(mLocationActivity,"please open loaction");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

	public void doSendContact() {
		Intent intent = new Intent();
		intent.putExtra("json",mLocationActivity.getIntent().getStringExtra("json"));
		MapManager.getInstance().mapFunctions.sendContact(mLocationActivity,intent);
	}
}
