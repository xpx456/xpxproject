package intersky.filetools.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import intersky.appbase.entity.Attachment;
import intersky.filetools.FileUtils;
import intersky.filetools.presenter.AttachmentPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class AttachmentActivity extends BaseActivity {

    public Attachment attachment;
    public ProgressBar mProgressBar;
    public TextView mNameView;
    public TextView mPathView;
    public TextView mSizeView;
    public TextView mfSizeView;
    public ImageView mImageView;
    public RelativeLayout shade;
    public PopupWindow popupWindow1;
    public boolean downloadfinish = false;
    public AttachmentPresenter mAttachmentPresenter = new AttachmentPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttachmentPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAttachmentPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener showMoreListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mAttachmentPresenter.doMore();
        }
    };

    public View.OnClickListener sendMailListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Attachment attachment = (Attachment) v.getTag();
            FileUtils.mFileUtils.operation.sendMail(mAttachmentPresenter.mAttachmentActivity,attachment.mPath);
            mAttachmentPresenter.mAttachmentActivity.popupWindow1.dismiss();
        }
    };

    public View.OnClickListener sendChatListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            FileUtils.mFileUtils.operation.sendChat(mAttachmentPresenter.mAttachmentActivity,attachment.mPath);
            mAttachmentPresenter.mAttachmentActivity.popupWindow1.dismiss();
        }
    };

    public View.OnClickListener shareListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            FileUtils.mFileUtils.operation.share(mAttachmentPresenter.mAttachmentActivity,attachment.mName,attachment.mPath);
            mAttachmentPresenter.mAttachmentActivity.popupWindow1.dismiss();
        }
    };

    public View.OnClickListener saveDocumentListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            FileUtils.mFileUtils.operation.sendDocument(mAttachmentPresenter.mAttachmentActivity,attachment.mPath);
            mAttachmentPresenter.mAttachmentActivity.popupWindow1.dismiss();
        }
    };

    public View.OnClickListener saveLocalListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mAttachmentPresenter.doSaveloacl(attachment);
        }
    };

    public View.OnClickListener openListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            mAttachmentPresenter.doOpen();
        }
    };
}
