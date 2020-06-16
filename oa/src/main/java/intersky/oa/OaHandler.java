package intersky.oa;


import android.os.Handler;
import android.os.Message;

import intersky.xpxnet.net.NetObject;

public class OaHandler extends Handler {

    public static final int EVENT_UPLOAD_FILES = 3030000;
    public static final int EVENT_UPLOAD_FINISH= 3030001;
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_UPLOAD_FILES:
                OaDataItem oaDataItem = (OaDataItem) msg.obj;
                OaUtils.getOaUtils().doSendFiles(oaDataItem);
                break;
            case EVENT_UPLOAD_FINISH:
                OaUtils.getOaUtils().praseHash((NetObject) msg.obj);
                break;
            case OaAsks.EVENT_GET_ATTCHMENT_SUCCESS:
                OaUtils.getOaUtils().praseAddtchment((NetObject) msg.obj);
                break;

        }

    }

}
