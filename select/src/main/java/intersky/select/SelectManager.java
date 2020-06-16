package intersky.select;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.select.entity.CustomSelect;
import intersky.select.entity.Select;
import intersky.select.entity.SelectComparator;
import intersky.select.view.activity.CustomSelectActivity;
import intersky.select.view.activity.SelectActivity;
import intersky.select.view.adapter.CustomSelectAdapter;

public class SelectManager {

    public ArrayList<Select> mSelects = new ArrayList<Select>();
    public ArrayList<Select> mSelectsEd = new ArrayList<Select>();
    public ArrayList<Select> mSearchSelects = new ArrayList<Select>();
    public ArrayList<Select> mHeaner = new ArrayList<Select>();
    public ArrayList<Select> mAllSelects = new ArrayList<Select>();
    public ArrayList<CustomSelect> mCustomSearchSelects = new ArrayList<CustomSelect>();
    public Select mSignal;
    public CustomSelect mCustomSignal;
    public ArrayList<Select> mList = new ArrayList<Select>();
    public static SelectManager mSelectManager;
    public SelectDetial selectDetial;
    public CustomSelectAdapter selectAdapter;
    public static synchronized SelectManager getInstance() {
        if (mSelectManager == null) {
            mSelectManager = new SelectManager();
        }
        return mSelectManager;
    }

    public void startSelectView(Context mContext, ArrayList<Select> selects, String title, String action, boolean signal, boolean showSearch) {
        SelectManager.getInstance().mSelects.clear();
        SelectManager.getInstance().mSignal = null;
        SelectManager.getInstance().mList.clear();
        SelectManager.getInstance().mSelects.addAll(selects);
        for(int i = 0 ; i < selects.size() ; i++)
        {
            if(selects.get(i).iselect == true)
            {
                if(signal)
                {
                    SelectManager.getInstance().mSignal = selects.get(i);
                    break;
                }
                else
                {
                    SelectManager.getInstance().mList.add(selects.get(i));
                }
            }
        }
        if(signal && SelectManager.getInstance().mSignal == null)
        {
            SelectManager.getInstance().mSignal = selects.get(0);
        }
        Intent intent = new Intent(mContext, SelectActivity.class);
        intent.putExtra("signal",signal);
        intent.putExtra("title",title);
        intent.putExtra("showSearch",showSearch);
        intent.setAction(action);
        mContext.startActivity(intent);
    }

    public void startCustomSelectView(Context mContext, CustomSelectAdapter selectAdapter, SelectDetial selectDetial,String title, String action, boolean signal, boolean showSearch) {
        this.selectAdapter = selectAdapter;
        this.selectDetial = selectDetial;
        Intent intent = new Intent(mContext, CustomSelectActivity.class);
        intent.putExtra("signal",signal);
        intent.putExtra("title",title);
        intent.putExtra("showSearch",showSearch);
        intent.setAction(action);
        mContext.startActivity(intent);
    }

    public interface SelectDetial{

        void onfoot();

        void onHead();

    }

    public String[] measureHead() {
        HashMap<String ,String> head = new HashMap<String ,String>();
        mHeaner.clear();
        mAllSelects.clear();
        for(int i = 0 ; i < mSelects.size() ;i++)
        {
            Select select = mSelects.get(i);
            if(!head.containsKey(select.mPingyin.substring(0,1).toLowerCase()))
            {
                head.put(select.mPingyin.substring(0,1).toLowerCase(),select.mPingyin.substring(0,1).toLowerCase());
                Select h = new Select(select.mPingyin.substring(0,1).toLowerCase(),select.mPingyin.substring(0,1).toLowerCase());
                h.mType = 1;
                mHeaner.add(h);
            }
        }
        Collections.sort(mHeaner,new SelectComparator());
        mAllSelects.addAll(mSelects);
        mAllSelects.addAll(mHeaner);
        Collections.sort(mAllSelects,new SelectComparator());
        String[] attrs = new String[mHeaner.size()];
        for(int i = 0 ; i < mHeaner.size() ; i++)
        {
            attrs[i] = mHeaner.get(i).mName.toUpperCase();
        }
        return attrs;
    }

    public static String praseSelectString(ArrayList<Select> selects)
    {
        String word = "";
        for(int i = 0 ;i < selects.size() ;i++)
        {
            word =  AppUtils.addString(word,selects.get(i).mName,",");
        }
        return word;
    }
}
