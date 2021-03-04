package intersky.task.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;

/**
 * Created by xpx on 2017/10/11.
 */

public class Project implements Parcelable {

    public static final int EVENT_PROJECT_MORE = 1002;

    public String mName = "";
    public String projectId = "";
    public String templateId = "0";
    public String src = "";
    public String leaderId = "";
    public String creattime = "";
    public int isPower = 0;
    public int isLayer = 0;
    public int isTop = 0;
    public String stageString = "";
    public String mTaskList = "";
    public String des = "";
    public String userid = "";
    public String fileid = "0";
    public boolean expend = false;
    public String nfileid = "";
    public int type = 0;
    public ArrayList<Project> projects = new ArrayList<Project>();
    public ArrayList<Stage> mStages = new ArrayList<Stage>();
    public HashMap<String, Stage> mStageHashs = new HashMap<String, Stage>();

    public Project() {

    }


    protected Project(Parcel in) {
        mName = in.readString();
        projectId = in.readString();
        templateId = in.readString();
        src = in.readString();
        leaderId = in.readString();
        isPower = in.readInt();
        isLayer = in.readInt();
        isTop = in.readInt();
        stageString = in.readString();
        mTaskList = in.readString();
        des = in.readString();
        userid = in.readString();
        fileid = in.readString();
        creattime = in.readString();
        praseStage(in.readString());
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(projectId);
        dest.writeString(templateId);
        dest.writeString(src);
        dest.writeString(leaderId);
        dest.writeInt(isPower);
        dest.writeInt(isLayer);
        dest.writeInt(isTop);
        dest.writeString(stageString);
        dest.writeString(mTaskList);
        dest.writeString(des);
        dest.writeString(userid);
        dest.writeString(fileid);
        dest.writeString(creattime);
        dest.writeString(praseStageJson());
    }

    private String praseStageJson() {

        try {
            JSONArray data = new JSONArray();
            for (int i = 0; i < mStages.size(); i++) {
                Stage stage = mStages.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("stageId", stage.stageId);
                jsonObject.put("name", stage.name);
                jsonObject.put("sort", stage.sort);
                jsonObject.put("isedit", stage.isedit);
                jsonObject.put("newname", stage.newname);
                jsonObject.put("newsoft", stage.newsoft);
                data.put(jsonObject);
                JSONArray ja = new JSONArray();
                if(stage.mTasks.size() > 0)
                {
                    for(int j = 0 ; j < stage.mTasks.size() ; j++) {
                        Task task = stage.mTasks.get(j);
                        praseTaskJson(task,ja);
                    }
                }
                jsonObject.put("task",ja);

            }
            return data.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void praseTaskJson(Task task ,JSONArray ja) {

        try {
            JSONObject jo = new JSONObject();
            jo.put("taskName",task.taskName);
            jo.put("taskId",task.taskId);
            jo.put("parentId",task.parentId);
            jo.put("parentIdList",task.parentIdList);
            jo.put("leaderId",task.leaderId);
            jo.put("senderIdList",task.senderIdList);
            jo.put("des",task.des);
            jo.put("projectId",task.projectId);
            jo.put("beginTime",task.beginTime);
            jo.put("endTime",task.endTime);
            jo.put("userId",task.userId);
            jo.put("sonJson",task.sonJson);
            jo.put("listJson",task.listJson);
            jo.put("tag",task.tag);
            jo.put("stageName",task.stageName);
            jo.put("projectName",task.projectName);
            jo.put("parentName",task.parentName);
            jo.put("completeTime",task.completeTime);
            jo.put("isStar",task.isStar);
            jo.put("isComplete",task.isComplete);
            jo.put("isLocked",task.isLocked);
            jo.put("taskfinish",task.taskfinish);
            jo.put("taskcount",task.taskcount);
            jo.put("listfinish",task.listfinish);
            jo.put("listcount",task.listcount);
            jo.put("isread",task.isread);
            jo.put("stageId",task.stageId);
            jo.put("expend",task.expend);
            jo.put("attachmentName",task.attachmentName);
            jo.put("attachmentHash",task.attachmentHash);
            JSONArray ja1 = new JSONArray();
            if(task.tasks.size() > 0)
            {
                for(int i = 0 ; i < task.tasks.size() ; i++)
                {
                    praseTaskJson(task.tasks.get(i),ja1);
                }
            }
            jo.put("task",ja1);
            ja.put(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void praseStage(String json) {
        if(json.length() > 0)
        {
            try {
                XpxJSONArray data = new XpxJSONArray(json);
                for (int i = 0; i < data.length(); i++) {

                    XpxJSONObject jsonObject = data.getJSONObject(i);
                    Stage stage = new Stage();
                    stage.stageId = jsonObject.getString("stageId");
                    stage.name = jsonObject.getString("name");
                    stage.sort = jsonObject.getString("sort");
                    stage.isedit = jsonObject.getBoolean("isedit",false);
                    stage.newname = jsonObject.getString("newname");
                    stage.newsoft = jsonObject.getString("newsoft");
                    mStages.add(stage);
                    XpxJSONArray ja = jsonObject.getJSONArray("task");
                    for(int j = 0 ; j < ja.length() ; j++) {
                        XpxJSONObject jo = ja.getJSONObject(j);
                        getTask(stage.mTasks,jo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getTask(ArrayList<Task> tasks,XpxJSONObject jo) {


        try {
            Task task = new Task();
            task.taskName = jo.getString("taskName");
            task.taskId = jo.getString("taskId");
            task.parentId = jo.getString("parentId");
            task.parentIdList = jo.getString("parentIdList");
            task.leaderId = jo.getString("leaderId");
            task.senderIdList = jo.getString("senderIdList");
            task.des = jo.getString("des");
            task.projectId = jo.getString("projectId");
            task.beginTime = jo.getString("beginTime");
            task.endTime = jo.getString("endTime");
            task.userId = jo.getString("userId");
            task.sonJson = jo.getString("sonJson");
            task.listJson = jo.getString("listJson");
            task.tag = jo.getString("tag");
            task.stageName = jo.getString("stageName");
            task.projectName = jo.getString("projectName");
            task.parentName = jo.getString("parentName");
            task.completeTime = jo.getString("completeTime");
            task.isStar = jo.getInt("isStar",0);
            task.isComplete = jo.getInt("isComplete",0);
            task.isLocked = jo.getInt("isLocked",0);
            task.taskfinish = jo.getInt("taskfinish",0);
            task.taskcount = jo.getInt("taskcount",0);
            task.listfinish = jo.getInt("listfinish",0);
            task.listcount = jo.getInt("listcount",0);
            task.isread = jo.getBoolean("isread",false);
            task.stageId = jo.getString("stageId");
            task.expend = jo.getBoolean("expend",false);
            task.attachmentName = jo.getString("attachmentName");
            task.attachmentHash = jo.getString("attachmentHash");
            tasks.add(task);
            XpxJSONArray ja = jo.getJSONArray("task");
            if(ja.length() > 0) {
                for(int i = 0 ; i < ja.length() ; i++)
                getTask(task.tasks,ja.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
