package intersky.task.entity;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.task.view.adapter.TaskTemplateAdapter;

/**
 * Created by xpx on 2017/12/14.
 */

public class TemplateType {

    public String name = "";
    public String typeid = "";
    public String type = "";
    public boolean select =false;
    public GridView gridView;
    public View mview;
    public TextView mHeadView;
    public ArrayList<Template> mTamplates = new ArrayList<Template>();
    public TaskTemplateAdapter mTaskTemplateAdapter;
    public boolean isall = false;
}
