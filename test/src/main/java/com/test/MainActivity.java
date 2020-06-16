package com.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView num = this.findViewById(R.id.number);
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        num.setText("screenHeight:"+String.valueOf(metric.heightPixels)
                +"/"+"screenWidth:"+String.valueOf(metric.widthPixels)
                +"/"+"density:"+String.valueOf(metric.density)
                +"/"+"densityDpi:"+String.valueOf(metric.densityDpi)
                +"/"+"xdpi:"+String.valueOf(metric.xdpi)
                +"/"+"ydpi:"+String.valueOf(metric.ydpi));
    }


    public String getzhishu() {
        HashMap<String,Integer> unPrime = getunPrime();
        String numnber = "";
        for(int i = 0 ; i < 101 ; i++)
        {
            if(!unPrime.containsKey(String.valueOf(i)))
            {
                if(numnber.length() == 0)
                {
                    numnber += String.valueOf(i);
                }
                else
                {
                    numnber += ","+String.valueOf(i);
                }
            }
        }
        return numnber;
    }

    public int zhishu(int number,int n) {
        int a = 1;
        for(int i = 0 ; i < n ; i++)
        {
            a = a*number;
        }
        return a;
    }

    public HashMap<String,Integer> getunPrime() {
        HashMap<String,Integer> unPrime = new HashMap<String,Integer>();
        unPrime.put("0",0);
        unPrime.put("1",1);
        for(int i = 2 ; i < 101 ; i++)
        {
            for(int j = 2 ; j < 101 ; j++)
            {
                if(i*j < 101)
                {
                    unPrime.put(String.valueOf(i*j),i*j);
                }
                else
                {
                    break;
                }
            }
        }
        return unPrime;
    }


    public HashMap<String,HashMap> rankbase(int count) {
        HashMap<String,HashMap> all = new HashMap<String,HashMap>();
        for(int i = 0 ; i < count ; i++)
        {
            HashMap<String,HashMap> temp = new HashMap<String,HashMap>();
            all.put(String.valueOf(i),temp);
            HashMap<String,String> has = new HashMap<String,String>();
            has.put(String.valueOf(i),String.valueOf(i));
            rank(temp,has,count);
        }
        return all;
    }

    public void rank(HashMap<String,HashMap> all,HashMap<String,String> has,int count)
    {
        for(int i = 0 ; i < count ; i++)
        {
            if(!has.containsKey(String.valueOf(i)))
            {
                HashMap<String,HashMap> temp = new HashMap<String,HashMap>();
                all.put(String.valueOf(i),temp);
                HashMap<String,String> has2 = new HashMap<String,String>();
                for(Map.Entry<String,String> entry:has.entrySet()){
                    has2.put(entry.getKey(),entry.getValue());
                }
                has2.put(String.valueOf(i),String.valueOf(i));
                if(has2.size() < 10)
                rank(temp,has2,count);
            }
        }

    }

    public String rankbase2(int count) {
        HashMap<String,String> numbers = new HashMap<String,String>();
        rank2(count,count,numbers);
        String v ="";
        for(Map.Entry<String,String> entry:numbers.entrySet()){
             v += entry.getValue()+";";
        }
        return v;
    }

    public void rank2(int max,int count,HashMap<String,String> numbers)
    {
        int countt = count-1;
        for(int i = 0 ; i < max ; i++)
        {
            rank22(max,countt,numbers,String.valueOf(i));
        }
    }

    public void rank22(int max,int count,HashMap<String,String> numbers,String before)
    {
        int countt = count-1;
        for(int i = 0 ; i < max ; i++)
        {
            String befort = before;
            if(!befort.contains(String.valueOf(i)))
            {
                if(befort.length() == 0)
                {
                    befort += String.valueOf(i);

                }
                else
                {
                    befort += ","+String.valueOf(i);
                }
                if(countt > 0)
                {
                    rank22(max,countt,numbers,befort);
                }
                else
                {
                    numbers.put(befort,befort);
                }
            }

        }
    }
}
