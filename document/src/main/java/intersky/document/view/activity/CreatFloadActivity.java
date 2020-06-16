package intersky.document.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import intersky.document.entity.DocumentNet;
import intersky.document.presenter.CreatFloadPresenter;
import intersky.appbase.BaseActivity;


@SuppressLint("ClickableViewAccessibility")
public class CreatFloadActivity extends BaseActivity
{
	public EditText mName;
	public ImageView clean;
	public DocumentNet pathItem;
	private CreatFloadPresenter mCreatFloadPresenter = new CreatFloadPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mCreatFloadPresenter.Create();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mCreatFloadPresenter.Pause();

	}
	
	public View.OnClickListener mFinishListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mCreatFloadPresenter.creatfload();
		}
	};

	public View.OnClickListener mCleanListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mCreatFloadPresenter.doClean();
		}
	};


	public View.OnClickListener mCancelListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			finish();
		}
	};

	public TextWatcher mTextchange = new TextWatcher()
	{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if(mName.getText().toString().length()> 0)
			{
				if(clean.getVisibility() == View.VISIBLE)
				clean.setVisibility(View.INVISIBLE);
			}
			else
			{
				if(clean.getVisibility() == View.INVISIBLE)
					clean.setVisibility(View.VISIBLE);
			}


		}
	};
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mCreatFloadPresenter.Resume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mCreatFloadPresenter.Destroy();
	}

}
