package intersky.function.entity;

import java.util.ArrayList;

import intersky.mywidget.Data;

public class WorkFlowData extends Data {

    public WorkFlowData()
    {
        this.dataType = Data.FUN_DATA_TYPE_WORKFLOW;
    }

    public ArrayList<WorkFlowItem> workFlowItems = new ArrayList<WorkFlowItem>();

    @Override
    public void update() {
        workFlowItems.clear();
    }
}
