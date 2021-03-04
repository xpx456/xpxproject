package intersky.mywidget;

import com.othershe.calendarview.listener.OnPagerChangeListener;

public class MyCalendarOnPagerChangeListener implements OnPagerChangeListener {

    public CalendarEventHelper calendarEventHelper;


    public MyCalendarOnPagerChangeListener(CalendarEventHelper calendarEventHelper) {
        this.calendarEventHelper = calendarEventHelper;
    }

    @Override
    public void onPagerChanged(int[] date) {
        int year = date[0];
        int month = date[1];
        calendarEventHelper.getYearEvent(year);
        calendarEventHelper.title = String.valueOf(date[0])+calendarEventHelper.context.getString(R.string.year)+String.valueOf(date[1])+calendarEventHelper.context.getString(R.string.month);
        if(month > 6)
        {
            calendarEventHelper.getYearEvent(year+1);
        }
        else
        {
            calendarEventHelper.getYearEvent(year-1);
        }
    }
}
