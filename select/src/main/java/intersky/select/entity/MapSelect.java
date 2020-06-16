package intersky.select.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;

public class MapSelect {

    public HashMap<String,Select> hashMap = new HashMap<String,Select>();
    public ArrayList<Select> list = new ArrayList<Select>();
    public HashMap<String,Select> selectitem = new HashMap<String,Select>();
    public ArrayList<Select> selectlist = new ArrayList<Select>();
    public void clear() {
        hashMap.clear();
        list.clear();
        selectlist.clear();
        selectitem.clear();
    }

    public String getString() {
        String a = "";
        for(int i = 0 ; i < list.size() ; i++)
        {
            a = AppUtils.addString(a,list.get(i).mName,",");
        }
        return a;
    }

    public void remove(String id)
    {
        Select select = hashMap.get(id);
        hashMap.remove(id);
        list.remove(select);
    }

    public void add(Select select)
    {
        hashMap.put(select.mId,select);
        selectlist.add(select);
        list.add(select);
    }

    public void reset() {
        for(int i = 0 ; i < selectlist.size() ; i++)
        {
            selectlist.get(i).iselect = false;
        }
        selectitem.clear();
        selectlist.clear();
    }

    public void addSelect(String port)
    {
        String prt[] = port.split(",");
        for(int i = 0 ; i < prt.length ; i++)
        {
            if(hashMap.containsKey(prt[i]))
            {
                Select select = hashMap.get(prt[i]);
                setSelect(select);
            }
        }
    }

    public void updataSelect(String port)
    {
        String prt[] = port.split(",");
        for(int i = 0 ; i < selectlist.size() ; i++)
        {
            selectlist.get(i).iselect = false;
        }
        selectlist.clear();
        selectitem.clear();
        addSelect(port);
    }


    public void setSelect(Select select)
    {
        select.iselect = true;
        selectlist.add(select);
        selectitem.put(select.mId,select);
    }

    public void removeSelect(Select select) {
        select.iselect = false;
        selectlist.remove(select);
        selectitem.remove(select);
    }
}
