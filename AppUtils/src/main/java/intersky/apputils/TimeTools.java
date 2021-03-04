package intersky.apputils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeTools {
    //获取当前时间，精确到毫秒
    public static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time);
        return nowTimeStamp;
    }
    /**
     * 日期格式字符串转换成时间戳（精确到毫秒）
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public static String Date2TimeStamp(String dateStr, String format) {
        try {
            //先转换成Long再做转换，否则会报错
            Date date = new Date(Long.valueOf(dateStr));
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String formattedTime = sdf.format(date);
            return formattedTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1488804520419";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss.SSS";
     * @return 返回结果 如："2017-03-06 20:48:40.020";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(formats)) {
            formats = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }
    public static void main(String args[]) {
        String time = TimeStamp2Date("1488804520419", "yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(time);
        time = Date2TimeStamp(getNowTimeStamp(), "yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(time);
    }
}
