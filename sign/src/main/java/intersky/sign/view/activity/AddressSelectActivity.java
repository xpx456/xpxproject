package intersky.sign.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import intersky.appbase.BaseActivity;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.sign.SignManager;
import intersky.sign.presenter.AddressSelectPresenter;
import intersky.sign.view.adapter.AddressAdapter;

@SuppressLint("ClickableViewAccessibility")
public class AddressSelectActivity extends BaseActivity implements  View.OnTouchListener,PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener,
		LocationSource,AMapLocationListener,PoiSearch.OnPoiSearchListener {


	public boolean isfirst = true;
	public ListView mListView;
	public MapView mMapView;
	public SearchViewLayout mSearch;
	public AddressAdapter mAddressAdapter;
	public PullToRefreshView mPullToRefreshView;
	public AddressSelectPresenter mAddressSelectPresenter = new AddressSelectPresenter(this);
	public PoiSearch poiSearch;
	public PoiSearch.Query mQuery;
	public OnLocationChangedListener mListener;
	public AMap aMap;
	public String mAddress = "";
	public String mCity = "";
	public int page = 0;
	public boolean isall = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAddressSelectPresenter.Create();
		mMapView.onCreate(savedInstanceState);
		mAddressSelectPresenter.initMap();
	}

	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			mAddressSelectPresenter.onItemClick(SignManager.getInstance().mPoiItems.get(arg2));
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAddressSelectPresenter.Destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAddressSelectPresenter.Resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mAddressSelectPresenter.Pause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}



	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		mAddressSelectPresenter.onLocation(aMapLocation);
	}

	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		mAddressSelectPresenter.onActivate(onLocationChangedListener);
	}

	@Override
	public void deactivate() {
		mAddressSelectPresenter.onDeactivate();
	}

	@Override
	public void onPoiSearched(PoiResult poiResult, int i) {
		mAddressSelectPresenter.doSelect(poiResult);
	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {

	}


	public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// TODO Auto-generated method stub
			mAddressSelectPresenter.onSearchClick(v, actionId, event);
			return true;
		}
	};

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mAddressSelectPresenter.onFoot();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mAddressSelectPresenter.onHead();
	}
}
