package intersky.apputils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static long getDateAndTimeCode(int s) {
        long code = (System.currentTimeMillis()/1000/s);
        return code;
    }


    public static String getDateAndTimeCode() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%04d%02d%02d%02d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));
        return data;
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        return data;
    }

    public static String getDateTomorrow() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        String data = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        return data;
    }

    public static int getDateId() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%04d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        return Integer.valueOf(data);
    }

    public static String getYesterdayByDate(){
        //实例化当天的日期
        Date today = new Date();
        //用当天的日期减去昨天的日期
        Date yesterdayDate = new Date(today.getTime()-86400000L);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(yesterdayDate);
        return yesterday.substring(0,10);
    }

    public static String getDate(int year,int month,int day) {
        Calendar c = Calendar.getInstance();
        if(year != 0)
        c.add(Calendar.YEAR,year);
        if(month != 0)
        c.add(Calendar.MONTH,month);
        if(day != 0)
        c.add(Calendar.DAY_OF_MONTH,day);
        String data = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        return data;
    }

    public static String getDate2() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%04d.%02d.%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH));
        return data;
    }


    public static String getTime() {
        Calendar c = Calendar.getInstance();
        String time = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        return time;
    }

    public static String getDateMYEx() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%04d-%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        return data;
    }

    public static String getTimeUid() {
        Calendar c = Calendar.getInstance();
        return String.format("%04d%02d%02d%02d%02d%02d",c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
    }

    public static String measureDeteForm(String time) {
        if(time.length() > 10)
        {
            if(time.substring(0,10).equals(getDate()))
            {
                return "今天"+time.substring(11,time.length()).substring(0,5);
            }
            else if(daysBetween(time.substring(0,10),getDate()) == -1)
            {
                return "昨天"+time.substring(11,time.length()).substring(0,5);
            }
            else{
                return time;
            }
        }
        else
        {
            if(time.substring(0,10).equals(getDate()))
            {
                return "今天";
            }
            else if(daysBetween(time.substring(0,10),getDate()) == -1)
            {
                return "昨天";
            }
            else{
                return time;
            }
        }

    }

    public static int daysBetween(String smdate, String bdate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int measureDayCount(String begin, String end) {

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(formatDate(end));
            Date d2 = df.parse(formatDate(begin));
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            if (days > 0) {
                return Integer.valueOf(String.valueOf(days)) + 1;
            } else {
                return Integer.valueOf(String.valueOf(days));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getWeek(String pTime) {

        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }

        return Week;
    }

    public static String getWeek(Context context) {

        String Week = "";
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week = context.getString(R.string.keyword_sun);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week = context.getString(R.string.keyword_mun);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week = context.getString(R.string.keyword_tus);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week = context.getString(R.string.keyword_wen);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week = context.getString(R.string.keyword_ths);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week = context.getString(R.string.keyword_fri);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week = context.getString(R.string.keyword_sat);
        }

        return Week;
    }

    public static int measureDayCount3(String begin, String end) {

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(formatDate(end));
            Date d2 = df.parse(formatDate(begin));
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long minute = diff / (1000 * 60);
            return Integer.valueOf(String.valueOf(minute));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int measureDayCount4(String begin, String end) {

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(formatDate(end));
            Date d2 = df.parse(formatDate(begin));
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long day = diff / (1000 * 60*60*24);
            return Integer.valueOf(String.valueOf(day));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getTimeSecond() {
        Calendar c = Calendar.getInstance();
        String time = String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));
        return time;
    }

    public static String getTimeSecond(int hour,int minute) {
        Calendar c = Calendar.getInstance();
        if(hour != 0)
        c.add(Calendar.HOUR_OF_DAY, hour);
        if(minute != 0)
        c.add(Calendar.MINUTE, minute);
        String time = String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));
        return time;
    }

    public static String getDateAndTime() {
        String tmp = getDate() + " " + getTimeSecond();
        return tmp;
    }

    public static String getDateAndTime(int year,int month,int day,int hour,int minute) {
        String tmp = getDate(year,month,day) + " " + getTimeSecond(hour,minute);
        return tmp;
    }

    public static boolean measureBefore(String begin, String end) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(formatDate(end));
            Date d2 = df.parse(formatDate(begin));
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            if (d1.getTime() > d2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean measureMinute(String begin, String end) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(formatDate(end));
            Date d2 = df.parse(formatDate(begin));
            long diff = d1.getTime()/1000/60/3 - d2.getTime()/1000/60/3;//这样得到的差值是微秒级别
            if (d1.getTime() >= d2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long measureDay(String begin, String end) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(formatDate(end));
            Date d2 = df.parse(formatDate(begin));
            long diff = d1.getTime()/1000/60/60/24 - d2.getTime()/1000/60/60/24;//这样得到的差值是微秒级别
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateAndTimeex(int day) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.DATE, day);//日期回滚7天
        str = sdf.format(lastDate.getTime());
        String tmp = str + " " + TimeUtils.getTime();
        return tmp;
    }

    public static String getDateDay() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%2d",
                c.get(Calendar.DAY_OF_MONTH));
        return data;
    }

    public static String getDateMYex() {
        Calendar c = Calendar.getInstance();
        String data = String.format("%4d/%2d/",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        return data;
    }

    public static int minuteBetween(String smdate, String bdate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(formatDate(smdate)));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(formatDate(bdate)));
            long time2 = cal.getTimeInMillis();
            long between_hours = (time2 - time1) / (1000 * 60);
            return Integer.parseInt(String.valueOf(between_hours));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String praseTaskItemdata(String time) {
        int a = daysBetween(getDate(), time);
        if (a == -1)
            return "昨天" + time.substring(11, 16);
        else if (a == 0)
            return "今天" + time.substring(11, 16);
        else if (a == 1)
            return "明天" + time.substring(11, 16);
        else if (time.substring(0, 4).equals(getDate().substring(0, 4)))
            return time.substring(5, 7) + "月" + time.substring(8, 10) + "日";
        else
            return time.substring(0, 10);
    }

    public static String praseHistoryDate(Context context,String time) {
        int a = daysBetween(getDate(), time);
        if (a == -1)
            return context.getString(R.string.keyword_yestday);
        else if (a == 0)
            return context.getString(R.string.keyword_today);
        else
            return context.getString(R.string.keyword_before);
    }

    public static int hoursBetween(String smdate, String bdate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_hours = (time2 - time1) / (1000 * 3600);
            return Integer.parseInt(String.valueOf(between_hours));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String dateToStamp(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(formatDate(s)).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    public static String stampToDate(String s) {

        if (s.equals("0")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(s + "000")));

    }

    public static boolean isSameDate(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
//        cal1.setFirstDayOfWeek(Calendar.MONDAY);//西方周日为一周的第一天，咱得将周一设为一周第一天
//        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (subYear == 0)// subYear==0,说明是同一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) //subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)//subYear==-1,说明cal比cal2小一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    public static boolean measureTokenDete(String endtime) {
        if(endtime.length() == 0)
        {
            return  false;
        }
        return measureMinute(getDateAndTime(),endtime);
    }

    public static boolean needupdataTokenDete(String endtime) {
        if(endtime.length() == 0)
        {
            return  false;
        }
        boolean need = false;
        if(measureDay(getDateAndTime(),endtime) > 1)
        {
            need = false;
        }
        else
        {
            need = true;
        }
        return need;
    }

    public static String formatDate(String date) {
        String a = date;
        if(date.length() < 19) {
            for(int i = date.length(); i < 19 ; i++) {
                if(i == 4|| i == 7)
                {
                    a += "-";
                }
                else if(i == 13 || i == 16)
                {
                    a += ":";
                }
                else if(i == 10)
                {
                    a += " ";
                }
                else
                {
                    a += "0";
                }
            }
        }
        return a;
    }
}
