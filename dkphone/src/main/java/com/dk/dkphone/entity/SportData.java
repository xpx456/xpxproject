package com.dk.dkphone.entity;


import com.dk.dkphone.view.DkPhoneApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SportData {

    public HashMap<String, ArrayList<TestItem>> records = new HashMap<String, ArrayList<TestItem>>();
    public HashMap<String, TestItem> allrecords = new HashMap<String, TestItem>();
    public long sumdistence = 0;
    public long sumsecond = 0;
//    public int days = 0;
    public double topspeed = 0;
    public long totalleave = 0;
    public double daydistence = 0;
    public double daysecond = 0;
    public double daycarl = 0;
//    public int totalcount = 0;
//    public int finishcount = 0;
    public TestItem now;

    public void putTestItem(TestItem testItem)
    {
//        if(!allrecords.containsKey(testItem.rid))
//        {
//            totalcount++;
//        }
        allrecords.put(testItem.rid,testItem);
    }

    public double getDayDistence()
    {
//        double data = (double) sumdistence/days/1000;
//        if(days == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            return Double.valueOf(new DecimalFormat("#.00").format(daydistence));
//        }
        if(daydistence < 50)
        return Double.valueOf(new DecimalFormat("#.00").format(daydistence));
        else
            return 50;
    }

    public double getDaycarl()
    {
        if(daycarl/1000 < 2500)
        return daycarl/1000;
        else
            return 2500;
    }

    public int getdayTime()
    {
//        if(days == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            return (int) (sumsecond /days/60);
//        }
        return (int) (daysecond/60);
    }


    public int getaverLeavel()
    {
        if(sumsecond == 0)
        {
            return 0;
        }
        else
        return (int) (totalleave/sumsecond);
    }

//    public double getrat()
//    {
//
//        if(finishcount == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            double data = 0;
//            int a = allrecords.size();
//            if(allrecords.size() > 0)
//            {
//                 data = finishcount*100/allrecords.size();
//            }
//            return data;
//        }
//
//    }

    public double gettopspeed()
    {
        double data = (double) topspeed;
        if(data > 50)
        {
            data = 50.00;
        }
        return Double.valueOf(new DecimalFormat("#.00").format(data));
    }

    public double getaspeed()
    {

        if(sumsecond == 0)
        {
            return 0;
        }
        else
        {
            double data = (double) sumdistence*36/sumsecond/10;
            if(data > 50)
            {
                data = 50.00;
            }
            return Double.valueOf(new DecimalFormat("#.00").format(data));
        }

    }

    public void updataSecond(String[] data) {
        sumdistence+= Double.valueOf(data[0])*10/36;
        daydistence+= Double.valueOf(data[0])*10/36;
        daysecond++;
        sumsecond++;
        if(topspeed < Double.valueOf(data[0]))
        {
            topspeed = Double.valueOf(data[0]);
        }
        totalleave += Integer.valueOf(data[2]);
        daycarl +=getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3]));
        if(DkPhoneApplication.mApp.sportData.now != null)
            DkPhoneApplication.mApp.sportData.putTestItem(DkPhoneApplication.mApp.sportData.now);
    }

    public Double getCar(int leavel, int rpm) {
        int a = 100 / DkPhoneApplication.mApp.maxSelect;
        int b = leavel / a;
        if (b > DkPhoneApplication.mApp.maxSelect) {
            b = DkPhoneApplication.mApp.maxSelect;
        }
        b = b - 1;
        if (b < 0) {
            b = 0;
        }

        int c = rpm / 10 - 1;
        if (c < 0) {
            c = 0;
        } else if (c > 11) {
            c = 11;
        }
        double k = EquipData.data[c][b] * 2.5;
        return k;
    }


//    public void setfinish(String[] data) {
//        sumdistence+= Double.valueOf(data[0])*10/36;
//        sumsecond++;
//        if(topspeed < Double.valueOf(data[0]))
//        {
//            topspeed = Double.valueOf(data[0]);
//        }
//        totalleave += Integer.valueOf(data[2]);
//        if(last != null)
//        {
//            last.finish = true;
//            if(finishcount < totalcount)
//            {
//                finishcount++;
//            }
//        }
//
//        if(DkPadApplication.mApp.sportData.last != null)
//            DkPadApplication.mApp.sportData.putTestItem(DkPadApplication.mApp.sportData.last);
//    }

    public void addNew(TestItem testItem) {
        now = testItem;
        allrecords.put(testItem.rid,testItem);
        ArrayList<TestItem> testItems = records.get(testItem.date);
        if(testItems != null)
        {
            testItems.add(testItem);
        }
//        totalcount++;
    }
}
