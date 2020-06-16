package intersky.filetools.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.appbase.entity.Attachment;
import intersky.filetools.database.DBHelper;
import intersky.filetools.entity.DownloadInfo;
import intersky.filetools.handler.AttachmentHandler;
import intersky.filetools.receiver.AttachmentReceiver;
import intersky.filetools.thread.DownloadThread;
import intersky.filetools.view.activity.AttachmentActivity;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class AttachmentPresenter implements Presenter {

    public AttachmentHandler mAttachmentHandler;
    public AttachmentActivity mAttachmentActivity;
    public AttachmentPresenter(AttachmentActivity mAttachmentActivity)
    {
        this.mAttachmentActivity = mAttachmentActivity;
        this.mAttachmentHandler = new AttachmentHandler(mAttachmentActivity);
        mAttachmentActivity.setBaseReceiver(new AttachmentReceiver(mAttachmentHandler));
    }

    @Override
    public void Create() {
        initView();
        initData();

    }

    @Override
    public void initView() {
        mAttachmentActivity.setContentView(R.layout.activity_attachment);
        RelativeLayout mLinearLayout = (RelativeLayout) mAttachmentActivity
                .findViewById(R.id.activity_fu_jian_dowanload);
        mAttachmentActivity.attachment = mAttachmentActivity.getIntent().getParcelableExtra("attachment");
        ToolBarHelper.setRightBtnText(mAttachmentActivity.mActionBar, mAttachmentActivity.showMoreListener, mAttachmentActivity.getString(R.string.menu_more));
        mAttachmentActivity.mNameView = (TextView) mAttachmentActivity.findViewById(R.id.fujian_download_name);
        mAttachmentActivity.mPathView = (TextView) mAttachmentActivity.findViewById(R.id.fujian_download_savepath);
        mAttachmentActivity.mSizeView = (TextView) mAttachmentActivity.findViewById(R.id.fujian_download_size);
        mAttachmentActivity.mProgressBar = (ProgressBar) mAttachmentActivity.findViewById(R.id.fujian_download_progress);
        mAttachmentActivity.mImageView = (ImageView) mAttachmentActivity.findViewById(R.id.fujian_download_img);
        mAttachmentActivity.mfSizeView = (TextView) mAttachmentActivity.findViewById(R.id.fujian_download_f_size);
        mAttachmentActivity.shade = mAttachmentActivity.findViewById(R.id.shade);
        mAttachmentActivity.mSizeView.setVisibility(View.VISIBLE);
        mAttachmentActivity.mProgressBar.setVisibility(View.VISIBLE);
        mAttachmentActivity.mPathView.setVisibility(View.INVISIBLE);
        mAttachmentActivity.mfSizeView.setVisibility(View.INVISIBLE);
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

    public void initData() {
        ToolBarHelper.setTitle(mAttachmentActivity.mActionBar, mAttachmentActivity.attachment.mName);
        mAttachmentActivity.mProgressBar.setMax(1000);
        mAttachmentActivity.mProgressBar.setProgress(0);
        mAttachmentActivity.mNameView.setText(mAttachmentActivity.attachment.mName);
        mAttachmentActivity.mPathView.setVisibility(View.INVISIBLE);
        mAttachmentActivity.mfSizeView.setVisibility(View.INVISIBLE);
        mAttachmentActivity.mPathView
                .setText(mAttachmentActivity.getString(R.string.title_attachment_path) + "：" + mAttachmentActivity.attachment.mPath);
        mAttachmentActivity.mfSizeView
                .setText(mAttachmentActivity.getString(R.string.title_attachment_size) + "：" + AppUtils.getSizeText(mAttachmentActivity.attachment.mSize));
        mAttachmentActivity.mSizeView
                .setText(AppUtils.getSizeText(mAttachmentActivity.attachment.mFinishSize) + "/"
                        + AppUtils.getSizeText(mAttachmentActivity.attachment.mSize));
        FileUtils.mFileUtils.setfileimg(mAttachmentActivity.mImageView, mAttachmentActivity.attachment.mName);
        File mFile = new File(mAttachmentActivity.attachment.mPath);
        if(mAttachmentActivity.attachment.mSize == 0)
        {
           DownloadInfo downloadInfo =  DBHelper.getInstance(mAttachmentActivity).getInfo(mAttachmentActivity.attachment.mUrl);
           if(downloadInfo != null)
           {
               mAttachmentActivity.attachment.mSize = downloadInfo.size;
           }
        }

        if (mFile.exists()) {
            if (mFile.length() == mAttachmentActivity.attachment.mSize && mFile.length() != 0) {
                mAttachmentActivity.downloadfinish = true;
                doOpen();
                mAttachmentActivity.mSizeView.setVisibility(View.INVISIBLE);
                mAttachmentActivity.mProgressBar.setVisibility(View.INVISIBLE);
                mAttachmentActivity.mPathView.setVisibility(View.VISIBLE);
                mAttachmentActivity.mfSizeView.setVisibility(View.VISIBLE);
                return;
            } else {
                DownloadThread mDownloadTask = FileUtils.mFileUtils.downloadAttachments.get(mAttachmentActivity.attachment.mUrl);
                if(mDownloadTask == null)
                {
                    mFile.delete();
                    mDownloadTask = new DownloadThread(
                            mAttachmentActivity.attachment,mAttachmentActivity.attachment);
                    FileUtils.mFileUtils.addDownloadTask(mAttachmentActivity.attachment.mUrl,mDownloadTask);
                }
                return;
            }
        }
        else {
            if (FileUtils.freeSpaceOnSd() < 100) {
                AppUtils.showMessage(mAttachmentActivity, mAttachmentActivity.getString(R.string.attachment_outcoche));
                return;
            } else {
                DownloadThread mDownloadTask = new DownloadThread(
                        mAttachmentActivity.attachment,mAttachmentActivity.attachment);
                FileUtils.mFileUtils.addDownloadTask(mAttachmentActivity.attachment.mUrl,mDownloadTask);
                return;
            }
        }

    }

    public void doMore() {
        if (mAttachmentActivity.downloadfinish) {
            File mfile = new File(mAttachmentActivity.attachment.mPath);
            if (mfile.exists())
                showDialog();
            else
                AppUtils.showMessage(mAttachmentActivity, mAttachmentActivity.getString(R.string.attachment_downloadununexist));
        } else {
            AppUtils.showMessage(mAttachmentActivity, mAttachmentActivity.getString(R.string.attachment_downloadagain));
        }
    }

    private void showDialog() {
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        if(mAttachmentActivity.getIntent().getBooleanExtra("isdoc",false) == false)
        {
            MenuItem item = new MenuItem();
            item.mListener = mAttachmentActivity.shareListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_share);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.sendMailListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_sendmail);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.saveDocumentListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_savedocument);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.saveLocalListener;
            item .item = mAttachmentActivity.attachment;
            if (FileUtils.mFileUtils.getFileType(mAttachmentActivity.attachment.mName) == FileUtils.FILE_TYPE_PICTURE)
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_savelocalpic);
            }
            else
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_savelocal);
            }
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.sendChatListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_sendim);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.openListener;
            item .item = mAttachmentActivity.attachment;
            if (FileUtils.mFileUtils.getFileType(mAttachmentActivity.attachment.mName) == FileUtils.FILE_TYPE_PICTURE)
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_openpic);
            }
            else
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_openfile);
            }

            items.add(item);
        }
        else
        {
            MenuItem item = new MenuItem();
            item.mListener = mAttachmentActivity.shareListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_share);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.sendMailListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_sendmail);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.saveLocalListener;
            item .item = mAttachmentActivity.attachment;
            if (FileUtils.mFileUtils.getFileType(mAttachmentActivity.attachment.mName) == FileUtils.FILE_TYPE_PICTURE)
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_savelocalpic);
            }
            else
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_savelocal);
            }
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.sendChatListener;
            item .item = mAttachmentActivity.attachment;
            item.btnName = mAttachmentActivity.getString(R.string.button_word_sendim);
            items.add(item);
            item = new MenuItem();
            item.mListener = mAttachmentActivity.openListener;
            item .item = mAttachmentActivity.attachment;
            if (FileUtils.mFileUtils.getFileType(mAttachmentActivity.attachment.mName) == FileUtils.FILE_TYPE_PICTURE)
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_openpic);
            }
            else
            {
                item.btnName = mAttachmentActivity.getString(R.string.button_word_openfile);
            }
            items.add(item);
        }
        mAttachmentActivity.popupWindow1 = AppUtils.creatButtomMenu(mAttachmentActivity,mAttachmentActivity.shade,items,mAttachmentActivity.findViewById(R.id.activity_fu_jian_dowanload));
    }

    public void doOpen() {
        if (mAttachmentActivity.downloadfinish) {

            File mfile = new File(mAttachmentActivity.attachment.mPath);

            if (mfile.exists() && mfile.length() > 0) {

                Intent mIntent = FileUtils.mFileUtils.openfile(mfile);
                if (mIntent != null) {
                    mAttachmentActivity.startActivity(mIntent);
                    return;
                } else {
                    AppUtils.showMessage(mAttachmentActivity, mAttachmentActivity.getString(R.string.file_unknowfile));
                    return;
                }
            }
            else {
                if (FileUtils.freeSpaceOnSd() < 100) {
                    AppUtils.showMessage(mAttachmentActivity, mAttachmentActivity.getString(R.string.attachment_outcoche));
                    return;
                } else {
                    DownloadThread mDownloadTask = new DownloadThread(
                            mAttachmentActivity.attachment,mAttachmentActivity.attachment);
                    FileUtils.mFileUtils.addDownloadTask(mAttachmentActivity.attachment.mUrl,mDownloadTask);
                    return;
                }
            }

        } else {
            AppUtils.showMessage(mAttachmentActivity, mAttachmentActivity.getString(R.string.file_downloadunfinish));
            return;
        }
    }

    public void doUpdata(Intent intent) {
        mAttachmentActivity.attachment.mFinishSize = intent.getLongExtra("finishsize", 0);
        if(mAttachmentActivity.attachment.mSize == 0)
        {
            mAttachmentActivity.attachment.mSize = intent.getLongExtra("totalsize", 0);
        }
        if(mAttachmentActivity.attachment.mSize > 0)
        {
            mAttachmentActivity.mSizeView
                    .setText(AppUtils.getSizeText(mAttachmentActivity.attachment.mFinishSize) + "/"
                            + AppUtils.getSizeText(mAttachmentActivity.attachment.mSize));
            mAttachmentActivity.mProgressBar
                    .setProgress((int) (1000 * mAttachmentActivity.attachment.mFinishSize
                            / mAttachmentActivity.attachment.mSize));
        }else
        {
            mAttachmentActivity.mSizeView.setText(
                    AppUtils.getSizeText(mAttachmentActivity.attachment.mFinishSize) + "/" + mAttachmentActivity.getString(R.string.sex_unknowfemale));
        }
    }

    public void doFinishDown(Intent intent) {

        File file = new File(mAttachmentActivity.attachment.mPath);
        mAttachmentActivity.downloadfinish = true;
        mAttachmentActivity.mSizeView.setVisibility(View.INVISIBLE);
        mAttachmentActivity.mProgressBar.setVisibility(View.INVISIBLE);
        mAttachmentActivity.mPathView.setVisibility(View.VISIBLE);
        mAttachmentActivity.mfSizeView.setVisibility(View.VISIBLE);
        mAttachmentActivity.mfSizeView
                .setText(mAttachmentActivity.getString(R.string.title_attachment_size) + "：" + AppUtils.getSizeText(mAttachmentActivity.attachment.mSize));
        //if(mAttachmentDowanloadActivity.startType == AttachmentDowanloadActivity.START_TYPE_CLOD)
        DownloadInfo downloadInfo = new DownloadInfo();
        if(mAttachmentActivity.attachment.mSize > 0)
        {
            downloadInfo.url = mAttachmentActivity.attachment.mUrl;
            downloadInfo.size = mAttachmentActivity.attachment.mSize;
            File file1 = new File(mAttachmentActivity.attachment.mPath);
            downloadInfo.name = file1.getName();
            DBHelper.getInstance(mAttachmentActivity).addInfo(downloadInfo);
        }
        doOpen();
    }

    public void doSaveloacl(Attachment attachment) {
        File file = new File(attachment.mPath);
        if(file.exists()) {
            FileUtils.mFileUtils.saveLocal(mAttachmentActivity,file);
        }
        else {
            AppUtils.showMessage(mAttachmentActivity,mAttachmentActivity.getString(R.string.file_unexitst));
        }
    }
}
