package intersky.sign.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.mywidget.MyLinearLayout;
import intersky.sign.entity.Sign;
import intersky.sign.presenter.SignDetialPresenter;
import intersky.sign.presenter.StatisticsDetialPresenter;


@SuppressLint("ClickableViewAccessibility")
public class StatisticsDetialActivity extends BaseActivity implements OnGestureListener, OnTouchListener {

	public PopupWindow popupWindow1;
	public TextView headImage;
	public TextView mAddressName;
	public TextView mAddress;
	public TextView mTime;
	public TextView mSave;
	public TextView editText;
	public TextView reason;
	public LinearLayout mImageLayer;
	public RelativeLayout mRelativeLayout;
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public Sign mSign;
	public boolean issub = false;
	public StatisticsDetialPresenter mStatisticsDetialPresenter = new StatisticsDetialPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStatisticsDetialPresenter.Create();
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
	}

}
