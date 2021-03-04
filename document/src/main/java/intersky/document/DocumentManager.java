package intersky.document;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Commend;
import intersky.apputils.AppUtils;
import intersky.document.entity.DMessage;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.DownUpHandler;
import intersky.document.view.activity.DocumentActivity;
import intersky.document.view.activity.SetPositionActivity;
import intersky.appbase.entity.Account;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;

public class DocumentManager {

    public static final String ACTION_SELECT_UPLOADFILE_FINISH = "ACTION_SELECT_UPLOADFILE_FINISH";
    public static final String ACTION_CREAT_DOCUMENT_FINISH = "ACTION_CREAT_DOCUMENT_FINISH";
    public static final String ACTION_DOCUMENT_UPADA_LOAD = "ACTION_DOCUMENT_UPADA_LOAD";
    public static final String ACTION_DOCUMENT_UPLOAD_FAIL = "ACTION_DOCUMENT_UPADA_LOAD";
    public static final String ACTION_DOCUMENT_DOWNLOAD_FAIL = "ACTION_DOCUMENT_DOWNLOAD_FAIL";
    public static final String ACTION_UPDATA_DOCUMENT = "ACTION_UPDATA_DOCUMENT";
    public static final int NOTIFICTION_DOWNLOAD_ID = 1;
    public static final int NOTIFICTION_UPLOAD_ID = 2;
    public static final int NOTIFICTION_DOWNLOAD_TIP_ID = 3;
    public static final int NOTIFICTION_UPLOAD_TIP_ID = 4;
    public static final int STATE_FINISH = 3;
    public static final int STATA_DOWNLOADING = 2;
    public static final int STATA_START = 1;
    public static final int STATA_PAUSE = 0;

    public static final int TYPE_DOCUMENT = 30;
    public static final int TYPE_DOWNLOAD_NOMAL = 20;
    public static final int TYPE_DOWNLOAD_FINISH = 21;
    public static final int TYPE_DOWNLOAD_UNFINISH = 22;
    public static final int TYPE_UPLOAD_NOMAL = 10;
    public static final int TYPE_UPLOAD_FINISH = 11;
    public static final int TYPE_UPLOAD_UNFINISH = 12;
    public DownUpHandler mDownUpHandler = new DownUpHandler();
    public static volatile DocumentManager mDocumentManager;

    public boolean isEdit = false;
    public boolean showSearch = false;
    public DocumentNet mdownloadTitleUnFinish;
    public DocumentNet mdownloadTitleFinish;
    public DocumentDownloadThread mStartDownloadTasks;
    public DocumentUpLoadThread mStartUploadTasks;
    public DocumentNet muploadTitleUnFinish;
    public DocumentNet muploadTitleFinish;
    public ArrayList<DocumentNet> downloads = new ArrayList<DocumentNet>();
    public HashMap<String, DocumentNet> downloadsMap = new HashMap<String, DocumentNet>();
    public HashMap<String, DocumentNet> uploadssMap = new HashMap<String, DocumentNet>();
    public ArrayList<DocumentNet> uploads = new ArrayList<DocumentNet>();
    public ArrayList<DocumentNet> pathList = new ArrayList<DocumentNet>();
    public ArrayList<DocumentNet> fileList = new ArrayList<DocumentNet>();
    public ArrayList<DocumentNet> sfileList = new ArrayList<DocumentNet>();
    public ArrayList<DocumentNet> selectPathList = new ArrayList<DocumentNet>();
    public Account mAccount;
    public int fileSelectCount = 0;
    public int downSelectCount = 0;
    public int upSelectCount = 0;
    public Context context;
    public Service service;
    public NotificationOperation notificationOperation;

    public static DocumentManager init(Context context,NotificationOperation notificationOperation) {
        if (mDocumentManager == null) {
            synchronized (DocumentManager.class) {
                if (mDocumentManager == null) {
                    mDocumentManager = new DocumentManager(context,notificationOperation);

                }
                else
                {
                    mDocumentManager.context = context;
                    mDocumentManager.notificationOperation = notificationOperation;
                    DocumentNet mDocumentNet = new DocumentNet();
                    mDocumentNet.mName = context.getString(R.string.document_documentmanager);
                    mDocumentNet.mType = TYPE_DOCUMENT;
                    mDocumentManager.pathList.clear();
                    mDocumentManager.pathList.add(mDocumentNet);
                }
            }
        }
        return mDocumentManager;
    }

    public void setmAccount(Account account) {
        this.mAccount = account;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public static DocumentManager getInstance() {
        return mDocumentManager;
    }

    public DocumentManager(Context context, NotificationOperation notificationOperation) {
        this.context = context;
        this.notificationOperation = notificationOperation;
        DocumentNet mDocumentNet = new DocumentNet();
        mDocumentNet.mName = context.getString(R.string.document_documentmanager);
        mDocumentNet.mType = TYPE_DOCUMENT;
        pathList.add(mDocumentNet);
    }

    public void cleanAll() {
        isEdit = false;
        showSearch = false;
        mdownloadTitleUnFinish = null;
        mdownloadTitleFinish = null;
        mStartDownloadTasks = null;
        mStartUploadTasks = null;
        muploadTitleUnFinish = null;
        muploadTitleFinish = null;
        downloads.clear();
        downloadsMap.clear();
        uploadssMap.clear();
        uploads.clear();
        pathList.clear();
        fileList.clear();
        sfileList.clear();
        selectPathList.clear();
        DocumentNet mDocumentNet = new DocumentNet();
        mDocumentNet.mName = context.getString(R.string.document_documentmanager);
        mDocumentNet.mType = TYPE_DOCUMENT;
        mDocumentManager.pathList.clear();
        mDocumentManager.pathList.add(mDocumentNet);
    }

    public void startDocument()
    {
        cleanAll();
        DBHelper.getInstance(context).scanDocument();
        mDocumentManager.startDownloadFirst();
        mDocumentManager.startUploadFirst();
    }

    public void addDownloads(ArrayList<DocumentNet> documentNets) {
        for (int i = documentNets.size() - 1; i >= 0; i--) {
            DocumentNet mDocumentNet = documentNets.get(i);
            if (checkDownload(mDocumentNet) == false) {
                if (mdownloadTitleUnFinish == null) {
                    mdownloadTitleUnFinish = new DocumentNet();
                    mdownloadTitleUnFinish.mType = TYPE_DOWNLOAD_UNFINISH;
                    downloads.add(0, mdownloadTitleUnFinish);
                }
                mDocumentNet.mState = STATA_START;
                downloadsMap.put(mDocumentNet.mRecordID, mDocumentNet);
                DBHelper.getInstance(context).addDoucment(mDocumentNet);
                if (downloads.size() > 1) {
                    if (downloads.get(1).mType == TYPE_DOWNLOAD_NOMAL) {
                        if (downloads.get(1).mState == STATA_DOWNLOADING) {
                            downloads.add(2, mDocumentNet);

                        } else {
                            downloads.add(1, mDocumentNet);
                        }
                    } else {
                        downloads.add(1, mDocumentNet);
                    }
                } else {
                    downloads.add(1, mDocumentNet);
                }
                mdownloadTitleUnFinish.mState++;
            }
        }
        if (mdownloadTitleUnFinish != null) {
            startDownload();
        }
    }

    public void addUploads(ArrayList<DocumentNet> documentNets) {


        for (int i = documentNets.size() - 1; i >= 0; i--) {
            DocumentNet mDocumentNet = documentNets.get(i);
            if (checkUpload(mDocumentNet) == false) {
                if (muploadTitleUnFinish == null) {
                    muploadTitleUnFinish = new DocumentNet();
                    muploadTitleUnFinish.mType = TYPE_UPLOAD_UNFINISH;
                    uploads.add(0, muploadTitleUnFinish);
                }
                mDocumentNet.mState = STATA_START;
                uploadssMap.put(mDocumentNet.mName + mDocumentNet.mCatalogueID + mDocumentNet.mOwnerType, mDocumentNet);
                DBHelper.getInstance(context).addDoucment(mDocumentNet);
                if (uploads.size() > 1) {
                    if (uploads.get(1).mType == TYPE_UPLOAD_NOMAL) {
                        if (uploads.get(1).mState == STATA_DOWNLOADING) {
                            uploads.add(2, mDocumentNet);

                        } else {
                            uploads.add(1, mDocumentNet);
                        }
                    } else {
                        uploads.add(1, mDocumentNet);
                    }
                } else {
                    uploads.add(1, mDocumentNet);
                }
                muploadTitleUnFinish.mState++;
            }
        }
        if (muploadTitleUnFinish != null) {
            startUpload();
        }
    }

    public void setDownloadStart(DocumentNet mDocumentNet) {
        mDocumentNet.mState = STATA_START;
        if (mdownloadTitleUnFinish.mState > 1) {
            DocumentNet documentNet1 = downloads.get(1);
            downloads.remove(mDocumentNet);
            if (documentNet1.mState == STATA_DOWNLOADING) {
                downloads.add(2, mDocumentNet);
            } else {
                downloads.add(1, mDocumentNet);
            }
        }
        DBHelper.getInstance(context).updataDocument(mDocumentNet);
        if (mdownloadTitleUnFinish != null) {
            startDownload();
        }
    }

    public void setUploadStart(DocumentNet mDocumentNet) {

        mDocumentNet.mState = STATA_START;
        if (mdownloadTitleUnFinish.mState > 1) {
            DocumentNet documentNet1 = uploads.get(1);
            uploads.remove(mDocumentNet);
            if (documentNet1.mState == STATA_DOWNLOADING) {
                uploads.add(2, mDocumentNet);
            } else {
                uploads.add(1, mDocumentNet);
            }
        }
        DBHelper.getInstance(context).updataDocument(mDocumentNet);
        if (muploadTitleUnFinish != null) {
            startUpload();
        }
    }

    public void setDownloadPause(DocumentNet mDocumentNet) {

        if (mDocumentNet.mState == STATA_DOWNLOADING) {
            if (mStartDownloadTasks != null) {
                mStartDownloadTasks.contral.stop = true;
                mStartDownloadTasks = null;
            }
        }
        mDocumentNet.mState = STATA_PAUSE;
        if (mdownloadTitleUnFinish.mState > 1) {
            downloads.remove(mDocumentNet);
            downloads.add(mdownloadTitleUnFinish.mState, mDocumentNet);
        }
        DBHelper.getInstance(context).updataDocument(mDocumentNet);
        if (mdownloadTitleUnFinish != null) {
            startDownload();
        }
    }

    public void setUploadPause(DocumentNet mDocumentNet) {

        if (mDocumentNet.mState == STATA_DOWNLOADING) {
            if (mStartUploadTasks != null) {
                mStartUploadTasks.contral.stop = true;
                mStartUploadTasks = null;
            }
        }
        mDocumentNet.mState = STATA_PAUSE;
        if (muploadTitleUnFinish.mState > 1) {
            uploads.remove(mDocumentNet);
            uploads.add(muploadTitleUnFinish.mState, mDocumentNet);
        }
        DBHelper.getInstance(context).updataDocument(mDocumentNet);
        if (muploadTitleUnFinish != null) {
            startDownload();
        }
    }

    public void deleteDownloads(ArrayList<DocumentNet> documentNets) {

        for (int i = 0; i < documentNets.size(); i++) {
            DocumentNet documentNet = documentNets.get(i);
            if (documentNet.mState == STATA_DOWNLOADING) {
                downloads.remove(documentNet);
                if (mStartDownloadTasks != null) {
                    mStartDownloadTasks.contral.stop = true;
                    mStartDownloadTasks = null;
                }
                DBHelper.getInstance(context).deleteDoucment(documentNet);
                mdownloadTitleUnFinish.mState--;
                if (mdownloadTitleUnFinish.mState == 0) {
                    downloads.remove(mdownloadTitleUnFinish);
                    mdownloadTitleUnFinish = null;
                }
            } else if (documentNet.mState == STATE_FINISH) {
                downloads.remove(documentNet);
                DBHelper.getInstance(context).deleteDoucment(documentNet);
                mdownloadTitleFinish.mState--;
                if (mdownloadTitleFinish.mState == 0) {
                    downloads.remove(mdownloadTitleFinish);
                    mdownloadTitleFinish = null;
                }
            } else {
                downloads.remove(documentNet);
                DBHelper.getInstance(context).deleteDoucment(documentNet);
                mdownloadTitleUnFinish.mState--;
                if (mdownloadTitleUnFinish.mState == 0) {
                    downloads.remove(mdownloadTitleUnFinish);
                    mdownloadTitleUnFinish = null;
                }
            }
            downloadsMap.remove(documentNet.mRecordID);
        }
        if (mdownloadTitleUnFinish != null) {
            startDownload();
        }
    }

    public void deleteUploads(ArrayList<DocumentNet> documentNets) {

        for (int i = 0; i < documentNets.size(); i++) {
            DocumentNet documentNet = documentNets.get(i);
            if (documentNet.mState == STATA_DOWNLOADING) {
                uploads.remove(documentNet);
                if (mStartUploadTasks != null) {
                    mStartUploadTasks.contral.stop = true;
                    mStartUploadTasks = null;
                }
                DBHelper.getInstance(context).deleteDoucment(documentNet);
                muploadTitleUnFinish.mState--;
                if (muploadTitleUnFinish.mState == 0) {
                    uploads.remove(muploadTitleUnFinish);
                    muploadTitleUnFinish = null;
                }
            } else if (documentNet.mState == STATE_FINISH) {
                uploads.remove(documentNet);
                DBHelper.getInstance(context).deleteDoucment(documentNet);
                muploadTitleFinish.mState--;
                if (muploadTitleFinish.mState == 0) {
                    uploads.remove(muploadTitleFinish);
                    muploadTitleFinish = null;
                }
            } else {
                uploads.remove(documentNet);
                DBHelper.getInstance(context).deleteDoucment(documentNet);
                muploadTitleUnFinish.mState--;
                if (muploadTitleUnFinish.mState == 0) {
                    uploads.remove(muploadTitleUnFinish);
                    muploadTitleUnFinish = null;
                }
            }
            uploadssMap.remove(documentNet.mName + documentNet.mCatalogueID + documentNet.mOwnerType);
        }
        if (muploadTitleUnFinish != null) {
            startDownload();
        }
    }

    public void setDownloadFinish(DocumentNet mDocumentNet) {

        if (mDocumentNet.mState == DocumentManager.STATE_FINISH) {
            return;
        }
        downloads.remove(mDocumentNet);
        mdownloadTitleUnFinish.mState--;
        if (mdownloadTitleFinish == null) {
            mdownloadTitleFinish = new DocumentNet();
            mdownloadTitleFinish.mType = TYPE_DOWNLOAD_FINISH;
            downloads.add(mdownloadTitleUnFinish.mState + 1, mdownloadTitleFinish);
        }
        downloads.add(mdownloadTitleUnFinish.mState + 2, mDocumentNet);
        mdownloadTitleFinish.mState++;
        mDocumentNet.mState = DocumentManager.STATE_FINISH;
        DBHelper.getInstance(context).updataDocument(mDocumentNet);
        if (mdownloadTitleUnFinish.mState == 0) {
            downloads.remove(mdownloadTitleUnFinish);
            mdownloadTitleUnFinish = null;
            mStartDownloadTasks = null;
        }

        if (mdownloadTitleUnFinish != null) {
            if (mStartDownloadTasks != null) {
                if (mStartDownloadTasks.mDocumentNet.mRecordID.equals(mDocumentNet.mRecordID)) {
                    mStartDownloadTasks = null;
                }
            }
            startDownload();
            if (mdownloadTitleUnFinish.mState == 0) {
                downloads.remove(mdownloadTitleUnFinish);
            }
        }
    }

    public void setUploadFinish(DocumentNet mDocumentNet) {


        uploads.remove(mDocumentNet);
        muploadTitleUnFinish.mState--;
        if (muploadTitleFinish == null) {
            muploadTitleFinish = new DocumentNet();
            muploadTitleFinish.mType = TYPE_UPLOAD_FINISH;
            uploads.add(muploadTitleUnFinish.mState + 1, muploadTitleFinish);
        }
        uploads.add(muploadTitleUnFinish.mState + 2, mDocumentNet);
        muploadTitleFinish.mState++;
        mDocumentNet.mState = DocumentManager.STATE_FINISH;
        DBHelper.getInstance(context).updataDocument(mDocumentNet);
        if (muploadTitleUnFinish.mState == 0) {
            uploads.remove(muploadTitleUnFinish);
            muploadTitleUnFinish = null;
            mStartUploadTasks = null;
        }

        if (muploadTitleUnFinish != null) {
            if (mStartUploadTasks != null) {
                if (mStartUploadTasks.mDocumentNet.mRecordID.equals(mDocumentNet.mRecordID)) {
                    mStartUploadTasks = null;
                }
            }
            startUpload();
            if (muploadTitleUnFinish.mState == 0) {
                uploads.remove(muploadTitleUnFinish);
            }
        }
    }

    public boolean checkDownload(DocumentNet mDocumentNet) {
        if (downloadsMap.containsKey(mDocumentNet.mRecordID)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUpload(DocumentNet mDocumentNet) {
        if (uploadssMap.containsKey(mDocumentNet.mName + mDocumentNet.mCatalogueID + mDocumentNet.mOwnerType)) {
            return true;
        } else {
            return false;
        }
    }

    public void updataDownload(DMessage dMessage) {
        DocumentNet documentNet = dMessage.documentNet;
        documentNet.speed = dMessage.finishsize-documentNet.mFinishSize;
        documentNet.mFinishSize = dMessage.finishsize;

    }

    public void updataUpoad(DMessage dMessage) {
        DocumentNet documentNet = dMessage.documentNet;
        documentNet.speed = dMessage.finishsize-documentNet.mFinishSize;
        documentNet.mFinishSize = dMessage.finishsize;
    }

    public void startDownloadFirst() {
        if (mStartDownloadTasks == null) {
            if (mdownloadTitleUnFinish != null) {
                DocumentNet documentNet = downloads.get(1);
                if (documentNet.mState == STATA_DOWNLOADING || documentNet.mState == STATA_START) {
                    documentNet.mState = STATA_DOWNLOADING;
                    DBHelper.getInstance(context).updataDocument(documentNet);
                    mStartDownloadTasks = new DocumentDownloadThread(documentNet);
                    mStartDownloadTasks.start();
                }
            }
        }
    }


    public void startDownload() {
        if (mStartDownloadTasks == null) {
            if (mdownloadTitleUnFinish != null) {
                DocumentNet documentNet = downloads.get(1);
                if (documentNet.mState == STATA_START) {
                    documentNet.mState = STATA_DOWNLOADING;
                    DBHelper.getInstance(context).updataDocument(documentNet);
                    mStartDownloadTasks = new DocumentDownloadThread(documentNet);
                    mStartDownloadTasks.start();
                }
            }
        }
    }


    private void startUploadFirst() {
        if (mStartUploadTasks == null) {
            if (muploadTitleUnFinish != null) {
                DocumentNet documentNet = uploads.get(1);
                if (documentNet.mState == STATA_DOWNLOADING || documentNet.mState == STATA_START) {
                    documentNet.mState = STATA_DOWNLOADING;
                    DBHelper.getInstance(context).updataDocument(documentNet);
                    mStartUploadTasks = new DocumentUpLoadThread(documentNet);
                    mStartUploadTasks.start();
                }
            }
        }
    }

    private void startUpload() {
        if (mStartUploadTasks == null) {
            if (muploadTitleUnFinish != null) {
                DocumentNet documentNet = uploads.get(1);
                if (documentNet.mState == STATA_START) {
                    documentNet.mState = STATA_DOWNLOADING;
                    DBHelper.getInstance(context).updataDocument(documentNet);
                    mStartUploadTasks = new DocumentUpLoadThread(documentNet);
                    mStartUploadTasks.start();
                }
            }
        }
    }

    public void setPosition(Context context) {
        Intent mainIntent = new Intent();
        mainIntent.setClass(context, SetPositionActivity.class);
        context.startActivity(mainIntent);
    }

    public void setPositionUpload(Context context,ArrayList<Attachment> attachments) {
        Intent mainIntent = new Intent();
        mainIntent.setClass(context, SetPositionActivity.class);
        mainIntent.putParcelableArrayListExtra("uploads",attachments);
        context.startActivity(mainIntent);
    }

    public static void startDocumentMain(Context context) {
        Intent intent = new Intent(context, DocumentActivity.class);
        context.startActivity(intent);
    }

    public static void startDocumentMainDownload(Context context, boolean isdownload) {
        Intent intent = new Intent(context, DocumentActivity.class);
        intent.putExtra("isdownload", true);
        if (isdownload)
            intent.putExtra("showup", false);
        else
            intent.putExtra("showup", true);
        context.startActivity(intent);
    }

    public void sendUpload(ArrayList<Attachment> attachments) {
        ArrayList<DocumentNet> documentNets = new ArrayList<DocumentNet>();
        DocumentNet path = null;
        if (DocumentManager.getInstance().selectPathList.size() == 0) {
            path = DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size() - 1);
        } else {
            path = DocumentManager.getInstance().selectPathList.get(DocumentManager.getInstance().selectPathList.size() - 1);
        }
        for (int i = 0; i < attachments.size(); i++) {
            Attachment attachment = attachments.get(i);
            DocumentNet documentNet = new DocumentNet();
            documentNet.mFinishSize = attachment.mFinishSize;
            documentNet.mSize = attachment.mSize;
            documentNet.mShared = path.mShared;
            documentNet.mOwnerType = path.mOwnerType;
            documentNet.mCatalogueID = path.mRecordID;
            documentNet.mOwnerID = path.mOwnerID;
            documentNet.mType = DocumentManager.TYPE_UPLOAD_NOMAL;
            documentNet.mPath = attachment.mPath;
            documentNet.mUrl = NetUtils.getInstance().praseUrl(service,DocumentUpLoadThread.HTTP_UPLOAD_PATH);
            documentNet.mName = attachment.mName;
            documentNet.parentid = path.parentid;
            documentNet.mRecordID = AppUtils.getguid();
            documentNets.add(documentNet);
        }
        Message message = new Message();
        message.what = DownUpHandler.EVETN_UPLOAD_ADD;
        message.obj = documentNets;
        DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
    }

    public interface NotificationOperation {
        void showProgress(String title, String content, int max, int min, String openfragment);

        void showMesage(String title, String content, String openfragment);

        void showCancle();
    }

    public Commend.CommendFun commendFunMain = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            startDocumentMain(context);
        }
    };
}


