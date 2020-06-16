package intersky.function.presenter;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.function.R;
import intersky.function.prase.FunPrase;
import intersky.function.view.activity.GridAttachmentActivity;
import intersky.function.view.adapter.GridAttachmentAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class GridAttachmentPresenter implements Presenter {

	private GridAttachmentActivity mGridAttachmentActivity;

	public GridAttachmentPresenter(GridAttachmentActivity mGridAttachmentActivity) {
		this.mGridAttachmentActivity = mGridAttachmentActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mGridAttachmentActivity.setContentView(R.layout.activity_grid_attachment);
		mGridAttachmentActivity.function = mGridAttachmentActivity.getIntent().getParcelableExtra("function");
		mGridAttachmentActivity.mListView = mGridAttachmentActivity.findViewById(R.id.mail_fujian_lyaer);
		ToolBarHelper.setTitle(mGridAttachmentActivity.mActionBar, mGridAttachmentActivity.getString(R.string.activity_gride_attachment));
		FunPrase.addFujianData(mGridAttachmentActivity.function.mCellValue,mGridAttachmentActivity.mAttachments,mGridAttachmentActivity.function);
		mGridAttachmentActivity.mGridAttachmentAdapter = new GridAttachmentAdapter(mGridAttachmentActivity.mAttachments,mGridAttachmentActivity);
		mGridAttachmentActivity.mListView.setAdapter(mGridAttachmentActivity.mGridAttachmentAdapter);

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

	public void dodownload(Attachment mFuJianItem) {
		Bus.callData(mGridAttachmentActivity,"filetools/startAttachment",mFuJianItem);
	}

}
