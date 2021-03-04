package intersky.task.entity;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/11/22.
 */

public class Contral {

    public String name = "";
    public String type = "";
    public int drawtype = 1;
    public String id = "";
    public String value = "";
    public String testvalue = "";
    public String testvalue2 = "";
    public String testvalue3 = "";
    public String testjson = "";
    public String taskid = "";
    public String defult = "";
    public String projectid = "";
    public String statu = "";
    public String unit = "";
    public int dayeType = 0;
    public int point = 0;
    public int isradio = 0;
    public int listcount = 1;
    public String listtitle = "";
    public int listpos = 0;
    public View view;
    public LinearLayout mImageLayer;
    public boolean attachdelete = false;
    public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
    public ArrayList<Select> mSelects = new ArrayList<Select>();
    public ArrayList<Select> postItems = new ArrayList<Select>();
    public ArrayList<Select> lastItems = new ArrayList<Select>();
    public Attachment attachment;

}
