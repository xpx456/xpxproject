package intersky.document.presenter;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.SetPositionHandler;
import intersky.document.receive.SetPositionReceiver;
import intersky.document.view.activity.CreatFloadActivity;
import intersky.document.view.activity.SetPositionActivity;
import intersky.document.view.adapter.DocumentAdapter;
import intersky.document.view.adapter.DocumentPostationAdapter;
import intersky.document.view.adapter.SelectFoladerListAdapter;
import intersky.appbase.Actions;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.TextButton;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SetPositionPresenter implements Presenter {

	private SetPositionActivity mSetPositionActivity;
	public SetPositionHandler mSetPositionHandler;

	public SetPositionPresenter(SetPositionActivity mSetPositionActivity) {
		this.mSetPositionActivity = mSetPositionActivity;
		this.mSetPositionHandler = new SetPositionHandler(mSetPositionActivity);
		mSetPositionActivity.setBaseReceiver(new SetPositionReceiver(mSetPositionHandler));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mSetPositionActivity.setContentView(R.layout.activity_set_position);
		TextView back = mSetPositionActivity.findViewById(R.id.cancle);
		back.setOnClickListener(mSetPositionActivity.mButtomLeft);

		mSetPositionActivity.mPathList = (HorizontalListView) mSetPositionActivity.findViewById(R.id.horizon_listview);
		mSetPositionActivity.mFloadListView = (ListView) mSetPositionActivity.findViewById(R.id.select_List);
		mSetPositionActivity.mBtnLeft = (TextView) mSetPositionActivity.findViewById(R.id.btnleft);
		mSetPositionActivity.mBtnRight = (TextView) mSetPositionActivity.findViewById(R.id.btnright);
		if(mSetPositionActivity.getIntent().hasExtra("uploads")) {
			mSetPositionActivity.mBtnRight.setText(mSetPositionActivity.getString(R.string.button_word_upload));
		}
		mSetPositionActivity.mBtnLeft.setOnClickListener(mSetPositionActivity.mHeadRightListener);
		mSetPositionActivity.mBtnRight.setOnClickListener(mSetPositionActivity.mButtomRight);
		mSetPositionActivity.mPathList.setOnItemClickListener(mSetPositionActivity.mPathItemClick);
		mSetPositionActivity.mFloadListView.setOnItemClickListener(mSetPositionActivity.mFolderItemClick);
		mSetPositionActivity.mNetPathItems.clear();
		if(DocumentManager.getInstance().selectPathList.size() > 0)
		{
			mSetPositionActivity.mNetPathItems.addAll(DocumentManager.getInstance().selectPathList);
		}
		else
		{
			mSetPositionActivity.mNetPathItems.addAll(DocumentManager.getInstance().pathList);
		}

		mSetPositionActivity.mSelectFoladerListAdapter = new SelectFoladerListAdapter(mSetPositionActivity,
				mSetPositionActivity.mNetPathItems);
		mSetPositionActivity.mSampleDocumentAdapter = new DocumentPostationAdapter(mSetPositionActivity,
				mSetPositionActivity.mDocumentItems,null);
		if(DocumentManager.getInstance().selectPathList.size() > 0)
		{
			mSetPositionActivity.mPaths.addAll(DocumentManager.getInstance().selectPathList);
		}
		else
		{
			mSetPositionActivity.mPaths.addAll(DocumentManager.getInstance().pathList);
		}

		mSetPositionActivity.mPathList.setAdapter(mSetPositionActivity.mSelectFoladerListAdapter);
		mSetPositionActivity.mFloadListView.setAdapter(mSetPositionActivity.mSampleDocumentAdapter);
		for(int i = 0 ; i < DocumentManager.getInstance().fileList.size() ; i++) {
			if(DocumentManager.getInstance().fileList.get(i).mType == DocumentManager.TYPE_DOCUMENT) {
				mSetPositionActivity.mDocumentItems.add(DocumentManager.getInstance().fileList.get(i));
			}
		}
		if(mSetPositionActivity.mDocumentItems.size() == 0 && DocumentManager.getInstance().pathList.size() == 1) {
			DocumentAsks.updataDocumentList(mSetPositionHandler,mSetPositionActivity,DocumentManager.getInstance().pathList.get(0));
		}

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


	public void doNext(DocumentNet mDocumentNet) {

		DocumentAsks.preDocumentList(mSetPositionHandler,mSetPositionActivity, mDocumentNet);
	}

	public void doBack(DocumentNet mDocumentNet) {
		DocumentAsks.backDocumentList(mSetPositionHandler,mSetPositionActivity, mDocumentNet);
	}

	public void doPathclick(DocumentNet mDocumentNet) {
		DocumentAsks.backDocumentList(mSetPositionHandler,mSetPositionActivity, mDocumentNet);
	}

	public void doBack() {
		if (mSetPositionActivity.mNetPathItems.size() == 1) {
			mSetPositionActivity.finish();
		}
		else if (mSetPositionActivity.mNetPathItems.size() > 1) {
			doBack(mSetPositionActivity.mNetPathItems.get(mSetPositionActivity.mNetPathItems.size() - 2));
		}
	}

	public void newfload() {
		if (mSetPositionActivity.mNetPathItems.get(mSetPositionActivity.mNetPathItems.size() - 1).mOwnerType
				.length() == 0
				|| (mSetPositionActivity.mNetPathItems.get(mSetPositionActivity.mNetPathItems.size() - 1).mOwnerType
						.equals("(User)")
						&& mSetPositionActivity.mNetPathItems.get(mSetPositionActivity.mNetPathItems.size() - 1)
								.mShared == true)) {
			AppUtils.showMessage(mSetPositionActivity, mSetPositionActivity.getString(R.string.document_cannotcreat));
		}
		else {
			startNewFload();
		}
	}

	public void doSelect() {
		DocumentManager.getInstance().selectPathList.clear();
		DocumentManager.getInstance().selectPathList.addAll(mSetPositionActivity.mNetPathItems);
		if(mSetPositionActivity.getIntent().hasExtra("uploads") == false)
		{
			Intent intent = new Intent(Actions.ATTACHMENT_SET_POSITION);
			intent.putExtra("position",mSetPositionActivity.mNetPathItems.get(mSetPositionActivity.mNetPathItems.size() - 1).mName);
			mSetPositionActivity.sendBroadcast(intent);
			mSetPositionActivity.finish();
		}
		else
		{
			ArrayList<Attachment> attachments = mSetPositionActivity.getIntent().getParcelableArrayListExtra("uploads");
			DocumentManager.getInstance().sendUpload(attachments);
			mSetPositionActivity.finish();
		}

    }

	private void startNewFload() {
		Intent mIntent = new Intent();
		mIntent.putExtra("document",mSetPositionActivity.mPaths.get(mSetPositionActivity.mNetPathItems.size() - 1));
		mIntent.setClass(mSetPositionActivity, CreatFloadActivity.class);
		mSetPositionActivity.startActivity(mIntent);
	}
}
