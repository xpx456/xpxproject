package intersky.task.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xpx on 2017/11/10.
 */

public class Stage implements Parcelable {

    public String stageId = "";
    public String name = "";
    public String sort = "";
    public int type = 0;
    public View mDetialview;
    public View mViewview;
    public boolean isedit = false;
    public String newname = "";
    public String newsoft = "";
    public ArrayList<Task> mTasks = new ArrayList<Task>();
    public HashMap<String,Task> mTaskHashs = new HashMap<String,Task>();
    /** 下面实现的是对象的序列化*/
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(stageId);
        dest.writeString(name);
        dest.writeString(sort);
    }

    public static final Creator<Stage> CREATOR = new Creator<Stage>()
    {
        public Stage createFromParcel(Parcel source)
        {
            Stage stage = new Stage();
            stage.stageId = source.readString();
            stage.name = source.readString();
            stage.sort = source.readString();
            return stage;
        }
        public Stage[] newArray(int size)
        {
            return new Stage[size];
        }
    };
}
