package intersky.sign.presenter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mywidget.PullToRefreshView;
import intersky.sign.R;
import intersky.sign.SignManager;
import intersky.sign.asks.SignAsks;
import intersky.sign.entity.Sign;
import intersky.sign.handler.StatisticsHandler;
import intersky.sign.receive.StatisticsReceiver;
import intersky.sign.view.activity.StatisticsActivity;
import intersky.sign.view.activity.StatisticsDetialActivity;
import intersky.sign.view.adapter.SignAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class StatisticsPresenter implements Presenter {

    private StatisticsActivity mStatisticsActivity;
    public StatisticsHandler mStatisticsHandler;

    public StatisticsPresenter(StatisticsActivity mStatisticsActivity) {
        this.mStatisticsActivity = mStatisticsActivity;
        this.mStatisticsHandler = new StatisticsHandler(mStatisticsActivity);
        mStatisticsActivity.setBaseReceiver(new StatisticsReceiver(mStatisticsHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mStatisticsActivity.setContentView(R.layout.activity_statistics);
        TextView title = mStatisticsActivity.findViewById(R.id.title);
        title.setText( mStatisticsActivity.getString(R.string.keyword_statistics));
        ImageView back = (ImageView) mStatisticsActivity.findViewById(R.id.back);
        back.setOnClickListener(mStatisticsActivity.mBackListener);
        mStatisticsActivity.mDate = (TextView) mStatisticsActivity.findViewById(R.id.date_text);
        mStatisticsActivity.hit = (TextView) mStatisticsActivity.findViewById(R.id.sign_count);
        mStatisticsActivity.head = mStatisticsActivity.findViewById(R.id.headimg);
        AppUtils.setContactCycleHead(mStatisticsActivity.head,SignManager.mSignModul.oaUtils.mAccount.getName());
        mStatisticsActivity.mItemList = (ListView) mStatisticsActivity.findViewById(R.id.sign_List);
        mStatisticsActivity.mDate.setText(TimeUtils.getWeek(mStatisticsActivity) + " " + TimeUtils.getDate());
        mStatisticsActivity.startTime = TimeUtils.getDate();
        mStatisticsActivity.mSignAdapter = new SignAdapter(mStatisticsActivity, mStatisticsActivity.mAttendances, mStatisticsHandler);
        mStatisticsActivity.mItemList.setAdapter(mStatisticsActivity.mSignAdapter);
        mStatisticsActivity.mItemList.setOnItemClickListener(mStatisticsActivity.mOnItemClickListener);
        mStatisticsActivity.mDate.setOnClickListener(mStatisticsActivity.dateListener);
        mStatisticsActivity.mPullToRefreshView = (PullToRefreshView) mStatisticsActivity
                .findViewById(R.id.task_pull_refresh_view);
        mStatisticsActivity.mPullToRefreshView.setOnHeaderRefreshListener(mStatisticsActivity);
        mStatisticsActivity.mPullToRefreshView.setOnFooterRefreshListener(mStatisticsActivity);
        mStatisticsActivity.mPullToRefreshView.getmFooterView().setVisibility(View.INVISIBLE);
        initHitView();
        TextView more = mStatisticsActivity.findViewById(R.id.more);
        more.setOnClickListener(mStatisticsActivity.mMoreListenter);
        mStatisticsActivity.mshada = (RelativeLayout) mStatisticsActivity.findViewById(R.id.shade);
    }


    public void showMy() {
        mStatisticsActivity.popupWindow1.dismiss();
        TextView title = mStatisticsActivity.findViewById(R.id.title);
        title.setText( mStatisticsActivity.getString(R.string.keyword_statistics));
        SignManager.getInstance().setContact = null;
        onHead();
    }

    public void showOther() {
        mStatisticsActivity.popupWindow1.dismiss();
        SignManager.mSignModul.oaUtils.mContactManager.setUnderlineContacts(mStatisticsActivity,"", StatisticsActivity.ACTION_SIGN_SET_CONTACTS);
    }

    public void setuser(Intent intent) {
        SignManager.getInstance().setContact =  intent.getParcelableExtra("contacts");
        TextView title = mStatisticsActivity.findViewById(R.id.title);
        title.setText( SignManager.getInstance().setContact.mName + mStatisticsActivity.getString(R.string.xml_statistics_top));
        onHead();
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        mStatisticsActivity.waitDialog.show();
        SignAsks.getSignList(mStatisticsHandler, mStatisticsActivity, SignManager.getInstance().getSetUserid(),
                mStatisticsActivity.mSignAdapter.page, mStatisticsActivity.startTime);
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


    public void showMore() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item = new MenuItem();
        item.btnName = mStatisticsActivity.getString(R.string.keyword_myattdence);
        item.mListener = mStatisticsActivity.mShowMy;
        items.add(item);
        item = new MenuItem();
        item.btnName = mStatisticsActivity.getString(R.string.keyword_otherattdence);
        item.mListener = mStatisticsActivity.mShowOther;
        items.add(item);
        mStatisticsActivity.popupWindow1 = AppUtils.creatButtomMenu(mStatisticsActivity, mStatisticsActivity.mshada, items, mStatisticsActivity.findViewById(R.id.activity_statistics));
    }



    public void onItemClick(Sign attendanceModel) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(mStatisticsActivity, StatisticsDetialActivity.class);
        intent.putExtra("sign", attendanceModel);
        mStatisticsActivity.startActivity(intent);
    }

    public void showTimeDialog() {
        AppUtils.creatDataPicker(mStatisticsActivity,mStatisticsActivity.startTime,mStatisticsActivity.getString(R.string.keyword_signtime),mOnDaySetListener);
    }

    public void onHead() {
        mStatisticsActivity.mAttendances.clear();
        mStatisticsActivity.mSignAdapter.page = 1;
        mStatisticsActivity.mSignAdapter.isall = false;
        mStatisticsActivity.waitDialog.show();
        SignAsks.getSignList(mStatisticsHandler,mStatisticsActivity,SignManager.getInstance().getSetUserid(),mStatisticsActivity.mSignAdapter.page,mStatisticsActivity.startTime);
    }

    public void onFoot() {
        if (mStatisticsActivity.mSignAdapter.isall == false) {
            mStatisticsActivity.waitDialog.hide();
            SignAsks.getSignList(mStatisticsHandler,mStatisticsActivity,SignManager.getInstance().getSetUserid(),mStatisticsActivity.mSignAdapter.page,mStatisticsActivity.startTime);
        } else {

        }
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnDaySetListener = new DoubleDatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String weektime = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
            mStatisticsActivity.startTime = weektime;
            String textString = String.format(TimeUtils.getWeek(weektime,mStatisticsActivity) + " " + weektime);
            mStatisticsActivity.mDate.setText(textString);
            onHead();
        }
    };


    public void initHitView() {
        if(SignManager.getInstance().signHit == 0)
        {
            mStatisticsActivity.hit.setText(mStatisticsActivity.getString(R.string.xml_sign_none));
        }
        else
        {
            mStatisticsActivity.hit.setText(mStatisticsActivity.getString(R.string.signcount)+String.valueOf(SignManager.getInstance().signHit)
                    +mStatisticsActivity.getString(R.string.unit1));
        }
    }

}
