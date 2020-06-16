package intersky.document.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.entity.DMessage;
import intersky.document.entity.DocumentNet;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;

public class DownUpHandler extends Handler {

    public static final int EVETN_DOWNLOAD_UPADTE = 3170100;
    public static final int EVETN_DOWNLOAD_FINISH = 3170101;
    public static final int EVETN_DOWNLOAD_START = 3170102;
    public static final int EVETN_DOWNLOAD_FAIL = 3170103;
    public static final int EVETN_DOWNLOAD_ADD = 3170104;
    public static final int EVETN_DOWNLOAD_DELETE = 3170105;
    public static final int EVETN_DOWNLOAD_PAUSE = 3170106;
    public static final int EVETN_UPLOAD_UPADTE = 3170107;
    public static final int EVETN_UPLOAD_FINISH = 3170108;
    public static final int EVETN_UPLOAD_START = 3170109;
    public static final int EVETN_UPLOAD_FAIL = 3170110;
    public static final int EVETN_UPLOAD_ADD = 3170111;
    public static final int EVETN_UPLOAD_DELETE = 3170112;
    public static final int EVETN_UPLOAD_PAUSE = 3170113;
    public static final int EVETN_UPLOAD_START_NO = 3170114;
    @Override
    public void handleMessage(Message msg) {
        DocumentNet documentNet = null;
        switch (msg.what) {
            case EVETN_DOWNLOAD_UPADTE:
                DocumentManager.getInstance().updataDownload((DMessage) msg.obj);
                documentNet = ((DMessage) msg.obj).documentNet;
                if(documentNet.mSize != 0)
                {
                    int max = 100;
                    int p = (int) (documentNet.mFinishSize*100/documentNet.mSize);
                    DocumentManager.getInstance().notificationOperation.showProgress(DocumentManager.getInstance().context.getString(R.string.document_downloading)
                            ,documentNet.mName,max,p,"download");
                }
                else
                {
                    DocumentManager.getInstance().notificationOperation.showMesage(DocumentManager.getInstance().context.getString(R.string.document_downloading)
                            ,documentNet.mName,"download");
                }
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_DOWNLOAD_FINISH:
                DocumentManager.getInstance().setDownloadFinish((DocumentNet) msg.obj);
                documentNet = (DocumentNet) msg.obj;
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_UPDATA_DOCUMENT);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                DocumentManager.getInstance().notificationOperation.showMesage(DocumentManager.getInstance().context.getString(R.string.document_downloadfinish)
                        ,documentNet.mName,"download");
                break;
            case EVETN_DOWNLOAD_START:
                DocumentManager.getInstance().setDownloadStart((DocumentNet) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_DOWNLOAD_PAUSE:
                DocumentManager.getInstance().setDownloadPause((DocumentNet) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                DocumentManager.getInstance().notificationOperation.showCancle();
                break;
            case EVETN_DOWNLOAD_FAIL:
                ArrayList<DocumentNet> documentNets = new ArrayList<DocumentNet>();
                documentNet = (DocumentNet) msg.obj;
                documentNets.add(documentNet);
                DocumentManager.getInstance().deleteDownloads(documentNets);
                break;
            case EVETN_DOWNLOAD_ADD:
                DocumentManager.getInstance().addDownloads((ArrayList<DocumentNet>) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_DOWNLOAD_DELETE:
                DocumentManager.getInstance().deleteDownloads((ArrayList<DocumentNet>) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_UPLOAD_UPADTE:
                DocumentManager.getInstance().updataUpoad((DMessage) msg.obj);
                documentNet = ((DMessage) msg.obj).documentNet;
                if(documentNet.mSize != 0)
                {
                    int max = 100;
                    int p = (int) (documentNet.mFinishSize*100/documentNet.mSize);
                    DocumentManager.getInstance().notificationOperation.showProgress(DocumentManager.getInstance().context.getString(R.string.document_uploading)
                            ,documentNet.mName,max,p,"upload");
                }
                else
                {
                    DocumentManager.getInstance().notificationOperation.showMesage(DocumentManager.getInstance().context.getString(R.string.document_uploading)
                            ,documentNet.mName,"upload");
                }
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_UPLOAD_FINISH:
                DocumentNet documentNet1 = (DocumentNet) msg.obj;
                DocumentManager.getInstance().setUploadFinish((DocumentNet) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_UPDATA_DOCUMENT);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                DocumentManager.getInstance().notificationOperation.showMesage(DocumentManager.getInstance().context.getString(R.string.document_uploadfinish)
                        ,documentNet1.mName,"upload");
                break;
            case EVETN_UPLOAD_START:
                DocumentManager.getInstance().setUploadStart((DocumentNet) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_UPLOAD_PAUSE:
                DocumentManager.getInstance().setUploadPause((DocumentNet) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                DocumentManager.getInstance().notificationOperation.showCancle();
                break;
            case EVETN_UPLOAD_FAIL:
                ArrayList<DocumentNet> documentNets2 = new ArrayList<DocumentNet>();
                documentNet = (DocumentNet) msg.obj;
                documentNets2.add(documentNet);
                DocumentManager.getInstance().deleteUploads(documentNets2);
                Intent intent = new Intent(DocumentManager.ACTION_DOCUMENT_UPLOAD_FAIL);
                intent.putExtra("msg",DocumentManager.getInstance().context.getString(R.string.button_word_upload)+ documentNet.mName
                        +DocumentManager.getInstance().context.getString(R.string.error_fail));
                DocumentManager.getInstance().context.sendBroadcast(intent);
                break;
            case EVETN_UPLOAD_ADD:
                DocumentManager.getInstance().addUploads((ArrayList<DocumentNet>) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_UPLOAD_DELETE:
                DocumentManager.getInstance().deleteUploads((ArrayList<DocumentNet>) msg.obj);
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
            case EVETN_UPLOAD_START_NO:
                AppUtils.sendSampleBroadCast(DocumentManager.getInstance().context,DocumentManager.ACTION_DOCUMENT_UPADA_LOAD);
                break;
        }

    }
}
