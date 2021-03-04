package intersky.document.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.document.entity.DocumentNet;
import intersky.document.presenter.SetPositionPresenter;
import intersky.document.view.adapter.DocumentAdapter;
import intersky.document.view.adapter.DocumentPostationAdapter;
import intersky.document.view.adapter.SelectFoladerListAdapter;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.TextButton;

@SuppressLint("ClickableViewAccessibility")
public class SetPositionActivity extends BaseActivity
{
	public ArrayList<DocumentNet> mDocumentItems = new ArrayList<DocumentNet>();
	public HorizontalListView mPathList;
	public ListView mFloadListView;
	public TextView mBtnLeft;
	public TextView mBtnRight;
	public DocumentPostationAdapter mSampleDocumentAdapter;
	public SelectFoladerListAdapter mSelectFoladerListAdapter;
	public ArrayList<DocumentNet> mNetPathItems = new ArrayList<DocumentNet>();
	public ArrayList<DocumentNet> mPaths = new ArrayList<DocumentNet>();
	public SetPositionPresenter mSetPositionPresenter = new SetPositionPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mSetPositionPresenter.Create();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mSetPositionPresenter.Pause();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mSetPositionPresenter.Resume();
	}
	

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{	
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			
			mSetPositionPresenter.doBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	public View.OnClickListener mBackListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mSetPositionPresenter.doBack();
		}
	};
	
	public View.OnClickListener mHeadRightListener = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mSetPositionPresenter.newfload();
			
		}
	};
	
	
	
	public View.OnClickListener mButtomRight = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			mSetPositionPresenter.doSelect();
		}
	};
	
	public View.OnClickListener mButtomLeft = new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	public AdapterView.OnItemClickListener mPathItemClick = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
		{
			// TODO Auto-generated method stub
			mSetPositionPresenter.doPathclick((DocumentNet) parent.getAdapter().getItem(position));
		}
	};
	
	public AdapterView.OnItemClickListener mFolderItemClick = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
		{
			// TODO Auto-generated method stub
			mSetPositionPresenter.doNext((DocumentNet) parent.getAdapter().getItem(position));
		}
	};
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mSetPositionPresenter.Destroy();
	}



}
