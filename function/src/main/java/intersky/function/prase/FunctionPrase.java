package intersky.function.prase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.entity.Function;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class FunctionPrase {

    public static List<Function> praseFunctions(String json)
    {
        List<Function> funcs = new ArrayList<Function>();
        try {

            XpxJSONObject jObject = new XpxJSONObject(json);
            XpxJSONArray jArray = jObject.getJSONArray("boards");
            for (int i = 0; i < jArray.length(); i++) {
                if (jArray.isNull(i))
                    continue;
                XpxJSONObject ob = (XpxJSONObject) jArray.getJSONObject(i);
                Function func = new Function();
                func.mCaption = ob.getString("Caption");
                func.mCatalogueName = ob.getString("CatalogueName");
                func.hintCount = ob.getInt("HintCount",0);
                func.mName = ob.getString("Name");
                func.iconName = ob.getString("Icon");
                func.mRecordId = AppUtils.getguid();
                func.typeName = ob.getString("TypeName");
                if(func.typeName.length() == 0)
                {
                    func.typeName = Function.MIX;
                }
                funcs.add(func);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return funcs;
    }

    public static List<Function> praseWebapp(String json) {
        List<Function> funcs = new ArrayList<Function>();
        try {
            XpxJSONObject jObject = new XpxJSONObject(json);
            if (jObject.length() > 0) {
                XpxJSONArray jArray = jObject.getJSONArray("apps");
                for (int i = 0; i < jArray.length(); i++) {
                    if (jArray.isNull(i))
                        continue;

                    XpxJSONObject ob = (XpxJSONObject) jArray.getJSONObject(i);
                    Function func = new Function();


                    func.mCaption = ob.getString("name");
                    func.mCatalogueName = ob.getString("grop");
                    func.hintCount = ob.getInt("HintCount",0);
                    func.mName = ob.getString("url");
                    func.iconName = ob.getString("icon");
                    func.mRecordId = AppUtils.getguid();
                    func.typeName = "WEB";
                    func.mRecordId = AppUtils.getguid();
                    funcs.add(func);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return funcs;
    }

    public static void updataWarmWorkHint(NetObject net) {
        int count = 0;
        int count1 = 0;
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject jsonboj = new XpxJSONObject(json);
            count1 = jsonboj.getInt("reminder",0);
            count = jsonboj.getInt("workflow",0);
            FunctionUtils.getInstance().mFunctions.get(0).hintCount = count1;
            FunctionUtils.getInstance().mFunctions.get(1).hintCount = count;
        } catch (JSONException e) {

        }

    }

    public static void updateOaHit(int[] hits) {
        if(hits != null)
        {
            ArrayList<Function> mfuns = FunctionUtils.getInstance().mFunctionGrids.get("oa");
            mfuns.get(0).hintCount = hits[0];
            mfuns.get(2).hintCount = hits[1];
            mfuns.get(3).hintCount = hits[2];
            mfuns.get(5).hintCount = hits[3];
            mfuns.get(6).hintCount = hits[4];
            mfuns.get(7).hintCount = hits[5];
        }
        else
        {
            ArrayList<Function> mfuns = FunctionUtils.getInstance().mFunctionGrids.get("oa");
            mfuns.get(0).hintCount = 0;
            mfuns.get(2).hintCount = 0;
            mfuns.get(3).hintCount = 0;
            mfuns.get(5).hintCount = 0;
            mfuns.get(6).hintCount = 0;
            mfuns.get(7).hintCount = 0;
        }
    }

}
