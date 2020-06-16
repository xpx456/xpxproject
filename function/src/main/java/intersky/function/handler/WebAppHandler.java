package intersky.function.handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import intersky.function.view.activity.WebAppActivity;
//08
public class WebAppHandler extends Handler {
    WebAppActivity theActivity;

    public static final int ADD_PICTURE = 3180800;

    public WebAppHandler(WebAppActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if (theActivity != null) {
            switch (msg.what) {
                case WebAppActivity.SCANNIN_EVENT:
                    theActivity.mWebAppPresenter.startScan();
                    break;
                case WebAppActivity.SCANNIN_EVENT_WITH_JSON:
                    theActivity.json = (String) msg.obj;
                    theActivity.mWebAppPresenter.startScan();
                    break;
                case WebAppActivity.GET_PIC_PATH:
                    theActivity.mWebAppPresenter.doupload((String) msg.obj);
                    break;
                case WebAppActivity.EVENT_GET_INFO:
                    theActivity.mWebAppPresenter.getloginfo();
                    break;
                case WebAppActivity.SCANNIN_GREQUEST_CODE_WITH_JSON:
                    Intent data = (Intent) msg.obj;
                    Bundle bundle = data.getExtras();
                    theActivity.mWebAppPresenter.showCode2(bundle.getString("result"));
                    break;
                case ADD_PICTURE:
                    theActivity.mWebAppPresenter.setpic((Intent) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
