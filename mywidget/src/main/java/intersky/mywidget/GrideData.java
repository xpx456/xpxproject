package intersky.mywidget;

import java.util.ArrayList;
import java.util.HashMap;

public class GrideData extends Data {

    public int gridPage;
    public boolean isLineClick = true;
    public boolean canEdit = false;
    public boolean showSearch = false;
    public boolean showBoardSearch = false;
    public String nextAppName = "";
    public String nextAppCaption = "";
    public String nextAppType = "";
    public String modul = "";
    public String head = "";
    public TableItem tabkeBase;
    public String keyWord = "";
    public boolean isall = false;
    public ArrayList<TableItem> tableHead = new ArrayList<TableItem>();
    public ArrayList<TableItem> tableLeft = new ArrayList<TableItem>();
    public ArrayList<TableItem> tableGrid = new ArrayList<TableItem>();
    public HashMap<String,String> tableContent = new HashMap<String,String>();
    public ArrayList<String> dataKeys = new ArrayList<String>();
    public ArrayList<TableCloumArts> tableCloums = new ArrayList<TableCloumArts>();
    public HashMap<String,TableCloumArts> tableCloumArtsHashMap = new HashMap<String,TableCloumArts>();
    public ArrayList<SearchHead> mSearchHeads = new ArrayList<SearchHead>();
    public GrideData()
    {
        this.dataType = Data.FUN_DATA_TYPE_GRID;
    }

    @Override
    public void update() {
        super.update();
        gridPage = 1;
        isall = false;
        tableLeft.clear();
        tableGrid.clear();
        tableContent.clear();
        dataKeys.clear();
    }
}
