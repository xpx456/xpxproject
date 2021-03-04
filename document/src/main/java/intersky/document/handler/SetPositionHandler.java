package intersky.document.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.entity.DocumentNet;
import intersky.document.prase.DocumentPrase;
import intersky.document.view.activity.SetPositionActivity;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;
//02
public class SetPositionHandler extends Handler {

    public SetPositionActivity theActivity;
    public static final int EVENT_UPDATE_DOCUMENT_LIST = 3170200;

    public SetPositionHandler(SetPositionActivity activity) {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        NetObject object;
        // AppUtils.dissMissDialog();
        if (theActivity != null) {
            switch (msg.what) {
                case DocumentAsks.DOCUMENT_UPDATE_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    object = (NetObject) msg.obj;
                    DocumentPrase.praseDocumentOnly(object.result, (DocumentNet) object.item,theActivity.mDocumentItems);
                    theActivity.mSampleDocumentAdapter.notifyDataSetChanged();
                    break;
                case DocumentAsks.DOCUMENT_PRE_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    object = (NetObject) msg.obj;
                    theActivity.mPaths.add((DocumentNet) object.item);
                    DocumentPrase.praseDocumentOnly(object.result, (DocumentNet) object.item,theActivity.mDocumentItems);
                    theActivity.mSelectFoladerListAdapter.notifyDataSetChanged();
                    theActivity.mSampleDocumentAdapter.notifyDataSetChanged();
                    break;
                case DocumentAsks.DOCUMENT_BACK_LIST_SUCCESS:
                    theActivity.waitDialog.hide();
                    object = (NetObject) msg.obj;
                    int index = theActivity.mPaths.indexOf(object.item);
                    for(int i = theActivity.mPaths.size()-1 ; i >= 0 ; i--) {
                        if(i == index)
                        {
                            break;
                        }
                        else
                        {
                            theActivity.mPaths.remove(i);
                        }
                    }
                    DocumentPrase.praseDocumentList(theActivity,object.result, (DocumentNet) object.item);
                    theActivity.mSelectFoladerListAdapter.notifyDataSetChanged();
                    theActivity.mSampleDocumentAdapter.notifyDataSetChanged();
                    break;
                case EVENT_UPDATE_DOCUMENT_LIST:
                    theActivity.waitDialog.show();
                    DocumentAsks.updataDocumentList(theActivity.mSetPositionPresenter.mSetPositionHandler,theActivity,theActivity.mPaths.get(theActivity.mPaths.size()-1));
                    break;
            }
            super.handleMessage(msg);
        }
    }
};
