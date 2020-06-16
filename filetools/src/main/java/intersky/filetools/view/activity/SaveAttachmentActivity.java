package intersky.filetools.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.filetools.entity.LocalDocument;
import intersky.filetools.presenter.SaveAttachmentPresenter;
import intersky.filetools.view.adapter.DocumentAdapter;

@SuppressLint("ClickableViewAccessibility")
public class SaveAttachmentActivity extends BaseActivity
{
	public ListView mListView;
	public ArrayList<LocalDocument> mDocumentItems = new ArrayList<LocalDocument>();
	public DocumentAdapter mDocumentAdapter;
	public String nowPath;
	public String path;
	public SaveAttachmentPresenter mSaveAttachmentPresenter = new SaveAttachmentPresenter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mSaveAttachmentPresenter.Create();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		mSaveAttachmentPresenter.Pause();

	}


	@Override
	protected void onStart()
	{
		super.onStart();
		mSaveAttachmentPresenter.Start();
	}

	@Override
	protected void onDestroy()
	{
		mSaveAttachmentPresenter.Destroy();
		super.onDestroy();

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mSaveAttachmentPresenter.Resume();
	}


	
	


	public View.OnClickListener mBackListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			finish();

		}
	};

	public View.OnClickListener mSaveListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			if(mSaveAttachmentPresenter.mSaveAttachmentActivity.getIntent().hasExtra("url"))
			{
				mSaveAttachmentPresenter.doDownload();
			}
			else
			mSaveAttachmentPresenter.dosave();
		}
	};

	public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
		{
			// TODO Auto-generated method stub
			mSaveAttachmentPresenter.clickItem(position);

		}
	};

}
