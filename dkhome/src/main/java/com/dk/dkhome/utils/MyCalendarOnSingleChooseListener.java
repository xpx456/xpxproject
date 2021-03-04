package com.dk.dkhome.utils;

import android.view.View;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnSingleChooseListener;

import intersky.mywidget.ClendarDay;

public abstract class MyCalendarOnSingleChooseListener implements OnSingleChooseListener {

    public CalendarEventHelper calendarEventHelper;

    public MyCalendarOnSingleChooseListener(CalendarEventHelper calendarEventHelper) {
        this.calendarEventHelper = calendarEventHelper;
    }

    @Override
    public void onSingleChoose(View view, DateBean date) {
        calendarEventHelper.setChoise(view,date);
        onEventGet(String.format("%04d-%02d-%02d",date.getSolar()[0],date.getSolar()[1],date.getSolar()[2]));
    }

    public abstract void onEventGet(String date);
}
