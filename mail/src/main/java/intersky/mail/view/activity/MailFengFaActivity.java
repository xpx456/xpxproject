package intersky.mail.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.mail.entity.Mail;
import intersky.mail.presenter.MailFenFaPresenter;
import intersky.mail.view.adapter.MailLableAdapter;
import intersky.appbase.BaseActivity;

@SuppressLint("ClickableViewAccessibility")
public class MailFengFaActivity extends BaseActivity
{
	public String personid = "";
	public String personname = "";
	public GestureDetector mGestureDetector;
	public RelativeLayout mCustomer;
	public RelativeLayout mTime;
	public RelativeLayout mLable;
	public RelativeLayout mListlayer;
	public TextView mFengfaName;
	public TextView mFengfaTime;
	public TextView mFengfaOk;
	public TextView mFengfaCancle;
	public EditText mFengfaditial;
	public Mail mail;
	public ListView mListView;
	public TextView mFengfaBiaoqian;
	public MailLableAdapter mMailSelectAdapter;
	public String lableRecord ="";
	public MailFenFaPresenter mMailFenFaPresenter = new MailFenFaPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mMailFenFaPresenter.Create();

	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mMailFenFaPresenter.Pause();

	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		mMailFenFaPresenter.Resume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mMailFenFaPresenter.Destroy();
	}
	
	public AdapterView.OnItemClickListener mUserMailItemClick = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			mMailFenFaPresenter.setlboal(position);
		}

	};
	
	public View.OnClickListener mOkListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mMailFenFaPresenter.dofenfa();
		}
	};
	
	public View.OnClickListener mCancleListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mMailFenFaPresenter.doCancle();
		}
	};
	
	public View.OnClickListener mCustomerListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mMailFenFaPresenter.setCustomer();
		}
	};
	
	public View.OnClickListener mTimeListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mMailFenFaPresenter.showTimeDialog();
		}
	};
	
	public View.OnClickListener mLableListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mMailFenFaPresenter.showSelectDialog();
		}
	};
	
	public View.OnClickListener mBackListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			finish();

		}
	};
	

	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		mGestureDetector.onTouchEvent(ev);
		// scroll.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
