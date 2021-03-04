package intersky.schedule.view.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.CalendarViewAdapter;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.CalendarEventHelper;
import intersky.schedule.R;

public class MyCalendarViewAdapter implements CalendarViewAdapter {

    public CalendarEventHelper calendarEventHelper;

    public MyCalendarViewAdapter(CalendarEventHelper calendarEventHelper) {
        this.calendarEventHelper = calendarEventHelper;
    }

    @Override
    public TextView[] convertView(View view, DateBean date) {

        String datestring = String.format("%04d%02d%02d",date.getSolar()[0],date.getSolar()[1],date.getSolar()[2]);
        TextView solarDay = (TextView) view.findViewById(R.id.solar_day);
        TextView lunarDay = (TextView) view.findViewById(R.id.lunar_day);
        TextView[] texts = new TextView[2];
        texts[0] = solarDay;
        texts[1] = lunarDay;
        if(date.getType() == 0)
        {
            calendarEventHelper.lasthashView.put(String.format("%04d-%02d-%02d", date.getSolar()[0],
                    date.getSolar()[1],date.getSolar()[2]),view);
        }
        else if(date.getType() == 1)
        {
            calendarEventHelper.hashView.put(String.format("%04d-%02d-%02d", date.getSolar()[0],
                    date.getSolar()[1],date.getSolar()[2]),view);
        }
        else if(date.getType() == 2)
        {
            calendarEventHelper.nexthashView.put(String.format("%04d-%02d-%02d", date.getSolar()[0],
                    date.getSolar()[1],date.getSolar()[2]),view);
        }

        if(datestring.equals(String.valueOf(TimeUtils.getDateId())))
        {
            if(calendarEventHelper.lastview == null)
            {
                calendarEventHelper.lastview = view;
                calendarEventHelper.choise = date;
            }
            solarDay.setTextColor(Color.parseColor("#ffffff"));
            lunarDay.setTextColor(Color.parseColor("#ffffff"));
            view.setBackgroundResource(R.drawable.shape_bg_round_red);
        }
        else if(calendarEventHelper.hasEvent(date))
        {
            view.setBackgroundResource(R.drawable.shape_bg_cycle_blue);
        }
        return texts;
    }
}
