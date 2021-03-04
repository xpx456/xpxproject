package intersky.document.presenter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.handler.CreatFloadHandler;
import intersky.document.view.activity.CreatFloadActivity;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class CreatFloadPresenter implements Presenter {

	public CreatFloadActivity mCreatFloadActivity;
	private CreatFloadHandler mCreatFloadHandler;

	public CreatFloadPresenter(CreatFloadActivity mCreatFloadActivity) {
		this.mCreatFloadActivity = mCreatFloadActivity;
		mCreatFloadHandler = new CreatFloadHandler(mCreatFloadActivity);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mCreatFloadActivity.setContentView(R.layout.activity_creat_fload);

		TextView back = mCreatFloadActivity.findViewById(R.id.back);
		back.setOnClickListener(mCreatFloadActivity.mBackListener);
		TextView finish = mCreatFloadActivity.findViewById(R.id.finish);
		finish.setOnClickListener(mCreatFloadActivity.mFinishListener);

		mCreatFloadActivity.pathItem = mCreatFloadActivity.getIntent().getParcelableExtra("document");
		mCreatFloadActivity.mName = (EditText) mCreatFloadActivity.findViewById(R.id.new_name_edit);
		mCreatFloadActivity.clean = (ImageView) mCreatFloadActivity.findViewById(R.id.clean);
		mCreatFloadActivity.clean.setOnClickListener(mCreatFloadActivity.mCleanListener);
		mCreatFloadActivity.mName.addTextChangedListener(mCreatFloadActivity.mTextchange);
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

	public void doClean()
	{
		mCreatFloadActivity.mName.setText("");
	}

	public void creatfload() {
		String name = mCreatFloadActivity.mName.getText().toString();
		if (name.length() == 0) {
			AppUtils.showMessage(mCreatFloadActivity,mCreatFloadActivity.getString(R.string.name_empty));
			return;
		}
		DocumentAsks.doCreatDocument(mCreatFloadActivity.pathItem,name,mCreatFloadHandler,mCreatFloadActivity);
	}
}
