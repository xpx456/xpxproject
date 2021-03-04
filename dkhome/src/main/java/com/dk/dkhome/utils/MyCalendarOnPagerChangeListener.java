package com.dk.dkhome.utils;

import com.dk.dkhome.view.DkhomeApplication;
import com.othershe.calendarview.listener.OnPagerChangeListener;

import intersky.apputils.AppUtils;


public class MyCalendarOnPagerChangeListener implements OnPagerChangeListener {

    public CalendarEventHelper calendarEventHelper;


    public MyCalendarOnPagerChangeListener(CalendarEventHelper calendarEventHelper) {
        this.calendarEventHelper = calendarEventHelper;
    }

    @Override
    public void onPagerChanged(int[] date) {
        int year = date[0];
        int month = date[1];
        if(AppUtils.isZh(DkhomeApplication.mApp))
        calendarEventHelper.title = String.valueOf(date[0])+calendarEventHelper.context.getString(intersky.mywidget.R.string.year)+String.valueOf(date[1])+calendarEventHelper.context.getString(intersky.mywidget.R.string.month);
        else
            calendarEventHelper.title = String.valueOf(date[0])+"-"+String.valueOf(date[1]);
    }
}
