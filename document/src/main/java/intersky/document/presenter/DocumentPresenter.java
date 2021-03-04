package intersky.document.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.document.DocumentManager;
import intersky.document.DocumentUpLoadThread;
import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.DocumentHandler;
import intersky.document.handler.DownUpHandler;
import intersky.document.receive.DocumentReceiver;
import intersky.document.view.activity.DocumentActivity;
import intersky.document.view.fragment.DocumentFragment;
import intersky.document.view.fragment.DownloadFragment;
import intersky.appbase.FragmentTabAdapter;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.CheckPath;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class DocumentPresenter implements Presenter {

    public DocumentHandler mDocumentHandler;
    public DocumentActivity mDocumentActivity;
    public DocumentPresenter(DocumentActivity mDocumentActivity)
    {
        this.mDocumentActivity = mDocumentActivity;
        this.mDocumentHandler = new DocumentHandler(mDocumentActivity);
        mDocumentActivity.setBaseReceiver(new DocumentReceiver(mDocumentHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mDocumentActivity.setContentView(R.layout.activity_document);
        mDocumentActivity.back = mDocumentActivity.findViewById(R.id.back);
        mDocumentActivity.back.setOnClickListener(mDocumentActivity.mBackListener);
        LayoutInflater inflater = LayoutInflater.from(mDocumentActivity);
        mDocumentActivity.uploadSelectView = inflater.inflate(R.layout.uploadselectview, null);
        mDocumentActivity.mPicture = (RelativeLayout) mDocumentActivity.uploadSelectView.findViewById(R.id.picturelayer);
        mDocumentActivity.mVideo = (RelativeLayout) mDocumentActivity.uploadSelectView.findViewById(R.id.videolayer);
        mDocumentActivity.mAllFile = (RelativeLayout) mDocumentActivity.uploadSelectView.findViewById(R.id.allfilelayer);
        mDocumentActivity.close = (ImageView) mDocumentActivity.uploadSelectView.findViewById(R.id.close);
        mDocumentActivity.close.setOnClickListener(mDocumentActivity.mCancleUpLoadListener);

        mDocumentActivity.upload = mDocumentActivity.findViewById(R.id.upload);
        mDocumentActivity.download = mDocumentActivity.findViewById(R.id.download);
        mDocumentActivity.download.setOnClickListener(mDocumentActivity.mDetialListener);
        mDocumentActivity.editDownload = mDocumentActivity.findViewById(R.id.edit);
        mDocumentActivity.editDownload.setOnClickListener(mDocumentActivity.mSelectAllListener);
        mDocumentActivity.title = mDocumentActivity.findViewById(R.id.title);
        mDocumentActivity.calcle = mDocumentActivity.findViewById(R.id.cancle);
        mDocumentActivity.calcle.setOnClickListener(mDocumentActivity.hidDocumentEditListener);
        mDocumentActivity.upload.setOnClickListener(mDocumentActivity.mSelectUpLoadListener);
        mDocumentActivity.mFragments.add(new DocumentFragment());
        mDocumentActivity.mFragments.add(new DownloadFragment());
        mDocumentActivity.buttomLayer = (LinearLayout) mDocumentActivity.findViewById(R.id.mian_buttom_layer_select);
        mDocumentActivity.buttomLayerd = (LinearLayout) mDocumentActivity.findViewById(R.id.mian_buttom_layer_delete);
        mDocumentActivity.buttomLayer.setVisibility(View.INVISIBLE);
        mDocumentActivity.mBtnDown = (RelativeLayout) mDocumentActivity.findViewById(R.id.mian_buttom_down);
        mDocumentActivity.mBtnDelete = (RelativeLayout) mDocumentActivity.findViewById(R.id.mian_buttom_delete);
        mDocumentActivity.mBtnMore = (RelativeLayout) mDocumentActivity.findViewById(R.id.mian_buttom_more);
        mDocumentActivity.mBtnDownDelete = (RelativeLayout) mDocumentActivity.findViewById(R.id.mian_buttom_download_delete);

        mDocumentActivity.showActionUp = AnimationUtils.loadAnimation(mDocumentActivity, R.anim.slide_in_top);
        mDocumentActivity.hideActionUp = AnimationUtils.loadAnimation(mDocumentActivity, R.anim.slide_out_top);
        mDocumentActivity.showActionDown = AnimationUtils.loadAnimation(mDocumentActivity, R.anim.slide_in_buttom);
        mDocumentActivity.hideActionDown = AnimationUtils.loadAnimation(mDocumentActivity, R.anim.slide_out_buttom);
        mDocumentActivity.mShade = (RelativeLayout) mDocumentActivity.findViewById(R.id.shade);
        mDocumentActivity.mOther = (RelativeLayout) mDocumentActivity.findViewById(R.id.tab_content);

        mDocumentActivity.mPicture.setOnClickListener(mDocumentActivity.mUpPictureListener);
        mDocumentActivity.mVideo.setOnClickListener(mDocumentActivity.mUpVideoListener);
        mDocumentActivity.mAllFile.setOnClickListener(mDocumentActivity.mUpAllfileListener);

        mDocumentActivity.mBtnDown.setOnClickListener(mDocumentActivity.mAddDowanloadListener);
        mDocumentActivity.mBtnDelete.setOnClickListener(mDocumentActivity.mDeleteDocumentListener);
        mDocumentActivity.mBtnMore.setOnClickListener(mDocumentActivity.showMoreListener);

        mDocumentActivity.mBtnDownDelete.setOnClickListener(mDocumentActivity.mDeleteDownloadListener);
        mDocumentActivity.tabAdapter = new FragmentTabAdapter(mDocumentActivity, mDocumentActivity.mFragments,
                R.id.tab_content);
        if(mDocumentActivity.getIntent().getBooleanExtra("isdownload",false) == true)
        {
            showDetial();
        }
        else
        {
            showYunpan();

        }

    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void showYunpan() {
        hideLoadEdit();
        mDocumentActivity.tebId = 0;
        mDocumentActivity.tabAdapter.onCheckedChanged(0);
        setDocumentTitle();
        mDocumentActivity.upload.setVisibility(View.VISIBLE);
        mDocumentActivity.download.setVisibility(View.VISIBLE);
        mDocumentActivity.back.setVisibility(View.VISIBLE);
        mDocumentActivity.editDownload.setVisibility(View.INVISIBLE);
    }

    public void showDetial() {
        hidEdit();
        hideUpload();
        mDocumentActivity.tebId = 1;
        mDocumentActivity.tabAdapter.onCheckedChanged(1);
        mDocumentActivity.editDownload.setVisibility(View.VISIBLE);
        mDocumentActivity.back.setVisibility(View.VISIBLE);
        mDocumentActivity.title.setText(mDocumentActivity.getString(R.string.document_chuanshu_download));
        mDocumentActivity.editDownload.setText(mDocumentActivity.getString(R.string.button_word_selectall));

        mDocumentActivity.upload.setVisibility(View.INVISIBLE);
        mDocumentActivity.download.setVisibility(View.INVISIBLE);

    }

    public void setDocumentTitle() {
        mDocumentActivity.title.setText(DocumentManager.getInstance().pathList
                .get(DocumentManager.getInstance().pathList.size() - 1).mName);
    }

    public void showSearch() {
        if (DocumentManager.getInstance().isEdit == true) {
            hidEdit();
        }
        DocumentManager.getInstance().showSearch = true;
        if(((DocumentFragment) mDocumentActivity.mFragments.get(0)).msearchLayer != null) {
            ((DocumentFragment) mDocumentActivity.mFragments.get(0)).documentList
                    .setAdapter(((DocumentFragment) mDocumentActivity.mFragments.get(0)).mDocumentSearchAdapter);
            updataDocumentList();
        }

    }

    public void hidSearch() {
        if (DocumentManager.getInstance().isEdit == true) {
            hidEdit();
        }
        DocumentManager.getInstance().showSearch = false;
        if(((DocumentFragment) mDocumentActivity.mFragments.get(0)).msearchLayer != null) {
            ((DocumentFragment) mDocumentActivity.mFragments.get(0)).documentList
                    .setAdapter(((DocumentFragment) mDocumentActivity.mFragments.get(0)).mDocumentAdapter);
            updataDocumentList();
        }

    }

    public void showEdit() {
        if(mDocumentActivity.buttomLayer.getVisibility() == View.INVISIBLE)
        {
            mDocumentActivity.buttomLayer.startAnimation(mDocumentActivity.showActionDown);
            mDocumentActivity.buttomLayer.setVisibility(View.VISIBLE);
            DocumentManager.getInstance().isEdit = true;
            mDocumentActivity.upload.setVisibility(View.INVISIBLE);
            mDocumentActivity.download.setVisibility(View.INVISIBLE);
            mDocumentActivity.editDownload.setVisibility(View.VISIBLE);
            mDocumentActivity.calcle.setVisibility(View.VISIBLE);
            mDocumentActivity.back.setVisibility(View.INVISIBLE);
            mDocumentActivity.editDownload.setText(mDocumentActivity.getString(R.string.button_word_selectall));

        }
    }

    public void hidEdit() {
        if(mDocumentActivity.buttomLayer.getVisibility() == View.VISIBLE)
        {
            mDocumentActivity.buttomLayer.startAnimation(mDocumentActivity.hideActionDown);
            mDocumentActivity.buttomLayer.setVisibility(View.INVISIBLE);
            if(mDocumentActivity.popupWindow != null)
            {
                mDocumentActivity.popupWindow.dismiss();
            }

            DocumentManager.getInstance().isEdit = false;
            mDocumentActivity.upload.setVisibility(View.VISIBLE);
            mDocumentActivity.download.setVisibility(View.VISIBLE);
            mDocumentActivity.editDownload.setVisibility(View.INVISIBLE);
            mDocumentActivity.calcle.setVisibility(View.INVISIBLE);
            mDocumentActivity.back.setVisibility(View.VISIBLE);
            ToolBarHelper.setRightBtnText(mDocumentActivity.mActionBar, mDocumentActivity.mSelectUpLoadListener, mDocumentActivity.getString(R.string.button_word_upload));
            if (DocumentManager.getInstance().showSearch == true) {

                for (int i = 0; i < DocumentManager.getInstance().sfileList.size(); i++) {
                    DocumentManager.getInstance().sfileList.get(i).isSelect = false;
                }
            } else {
                for (int i = 0; i < DocumentManager.getInstance().fileList.size(); i++) {
                    DocumentManager.getInstance().fileList.get(i).isSelect = false;
                }
            }
            DocumentManager.getInstance().fileSelectCount = 0;
            if(mDocumentActivity.mFragments.get(0) != null)
            {
                ((DocumentFragment)mDocumentActivity.mFragments.get(0)).mDocumentAdapter.notifyDataSetChanged();
                ((DocumentFragment)mDocumentActivity.mFragments.get(0)).mDocumentSearchAdapter.notifyDataSetChanged();
            }

        }
        if(mDocumentActivity.buttomLayerd.getVisibility() == View.VISIBLE)
        {
            hideLoadEdit();
        }
    }

    public void showUpload() {
        if(mDocumentActivity.popupWindow != null)
        {
            mDocumentActivity.popupWindow.dismiss();
        }
        mDocumentActivity.popupWindow = AppUtils.creatTopView(mDocumentActivity,mDocumentActivity.mShade,mDocumentActivity.findViewById(R.id.activity_main_document),mDocumentActivity.uploadSelectView);
    }

    public void hideUpload() {
        if(mDocumentActivity.popupWindow != null)
        {
            mDocumentActivity.popupWindow.dismiss();
        }
    }

    public void doSelectAll() {
        if(mDocumentActivity.tebId == 0) {
            if(DocumentManager.getInstance().showSearch) {
                for(int i = 0 ; i < DocumentManager.getInstance().sfileList.size() ; i++) {
                    DocumentManager.getInstance().sfileList.get(i).isSelect = true;
                }
            }
            else {
                for(int i = 0 ; i < DocumentManager.getInstance().fileList.size() ; i++) {
                    DocumentManager.getInstance().fileList.get(i).isSelect = true;
                }
            }
        }
        else
        {
            DownloadFragment mDownloadFragment = (DownloadFragment) mDocumentActivity.mFragments.get(1);
            if(mDownloadFragment != null)
            {
                if(mDownloadFragment.mViewPager != null)
                {
                    if(mDownloadFragment.mViewPager.getId() == 0)
                    {
                        for(int i = 0 ; i < DocumentManager.getInstance().downloads.size() ; i++) {
                            DocumentManager.getInstance().downloads.get(i).isSelect = true;
                        }
                        DocumentManager.getInstance().downSelectCount++;
                    }
                    else
                    {
                        for(int i = 0 ; i < DocumentManager.getInstance().uploads.size() ; i++) {
                            DocumentManager.getInstance().uploads.get(i).isSelect = true;
                        }
                        DocumentManager.getInstance().upSelectCount++;
                    }
                    updataLoadList();
                }
            }
        }
    }

    public void doSearch(String keyword) {
        mDocumentActivity.waitDialog.show();
        DocumentAsks.searchDocumentList(mDocumentHandler,mDocumentActivity,DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1),keyword);
    }

    public void doPre(DocumentNet documentNet) {
        mDocumentActivity.waitDialog.show();
        DocumentAsks.preDocumentList(mDocumentHandler,mDocumentActivity,documentNet);
    }

    public void doBack() {
        if(mDocumentActivity.tebId == 0)
        {
            if(DocumentManager.getInstance().pathList.size() > 1)
            {
                mDocumentActivity.waitDialog.show();
                DocumentAsks.backDocumentList(mDocumentHandler,mDocumentActivity,DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-2));
            }
            else
            {
                mDocumentActivity.finish();
            }
        }
        else
        {
            showYunpan();
        }
    }

    public void doUpdate() {
        mDocumentActivity.waitDialog.show();
        DocumentAsks.updataDocumentList(mDocumentHandler,mDocumentActivity,DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1));
    }

    public void doUpdate2() {
        DocumentAsks.updataDocumentList(mDocumentHandler,mDocumentActivity,DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1));
    }

    public void doDocumentClcick(DocumentNet documentNet) {
        if(documentNet.mType == DocumentManager.TYPE_DOCUMENT) {
            doPre(documentNet);
        }
        else {
            File file1 = null;
            if(documentNet.mState == DocumentManager.STATE_FINISH) {
                file1 = new File(Bus.callData(mDocumentActivity,"filetools/getfilePath","/download/"+documentNet.mRecordID)+"/"+documentNet.mName);
                if(file1.exists())
                {
                   if(file1.length() != documentNet.mSize) {
                       file1 = new File(Bus.callData(mDocumentActivity,"filetools/getfilePath","/attachment/"+documentNet.mRecordID)+"/"+documentNet.mName);
                   }
                }
                else
                {
                    file1 = new File(Bus.callData(mDocumentActivity,"filetools/getfilePath","/attachment/"+documentNet.mRecordID)+"/"+documentNet.mName);
                }
            }
            else
            {
                file1 = new File(Bus.callData(mDocumentActivity,"filetools/getfilePath","/attachment/"+documentNet.mRecordID)+"/"+documentNet.mName);
            }
            Attachment attachment = new Attachment(documentNet.mRecordID,documentNet.mName, file1.getPath(),DocumentAsks.praseUrl(documentNet),documentNet.mSize,0,documentNet.mDate);
            Bus.callData(mDocumentActivity,"filetools/startAttachmentDoc",attachment);
        }
    }

    public void doDowloadClick(DocumentNet documentNet) {
        if(documentNet.mState == DocumentManager.STATE_FINISH) {
            File file1 = new File(documentNet.mPath);
            if(file1.exists())
            {
                Attachment attachment = new Attachment(documentNet.mRecordID,documentNet.mName, file1.getPath(),DocumentAsks.praseUrl(documentNet),documentNet.mSize,0,documentNet.mDate);
                Bus.callData(mDocumentActivity,"filetools/startAttachmentDoc",attachment);
            }
            else
            {
                AppUtils.showMessage(mDocumentActivity,mDocumentActivity.getString(R.string.file_deleteed));
            }
        }
        else
        {
            AppUtils.showMessage(mDocumentActivity,mDocumentActivity.getString(R.string.file_downloadunfinish));
        }
    }

    public void updataDocumentList() {
        if(((DocumentFragment) mDocumentActivity.mFragments.get(0)).msearchLayer != null) {
            ((DocumentFragment) mDocumentActivity.mFragments.get(0)).showgrid();
            if(DocumentManager.getInstance().showSearch) {
                ((DocumentFragment) mDocumentActivity.mFragments.get(0)).mDocumentSearchAdapter.notifyDataSetChanged();
            }
            else
            {
                ((DocumentFragment) mDocumentActivity.mFragments.get(0)).mDocumentAdapter.notifyDataSetChanged();
            }
            ((DocumentFragment) mDocumentActivity.mFragments.get(0)).mDocumentGAdapter.notifyDataSetChanged();
        }
    }

    public void addDownload() {
        ArrayList<DocumentNet> documentNets = new ArrayList<DocumentNet>();
        if(DocumentManager.getInstance().showSearch == true)
        {
            for(int i = 0 ; i < DocumentManager.getInstance().sfileList.size() ; i++) {
                DocumentNet mDocumentNet = DocumentManager.getInstance().sfileList.get(i);
                if(mDocumentNet.isSelect == true)
                {
                    DocumentNet documentNet = new DocumentNet();
                    documentNet.copy(mDocumentNet);
                    documentNets.add(documentNet);
                }

            }
        }
        else
        {
            for(int i = 0 ; i < DocumentManager.getInstance().fileList.size() ; i++) {
                DocumentNet mDocumentNet = DocumentManager.getInstance().fileList.get(i);
                if(mDocumentNet.isSelect == true)
                {
                    DocumentNet documentNet = new DocumentNet();
                    documentNet.copy(mDocumentNet);
                    documentNets.add(documentNet);
                }
            }
        }
        hidEdit();
        AppUtils.showMessage(mDocumentActivity,mDocumentActivity.getString(R.string.document_hasaddto_download));
        Message message = new Message();
        message.what = DownUpHandler.EVETN_DOWNLOAD_ADD;
        message.obj = documentNets;
        DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
    }

    public void deleteDocument() {

        AppUtils.creatDialogTowButton(mDocumentActivity,mDocumentActivity.getString(R.string.document_deletefile),mDocumentActivity.getString(R.string.document_tip),
                mDocumentActivity.getString(R.string.button_word_cancle),mDocumentActivity.getString(R.string.button_word_ok),new DeleteDocumentListener(),new CancleDocumentListener());

    }

    public class DeleteDocumentListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ArrayList<DocumentNet> mDocumentNets = new ArrayList<DocumentNet>();
            if(DocumentManager.getInstance().showSearch)
            {
                for(int i = 0 ; i < DocumentManager.getInstance().sfileList.size() ; i++) {

                    if(DocumentManager.getInstance().sfileList.get(i).isSelect)
                    {
                        mDocumentNets.add(DocumentManager.getInstance().sfileList.get(i));
                    }
                }
            }
            else
            {
                for(int i = 0 ; i < DocumentManager.getInstance().fileList.size() ; i++) {

                    if(DocumentManager.getInstance().fileList.get(i).isSelect)
                    {
                        mDocumentNets.add(DocumentManager.getInstance().fileList.get(i));
                    }
                }
            }
            mDocumentActivity.waitDialog.hide();
            DocumentAsks.deleteDocumentList(mDocumentNets,mDocumentHandler,mDocumentActivity);
            hidEdit();
        }
    }


    public class CancleDocumentListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            hidEdit();
        }
    }

    public void showMenu() {
        if (mDocumentActivity.popupWindow != null) {

            mDocumentActivity.popupWindow.dismiss();
        } else {
            if (DocumentManager.getInstance().showSearch) {
                int count = 0;
                for(int i = 0 ; i < DocumentManager.getInstance().sfileList.size() ; i++) {
                    if(DocumentManager.getInstance().sfileList.get(i).isSelect == true) {
                        count++;
                    }
                    if(count == 2)
                    {
                        break;
                    }
                }
                if(count == 1)
                {
                    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
                    MenuItem menuItem = new MenuItem();
                    menuItem.btnName = mDocumentActivity.getString(R.string.button_word_rename);
                    menuItem.mListener = mDocumentActivity.renameListener;
                    menuItems.add(menuItem);
                    AppUtils.creatButtomMenu(mDocumentActivity,mDocumentActivity.mShade,menuItems,mDocumentActivity.findViewById(R.id.activity_main_document));
                }
            } else {
                int count = 0;
                for(int i = 0 ; i < DocumentManager.getInstance().fileList.size() ; i++) {
                    if(DocumentManager.getInstance().fileList.get(i).isSelect == true) {
                        count++;
                    }
                    if(count == 2)
                    {
                        break;
                    }
                }
                if(count == 1)
                {
                    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
                    MenuItem menuItem = new MenuItem();
                    menuItem.btnName = mDocumentActivity.getString(R.string.button_word_rename);
                    menuItem.mListener = mDocumentActivity.renameListener;
                    menuItems.add(menuItem);
                    AppUtils.creatButtomMenu(mDocumentActivity,mDocumentActivity.mShade,menuItems,mDocumentActivity.findViewById(R.id.activity_main_document));
                }
            }

        }
    }

    public void doRename() {
        hidEdit();
        DocumentNet mDocumentNet = null;
        if (DocumentManager.getInstance().showSearch) {
            for (int i = 0; i < DocumentManager.getInstance().sfileList.size(); i++) {
                if (DocumentManager.getInstance().sfileList.get(i).isSelect == true) {
                    mDocumentNet = DocumentManager.getInstance().sfileList.get(i);
                    break;
                }
            }
        }
        else
        {
            for (int i = 0; i < DocumentManager.getInstance().fileList.size(); i++) {
                if (DocumentManager.getInstance().fileList.get(i).isSelect == true) {
                    mDocumentNet = DocumentManager.getInstance().fileList.get(i);
                    break;
                }
            }
        }

        if (mDocumentNet != null) {
            AppUtils.creatDialogTowButtonEdit(mDocumentActivity,"",mDocumentActivity.getString(R.string.button_word_rename),
                    mDocumentActivity.getString(R.string.button_word_ok),mDocumentActivity.getString(R.string.button_word_cancle)
                    ,new RenameListener(mDocumentNet),null,mDocumentNet.mName.substring(0,mDocumentNet.mName.lastIndexOf(".")));
        }

    }

    public class RenameListener extends EditDialogListener {

        public DocumentNet mDocumentNet;

        public RenameListener(DocumentNet mDocumentNet) {
            this.mDocumentNet = mDocumentNet;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            DocumentAsks.doRename(mDocumentNet,editText.getText().toString(),mDocumentHandler,mDocumentActivity);
        }
    }

    public void startPhoto() {
        DocumentManager.getInstance().selectPathList.clear();
        Bus.callData(mDocumentActivity,"filetools/getPhotosUpload",true,9999,"intersky.document.view.activity.DocumentActivity",DocumentManager.ACTION_SELECT_UPLOADFILE_FINISH,onClickListener
                ,DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1).mName,checkPath);
        hideUpload();
    }

    public void startVideo() {
        DocumentManager.getInstance().selectPathList.clear();
        Bus.callData(mDocumentActivity,"filetools/getVideosUpload",true,"intersky.document.view.activity.DocumentActivity",DocumentManager.ACTION_SELECT_UPLOADFILE_FINISH,onClickListener,
                DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1).mName,checkPath);
        hideUpload();
    }

    public void startAllFile() {
        DocumentManager.getInstance().selectPathList.clear();
        Bus.callData(mDocumentActivity,"filetools/getFilesUpload","DocumentActivity",DocumentManager.ACTION_SELECT_UPLOADFILE_FINISH,onClickListener,
                DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1).mName,checkPath);
        hideUpload();
    }

    public void setUploadFiles(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        ArrayList<DocumentNet> documentNets = new ArrayList<DocumentNet>();
        DocumentNet path = null;
        if(DocumentManager.getInstance().selectPathList.size() == 0)
        {
            path = DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1);
        }
        else
        {
            path = DocumentManager.getInstance().selectPathList.get(DocumentManager.getInstance().selectPathList.size()-1);
        }

        for(int i = 0 ; i < attachments.size() ; i++) {
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
            documentNet.mDate = TimeUtils.getDateAndTime();
            documentNet.mUrl = NetUtils.getInstance().praseUrl(DocumentManager.getInstance().service,DocumentUpLoadThread.HTTP_UPLOAD_PATH);
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

    public CheckPath checkPath = new CheckPath() {
        @Override
        public boolean checkPath() {
            Boolean canupload = true;
            if(DocumentManager.getInstance().selectPathList.size() == 0)
            {
                DocumentNet path = DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().pathList.size()-1);
                if(path.mOwnerType.toString().length() == 0)
                {
                    canupload = false;
                }
                else if(path.mOwnerType.equals("(Company)")
                        && path.mRecordID.length() == 0)
                {
                    canupload = false;
                }
                else if(DocumentManager.getInstance().pathList.size()>1) {
                    if(DocumentManager.getInstance().pathList.get(1).mShared == true)
                    {
                        canupload = false;
                    }
                }
            }
            else
            {
                DocumentNet path = DocumentManager.getInstance().pathList.get(DocumentManager.getInstance().selectPathList.size()-1);
                if(path.mOwnerType.toString().length() == 0)
                {
                    canupload = false;
                }
                else if(path.mOwnerType.equals("(Company)")
                        && path.mRecordID.length() == 0)
                {
                    canupload = false;
                }
                else if(DocumentManager.getInstance().selectPathList.size()>1) {
                    if(DocumentManager.getInstance().selectPathList.get(1).mShared = true)
                    {
                        canupload = false;
                    }
                }
            }
            return canupload;
        }
    };

    //download
    public void showLoadEdit() {

        if(mDocumentActivity.buttomLayerd.getVisibility() == View.INVISIBLE) {
            mDocumentActivity.buttomLayerd.startAnimation(mDocumentActivity.showActionDown);
            mDocumentActivity.buttomLayerd.setVisibility(View.VISIBLE);
            mDocumentActivity.calcle.setVisibility(View.VISIBLE);
            mDocumentActivity.editDownload.setVisibility(View.VISIBLE);
            mDocumentActivity.back.setVisibility(View.INVISIBLE);
            mDocumentActivity.download.setVisibility(View.INVISIBLE);
            mDocumentActivity.upload.setVisibility(View.INVISIBLE);
        }
    }

    public void hideLoadEdit() {
        if(mDocumentActivity.buttomLayerd.getVisibility() == View.VISIBLE) {
            mDocumentActivity.buttomLayerd.setVisibility(View.INVISIBLE);
            mDocumentActivity.calcle.setVisibility(View.INVISIBLE);
            mDocumentActivity.editDownload.setVisibility(View.VISIBLE);
            mDocumentActivity.back.setVisibility(View.VISIBLE);

            mDocumentActivity.buttomLayerd.startAnimation(mDocumentActivity.hideActionDown);
            mDocumentActivity.buttomLayerd.setVisibility(View.INVISIBLE);

            DownloadFragment mDownloadFragment = (DownloadFragment) mDocumentActivity.mFragments.get(1);
            if(mDownloadFragment != null)
            {
                if(mDownloadFragment.mViewPager != null)
                {
                    if(mDownloadFragment.mViewPager.getId() == 0)
                    {
                        for(int i = 0 ; i < DocumentManager.getInstance().downloads.size() ; i++) {
                            DocumentManager.getInstance().downloads.get(i).isSelect = false;
                        }
                        DocumentManager.getInstance().downSelectCount = 0;
                    }
                    else
                    {
                        for(int i = 0 ; i < DocumentManager.getInstance().uploads.size() ; i++) {
                            DocumentManager.getInstance().uploads.get(i).isSelect = false;
                        }
                        DocumentManager.getInstance().upSelectCount = 0;
                    }
                }
            }
        }
    }

    public void updataLoadList() {
        DownloadFragment mDownloadFragment = (DownloadFragment) mDocumentActivity.mFragments.get(1);
        if(mDownloadFragment != null)
        {
            if(mDownloadFragment.mViewPager != null)
            {
                if(mDownloadFragment.mViewPager.getCurrentItem() == 0)
                {
                    mDownloadFragment.mDownLoadItemAdapter.notifyDataSetChanged();
                }
                else
                {
                    mDownloadFragment.mUpLoadItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void deleteDownloads() {
        DownloadFragment mDownloadFragment = (DownloadFragment) mDocumentActivity.mFragments.get(1);
        if(mDownloadFragment != null)
        {
            if(mDownloadFragment.mViewPager != null)
            {
                if(mDownloadFragment.mViewPager.getCurrentItem() == 0)
                {
                    ArrayList<DocumentNet> documentNets= new ArrayList<DocumentNet>();
                   for(int i = 0 ; i < DocumentManager.getInstance().downloads.size(); i++)
                   {
                       if(DocumentManager.getInstance().downloads.get(i).isSelect)
                       {
                           documentNets.add(DocumentManager.getInstance().downloads.get(i));
                       }
                   }
                    Message message = new Message();
                    message.what = DownUpHandler.EVETN_DOWNLOAD_DELETE;
                    message.obj = documentNets;
                    DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                    mDocumentHandler.sendEmptyMessage(DocumentHandler.EVENT_HIT_DOWNLOAD_EDIT);
                }
                else
                {
                    ArrayList<DocumentNet> documentNets= new ArrayList<DocumentNet>();
                    for(int i = 0 ; i < DocumentManager.getInstance().uploads.size(); i++)
                    {
                        if(DocumentManager.getInstance().uploads.get(i).isSelect)
                        {
                            documentNets.add(DocumentManager.getInstance().uploads.get(i));
                        }
                    }
                    Message message = new Message();
                    message.what = DownUpHandler.EVETN_UPLOAD_DELETE;
                    message.obj = documentNets;
                    DocumentManager.getInstance().mDownUpHandler.sendMessage(message);
                    mDocumentHandler.sendEmptyMessage(DocumentHandler.EVENT_HIT_DOWNLOAD_EDIT);
                }
            }
        }


    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DocumentManager.getInstance().setPosition(mDocumentActivity);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mDocumentActivity.popupWindow != null)
            {
                mDocumentActivity.popupWindow.dismiss();
            }
            if(mDocumentActivity.tebId == 0)
            {
                if(mDocumentActivity.buttomLayer.getVisibility() == View.VISIBLE) {
                    hidEdit();
                    return true;
                }
                else if(DocumentManager.getInstance().pathList.size() > 1)
                {
                    if(mDocumentActivity.tebId == 0)
                    {
                        doBack();
                    }
                    else
                    {
                        mDocumentActivity.finish();
                    }
                    return true;
                }
                else
                {
                    mDocumentActivity.finish();
                    return true;
                }
            }
            else
            {
                showYunpan();
                return true;
            }
        }
        else {
            return false;
        }
    }
}
