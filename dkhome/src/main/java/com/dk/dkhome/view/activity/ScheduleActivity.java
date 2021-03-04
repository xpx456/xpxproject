package com.dk.dkhome.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dk.dkhome.entity.Course;
import com.dk.dkhome.presenter.SchedulePresenter;
import com.dk.dkhome.view.adapter.CourseAdapter;
import com.dk.dkhome.view.adapter.MyCalendarViewAdapter;
import com.othershe.calendarview.weiget.CalendarView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.mywidget.CalendarEventHelper;


/**
 * Created by xpx on 2017/8/18.
 */

public class ScheduleActivity extends BaseActivity {

    public static final String ACTION_SET_SCHEDULE_CONTACT = "ACTION_SET_SCHEDULE_CONTACT";
    public static final String ACTION_UPDATA_EVENT= "ACTION_UPDATA_EVENT";
    public SchedulePresenter mSchedulePresenter = new SchedulePresenter(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSchedulePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSchedulePresenter.Destroy();
        super.onDestroy();
    }

    @Override
    public boolean onFling(MotionEvent motionCourse, MotionEvent motionCourse1, float v, float v1) {
        return false;
    }
}
