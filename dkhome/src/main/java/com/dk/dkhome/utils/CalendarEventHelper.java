package com.dk.dkhome.utils;

import android.content.Context;
import android.view.View;

import com.dk.dkhome.entity.Course;
import com.dk.dkhome.view.DkhomeApplication;
import com.othershe.calendarview.bean.DateBean;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.mywidget.ClendarDay;
import intersky.mywidget.ClendarEvent;

public class CalendarEventHelper {
    public HashMap<String,View> lasthashView = new HashMap<String,View>();
    public HashMap<String,View> hashView = new HashMap<String,View>();
    public HashMap<String,View> nexthashView = new HashMap<String,View>();
    public GetEvent getEvent;
    public DateBean choise;
    public View lastview;
    public String title;
    public Context context;
    public CalendarEventHelper(GetEvent getEvent, String title, Context context) {
        this.getEvent = getEvent;
        this.title = title;
        this.context = context;
    }

    public void setChoise(View view, DateBean choise) {
        getEvent.setChoise(view,choise);
        this.choise = choise;
        this.lastview = view;
    }

    public interface GetEvent
    {
        void setChoise(View view, DateBean choise);
    }


    public boolean hasEvent(DateBean dateBean) {
        String id = String.format("%04d-%02d-%02d",dateBean.getSolar()[0],dateBean.getSolar()[1],dateBean.getSolar()[2]);
        ArrayList<Course> data = DkhomeApplication.mApp.sportDataManager.dayPlans.get(id);
        if(data == null)
        {
            return false;
        }
        else
        {
            if(data.size() > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
