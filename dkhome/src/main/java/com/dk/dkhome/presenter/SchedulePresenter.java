package com.dk.dkhome.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.handler.ScheduleHandler;
import com.dk.dkhome.receiver.ScheduleReceiver;
import com.dk.dkhome.utils.CalendarEventHelper;
import com.dk.dkhome.utils.MyCalendarOnPagerChangeListener;
import com.dk.dkhome.utils.MyCalendarOnSingleChooseListener;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.NewPlanActivity;
import com.dk.dkhome.view.activity.ScheduleActivity;
import com.dk.dkhome.view.activity.SportDetialActivity;
import com.dk.dkhome.view.adapter.CourseAdapter;
import com.dk.dkhome.view.adapter.MyCalendarViewAdapter;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.weiget.CalendarView;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;


/**
 * Created by xpx on 2017/8/18.
 */

public class SchedulePresenter implements Presenter,CalendarEventHelper.GetEvent {

    public ScheduleHandler mScheduleHandler;
    public ScheduleActivity mScheduleActivity;
    public ArrayList<Course> courses = new ArrayList<Course>();
    public RecyclerView courseList;
    public ImageView btnCreat;
    public CourseAdapter mCourseAdapter;
    public CalendarView calendarView;
    public MyCalendarViewAdapter calendarViewAdapter;
    public CalendarEventHelper calendarEventHelper;
    public TextView title;
    public String choice = "";
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
        if(AppUtils.isZh(mScheduleActivity))
        mScheduleActivity.setContentView(R.layout.activity_schedule);
        else
            mScheduleActivity.setContentView(R.layout.activity_scheduleen);
        courses.clear();
        ArrayList<Course> mcourses = DkhomeApplication.mApp.sportDataManager.dayPlans.get(TimeUtils.getDate());
        if(mcourses != null)
        {
            courses.addAll(mcourses);
        }
        ImageView back = mScheduleActivity.findViewById(R.id.back);
        back.setOnClickListener(mScheduleActivity.mBackListener);
        calendarEventHelper = new CalendarEventHelper(this,TimeUtils.getMonthC(mScheduleActivity),mScheduleActivity);
        calendarViewAdapter = new MyCalendarViewAdapter(calendarEventHelper);
        calendarView = mScheduleActivity.findViewById(R.id.calendar);

        calendarView.setStartEndDate("1900.1", "2049.12").setInitDate(TimeUtils.getMonthCld()).setSingleDate(TimeUtils.getDayCld())
                .setOnCalendarViewAdapter(R.layout.item_month_layout,calendarViewAdapter).init();
        calendarView.setOnPagerChangeListener(new MyCalendarOnPagerChangeListener(calendarEventHelper)
        {
            @Override
            public void onPagerChanged(int[] date) {
                super.onPagerChanged(date);
                title.setText(calendarEventHelper.title);
                updataAllView();
            }
        });
        calendarView.setOnSingleChooseListener(new MyCalendarOnSingleChooseListener(calendarEventHelper)
        {

            @Override
            public void onEventGet(String  date) {
                choice = date;
                courses.clear();
                ArrayList<Course> mcourses = DkhomeApplication.mApp.sportDataManager.dayPlans.get(date);
                if(mcourses != null)
                {
                    courses.addAll(mcourses);
                }
                mCourseAdapter.notifyDataSetChanged();
                updataAllView();
            }
        });

        courseList = mScheduleActivity.findViewById(R.id.event_List);
        courseList.setLayoutManager(new LinearLayoutManager(mScheduleActivity));
        title = mScheduleActivity.findViewById(R.id.title);
        btnCreat = mScheduleActivity.findViewById(R.id.creat);
        btnCreat.setOnClickListener(doCreatListener);
        mCourseAdapter = new CourseAdapter(courses,mScheduleActivity);
        mCourseAdapter.setOnItemClickListener(onItemClickListener);
        mCourseAdapter.setCourseFunction(courseFunction);
        courseList.setAdapter(mCourseAdapter);
        if(AppUtils.isZh(mScheduleActivity))
        title.setText(TimeUtils.getMonthC(mScheduleActivity));
        else
            title.setText(TimeUtils.getMonth(mScheduleActivity));
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
    public void setChoise(View view, DateBean choise) {
        if(calendarEventHelper.choise != null)
        {
            String daystringlast = String.format("%04d%02d%02d",calendarEventHelper.choise.getSolar()[0]
                    ,calendarEventHelper.choise.getSolar()[1],
                    calendarEventHelper.choise.getSolar()[2]);
            if(daystringlast.equals(String.valueOf(TimeUtils.getDateId())))
            {
                calendarEventHelper.lastview.setBackgroundResource(R.drawable.shape_bg_round_red);
            }
            else if(calendarEventHelper.hasEvent(calendarEventHelper.choise))
            {
                calendarEventHelper.lastview.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
            }
        }

    }


    public void updataAllView() {
        View view = calendarEventHelper.lasthashView.get(TimeUtils.getDate());
        if(view != null)
        {
            view.setBackgroundResource(R.drawable.shape_bg_round_red);
        }
        view = calendarEventHelper.hashView.get(TimeUtils.getDate());
        if(view != null)
        {
            if(calendarEventHelper.choise != null)
            {
                if(!String.format("%04d-%02d-%02d",
                        calendarEventHelper.choise .getSolar()[0],
                        calendarEventHelper.choise .getSolar()[1],
                        calendarEventHelper.choise .getSolar()[2]).equals(TimeUtils.getDate()))
                    view.setBackgroundResource(R.drawable.shape_bg_round_red);
            }

        }
        view = calendarEventHelper.nexthashView.get(TimeUtils.getDate());
        if(view != null)
        {
            view.setBackgroundResource(R.drawable.shape_bg_round_red);
        }
    }

    public void doCreat() {
        Intent intent = new Intent(mScheduleActivity, NewPlanActivity.class);
        Course course = new Course();
        course.during = NewPlanPresenter.COURSE_TINME_HIT;
        if(choice.length() == 0)
        course.creat = TimeUtils.getDate();
        else
            course.creat = choice;
        intent.putExtra("plan",course);
        mScheduleActivity.startActivity(intent);
    }

    public void doEdit(Course course) {
        Intent intent = new Intent(mScheduleActivity, SportDetialActivity.class);
        intent.putExtra("plan",course);
        mScheduleActivity.startActivity(intent);
    }

    public void updataSelect()
    {
        ArrayList<Course> mcourses;
        courses.clear();
        if(choice.length() > 0)
            mcourses = DkhomeApplication.mApp.sportDataManager.dayPlans.get(choice);
        else
            mcourses = DkhomeApplication.mApp.sportDataManager.dayPlans.get(TimeUtils.getDate());
        if(mcourses != null)
        {
            courses.addAll(mcourses);
        }
        mCourseAdapter.notifyDataSetChanged();
        updataAllView();
    }

    public void doDelete(Course course) {
        AppUtils.creatDialogTowButton(mScheduleActivity,"",mScheduleActivity.getString(R.string.delete_course),mScheduleActivity.getString(R.string.button_word_ok)
                ,mScheduleActivity.getString(R.string.button_word_cancle),new EventDeleteListener(course),new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mCourseAdapter != null)
                        {
                            mCourseAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public class EventDeleteListener implements DialogInterface.OnClickListener
    {
        public Course course;

        public EventDeleteListener(Course course)
        {
            this.course = course;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mScheduleActivity.waitDialog.show();
            courses.remove(course);
            DkhomeApplication.mApp.deletePlan(course);
        }
    }


    public CourseAdapter.CourseFunction courseFunction = new CourseAdapter.CourseFunction() {

        @Override
        public void delete(Course course) {
            doDelete(course);
        }
    };



    public View.OnClickListener doCreatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doCreat();
        }
    };

    public CourseAdapter.OnItemClickListener onItemClickListener = new CourseAdapter.OnItemClickListener()
    {

        @Override
        public void onItemClick(Course contacts, int position, View view) {
            doEdit(contacts);
        }
    };
}
