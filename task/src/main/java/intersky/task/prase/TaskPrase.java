package intersky.task.prase;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.oa.OaUtils;
import intersky.select.SelectManager;
import intersky.select.entity.CustomSelect;
import intersky.select.entity.Select;
import intersky.task.TaskManager;
import intersky.task.asks.RagenAsks;
import intersky.task.entity.Contral;
import intersky.task.entity.Project;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class TaskPrase {

    public static void praseTaskHint(NetObject net) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }

            JSONObject jObject = new JSONObject(json);
            JSONObject data = jObject.getJSONObject("data");
            TaskManager.getInstance().taskHit = data.getInt("my_task_count");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean prasePraentTask(NetObject net, Context context,ArrayList<CustomSelect> tasks) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return true;
        }
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length() - 1; i++) {
                XpxJSONObject jo = data.getJSONObject(i);
                Task mTask = new Task();
                mTask.userId = jo.getString("user_id");
                mTask.taskId = jo.getString("task_id");
                mTask.taskName = jo.getString("name");
                mTask.parentId = jo.getString("parent_id");
                mTask.parentIdList = jo.getString("parent_list");
                mTask.leaderId = jo.getString("leader_id");
                mTask.senderIdList = jo.getString("sender_id");
                mTask.des = jo.getString("description");
                mTask.projectId = jo.getString("project_id");
                mTask.isComplete = jo.getInt("is_complete",0);
                mTask.isLocked = jo.getInt("is_locked",0);
                mTask.isStar = jo.getInt("is_star",0);
                mTask.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
                mTask.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
                mTask.stageId = jo.getString("project_stages_id");
                mTask.tag = jo.getString("tag");
                mTask.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));
                if (jo.has("has_one_notice")) {
                    XpxJSONObject jo3 = jo.getJSONObject("has_one_notice");
                    if (jo3.getString("sender_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                        if (jo3.getInt("is_read",0) == 1)
                            mTask.isread = true;
                    }
                }
                XpxJSONArray ja;
                if(jo.has("has_task_list_item"))
                {
                    ja = jo.getJSONArray("has_task_list_item");
                    if (ja.length() > 0) {
                        for (int j = 0; j < ja.length(); j++) {
                            XpxJSONObject jo2 = ja.getJSONObject(j);
                            if (jo2.getInt("is_complete",0) == 1) {
                                mTask.listfinish += 1;
                            }
                            mTask.listcount += 1;

                        }
                    }
                }

                if(jo.has("has_task"))
                {
                    ja = jo.getJSONArray("has_task");
                    if (ja.length() > 0) {
                        for (int j = 0; j < ja.length(); j++) {
                            XpxJSONObject jo2 = ja.getJSONObject(j);
                            if (jo2.getInt("is_complete",0) == 1) {

                                mTask.taskfinish += 1;
                            }
                            mTask.taskcount += 1;
                        }
                    }
                }
                CustomSelect customSelect = new CustomSelect(mTask.taskId,mTask.taskName,mTask);
                tasks.add(customSelect);
            }
            XpxJSONObject jo = data.getJSONObject(data.length() - 1);
            if (jo.getInt("total_results",0) == TaskManager.getInstance().mTasks.size()) {
                return true;
            }
            else
            {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void praseTask(NetObject net, Context context) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String keyword = (String) net.item;
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length() - 1; i++) {
                XpxJSONObject jo = data.getJSONObject(i);
                Task mTask = new Task();
                mTask.userId = jo.getString("user_id");
                mTask.taskId = jo.getString("task_id");
                mTask.taskName = jo.getString("name");
                mTask.parentId = jo.getString("parent_id");
                mTask.parentIdList = jo.getString("parent_list");
                mTask.leaderId = jo.getString("leader_id");
                mTask.senderIdList = jo.getString("sender_id");
                mTask.des = jo.getString("description");
                mTask.projectId = jo.getString("project_id");
                mTask.isComplete = jo.getInt("is_complete",0);
                mTask.isLocked = jo.getInt("is_locked",0);
                mTask.isStar = jo.getInt("is_star",0);
                mTask.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
                mTask.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
                mTask.stageId = jo.getString("project_stages_id");
                mTask.tag = jo.getString("tag");
                mTask.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));
                if (jo.has("has_one_notice")) {
                    XpxJSONObject jo3 = jo.getJSONObject("has_one_notice");
                    if (jo3.getString("sender_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                        if (jo3.getInt("is_read",0) == 1)
                            mTask.isread = true;
                    }
                }
                XpxJSONArray ja;
                if(jo.has("has_task_list_item"))
                {
                    ja = jo.getJSONArray("has_task_list_item");
                    if (ja.length() > 0) {
                        for (int j = 0; j < ja.length(); j++) {
                            XpxJSONObject jo2 = ja.getJSONObject(j);
                            if (jo2.getInt("is_complete",0) == 1) {
                                mTask.listfinish += 1;
                            }
                            mTask.listcount += 1;

                        }
                    }
                }

                if(jo.has("has_task"))
                {
                    ja = jo.getJSONArray("has_task");
                    if (ja.length() > 0) {
                        for (int j = 0; j < ja.length(); j++) {
                            XpxJSONObject jo2 = ja.getJSONObject(j);
                            if (jo2.getInt("is_complete",0) == 1) {

                                mTask.taskfinish += 1;
                            }
                            mTask.taskcount += 1;
                        }
                    }
                }
                if(keyword.length() == 0)
                TaskManager.getInstance().mTasks.add(mTask);
                else
                    TaskManager.getInstance().mSearchTasks.add(mTask);
            }
            if(keyword.length() == 0)
            TaskManager.getInstance().taskPage++;
            else
                TaskManager.getInstance().taskSearchPage++;
            XpxJSONObject jo = data.getJSONObject(data.length() - 1);
            if(keyword.length() == 0)
            {
                if (jo.getInt("total_results",0) == TaskManager.getInstance().mTasks.size()) {
                    TaskManager.getInstance().taskAll = true;
                }
            }
            else
            {
                if (jo.getInt("total_results",0) == TaskManager.getInstance().mSearchTasks.size()) {
                    TaskManager.getInstance().taskSearchAll = true;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseTask(NetObject net, Context context, Project mProject, HashMap<String,Task> expend) {
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

            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = jsonObject.getJSONArray("data");
            mProject.mTaskList = "";
            HashMap<String,ArrayList<Task>> tasktemp = new HashMap<String,ArrayList<Task>>();
            for (int i = 0; i < data.length() - 1; i++) {
                XpxJSONObject jo = data.getJSONObject(i);
                Task mTaskItemModel = new Task();
                mTaskItemModel.userId = jo.getString("user_id");
                mTaskItemModel.taskId = jo.getString("task_id");
                mTaskItemModel.taskName = jo.getString("name");
                mTaskItemModel.parentId = jo.getString("parent_id");
                mTaskItemModel.parentIdList = jo.getString("parent_list");
                mTaskItemModel.leaderId = jo.getString("leader_id");
                mTaskItemModel.senderIdList = jo.getString("sender_id");
                mTaskItemModel.des = jo.getString("description");
                mTaskItemModel.projectId = jo.getString("project_id");
                mTaskItemModel.isComplete = jo.getInt("is_complete",0);
                mTaskItemModel.isLocked = jo.getInt("is_locked",0);
                mTaskItemModel.isStar = jo.getInt("is_star",0);
                mTaskItemModel.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
                mTaskItemModel.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
                mTaskItemModel.stageId = jo.getString("project_stages_id");
                mTaskItemModel.tag = jo.getString("tag");
                mTaskItemModel.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));

                if(expend.containsKey(mTaskItemModel.taskId))
                {
                    mTaskItemModel.expend = true;
                }

                if (mProject.mTaskList.length() == 0) {
                    mProject.mTaskList += mTaskItemModel.taskId;
                } else {
                    mProject.mTaskList += "," + mTaskItemModel.taskId;
                }
                XpxJSONObject jo3 = jo.getJSONObject("has_one_notice");
                if (jo3.getString("sender_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    if (jo3.getInt("is_read",0) == 1)
                        mTaskItemModel.isread = true;
                }
                XpxJSONArray ja = jo.getJSONArray("has_task_list_item");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        XpxJSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete",0) == 1) {
                            mTaskItemModel.listfinish += 1;
                        }
                        mTaskItemModel.listcount += 1;
                    }
                }
                ja = jo.getJSONArray("has_task");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        XpxJSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete",0) == 1) {
                            mTaskItemModel.taskfinish += 1;
                        }
                        mTaskItemModel.taskcount += 1;
                    }
                }
                Stage stage = mProject.mStageHashs.get(mTaskItemModel.stageId);
                if(mTaskItemModel.parentId.equals("0"))
                {
                    stage.mTasks.add(mTaskItemModel);
                    stage.mTaskHashs.put(mTaskItemModel.taskId,mTaskItemModel);
                    ArrayList<Task> tasks = null;
                    if(tasktemp.containsKey(mTaskItemModel.taskId))
                    {
                        tasks = tasktemp.get(mTaskItemModel.taskId);
                        mTaskItemModel.tasks.addAll(tasks);
                    }
                }
                else
                {
                    praseStageTaskSon(tasktemp,stage,mTaskItemModel);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseStageTaskSon(HashMap<String,ArrayList<Task>> tasktemp ,Stage stage,Task task) {
        if(stage.mTaskHashs.containsKey(task.parentId)) {
            Task task1 = stage.mTaskHashs.get(task.parentId);
            task1.tasks.add(task);
        }
        else
        {
            ArrayList<Task> tasks = null;
            if(tasktemp.containsKey(task.parentId))
            {
                tasks = tasktemp.get(task.parentId);
            }
            if(tasks == null)
            {
                tasks = new ArrayList<Task>();
            }
            tasks.add(task);
            tasktemp.put(task.parentId,tasks);
        }
    }

    public static Task praseTaskDetial(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return null;
        }
        Task mTask = (Task) net.item;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            mTask.userId = jo.getString("user_id");
            mTask.taskId = jo.getString("task_id");
            if(!jo.isNull("name"))
                mTask.taskName = jo.getString("name");
            mTask.parentId = jo.getString("parent_id");
            mTask.parentIdList = jo.getString("parent_list");
            mTask.leaderId = jo.getString("leader_id");
            mTask.senderIdList = jo.getString("sender_id");
            if(!jo.isNull("description"))
                mTask.des = jo.getString("description");
            mTask.projectId = jo.getString("project_id");
            mTask.isComplete = jo.getInt("is_complete");
            mTask.isLocked = jo.getInt("is_locked");
            mTask.isStar = jo.getInt("is_star");
            mTask.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
            mTask.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
            mTask.stageId = jo.getString("project_stages_id");
            if(!jo.isNull("tag"))
                mTask.tag = jo.getString("tag");
            mTask.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));
            if(jo.has("has_one_notice"))
            {
                JSONObject jo3 = jo.getJSONObject("has_one_notice");
                if (jo3.getString("sender_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    if (jo3.getInt("is_read") == 1)
                        mTask.isread = true;
                }
            }
            JSONArray ja;
            if(jo.has("has_task_list_item"))
            {
                ja = jo.getJSONArray("has_task_list_item");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete") == 1) {
                            mTask.listfinish += 1;
                        }
                        mTask.listcount += 1;

                    }
                }
            }

            if(jo.has("has_task"))
            {
                ja = jo.getJSONArray("has_task");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete") == 1) {

                            mTask.taskfinish += 1;
                        }
                        mTask.taskcount += 1;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mTask;
    }

    public static boolean praseTaskDetial2(NetObject net, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        Task mTask = (Task) net.item;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            mTask.userId = jo.getString("user_id");
            mTask.taskId = jo.getString("task_id");
            if(!jo.isNull("name"))
                mTask.taskName = jo.getString("name");
            mTask.parentId = jo.getString("parent_id");
            mTask.parentIdList = jo.getString("parent_list");
            mTask.leaderId = jo.getString("leader_id");
            mTask.senderIdList = jo.getString("sender_id");
            if(!jo.isNull("description"))
                mTask.des = jo.getString("description");
            mTask.projectId = jo.getString("project_id");
            mTask.isComplete = jo.getInt("is_complete");
            mTask.isLocked = jo.getInt("is_locked");
            mTask.isStar = jo.getInt("is_star");
            mTask.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
            mTask.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
            mTask.stageId = jo.getString("project_stages_id");
            if(!jo.isNull("tag"))
                mTask.tag = jo.getString("tag");
            mTask.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));
            if(jo.has("has_one_notice"))
            {
                JSONObject jo3 = jo.getJSONObject("has_one_notice");
                if (jo3.getString("sender_id").equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)) {
                    if (jo3.getInt("is_read") == 1)
                        mTask.isread = true;
                }
            }
            JSONArray ja;
            if(jo.has("has_task_list_item"))
            {
                ja = jo.getJSONArray("has_task_list_item");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete") == 1) {
                            mTask.listfinish += 1;
                        }
                        mTask.listcount += 1;

                    }
                }
            }

            if(jo.has("has_task"))
            {
                ja = jo.getJSONArray("has_task");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete") == 1) {

                            mTask.taskfinish += 1;
                        }
                        mTask.taskcount += 1;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static HashMap<String,Task> praseSon(NetObject net, Context context) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return null;
            }
            Task mTask = (Task) net.item;

            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = jsonObject.getJSONArray("data");
            mTask.sonJson = json;
            mTask.tasks.clear();
            HashMap<String,Task> hashMap = new HashMap<String,Task>();
            for (int i = 0; i < data.length(); i++) {
                XpxJSONObject jo = data.getJSONObject(i);
                Task mTaskItemModel = new Task();
                mTaskItemModel.taskId = jo.getString("task_id");
                mTaskItemModel.taskName = jo.getString("name");
                mTaskItemModel.parentId = jo.getString("parent_id");
                mTaskItemModel.parentIdList = jo.getString("parent_list");
                mTaskItemModel.leaderId = jo.getString("leader_id");
                mTaskItemModel.senderIdList = jo.getString("sender_id");
                mTaskItemModel.des = jo.getString("description");
                mTaskItemModel.projectId = jo.getString("project_id");
                mTaskItemModel.stageId = jo.getString("project_stages_id");
                mTaskItemModel.isComplete = jo.getInt("is_complete",0);
                mTaskItemModel.isLocked = jo.getInt("is_locked",0);
                mTaskItemModel.isStar = jo.getInt("is_star",0);
                mTaskItemModel.beginTime = TimeUtils.stampToDate(jo.getString("begin_time"));
                mTaskItemModel.endTime = TimeUtils.stampToDate(jo.getString("end_time"));
                mTaskItemModel.completeTime = TimeUtils.stampToDate(jo.getString("complete_time"));
                mTaskItemModel.tag = jo.getString("tag");
                XpxJSONArray ja = jo.getJSONArray("has_task");
                if (ja.length() > 0) {
                    for (int j = 0; j < ja.length(); j++) {
                        XpxJSONObject jo2 = ja.getJSONObject(j);
                        if (jo2.getInt("is_complete",0) == 1) {
                            mTaskItemModel.taskfinish += 1;
                        }
                        mTaskItemModel.taskcount += 1;
                    }
                }

//                if (mTaskItemModel.isComplete == 0) {
//                    mTask.tasks.add(mTaskItemModel);
//                } else {
//
//                }
                mTask.tasks.add(mTaskItemModel);
                hashMap.put(mTaskItemModel.taskId,mTaskItemModel);
            }
            return hashMap;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void praseContral(NetObject net, Context context, Handler handler) {
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
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject tiems = data.getJSONObject("project_item");
            JSONArray itemarray = tiems.getJSONArray("has_many_item");
            JSONArray itemdata = data.getJSONArray("project_value");
            for (int i = 0; i < itemarray.length(); i++) {
                JSONObject jo = itemarray.getJSONObject(i);
                Contral Contral = new Contral();
                Contral.id = jo.getString("project_item_id");
                Contral.taskid = mTask.taskId;
                if(!jo.isNull("name"))
                    Contral.name = jo.getString("name");
                Contral.defult = jo.getString("placeholder");
                Contral.type = jo.getString("type");
                Contral.drawtype = TaskManager.getInstance().getDrawType(Contral.type);
                if (Contral.drawtype == TaskManager.CONTRAL_DRAW_TYPE_RANK) {
                    Contral.value = "0";
                }

                if (Contral.type.equals(TaskManager.CONTRAL_NUMBER) || Contral.type.equals(TaskManager.CONTRAL_AMOUNT)) {
                    JSONObject value = jo.getJSONObject("value");
                    Contral.unit = value.getString("unit");
                    Contral.point = value.getInt("point");
                } else if (Contral.type.equals(TaskManager.CONTRAL_SELECT) || Contral.type.equals(TaskManager.CONTRAL_OPTION)) {
                    JSONArray ja = jo.getJSONArray("has_many_config");
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject opt = ja.getJSONObject(j);
                        Select mSelectMore = new Select(opt.getString("project_config_id"), opt.getString("name"));
                        Contral.mSelects.add(mSelectMore);
                    }
                    JSONObject value = jo.getJSONObject("value");
                    JSONArray ja2 = value.getJSONArray("options");
                    if (Contral.type.equals(TaskManager.CONTRAL_OPTION)) {
                        Contral.isradio = value.getInt("is_radio");
                    }
                } else if (Contral.type.equals(TaskManager.CONTRAL_AREA) || Contral.type.equals(TaskManager.CONTRAL_CARD)
                        || Contral.type.equals(TaskManager.CONTRAL_DATE) || Contral.type.equals(TaskManager.CONTRAL_RANK)) {
                    JSONObject value = jo.getJSONObject("value");
                    if (Contral.type.equals(TaskManager.CONTRAL_AREA)) {
                        Contral.dayeType = value.getInt("area_type");
                        if (Contral.dayeType == 3) {
                            Contral.listcount = 1;
                        } else if (Contral.dayeType == 2) {
                            Contral.listcount = 2;
                        } else {
                            Contral.listcount = 3;
                        }
                    } else if (Contral.type.equals(TaskManager.CONTRAL_RANK)) {
                        Contral.dayeType = value.getInt("rank_type");
                    } else if (Contral.type.equals(TaskManager.CONTRAL_CARD)) {
                        Contral.dayeType = value.getInt("card_type");
                    } else if (Contral.type.equals(TaskManager.CONTRAL_DATE)) {
                        Contral.dayeType = value.getInt("is_time");
                    }
                }
                for (int k = 0; k < itemdata.length(); k++) {
                    JSONObject jo4 = itemdata.getJSONObject(k);
                    if (Contral.id.equals(jo4.getString("project_item_id"))) {
                        if (Contral.drawtype == TaskManager.CONTRAL_DRAW_TYPE_EDITTEXT || Contral.drawtype == TaskManager.CONTRAL_DRAW_TYPE_DATAPICK) {
                            if (Contral.type.equals(TaskManager.CONTRAL_DATE)) {
                                if (Contral.dayeType == 0) {
                                    Contral.value = TimeUtils.stampToDate(jo4.getString("value")).substring(0, 10);
                                } else {
                                    Contral.value = TimeUtils.stampToDate(jo4.getString("value")).substring(0, 16);
                                }
                            } else {
                                Contral.value = jo4.getString("value");
                            }
                        } else if (Contral.drawtype == TaskManager.CONTRAL_DRAW_TYPE_SELECT) {
                            String ids = jo4.getString("value");
                            if (!Contral.type.equals(TaskManager.CONTRAL_AREA)) {
                                for (int n = 0; n < Contral.mSelects.size(); n++) {
                                    if (ids.contains(Contral.mSelects.get(n).mId)) {
                                        Contral.lastItems.add(Contral.mSelects.get(n));
                                        if (Contral.value.length() == 0) {
                                            Contral.value += Contral.mSelects.get(n).mName;
                                        } else {
                                            Contral.value += "," + Contral.mSelects.get(n).mName;
                                        }
                                    }
                                }
                            } else {
                                RagenAsks.setLastRagenName(context,handler,Contral,ids);
                            }
                        } else if (Contral.drawtype == TaskManager.CONTRAL_DRAW_TYPE_ATTACHMENT) {
                            String ids = jo4.getString("value");
                            ids = ids.replaceAll("&quot;", "\"");
                            Contral.value = ids;
                            JSONArray jsonArray = new JSONArray(ids);
                            for (int n = 0; n < jsonArray.length(); n++) {
                                JSONObject object = jsonArray.getJSONObject(n);
                                Attachment mAttachmentModel = new Attachment(object.getString("hash"),object.getString( "name")
                                        ,Bus.callData(context,"filetools/getfilePath","/task")+"/"+object.getString("hash")+"."+object.getString("name").substring(object.getString("name").lastIndexOf("."), object.getString("name").length())
                                        , TaskManager.getInstance().oaUtils.praseClodAttchmentUrl(object.getString("hash")),0,0,"");
                                mAttachmentModel.mPath2 = Bus.callData(context,"filetools/getfilePath", "/task")+"/"+object.getString("hash")+"."+object.getString("name").substring(object.getString("name").lastIndexOf("."), object.getString("name").length());
                                mAttachmentModel.taskattid = object.getString("task_attence_id");
                                mAttachmentModel.taskuserid = object.getString("user_id");
                                Contral.mPics.add(mAttachmentModel);
                            }
                        } else if (Contral.drawtype == TaskManager.CONTRAL_DRAW_TYPE_RANK) {
                            String ids = jo4.getString("value");
                            Contral.value = ids;


                        }
                    }

                }
//                addContralView(Contral);
                mTask.mContrals.add(Contral);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseDes(NetObject net,Task task,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String data = (String) net.item;
        task.des = data;
    }

    public static void praseName(NetObject net,Task task,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String data = (String) net.item;
        task.taskName = data;
    }


    public static void praseBegin(NetObject net,Task task,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String data = (String) net.item;
        task.beginTime = data;
    }

    public static void praseEnd(NetObject net,Task task,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        String data = (String) net.item;
        task.endTime = data;
    }

    public static void praseTag(NetObject net, Task task, ArrayList<Select> selects, Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        ArrayList<Select> data = (ArrayList<Select>) net.item;
        String text = "";
        String text1 = "";
        for (int i = 0; i < data.size(); i++) {
            selects.get(i).iselect = SelectManager.getInstance().mSelects.get(i).iselect;
            if (selects.get(i).iselect == true) {
                if (text.length() == 0) {
                    text += selects.get(i).mId;
                    text1 += selects.get(i).mName;
                } else {
                    text += "," + selects.get(i).mId;
                    text1 += "," + selects.get(i).mName;
                }
            }
        }
        task.tag = text;
    }

    public static void praseLock(NetObject net,Task task,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        int data = (int) net.item;
        task.isLocked = data;
    }

    public static void praseStare(NetObject net,Task task,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        int data = (int) net.item;
        task.isStar = data;
    }

    public static boolean praseDelete(NetObject net,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return  true;
    }

    public static boolean praseExist(NetObject net,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return  true;
    }

    public static int praseReplyDelete(NetObject net,Context context,ArrayList<Reply> replies) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return -1;
        }

        Reply mreply = (Reply) net.item;
        int pos = replies.indexOf(mreply);
        replies.remove(mreply);
        return  pos;
    }

    public static Reply prasseReply(NetObject net, Context context, ArrayList<Reply> replies) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return null;
            }
            JSONObject object = new JSONObject(json);
            JSONObject jo2 = object.getJSONObject("data");
            Reply mReplyModel = new Reply(jo2.getString("task_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), jo2.getString("task_id"), jo2.getString("create_time"));
            replies.add(0, mReplyModel);
            return mReplyModel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean praseFinish(NetObject net,Context context,int finish) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        Task task = (Task) net.item;
        task.isComplete = finish;
        return true;
    }

    public static boolean praseContralAttachment(NetObject net,Context context,Contral contral) {

        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return false;
            }

            JSONArray updata = new JSONArray();
            String path = "";
            for(int i = 0 ; i < contral.mPics.size() ; i++)
            {
                JSONObject jo2 = new JSONObject();
                jo2.put("user_id",contral.mPics.get(i).taskuserid);
                jo2.put("hash",contral.mPics.get(i).mRecordid);
                jo2.put("task_attence_id",contral.mPics.get(i).taskattid);
                jo2.put("name",contral.mPics.get(i).mRecordid);
                updata.put(jo2);
            }
            if (json.length() > 0) {
                JSONObject mjson = null;
                mjson = new JSONObject(json);
                JSONArray data = mjson.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jo = data.getJSONObject(i);
                    JSONObject jo2 = new JSONObject();
                    jo2.put("user_id",jo.getString("user_id"));
                    jo2.put("hash",jo.getString("hash"));
                    jo2.put("task_attence_id",jo.getString("task_attence_id"));
                    jo2.put("name",jo.getString("name"));
                    updata.put(jo2);
                }
            }
            contral.testvalue = updata.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return  false;
        }

        return true;
    }

    public static Attachment praseContralAttachmentDel(NetObject net,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return null;
        }
        Attachment attachment = (Attachment) net.item;
        return attachment;
    }


    public static Contral praseContralSet(NetObject net,Context context) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return null;
        }
        Contral contral = (Contral) net.item;
        return contral;
    }

    public static Project praseTaskProject(NetObject net,Context context,Task task) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return null;
        }
        Project project = (Project) net.item;
        task.projectId = project.projectId;
        return project;
    }

    public static Task praseTaskParent(NetObject net,Context context,Task task) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return null;
        }
        Task parent = (Task) net.item;
        task.parentId = parent.taskId;
        return task;
    }

    public static Task praseAdd(NetObject net,Context context) {

        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(context,AppUtils.getfailmessage(json));
                return null;
            }
            Task task = (Task) net.item;
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject data = jsonObject.getJSONObject("data");
            task.taskId = data.getString("task_id");
            task.projectId = data.getString("project_id");
            task.stageId = data.getString("project_stages_id");
            return task;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
