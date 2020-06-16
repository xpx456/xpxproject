package intersky.mywidget;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/3/13.
 */

public class SearchHead {

    public String mCaption;
    public String mField;
    public String mDefaultValue;
    public String mFieldType;

    public ArrayList<String> valueSelect = new ArrayList<String>();

    public SearchHead(String mCaption, String mField, String mDefaultValue, String mFieldType)
    {
        this.mCaption = mCaption;
        this.mField = mField;
        this.mDefaultValue = mDefaultValue;
        this.mFieldType = mFieldType;
    }


}
