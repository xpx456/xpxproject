package intersky.task.prase;

import android.content.Context;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.entity.Task;
import intersky.task.entity.TaskList;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class TaskListPrase {

    public static void praseList(NetObject net, Context context, ArrayList<TaskList> mList) {
        try {


            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return;
            }
            Task mTask = (Task) net.item;

            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            mTask.listcount = 0;
            mTask.listfinish = 0;
            String expenid = "";
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).expend && mList.get(i).type == TaskList.LIST_TYPE_HEAD) {
                    if (expenid.length() == 0) {
                        expenid += mList.get(i).mId;
                    } else {
                        expenid += "," + mList.get(i).mId;
                    }
                }
            }
            String[] ids = new String[0];
            if (expenid.length() > 0)
                ids = expenid.split(",");
            mList.clear();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                TaskList mTaskList = new TaskList();
                mTaskList.mId = jo.getString("task_list_id");
                mTaskList.mTaskId = jo.getString("task_id");
                if(!jo.isNull("name"))
                    mTaskList.name = jo.getString("name");
                mTaskList.type = TaskList.LIST_TYPE_HEAD;
                mList.add(mTaskList);
                if (expenid.length() > 0) {
                    mTaskList.expend = checkExpend(ids, mTaskList.mId);
                }
                if (jo.has("has_many_task_list_item")) {
                    JSONArray ja2 = jo.getJSONArray("has_many_task_list_item");
                    for (int j = 0; j < ja2.length(); j++) {
                        JSONObject jo2 = ja2.getJSONObject(j);
                        TaskList mListItem = new TaskList();
                        mListItem.mId = jo2.getString("task_list_id");
                        mListItem.mListItemid = jo2.getString("task_list_item_id");
                        mListItem.mTaskId = mTaskList.mTaskId;
                        mListItem.isComplete = jo2.getInt("is_complete");
                        mListItem.name = jo2.getString("item_name");
                        mListItem.type = TaskList.LIST_TYPE_ITEM;
                        mTaskList.listcount++;
                        if (mTaskList.expend == true) {
                            mList.add(mListItem);
                        }
                        mTaskList.mLists.add(mListItem);
                        if (mListItem.isComplete == 1) {
                            mTaskList.finishcount++;
                            mTask.listfinish++;
                        }
                        mTask.listcount++;
                    }
                }
                if (mTask.isLocked == 0 || mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                        || mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    TaskList TaskList2 = new TaskList();
                    TaskList2.type = TaskList.LIST_TYPE_ADD;
                    TaskList2.mTaskId = mTaskList.mTaskId;
                    TaskList2.mId = jo.getString("task_list_id");
                    mTaskList.mLists.add(TaskList2);
                    if (mTaskList.expend == true) {
                        mList.add(TaskList2);
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkExpend(String[] ids, String id) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equals(id)) {
                return true;
            }
        }
        return false;

    }

    public static int praseAddList(NetObject net,Context context,ArrayList<TaskList> mList) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return 0;
            }

            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            TaskList TaskList = new TaskList();
            TaskList.mId = jo.getString("task_list_id");
            TaskList.mTaskId = jo.getString("task_id");
            if(!jo.isNull("name"))
                TaskList.name = jo.getString("name");
            TaskList.type = TaskList.LIST_TYPE_HEAD;
            mList.add(TaskList);
            TaskList TaskList2 = new TaskList();
            TaskList2.mId = TaskList.mId;
            TaskList2.type = TaskList.LIST_TYPE_ADD;
            TaskList.mLists.add(TaskList2);

            return mList.indexOf(TaskList);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void updataListItemName(NetObject item,Context context) {
        try {

            String json = item.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return ;
            }

            JSONObject jsonObject = new JSONObject(item.result);
            JSONObject data = jsonObject.getJSONObject("data");
            TaskList mTaskListItemModel = (TaskList) item.item;
            String name = data.getString("item_name");
            mTaskListItemModel.name = name;
            if (mTaskListItemModel.mView != null) {
                EditText title = (EditText) mTaskListItemModel.mView.findViewById(R.id.title);
                title.setText(mTaskListItemModel.name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void updataListName(NetObject item,Context context) {
        try {

            String json = item.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return ;
            }

            JSONObject jsonObject = new JSONObject(item.result);
            JSONObject data = jsonObject.getJSONObject("data");
            TaskList mTaskList = (TaskList) item.item;
            String name = data.getString("name");
            mTaskList.name = name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static EditText praseListItemAdd(NetObject item,Context context) {
        String json = item.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return null;
        }
        EditText taskList = (EditText) item.item;
        return taskList;
    }

    public static boolean praseChange(NetObject item,Context context) {
        String json = item.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }

    public static boolean praseListfinish(NetObject item,Context context) {
        String json = item.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }
}
