package intersky.function.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.function.asks.FunAsks;
import intersky.function.prase.FunPrase;
import intersky.function.view.activity.LabelActivity;
import intersky.xpxnet.net.NetObject;

//07
public class LabelHandler extends Handler {
    public LabelActivity theActivity;

    public LabelHandler(LabelActivity mLabelActivity) {
        theActivity = mLabelActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case FunAsks.BOARD_LABLE_DATA_SUCCESS:
                FunPrase.praseLable((NetObject) msg.obj,theActivity.mFunData);
                theActivity.mLabelPresenter.inidSerias();
                theActivity.waitDialog.hide();
                break;
        }

    }
}
