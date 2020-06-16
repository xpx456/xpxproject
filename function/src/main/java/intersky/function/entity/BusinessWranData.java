package intersky.function.entity;

import java.util.ArrayList;

import intersky.mywidget.Data;

public class BusinessWranData extends Data {

    public int count = 0;

    public BusinessWranData()
    {
        this.dataType = Data.FUN_DATA_TYPE_BUSINESSWARN;
    }

    public ArrayList<BussinessWarnItem> bussinessWarnItems = new ArrayList<BussinessWarnItem>();

    @Override
    public void update() {
        count = 0;
        bussinessWarnItems.clear();
    }
}
