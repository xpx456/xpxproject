package intersky.mywidget;

import android.content.Context;
import android.view.View;

import com.othershe.calendarview.bean.DateBean;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;

public class CalendarEventHelper {
    public HashMap<String,HashMap<String,ClendarDay>> dayeventHash = new HashMap<String,HashMap<String,ClendarDay>>();
    public HashMap<String,View> lasthashView = new HashMap<String,View>();
    public HashMap<String,View> hashView = new HashMap<String,View>();
    public HashMap<String,View> nexthashView = new HashMap<String,View>();
    public GetEvent getEvent;
    public DateBean choise;
    public View lastview;
    public String title;
    public Context context;
    public CalendarEventHelper(GetEvent getEvent,String title,Context context) {
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
        void doEvent(String begin, String end);
        void setChoise(View view, DateBean choise);
    }

    public void getYearEvent(int year) {
        if(!dayeventHash.containsKey(String.valueOf(year))) {
            getEvent.doEvent(String.format("%04d-01",year),String.format("%04d-01",year+1));
        }
    }


    public void updataYearEvent(int year) {
        if(dayeventHash.containsKey(String.valueOf(year))) {
            dayeventHash.remove(String.valueOf(year));

        }
        getYearEvent(year);
    }

    public ClendarDay getDayEvent(int year,String key) {
        HashMap<String,ClendarDay> yearhash = dayeventHash.get(String.valueOf(year));
        if(yearhash != null)
        {
            if(yearhash.containsKey(key))
            {
                return yearhash.get(key);
            }
        }
        return null;
    }

    public boolean hasEvent(DateBean dateBean) {
        HashMap<String,ClendarDay> data = dayeventHash.get(String.valueOf(dateBean.getSolar()[0]));
        if(data != null)
        {
            ClendarDay arrayList = data.get(String.format("%04d-%02d-%02d",dateBean.getSolar()[0],dateBean.getSolar()[1],dateBean.getSolar()[2]));
            if(arrayList != null)
            {
                if(arrayList.clendarEvents.size() > 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void addEvent(ClendarEvent clendarEvent) {
        HashMap<String,ClendarDay> yeardata = dayeventHash.get(clendarEvent.keyDay.substring(0,4));
        if(yeardata == null)
        {
             yeardata = new HashMap<String,ClendarDay>();
             dayeventHash.put(clendarEvent.keyDay.substring(0,4),yeardata);
        }
        ClendarDay clendarEvents = yeardata.get(clendarEvent.keyDay);
        if(clendarEvents == null)
        {
            clendarEvents = new ClendarDay();
            yeardata.put(clendarEvent.keyDay,clendarEvents);
        }
        clendarEvents.add(clendarEvent);
    }

    public void deleteEvent(ClendarEvent clendarEvent) {
        HashMap<String,ClendarDay> yeardata = dayeventHash.get(clendarEvent.keyDay.substring(0,4));
        if(yeardata == null)
        {
            yeardata = new HashMap<String,ClendarDay>();
            dayeventHash.put(clendarEvent.keyDay.substring(0,4),yeardata);
            return;
        }
        ClendarDay clendarEvents = yeardata.get(clendarEvent.keyDay);
        if(clendarEvents == null)
        {
            clendarEvents = new ClendarDay();
            yeardata.put(clendarEvent.keyDay,clendarEvents);
        }
        clendarEvents.remove(clendarEvent.eventId);
    }

    public void updateEvent(ClendarEvent clendarEvent) {
        getEvent.doEvent(clendarEvent.keyDay,clendarEvent.keyDay);
    }

    public void updateEventAll() {
        dayeventHash.clear();
        updataYearEvent(Integer.valueOf(title.substring(0,4)));
    }

}
