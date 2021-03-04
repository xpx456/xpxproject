package com.accesscontrol.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPageListData{

    private HashMap<String,Guest> hashData = new HashMap<String,Guest>();
    private ArrayList<Guest> listData = new ArrayList<Guest>();
    private ArrayList<Guest> showData = new ArrayList<Guest>();
    private int totalpage = 1;
    private int pagesize = 20;
    private int nowpage = 0;
    private int pagecutsize = 7;
    private int starpageshow = 0;
    private int endpageshow = 0;


    public MyPageListData(HashMap<String,Guest> hashData) {
        this.hashData = hashData;
        creat();
    }

    public MyPageListData(HashMap<String,Guest> hashData,int pagesize) {
        this.hashData = hashData;
        this.pagesize = pagesize;
        creat();
    }

    public MyPageListData(HashMap<String,Guest> hashData,int pagesize,int nowpage) {
        this.hashData = hashData;
        this.pagesize = pagesize;
        this.nowpage = nowpage-1;
        creat();
    }

    public String setData(HashMap<String,Guest> hashData)
    {
        this.hashData = hashData;
        return creat();
    }

    private String creat() {
        if(hashData == null) {
            return "data is null";
        }
        else
        {
            totalpage = hashData.size()/pagesize;
            if(hashData.size() % pagesize > 0)
            {
                totalpage++;
            }

            if(totalpage <= pagecutsize)
            {
                starpageshow = 0;
                endpageshow = totalpage;
            }
            else
            {
                if(nowpage < pagecutsize/2 )
                {
                    starpageshow = 0;
                    endpageshow = pagecutsize-1;
                }
                else if(nowpage >= totalpage-1-pagecutsize/2)
                {
                    starpageshow = totalpage-pagecutsize;
                    endpageshow = totalpage-1;
                }
            }

            int i = 0;
            for (Map.Entry<String, Guest> entry : hashData.entrySet()) {
                Guest object = entry.getValue();
                listData.add(object);
                if(pagesize*nowpage <= i && i < pagesize*(nowpage+1))
                {
                    showData.add(object);
                }
                i++;
            }
            return "pagesize = "+String.valueOf(pagesize)+
                    "" +
                    "/totalpage = "+String.valueOf(totalpage) +
                    "/nowpage = "+String.valueOf(nowpage);
        }
    }

    public ArrayList<Guest> setShowPage(int page) {

        nowpage = page-1;
        if(nowpage >= pagesize || nowpage < 0)
        {
            return new ArrayList<Guest>();
        }
        else
        {
            if(page != nowpage)
            {
                showData.clear();
                for(int i = pagesize*nowpage ; i < pagesize*(nowpage+1) ; i++)
                {
                    showData.add(listData.get(i));
                }
            }
            return showData;
        }
    }

    public void doReset() {
        hashData.clear();
        listData.clear();
        showData.clear();
        totalpage = 1;
        pagesize = 20;
        nowpage = 0;
    }

    public int getPageid()
    {
        return nowpage+1;
    }

    public int getTotalpage()
    {
        return totalpage;
    }

    public int getStarpageshow()
    {
        return starpageshow+1;
    }

    public int getEndpageshow() {
        return endpageshow+1;
    }

    public int getItemsize()
    {
        return hashData.size();
    }

    public ArrayList<Guest> getShowPage() {
        if(showData == null)
        {
            return new ArrayList<Guest>();
        }
        else
        {
            return showData;
        }
    }
}
