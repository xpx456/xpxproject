package intersky.mail.presenter;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailFile;
import intersky.mail.handler.MailFileHandler;
import intersky.mail.view.InputView;
import intersky.mail.view.NewLableView;
import intersky.mail.view.activity.MailFileActivity;
import intersky.mail.view.adapter.MailFile2Adapter;
import intersky.select.entity.Select;

public class MailFilePresenter implements Presenter {

	private MailFileActivity mMailFileActivity;
	public RecyclerView lableview;
	public RelativeLayout shade;
	public MailFileHandler mMailFileHandler;
	public MailFile2Adapter mMailFile2Adapter;
	public ArrayList<Mail> mails;
	public InputView timeInput;
	public ArrayList<MailFile> mailFiles = new ArrayList<MailFile>();
	public MailFile shoujian = new MailFile();
	public MailFile yifa = new MailFile();
	public MailFile caogao = new MailFile();
	public MailFile huishou = new MailFile();
	public MailFile laji = new MailFile();
	public MailFilePresenter(MailFileActivity mMailFileActivity)
	{
		this.mMailFileActivity = mMailFileActivity;
		mMailFileHandler = new MailFileHandler(mMailFileActivity);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mMailFileActivity.setContentView(R.layout.activity_file);
		shoujian.name = mMailFileActivity.getString(R.string.mail_shoujian);
		shoujian.rid = "0";
		yifa.name = mMailFileActivity.getString(R.string.mail_mailtitleasend);
		shoujian.rid = "2";
		caogao.name = mMailFileActivity.getString(R.string.mail_mailtitledraft);
		caogao.rid = "3";
		huishou.name = mMailFileActivity.getString(R.string.keyword_mailtitldelete2);
		caogao.rid = "4";
		laji.name = mMailFileActivity.getString(R.string.keyword_mailtitlercycle);
		caogao.rid = "5";
		addoteher();
		mails = mMailFileActivity.getIntent().getParcelableArrayListExtra("mails");
		mMailFile2Adapter = new MailFile2Adapter(mailFiles,mMailFileActivity);
		mMailFile2Adapter.setOnItemClickListener(onItemClickListener);
		lableview = mMailFileActivity.findViewById(R.id.lablelist);
		shade = mMailFileActivity.findViewById(R.id.shade);
		timeInput = new InputView(mMailFileActivity,doFileOkListener, InputType.TYPE_CLASS_TEXT ,mMailFileActivity.getString(R.string.mail_lable_file_input_hit));
		lableview.setLayoutManager(new LinearLayoutManager(mMailFileActivity));
		lableview.setAdapter(mMailFile2Adapter);
		TextView creat = mMailFileActivity.findViewById(R.id.newtab);
		TextView cancle = mMailFileActivity.findViewById(R.id.cancle);
		cancle.setOnClickListener(mMailFileActivity.mBackListener);
		creat.setOnClickListener(creatListener);
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


	public View.OnClickListener creatListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			timeInput.title1.setText(mMailFileActivity.getString(R.string.mail_lable_file_creat));
			timeInput.creat(mMailFileActivity,shade,mMailFileActivity.findViewById(R.id.activity_mail),"");
		}
	};


	public void updataFile(MailFile select) {
		boolean updata = false;
		for(int i = 0; i < MailManager.getInstance().mailFiles.size() ; i++)
		{
			MailFile select1 = MailManager.getInstance().mailFiles.get(i);
			if(select1.rid.equals(select.rid))
			{
				select1.name = select.name;
				updata = true;

			}
		}
		if(updata == false)
		{
			MailManager.getInstance().mailFiles.add(0,select);
		}
		addoteher();
		mMailFile2Adapter.notifyDataSetChanged();
		Intent intent = new Intent(MailManager.ACTION_MAIL_FILE_UPDATE);
		mMailFileActivity.sendBroadcast(intent);
	}

	public MailFile2Adapter.OnItemClickListener onItemClickListener = new MailFile2Adapter.OnItemClickListener()
	{

		@Override
		public void onItemClick(MailFile lable, int position, View view) {
			mMailFileActivity.waitDialog.show();
			MailAsks.moveMail(mMailFileActivity,mMailFileHandler,mails,lable);
		}
	};

	private InputView.DoOkListener doFileOkListener = new InputView.DoOkListener() {
		@Override
		public void OkListener() {

		}

		@Override
		public void OkListener(Object item) {
			MailFile select = (MailFile) item;
			mMailFileActivity.waitDialog.show();
			MailAsks.setFile(mMailFileActivity,mMailFileHandler,select);
		}
	};

	private void addoteher()
	{
		mailFiles.clear();
		switch (mMailFileActivity.getIntent().getIntExtra("type",0)){
			case 0:
				mailFiles.add(yifa);
				mailFiles.add(caogao);
				mailFiles.add(huishou);
				mailFiles.add(laji);
				mailFiles.addAll(MailManager.getInstance().mailFiles);
				break;
			case 2:
				mailFiles.add(shoujian);
				mailFiles.add(caogao);
				mailFiles.add(huishou);
				mailFiles.add(laji);
				mailFiles.addAll(MailManager.getInstance().mailFiles);
				break;
			case 3:
				mailFiles.add(shoujian);
				mailFiles.add(yifa);
				mailFiles.add(huishou);
				mailFiles.add(laji);
				mailFiles.addAll(MailManager.getInstance().mailFiles);
				break;
			case 4:
				mailFiles.add(shoujian);
				mailFiles.add(yifa);
				mailFiles.add(caogao);
				mailFiles.add(laji);
				mailFiles.addAll(MailManager.getInstance().mailFiles);
				break;
			case 5:
				mailFiles.add(shoujian);
				mailFiles.add(yifa);
				mailFiles.add(caogao);
				mailFiles.add(huishou);
				mailFiles.addAll(MailManager.getInstance().mailFiles);
				break;
			default:
				mailFiles.add(shoujian);
				mailFiles.add(yifa);
				mailFiles.add(caogao);
				mailFiles.add(huishou);
				mailFiles.add(laji);
				mailFiles.addAll(MailManager.getInstance().mailFiles);
				removeHas();
				break;
		}
	}

	private void removeHas() {
		for(int i = 0 ; i < mailFiles.size() ; i++)
		{
			if(mailFiles.get(i).rid.equals(mMailFileActivity.getIntent().getStringExtra("mailboxid")))
			{
				mailFiles.remove(i);
				break;
			}
		}
	}
}
