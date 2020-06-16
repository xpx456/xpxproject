package intersky.task.prase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.task.TaskManager;
import intersky.task.entity.Project;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ProjectFilePrase {

    public static void parseFilsName(NetObject net)
    {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return ;
            }
            ArrayList<Project> files = (ArrayList<Project>) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                files.get(i).mName = jo.getString("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseRename(NetObject net)
    {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return ;
            }
            Project file = (Project) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            file.mName = jo.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Project praseDelete(NetObject net)
    {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            return null;
        }
        Project file = (Project) net.item;
        return file;
    }

    public static void initCreat(NetObject net) {

        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                return;
            }
            Project item = (Project) net.item;

            JSONObject jsonObject = new JSONObject(json);
            JSONObject jo = jsonObject.getJSONObject("data");
            Project head = new Project();
            head.mName = jo.getString("name");
            head.fileid = jo.getString("project_file_id");
            head.projects.add(item);
            head.type = 1;
            item.fileid = jo.getString("project_file_id");
            int a = TaskManager.getInstance().mProjects2.indexOf(item);
            boolean has = false;
            for(int i = 0 ; i < TaskManager.getInstance().mHeads.size() ; i++)
            {
                int pos = TaskManager.getInstance().mProjects2.indexOf(TaskManager.getInstance().mHeads.get(i).projects.get(0));
                if(pos > a)
                {
                    has = true;
                    TaskManager.getInstance().mHeads.add(i,head);
                    TaskManager.getInstance().mProjects.add(i,head);
                    break;
                }
            }
            if(has == false)
            {
                if(TaskManager.getInstance().mHeads.size() > 0)
                {
                    TaskManager.getInstance().mHeads.add(head);
                    TaskManager.getInstance().mProjects.add(TaskManager.getInstance().mHeads.size()-1,head);
                }
                else
                {
                    TaskManager.getInstance().mHeads.add(head);
                    TaskManager.getInstance().mProjects.add(0,head);
                }

            }

            TaskManager.getInstance().mProjects.remove(item);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Project praseMove(NetObject net)
    {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            return null;
        }
        Project file = (Project) net.item;
        return file;
    }
}
