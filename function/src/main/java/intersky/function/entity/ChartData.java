package intersky.function.entity;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.mywidget.Data;

public class ChartData extends Data {
    public HashMap<String, ChartDataItem> mDataForm = new HashMap<String, ChartDataItem>();
    public ArrayList<String> xLable = new ArrayList<String>();
    public HashMap<String,String> xhash = new HashMap<String,String>();
    public String unit = "";
    public ChartData(String type) {
        this.dataType = type;
    }
}
