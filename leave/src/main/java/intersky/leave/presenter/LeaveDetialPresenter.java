package intersky.leave.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.leave.LeaveManager;
import intersky.leave.R;
import intersky.leave.asks.LeaveAsks;
import intersky.leave.entity.Approve;
import intersky.leave.handler.LeaveDetialHandler;
import intersky.leave.receiver.LeaveDetialReceiver;
import intersky.leave.view.activity.LeaveActivity;
import intersky.leave.view.activity.LeaveDetialActivity;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.oa.OaAsks;
import xpx.com.toolbar.utils.ToolBarHelper;


public class LeaveDetialPresenter implements Presenter {

	public LeaveDetialActivity mLeaveDetialActivity;
	public LeaveDetialHandler mLeaveDetialHandler;

	public LeaveDetialPresenter(LeaveDetialActivity mLeaveDetialActivity) {
		this.mLeaveDetialActivity = mLeaveDetialActivity;
		mLeaveDetialHandler = new LeaveDetialHandler(mLeaveDetialActivity);
		mLeaveDetialActivity.setBaseReceiver(new LeaveDetialReceiver(mLeaveDetialHandler));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mLeaveDetialActivity.setContentView(R.layout.activity_leave_detial);
		mLeaveDetialActivity.mLeave = mLeaveDetialActivity.getIntent().getParcelableExtra("leave");
		mLeaveDetialActivity.mCopyerLayer = (RelativeLayout) mLeaveDetialActivity.findViewById(R.id.ccayer);
		mLeaveDetialActivity.mbuttomLayer = (RelativeLayout) mLeaveDetialActivity.findViewById(R.id.buttom_layer);
		mLeaveDetialActivity.statuImage =  (ImageView) mLeaveDetialActivity.findViewById(R.id.state_img);
		mLeaveDetialActivity.mSendlayer = (LinearLayout) mLeaveDetialActivity.findViewById(R.id.sendayer);
		mLeaveDetialActivity.mRelativeLayout = (RelativeLayout) mLeaveDetialActivity.findViewById(R.id.shade);
		mLeaveDetialActivity.mBeginText = (TextView) mLeaveDetialActivity.findViewById(R.id.leavebeginname);
		mLeaveDetialActivity.mEndText = (TextView) mLeaveDetialActivity.findViewById(R.id.leaveendname);
		mLeaveDetialActivity.mBtnleft = (TextView) mLeaveDetialActivity.findViewById(R.id.btnleft);
		mLeaveDetialActivity.mBtnright = (TextView) mLeaveDetialActivity.findViewById(R.id.btnright);
		mLeaveDetialActivity.mBeginLayer = (RelativeLayout) mLeaveDetialActivity.findViewById(R.id.leavebegin);
		mLeaveDetialActivity.mEndLayer = (RelativeLayout) mLeaveDetialActivity.findViewById(R.id.leaveend);
		mLeaveDetialActivity.mTypeLayer = (RelativeLayout) mLeaveDetialActivity.findViewById(R.id.leavetype);
		mLeaveDetialActivity.mTypeText = (TextView) mLeaveDetialActivity.findViewById(R.id.leavetypename);
		mLeaveDetialActivity.mCountText = (TextView) mLeaveDetialActivity.findViewById(R.id.leavedayname);
		mLeaveDetialActivity.mReasonText = (WebEdit) mLeaveDetialActivity.findViewById(R.id.content1text);
		mLeaveDetialActivity.mReasonText.setEditable(false);
		mLeaveDetialActivity.mReasonText.setTxtColor(Color.rgb(154,156,163));
		mLeaveDetialActivity.mImageLayer = (LinearLayout) mLeaveDetialActivity.findViewById(R.id.image_content);
		mLeaveDetialActivity.copyer = (MyLinearLayout) mLeaveDetialActivity.findViewById(R.id.copyer);
		mLeaveDetialActivity.mBtnleft.setOnClickListener(mLeaveDetialActivity.mAcceptListenter);
		mLeaveDetialActivity.mBtnright.setOnClickListener(mLeaveDetialActivity.mRefuseListenter);
		ToolBarHelper.setTitle(mLeaveDetialActivity.mActionBar, Bus.callData(mLeaveDetialActivity,"chat/getContactName",mLeaveDetialActivity.mLeave.uid)
				+ mLeaveDetialActivity.getString(R.string.xml_leave_type_name));
		if(LeaveManager.getInstance().mLeaveTypes.size() == 0)
		{
			LeaveManager.getInstance().getLeaveType();
		}
		LeaveAsks.getDetial(mLeaveDetialHandler,mLeaveDetialActivity,mLeaveDetialActivity.mLeave);
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

	public void Destroy() {
		// TODO Auto-generated method stub
		mLeaveDetialActivity.mReasonText.destroy();
	}


	public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer) {
		LayoutInflater mInflater = (LayoutInflater) mLeaveDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlayer.removeAllViews();

		if (mselectitems.size() > 0) {
			mLeaveDetialActivity.mCopyerLayer.setVisibility(View.VISIBLE);
			for (int i = 0; i < mselectitems.size(); i++) {
				Contacts mContact = mselectitems.get(i);
				View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
				TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
				Bus.callData(mLeaveDetialActivity,"chat/setContactCycleHead",mhead,mContact);
				TextView name = (TextView) mview.findViewById(R.id.title);
				name.setText(mContact.mName);
				mview.setTag(mContact);
				mlayer.addView(mview);
			}
		}
		else
		{
			mLeaveDetialActivity.mCopyerLayer.setVisibility(View.INVISIBLE);
		}

	}

	public void dodismiss() {
		mLeaveDetialActivity.popupWindow1.dismiss();
	}

	public void showEdit() {

		ArrayList<MenuItem> items = new ArrayList<MenuItem>();
		MenuItem mMenuItem = new MenuItem();
		mMenuItem.btnName = mLeaveDetialActivity.getString(R.string.button_word_edit);
		mMenuItem.mListener = mLeaveDetialActivity.mEditListenter;
		items.add(mMenuItem);
		mMenuItem = new MenuItem();
		mMenuItem.btnName = mLeaveDetialActivity.getString(R.string.button_word_setoff);
		mMenuItem.mListener = mLeaveDetialActivity.mDeleteListenter;
		items.add(mMenuItem);
		mLeaveDetialActivity.popupWindow1 = AppUtils.creatButtomMenu(mLeaveDetialActivity, mLeaveDetialActivity.mRelativeLayout, items, mLeaveDetialActivity.findViewById(R.id.activity_about));
	}



	private void deleteData() {
		AppUtils.creatDialogTowButton(mLeaveDetialActivity,mLeaveDetialActivity.getString(R.string.xml_workreport_leave_delete),"",mLeaveDetialActivity.getString(R.string.button_word_cancle),
				mLeaveDetialActivity.getString(R.string.button_word_ok),null,new DeleteLeaveDialogListener());

	}

	public class DeleteLeaveDialogListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			doDelete();
		}
	}


	public void editdata() {
		Intent intent = new Intent(mLeaveDetialActivity,LeaveActivity.class);
		intent.putExtra("leave",mLeaveDetialActivity.mLeave);
		mLeaveDetialActivity.startActivity(intent);
	}

	public void onEdit() {
		editdata();
		mLeaveDetialActivity.popupWindow1.dismiss();
	}

	public void onDelete() {
		deleteData();
		mLeaveDetialActivity.popupWindow1.dismiss();
	}

	public void doAccept() {
		LeaveAsks.doAccept(mLeaveDetialHandler,mLeaveDetialActivity,mLeaveDetialActivity.mLeave);
	}

	public void doDelete() {
		LeaveAsks.doDelete(mLeaveDetialHandler,mLeaveDetialActivity,mLeaveDetialActivity.mLeave);
	}

	public void doRefouse() {
		LeaveAsks.doRefouse(mLeaveDetialHandler,mLeaveDetialActivity,mLeaveDetialActivity.mLeave);
	}

	public void initDetial() {
		try {
			mLeaveDetialActivity.mBeginText.setText(mLeaveDetialActivity.mLeave.start.substring(0, 16));
			mLeaveDetialActivity.mEndText.setText(mLeaveDetialActivity.mLeave.end.substring(0, 16));
			mLeaveDetialActivity.mTypeText.setText(LeaveManager.getInstance().gettypeName(mLeaveDetialActivity.mLeave.leave_type_id));
			mLeaveDetialActivity.mCountText.setText(mLeaveDetialActivity.mLeave.count);
			mLeaveDetialActivity.mReasonText.setText(mLeaveDetialActivity.mLeave.content);

			if(LeaveManager.getInstance().oaUtils.mAccount.mRecordId.equals(mLeaveDetialActivity.mLeave.uid) && mLeaveDetialActivity.mLeave.is_approval != 2)
			ToolBarHelper.setRightBtnText(mLeaveDetialActivity.mActionBar, mLeaveDetialActivity.mMoreListenter, " · · ·",true);
			else
				ToolBarHelper.hidRight(mLeaveDetialActivity.mActionBar);
			switch (mLeaveDetialActivity.mLeave.is_approval)
			{
				case 0:
					mLeaveDetialActivity.statuImage.setImageResource(R.drawable.approve0);
					break;
				case 1:
					mLeaveDetialActivity.statuImage.setImageResource(R.drawable.approve1);
					break;
				case 2:
					mLeaveDetialActivity.statuImage.setImageResource(R.drawable.approve2);
					break;
				case 3:
					mLeaveDetialActivity.statuImage.setImageResource(R.drawable.approve3);
					break;
			}
			mLeaveDetialActivity.mPics.clear();
			mLeaveDetialActivity.mImageLayer.removeAllViews();
			OaAsks.getAttachments(mLeaveDetialActivity.mLeave.attachs,mLeaveDetialHandler,mLeaveDetialActivity);
			mLeaveDetialActivity.mApproves.clear();
			mLeaveDetialActivity.mSendlayer.removeAllViews();
			if(mLeaveDetialActivity.mLeave.approverjson.length() > 0)
			{
				XpxJSONArray ja = new XpxJSONArray(mLeaveDetialActivity.mLeave.approverjson);
				for(int i = 0 ; i < ja.length() ; i++)
				{

					XpxJSONObject jo2 = ja.getJSONObject(i);
					mLeaveDetialActivity.mApproves.add(new Approve(jo2.getString("leave_prog_id"),jo2.getString("create_time").substring(0,16),jo2.getString("content")));
				}
			}


			if(mLeaveDetialActivity.mLeave.nowapprover.length() > 0 && mLeaveDetialActivity.mLeave.is_approval == 0)
			{
				if(mLeaveDetialActivity.mLeave.nowapprover.equals(LeaveManager.getInstance().oaUtils.mAccount.mRecordId) && mLeaveDetialActivity.mLeave.is_approval == 0)
				{
					mLeaveDetialActivity.mbuttomLayer.setVisibility(View.VISIBLE);
					mLeaveDetialActivity.statuImage.setImageResource(R.drawable.approve0);
				}
				else
				{
					ScrollView mScrollView = (ScrollView) mLeaveDetialActivity.findViewById(R.id.scrollView1);
					RelativeLayout.LayoutParams lay = (RelativeLayout.LayoutParams) mScrollView.getLayoutParams();
					lay.bottomMargin = 0;
					mScrollView.setLayoutParams(lay);
					if(mLeaveDetialActivity.mLeave.is_approval == 0)
					mLeaveDetialActivity.statuImage.setImageResource(R.drawable.approve1);
				}
			}

			initApproveView();
			Bus.callData(mLeaveDetialActivity,"chat/getContacts",mLeaveDetialActivity.mLeave.copyers, mLeaveDetialActivity.mCopyers);
			initselectView(mLeaveDetialActivity.mCopyers, mLeaveDetialActivity.copyer);

		} catch (JSONException e) {

			e.printStackTrace();
			AppUtils.showMessage(mLeaveDetialActivity,mLeaveDetialActivity.getString(R.string.xml_leave_type_no));
		}
	}

	public void praseAttahcmentViews() {
		for(int i = 0 ; i < mLeaveDetialActivity.mPics.size() ; i++)
		{
			Attachment attachment = mLeaveDetialActivity.mPics.get(i);
			String url = "";
			if(attachment.mRecordid.length() > 0)
			{
				url = LeaveManager.getInstance().oaUtils.praseClodAttchmentUrl(attachment.mRecordid, (int) (40 * mLeaveDetialActivity.mBasePresenter.mScreenDefine.density));
			}
			Bus.callData(mLeaveDetialActivity,"filetools/addPicView",attachment,mLeaveDetialActivity,url,mLeaveDetialActivity.mImageLayer);
		}
	}

	public void initApproveView() {
		LayoutInflater mInflater = (LayoutInflater) mLeaveDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i = 0 ; i < mLeaveDetialActivity.mApproves.size() ; i++)
		{
			Approve mApproveModel = mLeaveDetialActivity.mApproves.get(i);
			View mView;
			if(i == mLeaveDetialActivity.mApproves.size()-1)
			{
				mView = mInflater.inflate(R.layout.approve_item_end,null);
			}
			else
			{
				mView = mInflater.inflate(R.layout.approve_item,null);
			}
			Contacts mContact =mApproveModel.mContact;
			TextView mhead = (TextView) mView.findViewById(R.id.contact_img);
			Bus.callData(mLeaveDetialActivity,"chat/setContactCycleHead",mhead,mContact);
			TextView name = (TextView) mView.findViewById(R.id.conversation_title);
			name.setText(mContact.mName);
			TextView time = (TextView) mView.findViewById(R.id.conversation_time);
			time.setText(mApproveModel.create_time.substring(0,16));
			TextView sub = (TextView) mView.findViewById(R.id.conversation_sub_title);
			sub.setText(mApproveModel.content);
			ImageView image = (ImageView) mView.findViewById(R.id.iv_userhead);
			if(mApproveModel.isapprove == 0)
			{
				image.setImageResource(R.drawable.progresshud_wait);
			}
			else if(mApproveModel.isapprove == 2)
			{
				image.setImageResource(R.drawable.progresshud_success);
			}
			else
			{
				image.setImageResource(R.drawable.progresshud_refused);
			}
			mLeaveDetialActivity.mSendlayer.addView(mView);
		}
	}



}
