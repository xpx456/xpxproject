package intersky.schedule.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyViewPager;
import intersky.schedule.R;
import intersky.schedule.ScheduleHelper;
import intersky.schedule.ScheduleManager;
import intersky.schedule.asks.ScheduleAsks;
import intersky.schedule.entity.Day;
import intersky.schedule.entity.Event;
import intersky.schedule.handler.ScheduleHandler;
import intersky.schedule.receive.ScheduleReceiver;
import intersky.schedule.view.activity.EventEditActivity;
import intersky.schedule.view.activity.ScheduleActivity;
import intersky.schedule.view.adapter.EventAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SchedulePresenter implements Presenter,ScheduleHelper.GetEvent {

    public ScheduleHandler mScheduleHandler;
    public ScheduleActivity mScheduleActivity;
    public ScheduleHelper mScheduleHelper = new ScheduleHelper();

    public SchedulePresenter(ScheduleActivity mScheduleActivity)
    {
        this.mScheduleActivity = mScheduleActivity;
        this.mScheduleHandler = new ScheduleHandler(mScheduleActivity);
        mScheduleActivity.setBaseReceiver( new ScheduleReceiver(mScheduleHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mScheduleActivity.setContentView(R.layout.activity_schedule);
        mScheduleActivity.myViewPager = (MyViewPager) mScheduleActivity.findViewById(R.id.load_pager);
        mScheduleHelper.init(mScheduleActivity, mScheduleActivity.myViewPager,this,pageChangeListener);
        mScheduleActivity.eventList = mScheduleActivity.findViewById(R.id.event_List);
        mScheduleActivity.eventList.setLayoutManager(new LinearLayoutManager(mScheduleActivity));
        mScheduleActivity.btnCreat = mScheduleActivity.findViewById(R.id.creat);
        mScheduleActivity.btnCreat.setOnClickListener(mScheduleActivity.doCreatListener);
        //mScheduleActivity.eventList.setOnItemClickListener(mScheduleActivity.onItemClickListener);
        //mScheduleActivity.eventList.setOnItemLongClickListener(mScheduleActivity.onItemLongClickListener);
        ToolBarHelper.setTitle(mScheduleActivity.mActionBar,mScheduleActivity.setDateListener,mScheduleHelper.headTitle+"▼");
        ToolBarHelper.setRightBtnText(mScheduleActivity.mActionBar, mScheduleActivity.showMoreListener, " · · ·",true);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    @Override
    public void doEvent(String begin, String end) {
        if(mScheduleActivity.mContacts == null)
        ScheduleAsks.getEvents(mScheduleActivity, ScheduleManager.getInstance().oaUtils.mAccount.mRecordId,mScheduleHandler,begin,end);
        else
            ScheduleAsks.getEvents(mScheduleActivity, mScheduleActivity.mContacts.mRecordid,mScheduleHandler,begin,end);
    }

    @Override
    public void updataEvent(Day day) {
        mScheduleActivity.mEventAdapter = new EventAdapter(mScheduleActivity,day.mEvents);
        mScheduleActivity.mEventAdapter.setOnItemClickListener(mScheduleActivity.onItemClickListener);
        mScheduleActivity.mEventAdapter.setEventFunction(eventFunction);
        mScheduleActivity.eventList.setAdapter(mScheduleActivity.mEventAdapter);
        mScheduleActivity.mEventAdapter.notifyDataSetChanged();
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnDateSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            if(startYear != mScheduleHelper.titleYear || startMonthOfYear+1 !=  mScheduleHelper.titleMonth)
            {
                mScheduleHelper.setDate(startYear,startMonthOfYear+1);
                if(mScheduleActivity.mContacts == null)
                    ToolBarHelper.setTitle(mScheduleActivity.mActionBar,mScheduleActivity.setDateListener,mScheduleHelper.headTitle+"▼");
                else
                    ToolBarHelper.setTitle(mScheduleActivity.mActionBar, mScheduleActivity.setDateListener,mScheduleHelper.headTitle + "▼", mScheduleActivity.mContacts.getName()+mScheduleActivity.getString(R.string.schdule_member_schdule_title));

            }
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mScheduleHelper.initDate(position%mScheduleHelper.mSchedulePageAdapter.mViews.size());
            if(mScheduleActivity.mContacts == null)
                ToolBarHelper.setTitle(mScheduleActivity.mActionBar,mScheduleActivity.setDateListener,mScheduleHelper.headTitle+"▼");
            else
                ToolBarHelper.setTitle(mScheduleActivity.mActionBar, mScheduleActivity.setDateListener,mScheduleHelper.headTitle + "▼", mScheduleActivity.mContacts.getName()+mScheduleActivity.getString(R.string.schdule_member_schdule_title));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setUser(Intent intent) {
        Contacts mcontacts = intent.getParcelableExtra("contacts");
        if(mScheduleActivity.mContacts == null)
        {
            mScheduleActivity.mContacts = mcontacts;
            updataAll();
            mScheduleActivity.btnCreat.setVisibility(View.GONE);
        }
        else if(!mScheduleActivity.mContacts.mRecordid.equals(mcontacts.mRecordid))
        {
            mScheduleActivity.mContacts = mcontacts;
            updataAll();
            mScheduleActivity.btnCreat.setVisibility(View.GONE);
        }
    }

    public void updataAll() {

        if(mScheduleActivity.mContacts == null)
            ToolBarHelper.setTitle(mScheduleActivity.mActionBar,mScheduleActivity.setDateListener,mScheduleHelper.headTitle+"▼");
        else
            ToolBarHelper.setTitle(mScheduleActivity.mActionBar, mScheduleActivity.setDateListener,mScheduleHelper.headTitle + "▼", mScheduleActivity.mContacts.getName()+mScheduleActivity.getString(R.string.schdule_member_schdule_title));

        mScheduleHelper.updateEvent();
    }

    public void showMore() {
        ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
        mMenuItems.add(new MenuItem(mScheduleActivity.getString(R.string.schdule_more_my),mScheduleActivity.showMyListener));
        mMenuItems.add(new MenuItem(mScheduleActivity.getString(R.string.schdule_more_other),mScheduleActivity.showOthreListener));
        mScheduleActivity.mPopupWindow = AppUtils.creatButtomMenu(mScheduleActivity, (RelativeLayout) mScheduleActivity.findViewById(R.id.shade),mMenuItems,mScheduleActivity.findViewById(R.id.activity_schedule));
    }

    public void showMy() {
        if(mScheduleActivity.mContacts != null)
        {
            mScheduleActivity.mContacts = null;
            updataAll();
            mScheduleActivity.btnCreat.setVisibility(View.VISIBLE);
        }
    }

    public void showOther() {
        Bus.callData(mScheduleActivity,"chat/setContactsUnderline", mScheduleActivity.getString(R.string.schdule_select_contact),ScheduleActivity.ACTION_SET_SCHEDULE_CONTACT);
        mScheduleActivity.mPopupWindow.dismiss();
    }

    public void setDate() {

        AppUtils.creatMonthPicker(mScheduleActivity, String.format("%04d-%02d-%02d %02d:%02d",mScheduleHelper.titleYear,mScheduleHelper.titleMonth,1,0,0)
                ,mScheduleActivity.getString(R.string.schdule_select_time),mOnDateSetListener);
    }

    public void doCreat() {
        Intent intent = new Intent(mScheduleActivity, EventEditActivity.class);
        Event event = new Event();
        if(mScheduleHelper.mSelectDay != null) {
            event.mTime = mScheduleHelper.mSelectDay.key+" "+TimeUtils.getTime()+":00";
        }
        else {
            event.mTime = String.format("%04d-%02d-%02d",mScheduleHelper.todayYear,mScheduleHelper.todayMonth,mScheduleHelper.todayday)+" "+ TimeUtils.getTime()+":00";
        }
        intent.putExtra("event",event);
        mScheduleActivity.startActivity(intent);
    }

    public void doEdit(Event event) {
        Intent intent = new Intent(mScheduleActivity, EventEditActivity.class);
        intent.putExtra("event",event);
        mScheduleActivity.startActivity(intent);
    }

    public void doDelete(Event event) {
        AppUtils.creatDialogTowButton(mScheduleActivity,"",mScheduleActivity.getString(R.string.delete_event),mScheduleActivity.getString(R.string.button_word_ok)
                ,mScheduleActivity.getString(R.string.button_word_cancle),new EventDeleteListener(event),new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mScheduleActivity.mEventAdapter != null)
                        {
                            mScheduleActivity.mEventAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public class EventDeleteListener implements DialogInterface.OnClickListener
    {
        public Event event;

        public EventDeleteListener(Event event)
        {
            this.event = event;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mScheduleActivity.waitDialog.show();
            ScheduleAsks.deleteEvent(mScheduleActivity,mScheduleHandler,event);
        }
    }

    public ScheduleHelper.GetEvent getEvent = new ScheduleHelper.GetEvent()
    {

        @Override
        public void doEvent(String begin, String end) {

        }

        @Override
        public void updataEvent(Day day) {

        }
    };

    public EventAdapter.EventFunction eventFunction = new EventAdapter.EventFunction() {

        @Override
        public void delete(Event mEvent) {
            doDelete(mEvent);
        }
    };
}
