package intersky.function.handler;

import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.asks.FunAsks;
import intersky.function.prase.FunctionPrase;
import intersky.oa.OaAsks;
import intersky.oa.OaPrase;
import intersky.xpxnet.net.NetObject;

//04
public class FunHitHandler extends Handler {
    public Handler handler;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case FunAsks.GET_HIT_SUCCESS:
                FunctionPrase.updataWarmWorkHint((NetObject) msg.obj);
                AppUtils.sendSampleBroadCast(FunctionUtils.getInstance().context,FunctionUtils.ACTION_GET_BASE_HIT_FINISH);
                break;
            case OaAsks.EVENT_GET_OAHIT_SUCCESS:
                FunctionPrase.updateOaHit(OaPrase.praseOaHit((NetObject) msg.obj));
                AppUtils.sendSampleBroadCast(FunctionUtils.getInstance().context,FunctionUtils.ACTION_GET_OA_HIT_FINISH);
                break;
        }

    }
}
