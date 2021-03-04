package intersky.document.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Iterator;

import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.entity.DocumentNet;
import intersky.document.prase.DocumentPrase;
import intersky.document.view.activity.DocumentActivity;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//00
public class DocumentHandler extends Handler {

    public static final int EVENT_UPDATA_DOCUMENT_LIST = 3170000;
    public static final int EVENT_UPDATA_LOAD_LIST = 3170001;
    public static final int EVENT_SHOW_DOCUMENT_EDIT = 3170002;
    public static final int EVENT_HIT_DOCUMENT_EDIT = 3170003;
    public static final int EVENT_SHOW_DOWNLOAD_EDIT = 3170004;
    public static final int EVENT_HIT_DOWNLOAD_EDIT = 3170005;
    public static final int EVENT_SHOW_UPLOAD_EDIT = 3170006;
    public static final int EVENT_HIT_UPLOAD_EDIT = 3170007;
    public static final int EVENT_SET_UPLOAD_FILES = 3170008;
    public static final int EVENT_DOWNLOAD_FAIL = 317009;
    public static final int EVENT_UPLOAD_FAIL = 3170010;
    public static final int EVENT_DOCUMENT_UPDATA = 3170011;
    public DocumentActivity theActivity;

    public DocumentHandler(DocumentActivity mDocumentActivity) {
        theActivity = mDocumentActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        NetObject object;
        switch (msg.what) {
            case DocumentAsks.DOCUMENT_PRE_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                object = (NetObject) msg.obj;
                DocumentManager.getInstance().pathList.add((DocumentNet) object.item);
                DocumentPrase.praseDocumentList(theActivity,object.result, (DocumentNet) object.item);
                theActivity.mDocumentPresenter.updataDocumentList();
                break;
            case DocumentAsks.DOCUMENT_BACK_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                object = (NetObject) msg.obj;
                int index = DocumentManager.getInstance().pathList.indexOf(object.item);
                for(int i = index+1 ; i < DocumentManager.getInstance().pathList.size() ; i++)
                {
                    DocumentManager.getInstance().pathList.remove(i);
                    i--;
                }
                DocumentPrase.praseDocumentList(theActivity,object.result, (DocumentNet) object.item);
                theActivity.mDocumentPresenter.updataDocumentList();
                break;
            case DocumentAsks.DOCUMENT_UPDATE_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                object = (NetObject) msg.obj;
                DocumentPrase.praseDocumentList(theActivity,object.result, (DocumentNet) object.item);
                theActivity.mDocumentPresenter.updataDocumentList();
                break;
            case DocumentAsks.DOCUMENT_SEARCH_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                object = (NetObject) msg.obj;
                DocumentPrase.praseSearchDocumentList(object.result, (DocumentNet) object.item);
                theActivity.mDocumentPresenter.updataDocumentList();
                break;
            case DocumentAsks.DOCUMENT_DELETE_LIST_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mDocumentPresenter.doUpdate();
                break;
            case DocumentAsks.DOCUMENT_DELETE_LIST_FAIL:
                break;
            case DocumentAsks.DOCUMENT_RENAME_SUCCESS:
                theActivity.waitDialog.hide();
                theActivity.mDocumentPresenter.doUpdate();
                break;
            case EVENT_UPDATA_DOCUMENT_LIST:
                theActivity.mDocumentPresenter.updataDocumentList();
                break;
            case EVENT_UPDATA_LOAD_LIST:
                theActivity.mDocumentPresenter.updataLoadList();
                break;
            case EVENT_SHOW_DOCUMENT_EDIT:
                theActivity.mDocumentPresenter.showEdit();
                break;
            case EVENT_HIT_DOCUMENT_EDIT:
                theActivity.mDocumentPresenter.hidEdit();
                break;
            case EVENT_SHOW_DOWNLOAD_EDIT:
            case EVENT_SHOW_UPLOAD_EDIT:
                theActivity.mDocumentPresenter.showLoadEdit();
                break;
            case EVENT_HIT_DOWNLOAD_EDIT:
            case EVENT_HIT_UPLOAD_EDIT:
                theActivity.mDocumentPresenter.hideLoadEdit();
                break;
            case EVENT_SET_UPLOAD_FILES:
                theActivity.mDocumentPresenter.setUploadFiles((Intent) msg.obj);
                break;
            case EVENT_DOWNLOAD_FAIL:
            case EVENT_UPLOAD_FAIL:
                intent = (Intent) msg.obj;
                AppUtils.showMessage(theActivity,intent.getStringExtra("msg"));
                break;
            case EVENT_DOCUMENT_UPDATA:
                //theActivity.mDocumentPresenter.doUpdate2();
                break;
        }

    }
}