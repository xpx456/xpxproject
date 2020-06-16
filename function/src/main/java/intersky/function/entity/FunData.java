package intersky.function.entity;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.mywidget.Data;

public class FunData {

    public HashMap<String,Data> funDatas = new HashMap<String,Data>();
    public ArrayList<String> mKeys = new ArrayList<String>();
    public View showTab;
    public int page = 1;
    public boolean showSearch = false;
    public String keyWord = "";
    public String type = "";
    public void upDate()
    {
        page = 1;
        for(int i = 0 ; i < mKeys.size() ; i++)
        {
            Data mData = funDatas.get(mKeys.get(i));
            mData.update();
        }
    }

}
