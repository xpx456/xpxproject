package intersky.attendance.view.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

import intersky.attendance.AttendanceManager;
import intersky.attendance.asks.AttdenanceAsks;
import intersky.attendance.entity.Attendance;
import intersky.attendance.presenter.AttendancePresenter;


public class AttendanceActivity extends BaseActivity implements OnGestureListener,OnTouchListener,PoiSearch.OnPoiSearchListener
{
	public static final String ACTION_LEAVE_SET_CONTACTS = "ACTION_LEAVE_SET_CONTACTS";
	public AttendancePresenter mAttendancePresenter = new AttendancePresenter(this);
	public PopupWindow popupWindow;
	public PopupWindow popupWindow1;
	public RelativeLayout mSign;
	public ScrollView mbglayer;
	public RelativeLayout mbglayer1;
	public RelativeLayout mbglayer2;
	public RelativeLayout upData;
//	public MapView mMapView;
//	public AMap aMap;
	public TextView mHead;
	public TextView mAddresstext;
	public TextView mName;
	public TextView mDete;
	public TextView mSignTitle;
	public RelativeLayout mDetebtn;
	public LinearLayout attdenceList;
	public TextView mDepartMent;
	public String mDeteString = "";
	public TextView mclose;
//	public AMapLocationClientOption mLocationOption = null;
//	public AMapLocationClient mlocationClient;
//	public LocationSource.OnLocationChangedListener mListener;
	public String mAddress = "";
	public String mAddressOrg = "";
	public TextView signTime;
	public LatLonPoint mLatLonPoint = new LatLonPoint(0,0);
	public ArrayList<Attendance> mWorkAttendances = new ArrayList<Attendance>();
	public RelativeLayout mshada;
	public boolean bingSign = false;
	public PoiSearch poiSearch;
	public PoiSearch.Query mQuery;
	public String mCity = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mAttendancePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mAttendancePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mAttendancePresenter.Start();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mAttendancePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mAttendancePresenter.Resume();
	}

	public View.OnClickListener signexListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mAttendancePresenter.doSignex();
		}
	};

	public View.OnClickListener uplistListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			AttdenanceAsks.getWorkAttdenaceList(mAttendancePresenter.mAttendanceHandler,mAttendancePresenter.mAttendanceActivity, AttendanceManager.getInstance().getSetUserid()
					,mAttendancePresenter.mAttendanceActivity.mDeteString);
		}
	};

	public View.OnClickListener signListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mAttendancePresenter.doSign();
		}
	};

	public View.OnClickListener signupListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mAttendancePresenter.showDialog((Attendance) v.getTag());
		}
	};

	public View.OnClickListener dateListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mAttendancePresenter.showTimeDialog();
		}
	};

//	@Override
//	public void onLocationChanged(AMapLocation aMapLocation) {
//		mAttendancePresenter.onLocation(aMapLocation);
//	}

//	@Override
//	public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
//		mAttendancePresenter.onActivate(onLocationChangedListener);
//	}

//	@Override
//	public void activate(OnLocationChangedListener onLocationChangedListener) {
//		mAttendancePresenter.onActivate(onLocationChangedListener);
//	}

//	@Override
//	public void deactivate() {
//		mAttendancePresenter.onDeactivate();
//	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {

	}

	public View.OnClickListener mMoreListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAttendancePresenter.showMore();
		}

	};

	public View.OnClickListener mShowSet = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAttendancePresenter.showSet();
		}

	};


	public View.OnClickListener mShowMy = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAttendancePresenter.showMy();
		}

	};

	public View.OnClickListener mShowOther = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAttendancePresenter.showOther();
		}

	};

	public View.OnClickListener mCancleListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mAttendancePresenter.dodismiss();
		}

	};

	@Override
	public void onPoiSearched(PoiResult poiResult, int i) {
		mAttendancePresenter.doSelect(poiResult);
	}

//	public AMapLocationListener onLocationChanged = new AMapLocationListener() {
//		@Override
//		public void onLocationChanged(AMapLocation amapLocation) {
//			mAttendancePresenter.onLocation(amapLocation);
//		}
//	};
}
