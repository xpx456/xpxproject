package intersky.task.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;

public class Task implements Parcelable
{
	public String taskName = "";
	public String taskId = "";
	public String parentId = "";
	public String parentIdList = "";
	public String leaderId = "";
	public String senderIdList = "";
	public String des = "";
	public String projectId = "";
	public String beginTime = "";
	public String endTime = "";
	public String userId = "";
	public String sonJson = "";
	public String listJson = "";
	public String tag = "";
	public String stageName = "";
	public String projectName = "";
	public String parentName = "";
	public String completeTime = "";
	public int isStar = 0;
	public int isComplete = 0;
	public int isLocked = 0;
	public int taskfinish = 0;
	public int taskcount = 0;
	public int listfinish = 0;
	public int listcount = 0;
	public boolean isread = false;
	public String stageId = "";
	public View view;
	public View dview;
	public boolean expend = false;
	public String attachmentName = "";
	public String attachmentHash = "";
	public boolean show = false;
	public ArrayList<Task> tasks = new ArrayList<Task>();
	public ArrayList<Contral> mContrals = new ArrayList<Contral>();
//	public StageModel mStage;
	public Task()
	{

	}

	public Task(String taskName, String taskId, String parentId, String parentIdList, String leaderId, String senderIdList, String des
	, String projectId, String beginTime, String endTime, String userId, String sonJson, String listJson, int isStar, int isComplete, int isLocked
			, boolean isread, String stageId, String tag, String stageName, String projectName, String parentName,  int taskcount
			, int taskfinish, int listcount, int listfinish, String completeTime)
	{
		this.taskName = taskName;
		this.taskId = taskId;
		this.parentId = parentId;
		this.parentIdList = parentIdList;
		this.leaderId = leaderId;
		this.senderIdList = senderIdList;
		this.des = des;
		this.projectId = projectId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.userId = userId;
		this.sonJson = sonJson;
		this.listJson = listJson;
		this.isStar = isStar;
		this.isComplete = isComplete;
		this.isLocked = isLocked;
		this.isread = isread;
		this.stageId = stageId;
		this.tag = tag;
		if(this.tag.equals("null"))
		{
			this.tag = "";
		}
		this.stageName = stageName;
		this.projectName = projectName;
		this.parentName = parentName;
		this.taskcount = taskcount;
		this.taskfinish = taskfinish;
		this.listfinish = listfinish;
		this.listcount = listcount;
		this.completeTime = completeTime;
	}

	public static final Creator<Task> CREATOR = new Creator<Task>() {
		public Task createFromParcel(Parcel in) {
			return new Task(in.readString(), in.readString(), in.readString(), in.readString(),
					in.readString(), in.readString(),in.readString(),in.readString(),in.readString(),
					in.readString(),in.readString(),in.readString(),in.readString()
					,in.readInt(),in.readInt(),in.readInt(),Boolean.valueOf(in.readString())
					,in.readString(),in.readString(),in.readString(),in.readString(),in.readString()
			,in.readInt(),in.readInt(),in.readInt(),in.readInt(),in.readString());
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(taskName);
		dest.writeString(taskId);
		dest.writeString(parentId);
		dest.writeString(parentIdList);
		dest.writeString(leaderId);
		dest.writeString(senderIdList);
		dest.writeString(des);
		dest.writeString(projectId);
		dest.writeString(beginTime);
		dest.writeString(endTime);
		dest.writeString(userId);
		dest.writeString(sonJson);
		dest.writeString(listJson);
		dest.writeInt(isStar);
		dest.writeInt(isComplete);
		dest.writeInt(isLocked);
		dest.writeString(String.valueOf(isread));
		dest.writeString(stageId);
		dest.writeString(tag);
		dest.writeString(stageName);
		dest.writeString(projectName);
		dest.writeString(parentName);
		dest.writeInt(taskcount);
		dest.writeInt(taskfinish);
		dest.writeInt(listcount);
		dest.writeInt(listfinish);
		dest.writeString(completeTime);
	}


	@Override
	public int describeContents() {
		return 0;
	}
}

