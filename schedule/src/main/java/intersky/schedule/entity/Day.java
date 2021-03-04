package intersky.schedule.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Day {

    public static final int DATE_ITEM_TYPE_WEEK = 0;
    public static final int DATE_ITEM_TYPE_NOMAL = 2;

    public String title;
    public String key;
    public int type = DATE_ITEM_TYPE_NOMAL;
    public ArrayList<Event> mEvents = new ArrayList<Event>();
    public HashMap<String,Event> mhashEvents = new HashMap<String,Event>();
    public boolean isSelect = false;
    public boolean isToday = false;

    public Day(String title, int type, String key)
    {
        this.title = title;
        this.type = type;
        this.key = key;
    }

}
