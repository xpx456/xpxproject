package intersky.schedule;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import intersky.mywidget.MyViewPager;
import intersky.schedule.entity.Day;
import intersky.schedule.entity.Event;
import intersky.schedule.view.adapter.DateAdapter;
import intersky.schedule.view.adapter.SchedulePageAdapter;

public class ScheduleHelper {

    public MyViewPager mViewPager;
    public ArrayList<View> mViews = new ArrayList<View>();
    public HashMap<String,DateAdapter> mScheduleAdapters = new HashMap<String,DateAdapter>();
    public HashMap<String,ArrayList<Day>> mDayIndexes = new HashMap<String,ArrayList<Day>>();
    public ArrayList<Integer> mYears = new ArrayList<Integer>();
    public Context mContext;
    public int todayYear;
    public int todayMonth;
    public int todayday;
    public int titleYear;
    public int titleMonth;
    public String headTitle;
    public Day mSelectDay;
    public GetEvent mGetEvent;
    public SchedulePageAdapter mSchedulePageAdapter;

    public interface GetEvent
    {
        void doEvent(String begin, String end);
        void updataEvent(Day day);
    }

    public void init(Activity mContext, MyViewPager mViewPager, GetEvent getEvent, ViewPager.OnPageChangeListener mOnPageChangeListener)
    {
        Calendar c = Calendar.getInstance();
        mGetEvent = getEvent;
        this.mContext = mContext;
        this.mViewPager = mViewPager;
        todayYear = c.get(Calendar.YEAR);
        todayMonth = c.get(Calendar.MONTH)+1;
        todayday = c.get(Calendar.DAY_OF_MONTH);
        titleYear = todayYear;
        titleMonth = todayMonth;
        for (int i = 1; i < 13; i++) {
            String key = String.format("%04d-%02d", c.get(Calendar.YEAR), i);
            mScheduleAdapters.put(key,new DateAdapter(mContext,getData(c.get(Calendar.YEAR),i),key));
            View mView = mContext.getLayoutInflater().inflate(R.layout.date_page, null);
            mViews.add(mView);
        }
        setyaer(todayMonth,todayYear);
        mYears.add(todayYear);
        if(mGetEvent != null)
        mGetEvent.doEvent(getYaerBegin(0),getYaerEnd(0));
        if(titleMonth <= 2)
        {
            if(mScheduleAdapters.get(String.format("%04d-%02d",titleYear- 1,1)) == null)
            {
                initYear(titleYear-1);
            }
        }
        else if(titleMonth >= 11)
        {
            if(mScheduleAdapters.get(String.format("%04d-%02d",titleYear+ 1,1)) == null)
            {
                initYear(titleYear+1);
            }
        }
        headTitle = String.format("%04d年%02d月", todayYear, todayMonth);
        mSchedulePageAdapter = new SchedulePageAdapter(mViews,this);
        this.mViewPager.setAdapter(mSchedulePageAdapter);
        this.mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        this.mViewPager.setCurrentItem(33000+titleMonth-1);
    }

    private void initYear(int year)
    {
        for (int i = 1; i < 13; i++) {
            String key = String.format("%04d-%02d", year, i);
            mScheduleAdapters.put(key,new DateAdapter(mContext,getData(year,i),key));
        }
        if(mGetEvent != null)
            mGetEvent.doEvent(getYaerBegin(year),getYaerEnd(year));
        mYears.add(year);
    }

    private ArrayList<Day> getData(int year, int month) {
        ArrayList<Day> days = new ArrayList<Day>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int count = c.getActualMaximum(Calendar.DATE);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (month == 1) {
            c.set(Calendar.YEAR, year - 1);
            c.set(Calendar.MONTH, 11);
        } else {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month - 2);
        }
        int count1 = c.getActualMaximum(Calendar.DATE);
        setDates(days, count, count1, dayOfWeek, year, month);
        return days;
    }

    private void setDates(ArrayList<Day> mDates, int count, int count1, int week, int year, int month) {
        String key = String.format("%04d-%02d", year, month);
        mDates.clear();
        mDates.add(new Day(mContext.getString(R.string.week_mon_head), Day.DATE_ITEM_TYPE_WEEK, key));
        mDates.add(new Day(mContext.getString(R.string.week_tue_head), Day.DATE_ITEM_TYPE_WEEK, key));
        mDates.add(new Day(mContext.getString(R.string.week_wen_head), Day.DATE_ITEM_TYPE_WEEK, key));
        mDates.add(new Day(mContext.getString(R.string.week_ths_head), Day.DATE_ITEM_TYPE_WEEK, key));
        mDates.add(new Day(mContext.getString(R.string.week_fri_head), Day.DATE_ITEM_TYPE_WEEK, key));
        mDates.add(new Day(mContext.getString(R.string.week_sat_head), Day.DATE_ITEM_TYPE_WEEK, key));
        mDates.add(new Day(mContext.getString(R.string.week_sun_head), Day.DATE_ITEM_TYPE_WEEK, key));
        for (int i = count1-week+2; i < count1+1 ; i++) {
            String detial;
            if (month == 1)
                detial = String.format("%04d-%02d-%02d", year - 1, 12,i);
            else
                detial = String.format("%04d-%02d-%02d", year, month - 1,i);

            Day temp = new Day(String.valueOf(i), Day.DATE_ITEM_TYPE_NOMAL, detial);
            mDates.add(7, temp);
            checkIndex(temp,detial);
        }
        for (int i = 1; i < count + 1; i++) {
            String deital = String.format("%04d-%02d-%02d", year, month,i);
            String todetial = String.format("%04d-%02d-%02d", todayYear, todayMonth,todayday);
            Day mDateModel =  new Day(String.valueOf(i), Day.DATE_ITEM_TYPE_NOMAL, deital);
            if (todetial.equals(deital)) {

                mDateModel.isToday = true;
            }
            mDates.add(mDateModel);
            checkIndex(mDateModel,deital);
        }
        int count4 = mDates.size();
        for (int i = 1; i < 49 - count4 + 1; i++) {
            String detial;
            if (month == 12)
                detial = String.format("%04d-%02d-%02d", year + 1, 1,i);
            else
                detial = String.format("%04d-%02d-%02d", year, month + 1,i);

            Day temp = new Day(String.valueOf(i), Day.DATE_ITEM_TYPE_NOMAL, detial);
            mDates.add(temp);
            checkIndex(temp,detial);
        }
    }

    private void checkIndex(Day temp,String key)
    {
        if(mDayIndexes.containsKey(key))
        {
            ArrayList<Day> index = mDayIndexes.get(key);
            index.add(temp);
        }
        else
        {
            ArrayList<Day> index = new ArrayList<Day>();
            index.add(temp);
            mDayIndexes.put(key,index);
        }
    }

    public void initDate(int position)
    {
        int year = (int) mViews.get(position).getTag();
        int month = position+1;
        if(titleMonth == month && year == titleYear)
        {

        }
        else
        {
            setyaer(month,year);
            titleMonth = month;
            titleYear = year;
            headTitle = String.format("%04d年%02d月", titleYear, titleMonth);
            if (titleMonth == 11) {

                if (mScheduleAdapters.get(String.format("%04d-%02d",titleYear+ 1,1)) == null) {
                    initYear(titleYear+ 1);
                }
            } else if (titleMonth == 2) {
                if (mScheduleAdapters.get(String.format("%04d-%02d",titleYear- 1,1)) == null) {
                    initYear(titleYear- 1);
                }
            }
        }
    }

    private void setyaer(int month,int year) {
        switch (month)
        {
            case 1:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 0 && i <=6)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year-1);
                    }
                }
                break;
            case 2:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 1 && i <=7)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 1 && i >= 0 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year-1);
                    }
                }
                break;
            case 3:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 2 && i <=8)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 2 && i >= 0 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year-1);
                    }
                }
                break;
            case 4:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 3 && i <=9)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 3 && i >= 0 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year-1);
                    }
                }
                break;
            case 5:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 4 && i <=10)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 4 && i >= 0 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year-1);
                    }
                }
                break;
            case 6:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 5 && i <=11)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 5 && i >= 0 )
                    {
                        mViews.get(i).setTag(year);
                    }
                }
                break;
            case 7:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 6 && i <=11)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 6 && i >= 1 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year+1);
                    }
                }
                break;
            case 8:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 7 && i <=11)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 7 && i >= 2 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year+1);
                    }
                }
                break;
            case 9:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 8 && i <=11)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 8 && i >= 3 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year+1);
                    }
                }
                break;
            case 10:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 9 && i <=11)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 9 && i >= 4 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year+1);
                    }
                }
                break;
            case 11:
                for(int i =0 ; i < mViews.size() ; i++)
                {
                    if(i >= 10 && i <=11)
                    {
                        mViews.get(i).setTag(year);
                    }
                    else if(i < 10 && i >= 5 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year+1);
                    }
                }
                break;
            case 12:
                for(int i =0 ; i < mViews.size() ; i++)
                {

                    if(i <= 11 && i >= 6 )
                    {
                        mViews.get(i).setTag(year);
                    }
                    else
                    {
                        mViews.get(i).setTag(year+1);
                    }
                }
                break;

        }
    }



    private void doSelectDay(Day day)
    {
        day.isSelect = true;
        if(mSelectDay != null)
        {
            mSelectDay.isSelect = false;
        }
        mSelectDay = day;
        mScheduleAdapters.get(String.format("%04d-%02d",titleYear,titleMonth)).notifyDataSetChanged();
        mGetEvent.updataEvent(day);
    }

    private void cleanEvents(int year)
    {
        for(int i = 0 ; i < 12 ; i++)
        {
            DateAdapter mDateAdapter = mScheduleAdapters.get(String.format("%04d-%02d",year,i));
            if(mDateAdapter != null)
            {
                for(int j = 0 ; j < mDateAdapter.mDates.size(); j++)
                {
                    mDateAdapter.mDates.get(j).mhashEvents.clear();
                    mDateAdapter.mDates.get(j).mEvents.clear();
                }
            }
        }
    }

    public void setDate(int year,int month)
    {
        titleMonth = month;
        titleYear = year;
        headTitle = String.format("%04d年%02d月", titleYear, titleMonth);
        if (mScheduleAdapters.get(String.format("%04d-%02d",titleYear,titleMonth)) == null) {
            initYear(titleYear);
        }

        if (titleMonth >= 11 ) {
            if (mScheduleAdapters.get(String.format("%04d-%02d",titleYear+ 1,1)) == null) {
                initYear(titleYear+ 1);
            }
        } else if (titleMonth <= 2) {
            if (mScheduleAdapters.get(String.format("%04d-%02d",titleYear- 1,1)) == null) {
                initYear(titleYear- 1);
            }
        }
        setyaer(month,year);
        if(todayYear > titleYear)
        {
//            mViewPager.setAdapter(mSchedulePageAdapter);
            mViewPager.setCurrentItem(33000-(todayYear-titleYear)*12+titleMonth-1);
//            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
//            mSchedulePageAdapter.updataGride();
        }
        else
        {
//            mViewPager.setAdapter(mSchedulePageAdapter);
            mViewPager.setCurrentItem(33000+(titleYear-todayYear)*12+titleMonth-1);
//            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
//            mSchedulePageAdapter.updataGride();
        }
        mSchedulePageAdapter.notifyDataSetChanged();
    }

    public AdapterView.OnItemClickListener clickDatelistener = new  AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            doSelectDay((Day) parent.getAdapter().getItem(position));
        }
    };

    public String getYaerBegin(int year)
    {
        if(year == 0)
        {
            DateAdapter mDateAdapter = mScheduleAdapters.get(String.format("%04d-01",todayYear));
            Day day = mDateAdapter.mDates.get(7);
            return day.key;
        }
        else
        {
            DateAdapter mDateAdapter = mScheduleAdapters.get(String.format("%04d-01",year));
            if(mDateAdapter != null)
            {
                Day day = mDateAdapter.mDates.get(7);
                return day.key;
            }
            else
            {
                return "";
            }
        }
    }


    public String getYaerEnd(int year)
    {
        if(year == 0)
        {
            DateAdapter mDateAdapter = mScheduleAdapters.get(String.format("%04d-12",todayYear));
            Day day = mDateAdapter.mDates.get(48);
            return day.key;
        }
        else
        {
            DateAdapter mDateAdapter = mScheduleAdapters.get(String.format("%04d-12",year));
            if(mDateAdapter != null)
            {
                Day day = mDateAdapter.mDates.get(48);
                return day.key;
            }
            else
            {
                return "";
            }
        }
    }

    public void addEvent(Event event) {
        String time = event.mTime.substring(0,10);
        ArrayList<Day> index = mDayIndexes.get(time);
        for(int i = 0 ; i < index.size() ; i++)
        {
            if(!index.get(i).mhashEvents.containsKey(event.mScheduleid))
            {
                index.get(i).mEvents.add(event);
                index.get(i).mhashEvents.put(event.mScheduleid,event);
            }
            else
            {
                Event event1 = index.get(i).mhashEvents.get(event.mScheduleid);
                event1.mReminds = event.mReminds;
                event1.mTime = event.mTime;
                event1.mContent = event.mContent;
            }

            if(mSelectDay == null)
            {
                if(index.get(i).isToday)
                {
                    mGetEvent.updataEvent(index.get(i));
                }
            }
            else
            {
                if(index.get(i).isSelect)
                {
                    mGetEvent.updataEvent(index.get(i));
                }
            }
        }
        mScheduleAdapters.get(String.format("%04d-%02d",titleYear,titleMonth)).notifyDataSetChanged();

    }

    public void updateEvent()
    {
        for(int i = 0 ; i < mYears.size() ; i++)
        {
            cleanEvents(mYears.get(i));
            mGetEvent.doEvent(getYaerBegin(mYears.get(i)),getYaerEnd(mYears.get(i)));
        }
    }

    public void deleteEvent(Event event) {
        String time = event.mTime.substring(0,10);
        ArrayList<Day> index = mDayIndexes.get(time);
        for(int i = 0 ; i < index.size() ; i++)
        {
            if(index.get(i).mhashEvents.containsKey(event.mScheduleid))
            {
                index.get(i).mEvents.remove(event);
                index.get(i).mhashEvents.remove(event.mScheduleid);
            }


            if(mSelectDay == null)
            {
                if(index.get(i).isToday)
                {
                    mGetEvent.updataEvent(index.get(i));
                }
            }
            else
            {
                if(index.get(i).isSelect)
                {
                    mGetEvent.updataEvent(index.get(i));
                }
            }
        }
        mScheduleAdapters.get(String.format("%04d-%02d",titleYear,titleMonth)).notifyDataSetChanged();

    }
}
