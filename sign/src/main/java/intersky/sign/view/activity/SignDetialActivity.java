package intersky.sign.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.Query;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.mywidget.MyLinearLayout;
import intersky.sign.entity.Sign;
import intersky.sign.presenter.SignDetialPresenter;

@SuppressLint("ClickableViewAccessibility")
public class SignDetialActivity extends BaseActivity {
	public static final String ACTION_SIGN_ADDPICTORE = "ACTION_SIGN_ADDPICTORE";
	public static final String ACTION_SIGN_DELETEPICTORE = "ACTION_SIGN_DELETEPICTORE";

	public PopupWindow popupWindow1;
	public double x = 0;
	public double y = 0;
	public TextView headImage;
	public TextView mAddressName;
	public TextView mAddress;
	public TextView mTime;
	public TextView mSave;
	public EditText reason;
	public EditText editText;
	public MyLinearLayout mImageLayer;
	public RelativeLayout mRelativeLayout;
	public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
	public SignDetialPresenter mSignDetialPresenter = new SignDetialPresenter(this);
	public Sign mSign;
	public boolean issub = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSignDetialPresenter.Create();
	}
	
	@Override
	public void onPause()
	{
		mSignDetialPresenter.Pause();
		super.onPause();
	}
	
	@Override
	public void onResume()
	{
		mSignDetialPresenter.Resume();
		super.onResume();
	}
	
	@Override
	public void onDestroy()
	{
		mSignDetialPresenter.Destroy();
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	public View.OnClickListener mSaveListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSignDetialPresenter.doSave();
		}
	};

	public View.OnClickListener mShowAddListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSignDetialPresenter.showAdd( );
		}
	};

	public View.OnClickListener mShowPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSignDetialPresenter.showPic( v);
		}
	};


	
	public View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSignDetialPresenter.addPic();
		}
	};

	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mSignDetialPresenter.takePhoto();
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSignDetialPresenter.takePhotoResult(requestCode, resultCode, data);
	}

	public View.OnClickListener removePicListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mSignDetialPresenter.removePic(v);
		}
	};


	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return mSignDetialPresenter.onFling(motionEvent, motionEvent1, v, v1);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {//如果触及到返回键所要执行的操作是什么
			mSignDetialPresenter.chekcBack();
			return true;
		}
		return super.onKeyDown  (keyCode ,event);

	}
}
