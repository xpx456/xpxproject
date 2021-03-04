package intersky.notice.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.notice.R;
import intersky.notice.view.activity.NoticeReadlActivity;
import xpx.com.toolbar.utils.ToolBarHelper;

public class NoticeReadlPresenter implements Presenter {

    public NoticeReadlActivity mNoticeReadActivity;

    public NoticeReadlPresenter(NoticeReadlActivity mNoticeReadActivity) {
        this.mNoticeReadActivity = mNoticeReadActivity;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mNoticeReadActivity.setContentView(R.layout.activity_notice_read);
        ImageView back = mNoticeReadActivity.findViewById(R.id.back);
        back.setOnClickListener(mNoticeReadActivity.mBackListener);
        mNoticeReadActivity.notice = mNoticeReadActivity.getIntent().getParcelableExtra("notice");
        ToolBarHelper.setTitle(mNoticeReadActivity.mActionBar,mNoticeReadActivity.getString(R.string.keyword_notice_read_title));
        mNoticeReadActivity.mshada = (RelativeLayout) mNoticeReadActivity.findViewById(R.id.shade);
        mNoticeReadActivity.senderText = (TextView) mNoticeReadActivity.findViewById(R.id.sendtitle);
        mNoticeReadActivity.copyerText = (TextView) mNoticeReadActivity.findViewById(R.id.cctitle);
        mNoticeReadActivity.sender = (MyLinearLayout) mNoticeReadActivity.findViewById(R.id.sender);
        mNoticeReadActivity.copyer = (MyLinearLayout) mNoticeReadActivity.findViewById(R.id.copyer);
        initReader();
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

    public void initReader()
    {
        Bus.callData(mNoticeReadActivity,"chat/getContacts",mNoticeReadActivity.notice.read_id,mNoticeReadActivity.mCopyers);
        Bus.callData(mNoticeReadActivity,"chat/getContacts",mNoticeReadActivity.notice.unread_id,mNoticeReadActivity.mSenders);
        mNoticeReadActivity.copyerText.setText(mNoticeReadActivity.getString(R.string.xml_notice_read_hit)+"("+ String.valueOf(mNoticeReadActivity.mCopyers.size())+")");
        mNoticeReadActivity.senderText.setText(mNoticeReadActivity.getString(R.string.xml_notice_unread_hit)+"("+ String.valueOf(mNoticeReadActivity.mSenders.size())+")");
        initselectView(mNoticeReadActivity.mCopyers,mNoticeReadActivity.copyer);
        initselectView(mNoticeReadActivity.mSenders,mNoticeReadActivity.sender);
    }

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer) {
        LayoutInflater mInflater = (LayoutInflater) mNoticeReadActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.task_contact_item, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                AppUtils.setContactCycleHead(mhead,mContact.getmRName());
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.mName);
                mview.setTag(mContact);
                mlayer.addView(mview);
            }

        }
        else
        {
            mlayer.setVisibility(View.GONE);
        }
    }
}
