package intersky.function.receiver.entity;

import java.util.ArrayList;

import intersky.mywidget.Data;

public class LableData extends Data {

    public String caption1 = "";
    public String caption2 = "";
    public String field1 = "";
    public String field2 = "";
    public ArrayList<LableDataItem> lableDataItems = new ArrayList<LableDataItem>();
    public LableData()
    {
        this.dataType = Data.FUN_DATA_TYPE_LABLE;
    }
}
