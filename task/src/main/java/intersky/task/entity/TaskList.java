package intersky.task.entity;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/11/1.
 */

public class TaskList {

    public static final int LIST_TYPE_HEAD = 0;
    public static final int LIST_TYPE_ITEM = 1;
    public static final int LIST_TYPE_ADD = 2;

    public String mId = "";
    public String mTaskId = "";
    public String name = "";
    public String mListItemid = "";
    public int isComplete = 0;
    public boolean expend = false;
    public View mView;
    public int type;
    public int listcount;
    public int finishcount = 0;
    public ArrayList<TaskList> mLists = new ArrayList<TaskList>();
}
