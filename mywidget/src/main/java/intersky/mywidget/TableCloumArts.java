package intersky.mywidget;

import android.view.View;

public class TableCloumArts {

    public static final String GRIDE_DATA_TYPE_STRING = "string";
    public static final String GRIDE_DATA_TYPE_IMAGE = "image";
    public static final String GRIDE_DATA_TYPE_RECORDID = "recordid";

    public String mCaption = "";
    public String mFiledName = "";
    public String dataType = GRIDE_DATA_TYPE_STRING;
    public int mWidth = 80;
    public String mFieldType = "";
    public boolean isReadOnly = false;
    public boolean isVisiable = true;
    public boolean isFill = false;
    public String mDefault = "";
    public String mAttributes = "";
    public String mValues = "";
    public boolean isLinkage = false;
    public String mLinkageFields = "";
    public boolean isGrideVisiable = true;
    public String mToLinkageFields = "";
    public View view;
    public String localPath;
}
