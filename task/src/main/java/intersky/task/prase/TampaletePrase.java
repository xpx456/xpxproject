package intersky.task.prase;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.task.R;
import intersky.task.entity.Template;
import intersky.task.entity.TemplateType;
import intersky.task.view.adapter.TaskTemplateAdapter;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class TampaletePrase {

    public static void praseTypes(NetObject net, Context context, ArrayList<TemplateType> templateTypes) {
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

            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                JSONObject jo = ja.getJSONObject(i);
                TemplateType temp = new TemplateType();
                temp.name = jo.getString("name");
                temp.typeid = jo.getString("template_type_id");
                temp.type = jo.getString("type");
                temp.mTaskTemplateAdapter = new TaskTemplateAdapter(context,temp.mTamplates);
                if(temp.type.equals("1"))
                {
                    Template template = new Template();
                    template.mId = "0";
                    template.mType = "1";
                    template.mTypeId = "0";
                    template.name = context.getString(R.string.task_template_empty);
                    temp.mTamplates.add(0,template);
                }
                templateTypes.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void praseemplate(Context context,NetObject net) {
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
            TemplateType mTemplateTypeModel = (TemplateType) net.item;
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Template templateModel = new Template();
                templateModel.mId = jo.getString("template_id");
                templateModel.name = jo.getString("name");
                templateModel.mType = mTemplateTypeModel.type;
                templateModel.mTypeId = jo.getString("template_type_id");
                templateModel.mImage = jo.getString("image");
                mTemplateTypeModel.mTamplates.add(templateModel);
            }
            mTemplateTypeModel.isall = true;
            mTemplateTypeModel.mTaskTemplateAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
