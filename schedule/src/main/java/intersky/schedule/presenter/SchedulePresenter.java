package intersky.schedule.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.othershe.calendarview.bean.DateBean;

import java.util.ArrayList;
import java.util.Collection;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CalendarEventHelper;
import intersky.mywidget.ClendarDay;
import intersky.mywidget.ClendarEvent;
import intersky.mywidget.MyCalendarOnPagerChangeListener;
import intersky.mywidget.MyCalendarOnSingleChooseListener;
import intersky.schedule.R;
import intersky.schedule.ScheduleManager;
import intersky.schedule.asks.ScheduleAsks;
import intersky.schedule.entity.Event;
import intersky.schedule.handler.ScheduleHandler;
import intersky.schedule.receive.ScheduleReceiver;
import intersky.schedule.view.activity.EventEditActivity;
import intersky.schedule.view.activity.ScheduleActivity;
import intersky.schedule.view.adapter.EventAdapter;
import intersky.schedule.view.adapter.MyCalendarViewAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class SchedulePresenter implements Presenter,CalendarEventHelper.GetEvent {

    public ScheduleHandler mScheduleHandler;
    public ScheduleActivity mScheduleActivity;

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
        mScheduleActivity.flagFillBack = false;
        mScheduleActivity.setContentView(R.layout.activity_schedule);
        ImageView back = mScheduleActivity.findViewById(R.id.back);
        back.setOnClickListener(mScheduleActivity.mBackListener);
        mScheduleActivity.calendarEventHelper = new CalendarEventHelper(this,TimeUtils.getMonthC(mScheduleActivity),mScheduleActivity);
        mScheduleActivity.calendarEventHelper.updateEventAll();
        mScheduleActivity.calendarViewAdapter = new MyCalendarViewAdapter(mScheduleActivity.calendarEventHelper);
        mScheduleActivity.calendarView = mScheduleActivity.findViewById(R.id.calendar);
        mScheduleActivity.calendarView.setStartEndDate("1900.1", "2049.12").setInitDate(TimeUtils.getMonthCld()).setSingleDate(TimeUtils.getDayCld())
                .setOnCalendarViewAdapter(R.layout.item_month_layout,mScheduleActivity.calendarViewAdapter).init();
        mScheduleActivity.calendarView.setOnPagerChangeListener(new MyCalendarOnPagerChangeListener(mScheduleActivity.calendarEventHelper)
        {
            @Override
            public void onPagerChanged(int[] date) {
                super.onPagerChanged(date);
                mScheduleActivity.title.setText(mScheduleActivity.calendarEventHelper.title+"▼");
                updataAllView();
            }
        });
        mScheduleActivity.calendarView.setOnSingleChooseListener(new MyCalendarOnSingleChooseListener(mScheduleActivity.calendarEventHelper)
        {

            @Override
            public void onEventGet(ClendarDay clendarDay) {
                mScheduleActivity.events.clear();
                if(clendarDay != null)
                {
                    for(int i = 0 ; i < clendarDay.clendarEvents.size() ;i ++)
                    {
                        Event event = (Event) clendarDay.clendarEvents.get(i);
                        mScheduleActivity.events.add(event);
                    }
                }
                mScheduleActivity.mEventAdapter.notifyDataSetChanged();
                updataAllView();
            }
        });

        mScheduleActivity.eventList = mScheduleActivity.findViewById(R.id.event_List);
        mScheduleActivity.eventList.setLayoutManager(new LinearLayoutManager(mScheduleActivity));
        mScheduleActivity.title = mScheduleActivity.findViewById(R.id.title);
        mScheduleActivity.btnCreat = mScheduleActivity.findViewById(R.id.creat);

        mScheduleActivity.btnCreat.setOnClickListener(mScheduleActivity.doCreatListener);

        mScheduleActivity.mEventAdapter = new EventAdapter(mScheduleActivity,mScheduleActivity.events);
        mScheduleActivity.mEventAdapter.setOnItemClickListener(mScheduleActivity.onItemClickListener);
        mScheduleActivity.mEventAdapter.setEventFunction(eventFunction);
        mScheduleActivity.eventList.setAdapter(mScheduleActivity.mEventAdapter);
        mScheduleActivity.title.setText(TimeUtils.getMonthC(mScheduleActivity)+"▼");
        mScheduleActivity.title.setOnClickListener(mScheduleActivity.setDateListener);
        TextView more = mScheduleActivity.findViewById(R.id.more);
        more.setOnClickListener(mScheduleActivity.showMoreListener);
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
    public void setChoise(View view, DateBean choise) {
        //String daystring = String.format("%04d%02d%02d",choise.getSolar()[0],choise.getSolar()[1],choise.getSolar()[2]);

        if(mScheduleActivity.calendarEventHelper.choise != null)
        {
            String daystringlast = String.format("%04d%02d%02d",mScheduleActivity.calendarEventHelper.choise.getSolar()[0]
                    ,mScheduleActivity.calendarEventHelper.choise.getSolar()[1],
                    mScheduleActivity.calendarEventHelper.choise.getSolar()[2]);
            if(daystringlast.equals(String.valueOf(TimeUtils.getDateId())))
            {
                mScheduleActivity.calendarEventHelper.lastview.setBackgroundResource(R.drawable.shape_bg_round_red);
            }
            else if(mScheduleActivity.calendarEventHelper.hasEvent(mScheduleActivity.calendarEventHelper.choise))
            {
                mScheduleActivity.calendarEventHelper.lastview.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
            }
        }

    }

    public void updataAllView() {
        View view = mScheduleActivity.calendarEventHelper.lasthashView.get(TimeUtils.getDate());
        if(view != null)
        {
            view.setBackgroundResource(R.drawable.shape_bg_round_red);
        }
        view = mScheduleActivity.calendarEventHelper.hashView.get(TimeUtils.getDate());
        if(view != null)
        {
            if(mScheduleActivity.calendarEventHelper.choise != null)
            {
                if(!String.format("%04d-%02d-%02d",
                        mScheduleActivity.calendarEventHelper.choise .getSolar()[0],
                        mScheduleActivity.calendarEventHelper.choise .getSolar()[1],
                        mScheduleActivity.calendarEventHelper.choise .getSolar()[2]).equals(TimeUtils.getDate()))
                    view.setBackgroundResource(R.drawable.shape_bg_round_red);
            }

        }
        view = mScheduleActivity.calendarEventHelper.nexthashView.get(TimeUtils.getDate());
        if(view != null)
        {
            view.setBackgroundResource(R.drawable.shape_bg_round_red);
        }
    }

    public void updataSelectEvent() {
        mScheduleActivity.events.clear();
        if(mScheduleActivity.calendarEventHelper.choise != null)
        {
            ClendarDay clendarDay = mScheduleActivity.calendarEventHelper.getDayEvent(mScheduleActivity.calendarEventHelper.choise .getSolar()[0],String.format("%04d-%02d-%02d",
                    mScheduleActivity.calendarEventHelper.choise .getSolar()[0],
                    mScheduleActivity.calendarEventHelper.choise .getSolar()[1],
                    mScheduleActivity.calendarEventHelper.choise .getSolar()[2]));
            if(clendarDay != null)
            {
                for(int i = 0 ; i < clendarDay.clendarEvents.size() ;i ++)
                {
                    Event event = (Event) clendarDay.clendarEvents.get(i);
                    mScheduleActivity.events.add(event);
                }
            }

        }
        mScheduleActivity.mEventAdapter.notifyDataSetChanged();
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnDateSetListener = new DoubleDatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {

//            mScheduleActivity.calendarView.toSpecifyDate(startYear,startMonthOfYear+1,33);
            int currentyear = Integer.valueOf(mScheduleActivity.calendarEventHelper.title.substring(0,4));
            int currentmonty = Integer.valueOf(mScheduleActivity.calendarEventHelper.title.substring(5,7));
            if(currentyear < startYear)
            {
                for(int i = currentyear ; i < startYear ; i++)
                {
                    mScheduleActivity.calendarView.nextYear();
                }
            }
            else
            {
                for(int i = startYear ; i < currentyear ; i++)
                {
                    mScheduleActivity.calendarView.lastYear();
                }
            }

            if(currentmonty < startMonthOfYear+1)
            {
                for(int i = currentmonty ; i < startMonthOfYear+1 ; i++)
                {
                    mScheduleActivity.calendarView.nextMonth();
                }
            }
            else
            {
                for(int i = startMonthOfYear+1 ; i < currentmonty ; i++)
                {
                    mScheduleActivity.calendarView.lastMonth();
                }
            }
//            mScheduleActivity.calendarEventHelper.title = String.format("%04d年%02d月",startYear,startMonthOfYear+1);
//            mScheduleActivity.title.setText(String.format("%04d年%02d月",startYear,startMonthOfYear+1)+"▼");
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
            mScheduleActivity.title.setText(mScheduleActivity.calendarEventHelper.title+"▼");
        else
            mScheduleActivity.title.setText(mScheduleActivity.calendarEventHelper.title+"▼"+"("+mScheduleActivity.mContacts.getName()+")");
        mScheduleActivity.calendarEventHelper.updateEventAll();
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

        AppUtils.creatMonthPicker(mScheduleActivity, String.format("%04d-%02d-%02d %02d:%02d",Integer.valueOf(mScheduleActivity.calendarEventHelper.title.substring(0,4))
                ,Integer.valueOf(mScheduleActivity.calendarEventHelper.title.substring(5,7)),1,0,0)
                ,mScheduleActivity.getString(R.string.schdule_select_time),mOnDateSetListener);
    }

    public void doCreat() {
        Intent intent = new Intent(mScheduleActivity, EventEditActivity.class);
        Event event = new Event();
        if(mScheduleActivity.calendarEventHelper.choise != null) {
            event.mTime = String.format("%04d-%02d-%02d", mScheduleActivity.calendarEventHelper.choise.getSolar()[0],
                    mScheduleActivity.calendarEventHelper.choise.getSolar()[1],mScheduleActivity.calendarEventHelper.choise.getSolar()[2]) +" "+TimeUtils.getTime()+":00";
        }
        else {
            event.mTime = TimeUtils.getDate()+" "+ TimeUtils.getTime()+":00";
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


    public EventAdapter.EventFunction eventFunction = new EventAdapter.EventFunction() {

        @Override
        public void delete(Event mEvent) {
            doDelete(mEvent);
        }
    };




}
