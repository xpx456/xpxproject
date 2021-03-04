package intersky.mywidget;

import android.view.View;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnSingleChooseListener;

import java.util.ArrayList;

public abstract class MyCalendarOnSingleChooseListener implements OnSingleChooseListener {

    public CalendarEventHelper calendarEventHelper;

    public MyCalendarOnSingleChooseListener(CalendarEventHelper calendarEventHelper) {
        this.calendarEventHelper = calendarEventHelper;
    }

    @Override
    public void onSingleChoose(View view, DateBean date) {
        calendarEventHelper.setChoise(view,date);
        onEventGet( calendarEventHelper.getDayEvent(date.getSolar()[0],String.format("%04d-%02d-%02d",date.getSolar()[0],date.getSolar()[1],date.getSolar()[2])));
    }

    public abstract void onEventGet(ClendarDay clendarDay);
}
