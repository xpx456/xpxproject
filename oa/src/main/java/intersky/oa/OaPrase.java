package intersky.oa;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class OaPrase {

    public static int[] praseOaHit(NetObject net) {

        try {
            JSONObject mJSONObject;
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return null;
            int hit[] = new int[6];
            mJSONObject = new JSONObject(json);
            JSONObject ja = mJSONObject.getJSONObject("data");
            hit[0] = ja.getInt("Report");
            hit[1] = ja.getInt("Leave");
            hit[2] = ja.getInt("Schedule");
            hit[3] = ja.getInt("Notice");
            hit[4] = ja.getInt("Vote");
            hit[5] = ja.getInt("Task");
            return hit;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
