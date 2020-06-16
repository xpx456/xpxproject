package intersky.mail.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mail.AttachmentUploadThread;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.URLImageParser;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.handler.MailEditHandler;
import intersky.mail.prase.MailPrase;
import intersky.mail.receiver.MailEditReceiver;
import intersky.mail.view.activity.MailAddressActivity;
import intersky.mail.view.activity.MailContactsActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.mail.view.adapter.AttachmentAdapter;
import intersky.mail.view.adapter.MailSelectAdapter;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.MyLinearLayout;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailEditPresenter implements Presenter {

    public MailEditHandler mMailEditHandler;
    public MailEditActivity mMailEditActivity;
    public MailEditPresenter(MailEditActivity mMailEditActivity) {
        this.mMailEditActivity = mMailEditActivity;
        this.mMailEditHandler = new MailEditHandler(mMailEditActivity);
        mMailEditActivity.setBaseReceiver(new MailEditReceiver(mMailEditHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {

        mMailEditActivity.setContentView(R.layout.activity_mail_edit);
        mMailEditActivity.mAction = mMailEditActivity.getIntent().getIntExtra("action", MailManager.ACTION_NEW);
        mMailEditActivity.mMail = mMailEditActivity.getIntent().getParcelableExtra("maildata");
        if (mMailEditActivity.getIntent().getBooleanExtra("sendfujian", false)) {
            Attachment mAttachment = mMailEditActivity.getIntent().getParcelableExtra("attachment");
            mMailEditActivity.mMail.attachments.add(mAttachment);
        }
        if (mMailEditActivity.mAction == MailManager.ACTION_REPEAT
                || mMailEditActivity.mAction == MailManager.ACTION_REPEATALL || mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
            initrepeat();
        }


        mMailEditActivity.mshada = (RelativeLayout) mMailEditActivity.findViewById(R.id.shade);
        ToolBarHelper.setBackListenr(mMailEditActivity.mActionBar, mMailEditActivity.mBackListener, mMailEditActivity.getString(R.string.button_word_cancle));
        mMailEditActivity.mWebView = (WebView) mMailEditActivity.findViewById(R.id.mail_webview);
        mMailEditActivity.mShoujianLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.shoujianren_layer);
        mMailEditActivity.mShoujianContentLayer = (MyLinearLayout) mMailEditActivity.findViewById(R.id.shoujianren_content);
        mMailEditActivity.mLccLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.lcc_layer);
        mMailEditActivity.mLccLayerContentLayer = (MyLinearLayout) mMailEditActivity.findViewById(R.id.lcc_content);
        mMailEditActivity.mCcLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.cc_layer);
        mMailEditActivity.mCcLayerContentLayer = (MyLinearLayout) mMailEditActivity.findViewById(R.id.cc_content);
        mMailEditActivity.mBccLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.bcc_layer);
        mMailEditActivity.mBccLayerContentLayer = (MyLinearLayout) mMailEditActivity.findViewById(R.id.bcc_content);
        mMailEditActivity.mFajianLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.from_layer);
        mMailEditActivity.mShoujianAdd = (ImageView) mMailEditActivity.findViewById(R.id.shoujian_addimg);
        mMailEditActivity.mLccAdd = (ImageView) mMailEditActivity.findViewById(R.id.lcc_addimg);
        mMailEditActivity.mCcAdd = (ImageView) mMailEditActivity.findViewById(R.id.cc_addimg);
        mMailEditActivity.mCcAdd.setVisibility(View.INVISIBLE);
        mMailEditActivity.mBccAdd = (ImageView) mMailEditActivity.findViewById(R.id.bcc_addimg);
        mMailEditActivity.mBccAdd.setVisibility(View.INVISIBLE);
        mMailEditActivity.mFajian = (TextView) mMailEditActivity.findViewById(R.id.from_text);
        mMailEditActivity.mFajianTitle = (TextView) mMailEditActivity.findViewById(R.id.from_title);
        mMailEditActivity.mZhuti = (EditText) mMailEditActivity.findViewById(R.id.theme_text);
        mMailEditActivity.mContent = (EditText) mMailEditActivity.findViewById(R.id.content_text);
        mMailEditActivity.mFujian = (ImageView) mMailEditActivity.findViewById(R.id.fujian_img);
        mMailEditActivity.mButtomLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.fujian_button_layer);
        mMailEditActivity.mfujianLayer = (RelativeLayout) mMailEditActivity.findViewById(R.id.fujian_img_layer);
        mMailEditActivity.mFujianList = (HorizontalListView) mMailEditActivity.findViewById(R.id.fujian_listview);
        mMailEditActivity.takePhoto = (RelativeLayout) mMailEditActivity.findViewById(R.id.buttom_takephoto);
        mMailEditActivity.picture = (RelativeLayout) mMailEditActivity.findViewById(R.id.buttom_picture);
        mMailEditActivity.video = (RelativeLayout) mMailEditActivity.findViewById(R.id.buttom_video);
        mMailEditActivity.mFujian.setOnClickListener(mMailEditActivity.mFujianListener);
        mMailEditActivity.mAttachmentAdapter = new AttachmentAdapter(mMailEditActivity, mMailEditActivity.mMail.attachments);
        mMailEditActivity.mFujianList.setAdapter(mMailEditActivity.mAttachmentAdapter);
        mMailEditActivity.mMailSelectAdapter = new MailSelectAdapter(mMailEditActivity, MailManager.getInstance().mMailBoxs);
        mMailEditActivity.mShoujianLayer.setOnClickListener(mMailEditActivity.onEditclicklisten1);
        mMailEditActivity.mLccLayer.setOnClickListener(mMailEditActivity.onEditclicklisten12);
        mMailEditActivity.mCcLayerContentLayer.setOnClickListener(mMailEditActivity.onEditclicklisten2);
        mMailEditActivity.mBccLayerContentLayer.setOnClickListener(mMailEditActivity.onEditclicklisten4);
        mMailEditActivity.mZhuti.setOnClickListener(mMailEditActivity.onEditclicklisten3);
        mMailEditActivity.mZhuti.setOnFocusChangeListener(mMailEditActivity.mOnFocusChangeListener3);
        mMailEditActivity.mZhuti.setOnEditorActionListener(mMailEditActivity.mOnEditorActionListener4);
        mMailEditActivity.mContent.setOnClickListener(mMailEditActivity.montouchlistener);
        mMailEditActivity.mContent.setOnFocusChangeListener(mMailEditActivity.mOnFocusChangeListener4);
        mMailEditActivity.takePhoto.setOnClickListener(mMailEditActivity.mtekePhotoListener);
        mMailEditActivity.picture.setOnClickListener(mMailEditActivity.mPictureListener);
        mMailEditActivity.video.setOnClickListener(mMailEditActivity.mVideoListener);
        mMailEditActivity.mShoujianAdd.setOnClickListener(mMailEditActivity.mAddClickListener1);
        mMailEditActivity.mCcAdd.setOnClickListener(mMailEditActivity.mAddClickListener2);
        mMailEditActivity.mBccAdd.setOnClickListener(mMailEditActivity.mAddClickListener3);
        mMailEditActivity.mLccAdd.setOnClickListener(mMailEditActivity.mAddClickListener4);
        mMailEditActivity.mFajian.setOnClickListener(mMailEditActivity.mFajianlistener);
        mMailEditActivity.mFujianList.setOnItemClickListener(mMailEditActivity.mFujianItemClick);


        switch (mMailEditActivity.mAction) {
            case MailManager.ACTION_NEW:
                ToolBarHelper.setTitle(mMailEditActivity.mActionBar, mMailEditActivity.getString(R.string.mail_newmail));
                ToolBarHelper.setRightBtnText(mMailEditActivity.mActionBar, mMailEditActivity.mSendListener, "   " + mMailEditActivity.getString(R.string.button_word_send));
                break;
            case MailManager.ACTION_REPEAT:
            case MailManager.ACTION_REPEATALL:
                ToolBarHelper.setTitle(mMailEditActivity.mActionBar, mMailEditActivity.getString(R.string.mail_repickmail));
                ToolBarHelper.setRightBtnText(mMailEditActivity.mActionBar, mMailEditActivity.mSendListener, "   " + mMailEditActivity.getString(R.string.button_word_send));
                break;
            case MailManager.ACTION_EDIT:
                ToolBarHelper.setTitle(mMailEditActivity.mActionBar, mMailEditActivity.getString(R.string.mail_resendmail));
                ToolBarHelper.setRightBtnText(mMailEditActivity.mActionBar, mMailEditActivity.mSendListener, "   " + mMailEditActivity.getString(R.string.button_word_save));
                break;
            case MailManager.ACTION_RESEND:
                ToolBarHelper.setTitle(mMailEditActivity.mActionBar, mMailEditActivity.getString(R.string.mail_editmail));
                ToolBarHelper.setRightBtnText(mMailEditActivity.mActionBar, mMailEditActivity.mSendListener, "   " + mMailEditActivity.getString(R.string.button_word_send));
                break;
        }
        initMailPerosnView();
        if (mMailEditActivity.mMail != null) {
            if (mMailEditActivity.mAction == MailManager.ACTION_EDIT) {
                mMailEditActivity.mContent.setText(Html.fromHtml(mMailEditActivity.mMail.mContentHtml,
                        new URLImageParser(mMailEditActivity.mContent, mMailEditActivity), tagHandler));
            } else {
                mMailEditActivity.mWebView.loadDataWithBaseURL(
                        "http://" + MailManager.getInstance().service.sAddress + ":"
                                + MailManager.getInstance().service.sPort,
                        mMailEditActivity.mMail.mContentHtml, "text/html", "utf-8", null);
                mMailEditActivity.mWebView.setVisibility(View.VISIBLE);
            }
        }
        if (mMailEditActivity.mAction != MailManager.ACTION_NEW) {
            int aciton = mMailEditActivity.mAction;
            if (aciton == MailManager.ACTION_REPEATALL) {
                aciton = MailManager.ACTION_REPEAT;
            }
            if (!MailManager.getInstance().mSelectMailBox.mRecordId.equals(mMailEditActivity.mMail.mMailBoxId)) {
                for (int i = 0; i < MailManager.getInstance().mMailBoxs.size(); i++) {
                    if (MailManager.getInstance().mMailBoxs.get(i).mRecordId.equals(mMailEditActivity.mMail.mMailBoxId)) {
                        MailManager.getInstance().mMailBoxs.get(i).isSelect = true;
                        MailManager.getInstance().mSelectMailBox = MailManager.getInstance().mMailBoxs.get(i);
                    }
                    MailManager.getInstance().mMailBoxs.get(i).isSelect = false;
                }
            }
            if(MailManager.getInstance().account.iscloud == false)
            {
                MailAsks.getmailData(mMailEditActivity, mMailEditHandler, mMailEditActivity.mMail.mRecordId, aciton);
                mMailEditActivity.waitDialog.show();
            }
            else
            {
                mMailEditHandler.sendEmptyMessageDelayed(MailAsks.MAIL_NEW_MAIL_SUCCESS, 100);
            }
        } else {
            mMailEditHandler.sendEmptyMessageDelayed(MailAsks.MAIL_NEW_MAIL_SUCCESS, 100);
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

    public void initData() {

    }

    public void doSend() {
        InputMethodManager imm2 = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mShoujian.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mZhuti.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mCc.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mBcc.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mContent.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mLcc.getWindowToken(), 0);
        if(MailManager.getInstance().account.iscloud)
        {
            sendMail(true);
        }
        else
        {
            if (mMailEditActivity.count == mMailEditActivity.finishcount)
                sendMail(true);
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mMailEditActivity);
                builder.setTitle(mMailEditActivity.getString(R.string.mail_attachment_unfinish));
                builder.setNegativeButton(mMailEditActivity.getString(R.string.button_word_cancle), null);
                builder.setPositiveButton(mMailEditActivity.getString(R.string.button_word_continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendMail(true);
                    }
                });
                builder.show();
            }
        }


    }

    public void doBack() {
        InputMethodManager imm2 = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mShoujian.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mZhuti.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mCc.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mBcc.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mContent.getWindowToken(), 0);
        imm2.hideSoftInputFromWindow(mMailEditActivity.mLcc.getWindowToken(), 0);

        if (mMailEditActivity.mToPesrons.size() == 0 && mMailEditActivity.mCcPesrons.size() == 0 && mMailEditActivity.mLccPesrons.size() == 0
                && mMailEditActivity.mBccPesrons.size() == 0 && mMailEditActivity.mZhuti.getText().toString().length() == 0
                && mMailEditActivity.mContent.getText().toString().length() == 0) {
            mMailEditActivity.finish();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mMailEditActivity);
            builder.setMessage(mMailEditActivity.getString(R.string.mail_exitmailtip));
            builder.setTitle(mMailEditActivity.getString(R.string.mail_exitmail));
            builder.setNegativeButton(mMailEditActivity.getString(R.string.button_word_cancle), null);
            builder.setNeutralButton(mMailEditActivity.getString(R.string.mail_savemail), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    sendMail(false);
                }
            });
            builder.setPositiveButton(mMailEditActivity.getString(R.string.button_word_exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    mMailEditActivity.finish();
                }
            });
            builder.create().show();
        }


    }

    public void praseMail() {
        if (mMailEditActivity.mMail != null) {
            if (mMailEditActivity.mMail.haveAttachment == true) {
                try {
                    JSONArray ja = new JSONArray(mMailEditActivity.mMail.mAttachmentsJson);
                    MailPrase.praseAttachment(ja, mMailEditActivity.mMail);
                    mMailEditActivity.finishcount = mMailEditActivity.mMail.attachments.size();
                    if(MailManager.getInstance().account.iscloud == false)
                    getMailAttachment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initMailDetial();
        } else {

        }
        mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
        setEditLoaction();
    }

    public void getMailAttachment() {
        boolean has = false;
        for (int i = 0; i < mMailEditActivity.mMail.attachments.size(); i++) {
            Attachment mAttachmentModel = mMailEditActivity.mMail.attachments.get(i);
            File mFile = new File(mAttachmentModel.mPath);
            if (mFile.exists()) {
                if (mFile.length() == mAttachmentModel.mSize) {
                    mMailEditActivity.count++;
                } else {

                    boolean con = (boolean) Bus.callData(mMailEditActivity,"filetools/hasThread",mAttachmentModel.mUrl);
                    if(con == false)
                    {
                        mFile.delete();
                        Bus.callData(mMailEditActivity,"filetools/addDownloadTask",mAttachmentModel.mUrl
                                ,Bus.callData(mMailEditActivity,"filetools/downloadthread",mAttachmentModel,MailManager.EVENT_MAIL_DOWNLOAD_FAIL,MailManager.EVENT_MAIL_UPADA_DOWNLOAD,MailManager.EVENT_MAIL_FINISH_DOWNLOAD,mAttachmentModel));
                    }
                    has = true;
                }
            } else {
                int a = (int) Bus.callData(mMailEditActivity,"filetools/freeSpaceOnSd","");
                if (a < 100) {
                    AppUtils.showMessage(mMailEditActivity, mMailEditActivity.getString(R.string.attachment_outcoche));
                    return;
                } else {
                    Bus.callData(mMailEditActivity,"filetools/addDownloadTask",mAttachmentModel.mUrl
                            ,Bus.callData(mMailEditActivity,"filetools/downloadthread",mAttachmentModel,MailManager.EVENT_MAIL_DOWNLOAD_FAIL,MailManager.EVENT_MAIL_UPADA_DOWNLOAD,MailManager.EVENT_MAIL_FINISH_DOWNLOAD,mAttachmentModel));
                    has = true;
                }
            }

        }
        if (has) {
//				mMailEditActivity.waitDialog.setCancelable(false);
            mMailEditActivity.waitDialog.show();
        }
    }

    public void updateAttachment(Intent intent) {
        Attachment attachment = intent.getParcelableExtra("attchment");
        for(int i = 0 ; i < mMailEditActivity.mMail.attachments.size() ; i++)
        {
            if(mMailEditActivity.mMail.attachments.get(i).mRecordid.equals(attachment.mRecordid))
            {
                mMailEditActivity.mMail.attachments.get(i).mFinishSize = intent.getLongExtra("finishsize",0);
                mMailEditActivity.mMail.attachments.get(i).mSize = intent.getLongExtra("totalsize",0);
                break;
            }
        }
        mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
    }

    public void addAttchment(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        if(MailManager.getInstance().account.iscloud)
        {
            for (int i = 0 ; i < attachments.size() ; i++) {
                Attachment attachmentModel = attachments.get(i);
                File file = new File(attachmentModel.mPath);
                MultipartBody.Builder builder = new MultipartBody.Builder();
                RequestBody fileBody = RequestBody.create(NetUtils.MEDIA_TYPE_MARKDOWN, file);
                builder.addFormDataPart("file", file.getName(), fileBody);
                builder.addFormDataPart("uid", MailManager.getInstance().account.mRecordId);
                builder.addFormDataPart("type", "2");
                attachmentModel.stata = Attachment.STATA_NOSTART;
                mMailEditActivity.mMail.attachments.add(attachmentModel);
                MailManager.getInstance().upthreads.add(new AttachmentUploadThread(attachmentModel, builder, mMailEditHandler));
            }
            if (mMailEditHandler != null) {
                Message msg = new Message();
                msg.what = AttachmentUploadThread.EVENT_UPLOAD_FUJIAN_NEXT;
                msg.obj = new NetObject();
                mMailEditHandler.sendMessage(msg);
            }
        }
        else {
            mMailEditActivity.mMail.attachments.addAll(attachments);
        }
        mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
    }

    public void praseMail(NetObject net) {

        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == true)
            {

            }JSONObject jsonObject = new JSONObject(json);
            mMailEditActivity.mRecordID = jsonObject.getString("ID");
            if (jsonObject.getString("From").length() != 0) {
                //mMailEditActivity.mMail.setmFrom(jsonObject.getString("From"));
            }
            if (jsonObject.getString("To").length() != 0) {
                //mMailEditActivity.mMail.setmTo(jsonObject.getString("To"));
            }
            if (jsonObject.getString("Cc").length() != 0) {
                mMailEditActivity.mMail.mCc = jsonObject.getString("Cc");
            }
            if (jsonObject.getString("BCc").length() != 0) {
                mMailEditActivity.mMail.mBcc = jsonObject.getString("BCc");
            }
            if (jsonObject.getString("LocalCc").length() != 0) {
                mMailEditActivity.mMail.mLcc = jsonObject.getString("LocalCc");
            }
            if (jsonObject.getString("Subject").length() != 0) {
                mMailEditActivity.mMail.mSubject = jsonObject.getString("Subject");
            } else {
                if (mMailEditActivity.mAction == MailManager.ACTION_REPEAT || mMailEditActivity.mAction == MailManager.ACTION_REPEATALL) {

                    mMailEditActivity.mZhuti.setText("Re:" + mMailEditActivity.mMail.mSubject);
                } else if (mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
                    mMailEditActivity.mZhuti.setText("Fw:" + mMailEditActivity.mMail.mSubject);
                } else {
                    mMailEditActivity.mZhuti.setText(mMailEditActivity.mMail.mSubject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (mMailEditActivity.mAction == MailManager.ACTION_REPEAT || mMailEditActivity.mAction == MailManager.ACTION_REPEATALL) {

                mMailEditActivity.mZhuti.setText("Re:" + mMailEditActivity.mMail.mSubject);
            } else if (mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
                mMailEditActivity.mZhuti.setText("Fw:" + mMailEditActivity.mMail.mSubject);
            } else {
                mMailEditActivity.mZhuti.setText(mMailEditActivity.mMail.mSubject);
            }
        }
        praseMail();
    }

    public void onfajianlistener() {
        if (mMailEditActivity.unshowall == true) {
            mMailEditActivity.onShoujian = false;
            mMailEditActivity.onCc = true;
            mMailEditActivity.onBcc = false;
            mMailEditActivity.onZhuti = false;
            mMailEditActivity.onContent = false;
            mMailEditActivity.mShoujian.clearFocus();
            mMailEditActivity.mBcc.clearFocus();
            mMailEditActivity.mCc.requestFocus();
            mMailEditActivity.mZhuti.clearFocus();
            mMailEditActivity.mContent.clearFocus();
            mMailEditActivity.mLcc.clearFocus();
            mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
            mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
            mMailEditActivity.isFujian = false;
            InputMethodManager imm = (InputMethodManager) mMailEditActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mMailEditActivity.mCc, InputMethodManager.SHOW_FORCED);
        } else {
            showSelectDialog();
        }
    }

    public void onKeylistener(int type) {
        switch (type) {
            case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                if (mMailEditActivity.mToPesrons.size() > 0) {
                    if (mMailEditActivity.lastSelectTo != null) {
                        mMailEditActivity.mShoujianContentLayer
                                .removeViewAt(mMailEditActivity.mToPesrons.indexOf(mMailEditActivity.lastSelectTo) + 1);
                        mMailEditActivity.mToPesrons.remove(mMailEditActivity.lastSelectTo);
                        mMailEditActivity.lastSelectTo = null;
                    } else if (mMailEditActivity.mShoujian.getText().toString().length() == 0) {
                        mMailEditActivity.mShoujianContentLayer
                                .removeViewAt(mMailEditActivity.mShoujianContentLayer.getChildCount() - 2);
                        mMailEditActivity.mToPesrons.remove(mMailEditActivity.mToPesrons.size() - 1);
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CC:
                if (mMailEditActivity.mCcPesrons.size() > 0) {
                    if (mMailEditActivity.lastselectCC != null) {
                        mMailEditActivity.mCcLayerContentLayer
                                .removeViewAt(mMailEditActivity.mCcPesrons.indexOf(mMailEditActivity.lastselectCC) + 1);
                        mMailEditActivity.mCcPesrons.remove(mMailEditActivity.lastselectCC);
                        mMailEditActivity.lastselectCC = null;
                    } else if (mMailEditActivity.mCc.getText().toString().length() == 0) {
                        mMailEditActivity.mCcLayerContentLayer
                                .removeViewAt(mMailEditActivity.mCcLayerContentLayer.getChildCount() - 2);
                        mMailEditActivity.mCcPesrons.remove(mMailEditActivity.mCcPesrons.size() - 1);
                    }
                }

                break;
            case MailEditActivity.CONTENT_TYPE_BCC:
                if (mMailEditActivity.mBccPesrons.size() > 0) {
                    if (mMailEditActivity.lastselectBCC != null) {
                        mMailEditActivity.mBccLayerContentLayer
                                .removeViewAt(mMailEditActivity.mBccPesrons.indexOf(mMailEditActivity.lastselectBCC) + 1);
                        mMailEditActivity.mBccPesrons.remove(mMailEditActivity.lastselectBCC);
                        mMailEditActivity.lastselectBCC = null;
                    } else if (mMailEditActivity.mBcc.getText().toString().length() == 0) {
                        mMailEditActivity.mBccLayerContentLayer
                                .removeViewAt(mMailEditActivity.mBccLayerContentLayer.getChildCount() - 2);
                        mMailEditActivity.mBccPesrons.remove(mMailEditActivity.mBccPesrons.size() - 1);
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_LCC:
                if (mMailEditActivity.mLccPesrons.size() > 0) {
                    if (mMailEditActivity.lastSelectLcc != null) {
                        mMailEditActivity.mLccLayerContentLayer
                                .removeViewAt(mMailEditActivity.mLccPesrons.indexOf(mMailEditActivity.lastSelectLcc) + 1);
                        mMailEditActivity.mToPesrons.remove(mMailEditActivity.lastSelectLcc);
                        mMailEditActivity.lastSelectLcc = null;
                    } else if (mMailEditActivity.mLcc.getText().toString().length() == 0) {
                        mMailEditActivity.mLccLayerContentLayer
                                .removeViewAt(mMailEditActivity.mLccLayerContentLayer.getChildCount() - 2);
                        mMailEditActivity.mLccPesrons.remove(mMailEditActivity.mLccPesrons.size() - 1);
                    }
                }
                break;
        }
    }

    public void onPersonTextListener(int type, MailContact mMailGuest) {
        boolean isselect;
        InputMethodManager imm;
        switch (type) {
            case MailEditActivity.CONTENT_TYPE_SHOUJIAN:

                isselect = mMailGuest.isselect;
                if (mMailEditActivity.lastSelectTo != null) {
                    mMailEditActivity.lastSelectTo.isselect = false;
                    mMailEditActivity.lastSelectTo.reSetmTextView();
                }
                if (isselect) {
                    poernimf(mMailGuest, false);
                } else {
                    mMailGuest.isselect = true;
                    mMailGuest.reSetmTextView();
                }
                mMailEditActivity.lastSelectTo = mMailGuest;
                mMailEditActivity.onShoujian = true;
                mMailEditActivity.onLcc = false;
                mMailEditActivity.onCc = false;
                mMailEditActivity.onBcc = false;
                mMailEditActivity.onZhuti = false;
                mMailEditActivity.onContent = false;
                mMailEditActivity.mShoujian.requestFocus();
                mMailEditActivity.mBcc.clearFocus();
                mMailEditActivity.mCc.clearFocus();
                mMailEditActivity.mZhuti.clearFocus();
                mMailEditActivity.mContent.clearFocus();
                mMailEditActivity.mLcc.clearFocus();
                mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                mMailEditActivity.isFujian = false;
                imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMailEditActivity.mShoujian, InputMethodManager.SHOW_FORCED);
                break;
            case MailEditActivity.CONTENT_TYPE_CC:
                isselect = mMailGuest.isselect;
                if (mMailEditActivity.lastselectCC != null) {
                    mMailEditActivity.lastselectCC.isselect = false;
                    mMailEditActivity.lastselectCC.reSetmTextView();
                }
                if (isselect) {
                    poernimf(mMailGuest, false);
                } else {
                    mMailGuest.isselect = true;
                    mMailGuest.reSetmTextView();
                }
                mMailEditActivity.lastselectCC = mMailGuest;
                mMailEditActivity.onShoujian = false;
                mMailEditActivity.onLcc = false;
                mMailEditActivity.onCc = true;
                mMailEditActivity.onBcc = false;
                mMailEditActivity.onZhuti = false;
                mMailEditActivity.onContent = false;
                mMailEditActivity.mShoujian.clearFocus();
                mMailEditActivity.mBcc.clearFocus();
                mMailEditActivity.mCc.requestFocus();
                mMailEditActivity.mZhuti.clearFocus();
                mMailEditActivity.mContent.clearFocus();
                mMailEditActivity.mLcc.clearFocus();
                mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams2 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                mFajianLayerParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams2);
                mMailEditActivity.isFujian = false;
                imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMailEditActivity.mCc, InputMethodManager.SHOW_FORCED);
                break;
            case MailEditActivity.CONTENT_TYPE_BCC:
                isselect = mMailGuest.isselect;
                if (mMailEditActivity.lastselectBCC != null) {
                    mMailEditActivity.lastselectBCC.isselect = false;
                    mMailEditActivity.lastselectBCC.reSetmTextView();
                }
                if (isselect) {
                    poernimf(mMailGuest, false);
                } else {
                    mMailGuest.isselect = true;
                    mMailGuest.reSetmTextView();
                }
                mMailEditActivity.lastselectBCC = mMailGuest;
                mMailEditActivity.onShoujian = false;
                mMailEditActivity.onCc = false;
                mMailEditActivity.onBcc = true;
                mMailEditActivity.onLcc = false;
                mMailEditActivity.onZhuti = false;
                mMailEditActivity.onContent = false;
                mMailEditActivity.mShoujian.clearFocus();
                mMailEditActivity.mBcc.requestFocus();
                mMailEditActivity.mCc.clearFocus();
                mMailEditActivity.mZhuti.clearFocus();
                mMailEditActivity.mContent.clearFocus();
                mMailEditActivity.mLcc.clearFocus();
                mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams3 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                mFajianLayerParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams3);
                mMailEditActivity.isFujian = false;
                imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMailEditActivity.mBcc, InputMethodManager.SHOW_FORCED);
                break;
            case MailEditActivity.CONTENT_TYPE_LCC:
                isselect = mMailGuest.isselect;
                if (mMailEditActivity.lastSelectLcc != null) {
                    mMailEditActivity.lastSelectLcc.isselect = false;
                    mMailEditActivity.lastSelectLcc.reSetmTextView();
                }
                if (isselect) {
                    poernimf(mMailGuest, true);
                } else {
                    mMailGuest.isselect = true;
                    mMailGuest.reSetmTextView();
                }
                mMailEditActivity.lastSelectLcc = mMailGuest;
                mMailEditActivity.onShoujian = false;
                mMailEditActivity.onCc = false;
                mMailEditActivity.onBcc = false;
                mMailEditActivity.onLcc = true;
                mMailEditActivity.onZhuti = false;
                mMailEditActivity.onContent = false;
                mMailEditActivity.mShoujian.clearFocus();
                mMailEditActivity.mBcc.clearFocus();
                mMailEditActivity.mCc.clearFocus();
                mMailEditActivity.mLcc.requestFocus();
                mMailEditActivity.mZhuti.clearFocus();
                mMailEditActivity.mContent.clearFocus();
                mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams4 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                mFajianLayerParams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams4);
                mMailEditActivity.isFujian = false;
                imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMailEditActivity.mLcc, InputMethodManager.SHOW_FORCED);
                break;
        }
    }

    public void onNext(int type) {
        MailContact mPesrons;
        InputMethodManager imm;
        switch (type) {
            case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                if (mMailEditActivity.mShoujian.getText().toString().length() > 0) {
                    mPesrons = new MailContact(mMailEditActivity.mShoujian.getText().toString(),mMailEditActivity.mShoujian.getText().toString());
                    mMailEditActivity.mShoujian.setText("");
                    mMailEditActivity.mToPesrons.add(mPesrons);
                    onViewMeasure(mMailEditActivity.mShoujianContentLayer, mPesrons,
                            MailEditActivity.CONTENT_TYPE_SHOUJIAN);
                } else {
                    mMailEditActivity.onLcc = true;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.requestFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                    mMailEditActivity.isFujian = false;
                    imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mLcc, InputMethodManager.SHOW_FORCED);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CC:
                if (mMailEditActivity.mCc.getText().toString().length() > 0) {
                    mPesrons = new MailContact(mMailEditActivity.mCc.getText().toString(),mMailEditActivity.mCc.getText().toString());
                    mMailEditActivity.mCc.setText("");
                    mMailEditActivity.mCcPesrons.add(mPesrons);
                    onViewMeasure(mMailEditActivity.mCcLayerContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_CC);
                } else {
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = true;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.requestFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                    mMailEditActivity.isFujian = false;
                    imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mBcc, InputMethodManager.SHOW_FORCED);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_BCC:
                if (mMailEditActivity.mBcc.getText().toString().length() > 0) {
                    mPesrons = new MailContact(mMailEditActivity.mBcc.getText().toString(),mMailEditActivity.mBcc.getText().toString());
                    mMailEditActivity.mBcc.setText("");
                    mMailEditActivity.mBccPesrons.add(mPesrons);
                    onViewMeasure(mMailEditActivity.mBccLayerContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_BCC);
                } else {
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = true;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.requestFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                    mMailEditActivity.isFujian = false;
                    imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mZhuti, InputMethodManager.SHOW_FORCED);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_LCC:
                if (mMailEditActivity.mLcc.getText().toString().length() > 0) {
                    mPesrons = matchLocal(mMailEditActivity.mLcc.getText().toString());
                    boolean has = false;
                    if (mPesrons != null) {
                        has = onViewMeasure(mMailEditActivity.mLccLayerContentLayer, mPesrons,
                                MailEditActivity.CONTENT_TYPE_LCC);
                        if (has == true)
                            mMailEditActivity.mLccPesrons.add(mPesrons);
                    } else {
                        mPesrons = new MailContact(mMailEditActivity.mLcc.getText().toString(),mMailEditActivity.mLcc.getText().toString());
                        has = onViewMeasure(mMailEditActivity.mLccLayerContentLayer, mPesrons,
                                MailEditActivity.CONTENT_TYPE_LCC);
                        if (has == true)
                            mMailEditActivity.mLccPesrons.add(mPesrons);
                    }
                    mMailEditActivity.mLcc.setText("");
                } else {
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = true;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.requestFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                    mMailEditActivity.isFujian = false;
                    imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mZhuti, InputMethodManager.SHOW_FORCED);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CONTENT:
                mMailEditActivity.onLcc = false;
                mMailEditActivity.onShoujian = false;
                mMailEditActivity.onCc = false;
                mMailEditActivity.onBcc = false;
                mMailEditActivity.onZhuti = false;
                mMailEditActivity.onContent = true;
                mMailEditActivity.mShoujian.clearFocus();
                mMailEditActivity.mBcc.clearFocus();
                mMailEditActivity.mCc.clearFocus();
                mMailEditActivity.mZhuti.clearFocus();
                mMailEditActivity.mContent.requestFocus();
                mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                mMailEditActivity.isFujian = false;
                imm = (InputMethodManager) mMailEditActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMailEditActivity.mContent, InputMethodManager.SHOW_FORCED);
                break;
        }
    }

    public void OnFocusChangeListener(int type, boolean hasFocus) {
        switch (type) {
            case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                if (hasFocus) {
                    mMailEditActivity.isFujian = false;
                    mMailEditActivity.onShoujian = true;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    if (mMailEditActivity.lastselectBCC != null) {
                        mMailEditActivity.lastselectBCC.isselect = false;
                        mMailEditActivity.lastselectBCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectCC != null) {
                        mMailEditActivity.lastselectCC.isselect = false;
                        mMailEditActivity.lastselectCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectLcc != null) {
                        mMailEditActivity.lastSelectLcc.isselect = false;
                        mMailEditActivity.lastSelectLcc.reSetmTextView();
                    }
                    mMailEditActivity.mLccAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mShoujianAdd.setVisibility(View.VISIBLE);

                    mMailEditActivity.mCcAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mBccAdd.setVisibility(View.INVISIBLE);
                    // 此处为得到焦点时的处理内容
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);

                    if (mMailEditActivity.unshowall == false) {
                        mMailEditActivity.unshowall = true;
                        if (mMailEditActivity.mCcLayer != null && mMailEditActivity.mBccLayer != null
                                && mMailEditActivity.mFajianLayer != null) {
                            mMailEditActivity.mCcLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mBccLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mFajianTitle.setText(mMailEditActivity.getString(R.string.mail_bcctitle));
                            mMailEditActivity.mFajian.setTextColor(Color.rgb(149, 149, 149));
                            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mFajianLayer
                                    .getLayoutParams();
                            // mFajianLayerParams.removeRule(RelativeLayout.BELOW);
                            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.lcc_layer);
                            mMailEditActivity.mFajianLayer.setLayoutParams(mFajianLayerParams);
                        }
                    }
                } else {
                    if (mMailEditActivity.mShoujian.getText().toString().length() > 0) {
                        MailContact mPesrons = new MailContact(mMailEditActivity.mShoujian.getText().toString(),mMailEditActivity.mShoujian.getText().toString());
                        mMailEditActivity.mShoujian.setText("");
                        mMailEditActivity.mToPesrons.add(mPesrons);
                        onViewMeasure(mMailEditActivity.mShoujianContentLayer, mPesrons,
                                MailEditActivity.CONTENT_TYPE_SHOUJIAN);
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CC:
                if (hasFocus) {
                    mMailEditActivity.isFujian = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = true;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    if (mMailEditActivity.lastselectBCC != null) {
                        mMailEditActivity.lastselectBCC.isselect = false;
                        mMailEditActivity.lastselectBCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectTo != null) {
                        mMailEditActivity.lastSelectTo.isselect = false;
                        mMailEditActivity.lastSelectTo.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectLcc != null) {
                        mMailEditActivity.lastSelectLcc.isselect = false;
                        mMailEditActivity.lastSelectLcc.reSetmTextView();
                    }
                    mMailEditActivity.mLccAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mShoujianAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mCcAdd.setVisibility(View.VISIBLE);
                    mMailEditActivity.mBccAdd.setVisibility(View.INVISIBLE);
                    // 此处为得到焦点时的处理内容
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                    if (mMailEditActivity.unshowall == true) {
                        mMailEditActivity.unshowall = false;
                        if (mMailEditActivity.mCcLayer != null && mMailEditActivity.mBccLayer != null
                                && mMailEditActivity.mFajianLayer != null) {
                            mMailEditActivity.mCcLayer.setVisibility(View.VISIBLE);
                            mMailEditActivity.mBccLayer.setVisibility(View.VISIBLE);
                            mMailEditActivity.mFajianTitle.setText(mMailEditActivity.getString(R.string.mail_fromtitle));
                            mMailEditActivity.mFajian.setTextColor(Color.rgb(0, 0, 0));
                            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mFajianLayer
                                    .getLayoutParams();
                            // mFajianLayerParams.removeRule(RelativeLayout.BELOW);
                            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.bcc_layer);
                            mMailEditActivity.mFajianLayer.setLayoutParams(mFajianLayerParams);
                        }
                    }
                } else {
                    if (mMailEditActivity.mCc.getText().toString().length() > 0) {
                        MailContact mPesrons = new MailContact(mMailEditActivity.mCc.getText().toString(),mMailEditActivity.mCc.getText().toString());
                        mMailEditActivity.mCc.setText("");
                        mMailEditActivity.mCcPesrons.add(mPesrons);
                        onViewMeasure(mMailEditActivity.mCcLayerContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_CC);
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_BCC:
                if (hasFocus) {
                    mMailEditActivity.isFujian = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = true;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    if (mMailEditActivity.lastSelectTo != null) {
                        mMailEditActivity.lastSelectTo.isselect = false;
                        mMailEditActivity.lastSelectTo.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectCC != null) {
                        mMailEditActivity.lastselectCC.isselect = false;
                        mMailEditActivity.lastselectCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectLcc != null) {
                        mMailEditActivity.lastSelectLcc.isselect = false;
                        mMailEditActivity.lastSelectLcc.reSetmTextView();
                    }
                    mMailEditActivity.mLccAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mShoujianAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mCcAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mBccAdd.setVisibility(View.VISIBLE);
                    // 此处为得到焦点时的处理内容
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
                    if (mMailEditActivity.unshowall == true) {
                        mMailEditActivity.unshowall = false;
                        if (mMailEditActivity.mCcLayer != null && mMailEditActivity.mBccLayer != null
                                && mMailEditActivity.mFajianLayer != null) {
                            mMailEditActivity.mCcLayer.setVisibility(View.VISIBLE);
                            mMailEditActivity.mBccLayer.setVisibility(View.VISIBLE);
                            mMailEditActivity.mFajianTitle.setText(mMailEditActivity.getString(R.string.mail_fromtitle));
                            mMailEditActivity.mFajian.setTextColor(Color.rgb(0, 0, 0));
                            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mFajianLayer
                                    .getLayoutParams();
                            // mFajianLayerParams.removeRule(RelativeLayout.BELOW);
                            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.bcc_layer);
                            mMailEditActivity.mFajianLayer.setLayoutParams(mFajianLayerParams);
                        }
                    }
                } else {
                    if (mMailEditActivity.mBcc.getText().toString().length() > 0) {
                        MailContact mPesrons = new MailContact(mMailEditActivity.mBcc.getText().toString(),mMailEditActivity.mBcc.getText().toString());
                        mMailEditActivity.mBcc.setText("");
                        mMailEditActivity.mBccPesrons.add(mPesrons);
                        onViewMeasure(mMailEditActivity.mBccLayerContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_BCC);
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_LCC:
                if (hasFocus) {
                    mMailEditActivity.isFujian = false;
                    mMailEditActivity.onLcc = true;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    if (mMailEditActivity.lastselectBCC != null) {
                        mMailEditActivity.lastselectBCC.isselect = false;
                        mMailEditActivity.lastselectBCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectCC != null) {
                        mMailEditActivity.lastselectCC.isselect = false;
                        mMailEditActivity.lastselectCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectTo != null) {
                        mMailEditActivity.lastSelectTo.isselect = false;
                        mMailEditActivity.lastSelectTo.reSetmTextView();
                    }
                    mMailEditActivity.mShoujianAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mLccAdd.setVisibility(View.VISIBLE);
                    mMailEditActivity.mCcAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mBccAdd.setVisibility(View.INVISIBLE);
                    // 此处为得到焦点时的处理内容
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);

                    if (mMailEditActivity.unshowall == false) {
                        mMailEditActivity.unshowall = true;
                        if (mMailEditActivity.mCcLayer != null && mMailEditActivity.mBccLayer != null
                                && mMailEditActivity.mFajianLayer != null) {
                            mMailEditActivity.mCcLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mBccLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mFajianTitle.setText(mMailEditActivity.getString(R.string.mail_bcctitle));
                            mMailEditActivity.mFajian.setTextColor(Color.rgb(149, 149, 149));
                            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mFajianLayer
                                    .getLayoutParams();
                            // mFajianLayerParams.removeRule(RelativeLayout.BELOW);
                            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.lcc_layer);
                            mMailEditActivity.mFajianLayer.setLayoutParams(mFajianLayerParams);
                        }
                    }
                } else {
                    if (mMailEditActivity.mLcc.getText().toString().length() > 0) {
                        MailContact mMailPersonItem = matchLocal(mMailEditActivity.mLcc.getText().toString());
                        boolean has = false;
                        if (mMailPersonItem != null) {

                            has = onViewMeasure(mMailEditActivity.mLccLayerContentLayer, mMailPersonItem,
                                    MailEditActivity.CONTENT_TYPE_LCC);
                            if (has == true)
                                mMailEditActivity.mLccPesrons.add(mMailPersonItem);
                        } else {
                            MailContact mPesrons = new MailContact(mMailEditActivity.mLcc.getText().toString(),mMailEditActivity.mLcc.getText().toString());
                            has = onViewMeasure(mMailEditActivity.mLccLayerContentLayer, mPesrons,
                                    MailEditActivity.CONTENT_TYPE_LCC);
                            if (has == true)
                                mMailEditActivity.mLccPesrons.add(mPesrons);
                        }
                        mMailEditActivity.mLcc.setText("");
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_SUBJECT:
                if (hasFocus) {
                    mMailEditActivity.isFujian = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = true;
                    mMailEditActivity.onContent = false;
                    if (mMailEditActivity.lastSelectTo != null) {
                        mMailEditActivity.lastSelectTo.isselect  = false;
                        mMailEditActivity.lastSelectTo.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectCC != null) {
                        mMailEditActivity.lastselectCC.isselect = false;
                        mMailEditActivity.lastselectCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectBCC != null) {
                        mMailEditActivity.lastselectBCC.isselect = false;
                        mMailEditActivity.lastselectBCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectLcc != null) {
                        mMailEditActivity.lastSelectLcc.isselect = false;
                        mMailEditActivity.lastSelectLcc.reSetmTextView();
                    }
                    mMailEditActivity.mLccAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mShoujianAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mCcAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mBccAdd.setVisibility(View.INVISIBLE);
                    // 此处为得到焦点时的处理内容
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);

                    if (mMailEditActivity.unshowall == false) {
                        mMailEditActivity.unshowall = true;
                        if (mMailEditActivity.mCcLayer != null && mMailEditActivity.mBccLayer != null
                                && mMailEditActivity.mFajianLayer != null) {
                            mMailEditActivity.mCcLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mBccLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mFajianTitle.setText(mMailEditActivity.getString(R.string.mail_bcctitle));
                            mMailEditActivity.mFajian.setTextColor(Color.rgb(149, 149, 149));
                            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mFajianLayer
                                    .getLayoutParams();
                            // mFajianLayerParams.removeRule(RelativeLayout.BELOW);
                            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.lcc_layer);
                            mMailEditActivity.mFajianLayer.setLayoutParams(mFajianLayerParams);
                        }
                    }
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CONTENT:
                if (hasFocus) {
                    mMailEditActivity.isFujian = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = true;
                    if (mMailEditActivity.lastSelectTo != null) {
                        mMailEditActivity.lastSelectTo.isselect = false;
                        mMailEditActivity.lastSelectTo.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectCC != null) {
                        mMailEditActivity.lastselectCC.isselect = false;
                        mMailEditActivity.lastselectCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastselectBCC != null) {
                        mMailEditActivity.lastselectBCC.isselect = false;
                        mMailEditActivity.lastselectBCC.reSetmTextView();
                    }
                    if (mMailEditActivity.lastSelectLcc != null) {
                        mMailEditActivity.lastSelectLcc.isselect = false;
                        mMailEditActivity.lastSelectLcc.reSetmTextView();
                    }
                    mMailEditActivity.mLccAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mShoujianAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mCcAdd.setVisibility(View.INVISIBLE);
                    mMailEditActivity.mBccAdd.setVisibility(View.INVISIBLE);
                    // 此处为得到焦点时的处理内容
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);

                    if (mMailEditActivity.unshowall == false) {
                        mMailEditActivity.unshowall = true;
                        if (mMailEditActivity.mCcLayer != null && mMailEditActivity.mBccLayer != null
                                && mMailEditActivity.mFajianLayer != null) {
                            mMailEditActivity.mCcLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mBccLayer.setVisibility(View.INVISIBLE);
                            mMailEditActivity.mFajianTitle.setText(mMailEditActivity.getString(R.string.mail_bcctitle));
                            mMailEditActivity.mFajian.setTextColor(Color.rgb(149, 149, 149));
                            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mFajianLayer
                                    .getLayoutParams();
                            // mFajianLayerParams.removeRule(RelativeLayout.BELOW);
                            mFajianLayerParams.addRule(RelativeLayout.BELOW, R.id.lcc_layer);
                            mMailEditActivity.mFajianLayer.setLayoutParams(mFajianLayerParams);
                        }
                    }

                }
                break;

        }
    }

    public void onEditclicklisten(int type) {
        switch (type) {
            case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                if (mMailEditActivity.onShoujian == false) {
                    mMailEditActivity.onShoujian = true;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.requestFocus();
                    mMailEditActivity.mLcc.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mShoujian, InputMethodManager.SHOW_FORCED);
                } else {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.requestFocus();
                    mMailEditActivity.mLcc.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMailEditActivity.mShoujian.getWindowToken(), 0);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CC:
                if (mMailEditActivity.onCc == false) {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = true;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mLcc.clearFocus();
                    mMailEditActivity.mCc.requestFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mCc, InputMethodManager.SHOW_FORCED);
                } else {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mLcc.clearFocus();
                    mMailEditActivity.mCc.requestFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMailEditActivity.mCc.getWindowToken(), 0);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_BCC:
                if (mMailEditActivity.onBcc == false) {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = true;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.requestFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mBcc, InputMethodManager.SHOW_FORCED);
                } else {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.requestFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMailEditActivity.mBcc.getWindowToken(), 0);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_LCC:
                if (mMailEditActivity.onLcc == false) {
                    mMailEditActivity.onLcc = true;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mLcc.requestFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mLcc, InputMethodManager.SHOW_FORCED);
                } else {
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mLcc.requestFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMailEditActivity.mLcc.getWindowToken(), 0);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_CONTENT:
                if (mMailEditActivity.onContent == false) {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = true;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.requestFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mContent, InputMethodManager.SHOW_FORCED);
                } else {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = false;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.clearFocus();
                    mMailEditActivity.mContent.requestFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMailEditActivity.mContent.getWindowToken(), 0);
                }
                break;
            case MailEditActivity.CONTENT_TYPE_SUBJECT:
                if (mMailEditActivity.onZhuti == false) {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = true;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = true;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.requestFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mMailEditActivity.mZhuti, InputMethodManager.SHOW_FORCED);
                } else {
                    mMailEditActivity.onShoujian = false;
                    mMailEditActivity.onLcc = true;
                    mMailEditActivity.onCc = false;
                    mMailEditActivity.onBcc = false;
                    mMailEditActivity.onZhuti = false;
                    mMailEditActivity.onContent = false;
                    mMailEditActivity.mShoujian.clearFocus();
                    mMailEditActivity.mBcc.clearFocus();
                    mMailEditActivity.mCc.clearFocus();
                    mMailEditActivity.mZhuti.requestFocus();
                    mMailEditActivity.mContent.clearFocus();
                    mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
                    mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
                    mMailEditActivity.isFujian = false;
                    InputMethodManager imm = (InputMethodManager) mMailEditActivity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mMailEditActivity.mZhuti.getWindowToken(), 0);
                }
                break;
        }
    }

    public void Fujianclick() {
        if (mMailEditActivity.isFujian) {
            mMailEditActivity.isFujian = false;
            RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
            mFajianLayerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
            mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
//			mMailEditActivity.mfujianLayer.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) mMailEditActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mMailEditActivity.onShoujian == true) {
                imm.showSoftInput(mMailEditActivity.mShoujian, InputMethodManager.SHOW_FORCED);
            } else if (mMailEditActivity.onZhuti == true) {
                imm.showSoftInput(mMailEditActivity.mZhuti, InputMethodManager.SHOW_FORCED);
            } else if (mMailEditActivity.onCc == true) {
                imm.showSoftInput(mMailEditActivity.mCc, InputMethodManager.SHOW_FORCED);
            } else if (mMailEditActivity.onBcc == true) {
                imm.showSoftInput(mMailEditActivity.mBcc, InputMethodManager.SHOW_FORCED);
            } else if (mMailEditActivity.onContent == true) {
                imm.showSoftInput(mMailEditActivity.mContent, InputMethodManager.SHOW_FORCED);
            } else if (mMailEditActivity.onLcc == true) {
                imm.showSoftInput(mMailEditActivity.mLcc, InputMethodManager.SHOW_FORCED);
            }

        } else {
            InputMethodManager imm = (InputMethodManager) mMailEditActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mMailEditActivity.mShoujian.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mMailEditActivity.mZhuti.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mMailEditActivity.mCc.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mMailEditActivity.mBcc.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mMailEditActivity.mContent.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mMailEditActivity.mLcc.getWindowToken(), 0);
            if (mMailEditHandler != null)
                mMailEditHandler.sendEmptyMessageDelayed(MailEditActivity.EVENT_SHOW_BUTTOM_LAYER, 100);
        }
    }

    public static Dialog createSelectDialog(Context context, String msg) {
        Dialog loadingDialog = new Dialog(context, R.style.oper_dialog);
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(R.layout.select_dialog);// 设置布局

        TextView tv = (TextView) loadingDialog.findViewById(R.id.title_text);
        tv.setText(msg);
        return loadingDialog;
    }

    public void startAddContacts(int ltype) {
        MailManager.getInstance().mAdd.clear();
        Intent mIntent = new Intent();
        mIntent.setClass(mMailEditActivity, MailContactsActivity.class);
        mIntent.putExtra("ltype", ltype);
        mMailEditActivity.startActivity(mIntent);
    }

    public void takePhoto() {
        mMailEditActivity.permissionRepuest = (PermissionResult) Bus.callData(mMailEditActivity,"filetools/checkPermissionTakePhoto",mMailEditActivity,Bus.callData(mMailEditActivity,"filetools/getfilePath","/mail/photo"));
    }

    public void takePhotoResult() {
        File mFile = new File(mMailEditActivity.fileUri.getPath());
        if (mFile.exists()) {
            Attachment attachment = new Attachment(AppUtils.getguid(),mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(), TimeUtils.getDate());

            if(MailManager.getInstance().account.iscloud) {
                mMailEditActivity.mMail.attachments.add(attachment);
                mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
            }
            else {
                uploadAttchment(mFile);
                mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
            }
        }
    }

    public void uploadAttchment(File mupfile) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody fileBody = RequestBody.create(NetUtils.MEDIA_TYPE_MARKDOWN, mupfile);
        builder.addFormDataPart("file", mupfile.getName(), fileBody);
        builder.addFormDataPart("uid", MailManager.getInstance().account.mRecordId);
        builder.addFormDataPart("type", "2");
        Attachment attachment = new Attachment(AppUtils.getguid(),mupfile.getName(),mupfile.getPath(),"",mupfile.length(),mupfile.length(), TimeUtils.getDate());
        mMailEditActivity.mMail.attachments.add(attachment);
        MailManager.getInstance().upthreads.add(new AttachmentUploadThread(attachment, builder, mMailEditHandler));
        NetObject netObject = new NetObject();
        netObject.item = attachment;
        if (mMailEditHandler != null) {
            Message msg = new Message();
            msg.what = AttachmentUploadThread.EVENT_UPLOAD_FUJIAN_NEXT;
            msg.obj = netObject;
            mMailEditHandler.sendMessage(msg);
        }
    }

    public void praseUploadNext(NetObject netObject) {
        String json = netObject.result;
        Attachment attachmentModel = null;
        if(netObject.item != null)
            attachmentModel = (Attachment) netObject.item;
        if(json.length() == 0)
        {
            if(attachmentModel != null)
                attachmentModel.stata = Attachment.STATA_NOSTART;
            if(MailManager.getInstance().upthreads.size() > 0)
            {
                AttachmentUploadThread uploadThread = MailManager.getInstance().upthreads.get(0);
                MailManager.getInstance().upthreads.remove(uploadThread);
                uploadThread.attachment.stata = Attachment.STATA_DWONLODING;
                uploadThread.start();
            }
        }
        else
        {
            try {
                JSONObject jsonObject = new JSONObject(json);

                if(jsonObject.getInt("code") == 0)
                {
                    JSONObject data = jsonObject.getJSONObject("data");
                    attachmentModel.mailUploadJson = new JSONObject();
                    attachmentModel.mRecordid = data.getString("rid");
                    attachmentModel.mailUploadJson.put("filename",data.getString("filename"));
                    attachmentModel.mailUploadJson.put("filepath",data.getString("filepath"));
                    attachmentModel.mailUploadJson.put("size",data.getString("size"));
                    attachmentModel.mailUploadJson.put("ext",data.getString("extension"));
                    attachmentModel.stata = Attachment.STATA_DOWNLOADFINISH;
                }
                else
                {
                    attachmentModel.stata = Attachment.STATA_DOWNLOADFIAL;
                }

                if(MailManager.getInstance().upthreads.size() > 0)
                {
                    AttachmentUploadThread uploadThread = MailManager.getInstance().upthreads.get(0);
                    MailManager.getInstance().upthreads.remove(uploadThread);
                    uploadThread.start();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                attachmentModel.stata = Attachment.STATA_DOWNLOADFIAL;
                if(MailManager.getInstance().upthreads.size() > 0)
                {
                    AttachmentUploadThread uploadThread = MailManager.getInstance().upthreads.get(0);
                    MailManager.getInstance().upthreads.remove(uploadThread);
                    uploadThread.start();
                }
            }
        }
        mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
    }

    public boolean checkAttachment() {
        for(int i = 0 ; i < mMailEditActivity.mMail.attachments.size() ; i++)
        {
            if(mMailEditActivity.mMail.attachments.get(i).stata != Attachment.STATA_NOSTART ||
                    mMailEditActivity.mMail.attachments.get(i).stata !=  Attachment.STATA_DOWNLOADFIAL)
            {
                return false;
            }
        }
        return true;
    }

    public void getVideo() {
        Bus.callData(mMailEditActivity,"filetools/getVideos",false,"intersky.mail.view.activityMailEditActivity", MailEditActivity.ACTION_MAIL_VIDEO_SELECT);
    }

    public void getPicture() {
        Bus.callData(mMailEditActivity,"filetools/getPhotos",false,9,"intersky.mail.view.activityMailEditActivity", MailEditActivity.ACTION_MAIL_PHOTO_SELECT);
    }

    public void showDeletedialog(Attachment mAttachment) {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem menuItem = new MenuItem();
        menuItem.btnName = mMailEditActivity.getString(R.string.button_word_check);
        menuItem.item = mAttachment;
        menuItem.mListener = mMailEditActivity.mattachshowListenter;
        menuItems.add(menuItem);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.btnName = mMailEditActivity.getString(R.string.button_delete);
        menuItem2.item = mAttachment;
        menuItem2.mListener = mMailEditActivity.mattachdeleteListenter;
        menuItems.add(menuItem2);
        mMailEditActivity.popupWindow1 = AppUtils.creatButtomMenu(mMailEditActivity, mMailEditActivity.mshada,
                menuItems, mMailEditActivity.findViewById(R.id.activity_mail_edit));
    }

    public void showpic(Attachment mAttachment) {
        String path = mAttachment.mPath;
        mMailEditActivity.popupWindow1.dismiss();
        mMailEditActivity.startActivity((Intent) Bus.callData(mMailEditActivity,"filetools/getImageFileIntent",new File(path)));
    }

    public void deletepic(Attachment mAttachment) {
        mMailEditActivity.popupWindow1.dismiss();
        mMailEditActivity.mMail.attachments.remove(mAttachment);
        mMailEditActivity.mAttachmentAdapter.notifyDataSetChanged();
    }

    public void selectMail(int position) {
        MailBox mMailBox;
        mMailBox = MailManager.getInstance().mMailBoxs.get(position);
        MailManager.getInstance().mSelectMailBox.isSelect = false;
        mMailBox.isSelect = true;
        MailManager.getInstance().mSelectMailBox = mMailBox;
        if (MailManager.getInstance().mSelectMailBox.isloacl) {
            mMailEditActivity.mFajian.setText(MailManager.getInstance().mSelectMailBox.mFullAddress);
        } else
            mMailEditActivity.mFajian.setText(MailManager.getInstance().mSelectMailBox.mAddress);
        mMailEditActivity.mMailSelectAdapter.notifyDataSetChanged();
        mMailEditActivity.selectDialog.dismiss();
    }

    public void initMailDetial() {
        ArrayList<MailContact> maddMailPersonItems = new ArrayList<MailContact>();
        if(mMailEditActivity.mAction == MailManager.ACTION_REPEAT)
        {
            if(mMailEditActivity.mMail.isLocal)
            {
                maddMailPersonItems.clear();
                for (int i = 0; i < MailManager.getInstance().mailLContacts.size(); i++) {
                    if(MailManager.getInstance().mailLContacts.get(i).mailRecordID.equals(mMailEditActivity.mMail.mCatalogueID))
                    {
                        maddMailPersonItems.add(MailManager.getInstance().mailLContacts.get(i));
                    }
                }
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                    {
                        addPersonView( mMailEditActivity.mLccLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mLccPesrons,mMailEditActivity.mPersonTextClickListener4);
                    }
                }
            }
            else
            {
                maddMailPersonItems.clear();
                gethtmMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mFrom);
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if (!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase())) {
                        addPersonView(mMailEditActivity.mShoujianContentLayer, maddMailPersonItems.get(i), mMailEditActivity.mToPesrons, mMailEditActivity.mPersonTextClickListener1);
                    }
                }
            }
        }
        else if(mMailEditActivity.mAction == MailManager.ACTION_REPEATALL) {
            if(mMailEditActivity.mMail.isLocal)
            {
                maddMailPersonItems.clear();
                for (int i = 0; i < MailManager.getInstance().mailLContacts.size(); i++) {
                    if(MailManager.getInstance().mailLContacts.get(i).mailRecordID.equals(mMailEditActivity.mMail.mCatalogueID))
                    {
                        maddMailPersonItems.add(MailManager.getInstance().mailLContacts.get(i));
                    }
                }
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                    {
                        addPersonView( mMailEditActivity.mLccLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mLccPesrons,mMailEditActivity.mPersonTextClickListener4);
                    }
                }
                maddMailPersonItems.clear();
                gethtmLMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mLcc);
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                    {
                        addPersonView( mMailEditActivity.mLccLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mLccPesrons,mMailEditActivity.mPersonTextClickListener4);
                    }
                }
            }
            else
            {
                maddMailPersonItems.clear();
                gethtmMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mTo);
                gethtmMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mFrom);
                boolean has = false;
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if (maddMailPersonItems.get(maddMailPersonItems.size() - 1).getMailAddress().toLowerCase()
                            .equals(maddMailPersonItems.get(i).getMailAddress().toLowerCase())) {
                        has = true;
                        break;
                    }
                }
                if (has == true) {
                    maddMailPersonItems.remove(maddMailPersonItems.size() - 1);
                }
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                    {
                        addPersonView( mMailEditActivity.mShoujianContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mToPesrons,mMailEditActivity.mPersonTextClickListener1);
                    }
                }
                maddMailPersonItems.clear();
                gethtmMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mCc);
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                    {
                        addPersonView( mMailEditActivity.mCcLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mCcPesrons,mMailEditActivity.mPersonTextClickListener2);
                    }
                }
                maddMailPersonItems.clear();
                gethtmLMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mLcc);
                for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                    if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                    {
                        addPersonView( mMailEditActivity.mLccLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mLccPesrons,mMailEditActivity.mPersonTextClickListener4);
                    }
                }
            }


        }
        else if(mMailEditActivity.mAction == MailManager.ACTION_EDIT)
        {
            maddMailPersonItems.clear();
            gethtmMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mTo);
            for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                {
                    addPersonView( mMailEditActivity.mShoujianContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mToPesrons,mMailEditActivity.mPersonTextClickListener1);
                }
            }

            maddMailPersonItems.clear();
            gethtmMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mCc);
            for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                {
                    addPersonView( mMailEditActivity.mCcLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mCcPesrons,mMailEditActivity.mPersonTextClickListener2);
                }
            }
            maddMailPersonItems.clear();
            gethtmLMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mLcc);
            for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                {
                    addPersonView( mMailEditActivity.mLccLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mLccPesrons,mMailEditActivity.mPersonTextClickListener4);
                }
            }
            maddMailPersonItems.clear();
            gethtmLMialPersons(maddMailPersonItems, mMailEditActivity.mMail.mBcc);
            for(int i = 0 ; i < maddMailPersonItems.size() ; i++) {
                if(!maddMailPersonItems.get(i).mailAddress.toLowerCase().equals(MailManager.getInstance().mSelectMailBox.mAddress.toLowerCase()))
                {
                    addPersonView( mMailEditActivity.mBccLayerContentLayer,maddMailPersonItems.get(i),mMailEditActivity.mBccPesrons,mMailEditActivity.mPersonTextClickListener3);
                }
            }
        }
        else
        {

        }

    }

    public void addContact(int type) {
        if (MailManager.getInstance().mAdd.size() > 0) {
            switch (type) {
                case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                    onViewMeasure(mMailEditActivity.mShoujianContentLayer, MailManager.getInstance().mAdd, type);
                    break;
                case MailEditActivity.CONTENT_TYPE_CC:
                    onViewMeasure(mMailEditActivity.mCcLayerContentLayer, MailManager.getInstance().mAdd, type);
                    break;
                case MailEditActivity.CONTENT_TYPE_BCC:
                    onViewMeasure(mMailEditActivity.mBccLayerContentLayer, MailManager.getInstance().mAdd, type);
                    break;
                case MailEditActivity.CONTENT_TYPE_LCC:
                    onViewMeasure(mMailEditActivity.mLccLayerContentLayer, MailManager.getInstance().mAdd, type);
                    break;
            }
            MailManager.getInstance().mAdd.clear();
        }
    }

    private void initMailPerosnView() {
        LayoutInflater mInflater = (LayoutInflater) mMailEditActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.mailtitle, null);
        TextView mTextView = (TextView) mView.findViewById(R.id.title_text);
        mTextView.setText("收件人：");
        mMailEditActivity.mShoujianContentLayer.addView(mView);
        mMailEditActivity.mShoujianEditLayer = mInflater.inflate(R.layout.mailedit, null);
        mMailEditActivity.mShoujian = (EditText) mMailEditActivity.mShoujianEditLayer.findViewById(R.id.content_edit);
        mMailEditActivity.mShoujianContentLayer.addView(mMailEditActivity.mShoujianEditLayer);

        if (mMailEditActivity.getIntent().getBooleanExtra("hasaddress", false)) {
            View mtView = mInflater.inflate(R.layout.mailperson, null);
            MailContact tperson = new MailContact(mMailEditActivity.getIntent().getStringExtra("mailaddress"),
                    mMailEditActivity.getIntent().getStringExtra("mailaddress"), false);
            tperson.setmTextView((TextView) mtView.findViewById(R.id.person_text));
            mMailEditActivity.mToPesrons.add(tperson);
            tperson.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener1);
            tperson.getmTextView().setTag(tperson);
            mMailEditActivity.mShoujianContentLayer.addView(mtView,
                    mMailEditActivity.mShoujianContentLayer.getChildCount() - 1);
        }

        View mView1 = mInflater.inflate(R.layout.mailtitle, null);
        TextView mTextView1 = (TextView) mView1.findViewById(R.id.title_text);
        mTextView1.setText("内部抄送：");
        mMailEditActivity.mLccLayerContentLayer.addView(mView1);
        mMailEditActivity.mLccEditLayer = mInflater.inflate(R.layout.mailedit, null);
        mMailEditActivity.mLcc = (EditText) mMailEditActivity.mLccEditLayer.findViewById(R.id.content_edit);
        mMailEditActivity.mLccLayerContentLayer.addView(mMailEditActivity.mLccEditLayer);

        View mView2 = mInflater.inflate(R.layout.mailtitle, null);
        TextView mTextView2 = (TextView) mView2.findViewById(R.id.title_text);
        mTextView2.setText("抄送：");
        mMailEditActivity.mCcLayerContentLayer.addView(mView2);
        mMailEditActivity.mCcEditLayer = mInflater.inflate(R.layout.mailedit, null);
        mMailEditActivity.mCc = (EditText) mMailEditActivity.mCcEditLayer.findViewById(R.id.content_edit);
        mMailEditActivity.mCcLayerContentLayer.addView(mMailEditActivity.mCcEditLayer);
        View mView3 = mInflater.inflate(R.layout.mailtitle, null);
        TextView mTextView3 = (TextView) mView3.findViewById(R.id.title_text);
        mTextView3.setText("密送：");
        mMailEditActivity.mBccLayerContentLayer.addView(mView3);
        mMailEditActivity.mBccEditLayer = mInflater.inflate(R.layout.mailedit, null);
        mMailEditActivity.mBcc = (EditText) mMailEditActivity.mBccEditLayer.findViewById(R.id.content_edit);
        mMailEditActivity.mBccLayerContentLayer.addView(mMailEditActivity.mBccEditLayer);

        mMailEditActivity.mShoujian.setOnClickListener(mMailEditActivity.onEditclicklisten1);
        mMailEditActivity.mShoujian.setOnFocusChangeListener(mMailEditActivity.mOnFocusChangeListener1);
        mMailEditActivity.mShoujian.setOnEditorActionListener(mMailEditActivity.mOnEditorActionListener1);
        mMailEditActivity.mShoujian.setOnKeyListener(mMailEditActivity.mOnKeyListener1);
        mMailEditActivity.mLcc.setOnClickListener(mMailEditActivity.onEditclicklisten12);
        mMailEditActivity.mLcc.setOnFocusChangeListener(mMailEditActivity.mOnFocusChangeListener12);
        mMailEditActivity.mLcc.setOnEditorActionListener(mMailEditActivity.mOnEditorActionListener12);
        mMailEditActivity.mLcc.setOnKeyListener(mMailEditActivity.mOnKeyListener12);

        mMailEditActivity.mCc.setOnClickListener(mMailEditActivity.onEditclicklisten2);
        mMailEditActivity.mCc.setOnFocusChangeListener(mMailEditActivity.mOnFocusChangeListener2);
        mMailEditActivity.mCc.setOnEditorActionListener(mMailEditActivity.mOnEditorActionListener2);
        mMailEditActivity.mCc.setOnKeyListener(mMailEditActivity.mOnKeyListener2);
        mMailEditActivity.mBcc.setOnClickListener(mMailEditActivity.onEditclicklisten4);
        mMailEditActivity.mBcc.setOnFocusChangeListener(mMailEditActivity.mOnFocusChangeListener5);
        mMailEditActivity.mBcc.setOnEditorActionListener(mMailEditActivity.mOnEditorActionListener3);
        mMailEditActivity.mBcc.setOnKeyListener(mMailEditActivity.mOnKeyListener3);
    }

    private void addPersonView(MyLinearLayout mMyLinearLayout, MailContact mMailPersonItem, ArrayList<MailContact> mToPesrons, View.OnClickListener clickListener) {
        LayoutInflater mInflater = (LayoutInflater) mMailEditActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mToPesrons.add(mMailPersonItem);
        View mView = mInflater.inflate(R.layout.mailperson, null);
        mMailPersonItem.setmTextView((TextView) mView.findViewById(R.id.person_text));
        mMyLinearLayout.addView(mView,
                mMyLinearLayout.getChildCount() - 1);
        mMailPersonItem.getmTextView()
                .setOnClickListener(clickListener);
        mMailPersonItem.getmTextView().setTag(mMailPersonItem);
    }

    private void gethtmMialPersons(ArrayList<MailContact> mMailPersonItems, String from) {

        String str[];
        if(from.contains("; "))
        {
            str = from.split("; ");
        }
        else
        {
            str = from.split(";");
        }
        if(from.length() > 0)
        {
            for (int i = 0; i < str.length; i++) {
                String word = str[i];
                String address = "";
                String name = "";
                if(word.contains("<"))
                {
                    int a1 = word.indexOf("<");
                    if(word.contains(">"))
                    {
                        int a2 = word.indexOf(">");
                        address = word.substring(a1+1,a2);
                        name = word.substring(0,a1);
                    }
                }
                if(address.length() == 0)
                {
                    address = word;
                }
                if(name.length() == 0)
                {
                    name = word;
                }
                MailContact t = new MailContact(name,address);
                if (t != null)
                {
                    mMailPersonItems.add(t);
                }


            }
        }

    }

    private void gethtmLMialPersons(ArrayList<MailContact> mMailPersonItems, String from) {
        String temp;
        String str[] = from.split(";");
        for (int j = 0; j < str.length; j++) {
            for (int i = 0; i < MailManager.getInstance().mailLContacts.size(); i++) {
                if (str[j].equals( MailManager.getInstance().mailLContacts.get(i).mailRecordID)) {
                    // tlcc+=MailPersonManageer.getInstance().getmSampleMailPersonItems().get(i).getmName()+";";
                    mMailPersonItems.add(MailManager.getInstance().mailLContacts.get(i));
                    break;
                }
            }
        }
        for (int j = 0; j < str.length; j++) {
            for (int i = 0; i < MailManager.getInstance().mailLContacts.size(); i++) {
                if (str[j].equals(MailManager.getInstance().mailLContacts.get(i).mName)) {
                    // tlcc+=MailPersonManageer.getInstance().getmSampleMailPersonItems().get(i).getmName()+";";
                    mMailPersonItems.add(MailManager.getInstance().mailLContacts.get(i));
                    break;
                }
            }
        }
    }

    private void setEditLoaction() {
        if (mMailEditActivity.mAction == MailManager.ACTION_REPEAT
                || mMailEditActivity.mAction == MailManager.ACTION_REPEATALL) {
            mMailEditActivity.onShoujian = false;
            mMailEditActivity.onCc = false;
            mMailEditActivity.onBcc = false;
            mMailEditActivity.onZhuti = false;
            mMailEditActivity.onContent = true;
            mMailEditActivity.mShoujian.clearFocus();
            mMailEditActivity.mBcc.clearFocus();
            mMailEditActivity.mCc.clearFocus();
            mMailEditActivity.mZhuti.clearFocus();
            mMailEditActivity.mContent.requestFocus();
            mMailEditActivity.mButtomLayer.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams mFajianLayerParams1 = (RelativeLayout.LayoutParams) mMailEditActivity.mfujianLayer.getLayoutParams();
            mFajianLayerParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mMailEditActivity.mfujianLayer.setLayoutParams(mFajianLayerParams1);
            mMailEditActivity.isFujian = false;
            Selection.setSelection(mMailEditActivity.mContent.getText(), 0);
        }
    }

    private void initrepeat() {

        String head = MailManager.getInstance().getheadrepead(mMailEditActivity);
        String thtml = "";
        head = head.replaceFirst("%s", mMailEditActivity.mMail.mFrom);
        head = head.replaceFirst("%s", mMailEditActivity.mMail.mTo);
        head = head.replaceFirst("%s", mMailEditActivity.mMail.mCc);
        head = head.replaceFirst("%s", mMailEditActivity.mMail.mDate);
        head = head.replaceFirst("%s", mMailEditActivity.mMail.mSubject);
//		String temp = head + mMailEditActivity.mMail.getmContentHtml() + "</BLOCKQUOTE>";
//		mMailEditActivity.mMail.setmContentHtml(temp);
//		mMailEditActivity.mMail.setmContentHtml(mMailEditActivity.mMail.getmContentHtml() + "</BLOCKQUOTE>");

        String temp = head + mMailEditActivity.mMail.mContentHtml + "</BLOCKQUOTE>";
        mMailEditActivity.mMail.mContentHtml = temp;
        mMailEditActivity.mMail.mContentHtml = mMailEditActivity.mMail.mContentHtml + "</BLOCKQUOTE>";
    }

    private void poernimf(MailContact mMailGuest, boolean islocal) {
        Intent mIntent = new Intent();
        mIntent.setClass(mMailEditActivity, MailAddressActivity.class);
        mIntent.putExtra("name", mMailGuest.mName);
        mIntent.putExtra("mail", mMailGuest.mailAddress);
        mIntent.putExtra("islocal", mMailGuest.islocal);
        mMailEditActivity.startActivity(mIntent);
    }



    private void showSelectDialog() {
        mMailEditActivity.selectDialog = MailEditPresenter.createSelectDialog(mMailEditActivity, "选择发件人");
        ListView mListView = (ListView) mMailEditActivity.selectDialog.findViewById(R.id.user_mail_List);
        mListView.setAdapter(mMailEditActivity.mMailSelectAdapter);
        mListView.setOnItemClickListener(mMailEditActivity.mUserMailItemClick);
        mMailEditActivity.selectDialog.show();
    }

    private boolean onViewMeasure(MyLinearLayout mMyLinearLayout, MailContact mPesron, int type) {
        LayoutInflater mInflater = (LayoutInflater) mMailEditActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.mailperson, null);
        mPesron.setmTextView((TextView) mView.findViewById(R.id.person_text));
        switch (type) {
            case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                mPesron.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener1);
                break;
            case MailEditActivity.CONTENT_TYPE_CC:
                mPesron.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener2);
                break;
            case MailEditActivity.CONTENT_TYPE_BCC:
                mPesron.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener3);
                break;
            case MailEditActivity.CONTENT_TYPE_LCC:
                for (int i = 0; i < mMailEditActivity.mLccPesrons.size(); i++) {
                    if (mMailEditActivity.mLccPesrons.get(i).mName.equals(mPesron.mName)) {
                        AppUtils.showMessage(mMailEditActivity, mPesron.mName + mMailEditActivity.getString(R.string.mail_alreadyin));
                        return false;
                    }
                }
                mPesron.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener4);
                break;

        }
        mPesron.getmTextView().setTag(mPesron);
        mMyLinearLayout.addView(mView, mMyLinearLayout.getChildCount() - 1);
        return true;

    }

    private MailContact matchLocal(String name) {
        for (int i = 0; i < MailManager.getInstance().mailLContacts.size(); i++) {
            MailContact mMailPersonItem = MailManager.getInstance().mailLContacts.get(i);
            return mMailPersonItem;
        }
        return null;
    }

    private void sendMail(boolean issend) {
        if (mMailEditActivity.mShoujian.getText().toString().length() > 0 && issend == true) {
            MailContact mPesrons = new MailContact(mMailEditActivity.mShoujian.getText().toString(),mMailEditActivity.mShoujian.getText().toString());
            mMailEditActivity.mShoujian.setText("");
            mMailEditActivity.mToPesrons.add(mPesrons);
            onViewMeasure(mMailEditActivity.mShoujianContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_SHOUJIAN);
        }
        if (mMailEditActivity.mLcc.getText().toString().length() > 0 && issend == true) {
            MailContact mMailPersonItem = matchLocal(mMailEditActivity.mLcc.getText().toString());
            boolean has = false;
            if (mMailPersonItem != null) {
                has = onViewMeasure(mMailEditActivity.mLccLayerContentLayer, mMailPersonItem,
                        MailEditActivity.CONTENT_TYPE_LCC);
                if (has == true)
                    mMailEditActivity.mLccPesrons.add(mMailPersonItem);
            } else {
                MailContact mPesrons = new MailContact(mMailEditActivity.mLcc.getText().toString(),mMailEditActivity.mLcc.getText().toString());
                has = onViewMeasure(mMailEditActivity.mLccLayerContentLayer, mPesrons,
                        MailEditActivity.CONTENT_TYPE_LCC);
                if (has == true)
                    mMailEditActivity.mLccPesrons.add(mPesrons);
            }
            mMailEditActivity.mLcc.setText("");
        }
        if (mMailEditActivity.mCc.getText().toString().length() > 0 && issend == true) {
            MailContact mPesrons = new MailContact(mMailEditActivity.mCc.getText().toString(),mMailEditActivity.mCc.getText().toString());
            mMailEditActivity.mCc.setText("");
            mMailEditActivity.mCcPesrons.add(mPesrons);
            onViewMeasure(mMailEditActivity.mCcLayerContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_CC);
        }
        if (mMailEditActivity.mBcc.getText().toString().length() > 0 && issend == true) {
            MailContact mPesrons = new MailContact(mMailEditActivity.mBcc.getText().toString(),mMailEditActivity.mBcc.getText().toString());
            mMailEditActivity.mBcc.setText("");
            mMailEditActivity.mBccPesrons.add(mPesrons);
            onViewMeasure(mMailEditActivity.mBccLayerContentLayer, mPesrons, MailEditActivity.CONTENT_TYPE_BCC);
        }
        if (checkMailAddress() == false && issend == true) {
            AppUtils.showMessage(mMailEditActivity, mMailEditActivity.getString(R.string.mail_mailaddresswrong));
            return;
        }
        if ((measureMialPersonWord(mMailEditActivity.mToPesrons, false).length() == 0
                && measureMialPersonWord(mMailEditActivity.mLccPesrons, true).length() == 0) && issend == true) {
            AppUtils.showMessage(mMailEditActivity, mMailEditActivity.getString(R.string.mail_mailaddressempty));
            return;
        }
        if (mMailEditActivity.mZhuti.getText().toString().length() == 0) {
            AppUtils.showMessage(mMailEditActivity, mMailEditActivity.getString(R.string.mail_mailzhuti));
            return;
        } else if ((measureMialPersonWord(mMailEditActivity.mToPesrons, false).length() > 0
                || measureMialPersonWord(mMailEditActivity.mCcPesrons, false).length() > 0
                || measureMialPersonWord(mMailEditActivity.mBccPesrons, false).length() > 0)
                && MailManager.getInstance().mSelectMailBox.isloacl && issend == true) {
            AppUtils.showMessage(mMailEditActivity, mMailEditActivity.getString(R.string.mail_cannotsendouter1));
            return;
        } else {
            String Send = String.valueOf(issend);
            String SourceMailID = "";
            String Action = "0";
            String RecordID = mMailEditActivity.mRecordID;
            String Content = mMailEditActivity.mContent.getText().toString().replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
            Content = Content.replace(" ", "&nbsp;");
            String content = mMailEditActivity.mContent.getText().toString();
            String real = "";
            if (mMailEditActivity.mAction == MailManager.ACTION_NEW) {
                SourceMailID = "";
                Action = "0";
            } else {
                if (mMailEditActivity.mMail != null)
                    SourceMailID = mMailEditActivity.mMail.mRecordId;
                if (mMailEditActivity.mAction == MailManager.ACTION_EDIT) {
                    Action = "1";
                    if (content.indexOf("-----") != -1) {

                        real = content.substring(0, content.indexOf("-----"));
                        real = real.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
                        real = real.replace(" ", "&nbsp;");
                        Content = real + mMailEditActivity.mMail.mContentHtml;
                    } else {
                        String newString = mMailEditActivity.mContent.getText().toString()
                                .replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
                        newString = newString.replace(" ", "&nbsp;");
                        Content = newString;
                    }
                } else {

                    if (mMailEditActivity.mAction == MailManager.ACTION_REPEAT || mMailEditActivity.mAction == MailManager.ACTION_REPEATALL)
                        Action = "2";
                    else if (mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
                        Action = "4";
                    } else {
                        Action = "0";
                    }
                    real = content;
                    real = real.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
                    real = real.replace(" ", "&nbsp;");
                    Content = real + mMailEditActivity.mMail.mContentHtml;
                }
            }

            String SendTime = "";
            String Subject = mMailEditActivity.mZhuti.getText().toString();
            String From = "";
            String To = measureMialPersonWord(mMailEditActivity.mToPesrons, false);
            String Cc = measureMialPersonWord(mMailEditActivity.mCcPesrons, false);
            String Bcc = measureMialPersonWord(mMailEditActivity.mBccPesrons, false);
            String LocalCC = measureMialPersonWord(mMailEditActivity.mLccPesrons, true);
            String Charset = "UTF-8";
            String Priority = "0";
            String UserID = MailManager.getInstance().mSelectUser.mailRecordID;
            if (MailManager.getInstance().mSelectMailBox.isloacl) {
                String addres = "";
                if (MailManager.getInstance().mSelectUser.mailRecordID
                        .equals(MailManager.getInstance().account.mRecordId)) {
                    if (MailManager.getInstance().account.mRealName.length() != 0)
                        addres = "\"" + MailManager.getInstance().account.mRealName + "\"" + "<"
                                + MailManager.getInstance().account.mRecordId + "@local.com" + ">";
                    else
                        addres = "\"" + MailManager.getInstance().account.mRecordId + "\"" + "<"
                                + MailManager.getInstance().account.mRecordId + "@local.com" + ">";
                } else {
                    addres = "\"" + MailManager.getInstance().mSelectUser.mName + "\"" + " <"
                            + MailManager.getInstance().mSelectUser.mName + "@local.com" + ">";
                }
                From = addres;
            } else {
                From = mMailEditActivity.mFajian.getText().toString();
            }


            if (MailManager.getInstance().account.iscloud == false) {

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                NameValuePair mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Send", Send);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("SourceID", SourceMailID);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Action", Action);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("RecordID", RecordID);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Content", Content);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("SendTime", SendTime);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Subject", Subject);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("From", From);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("To", To);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Cc", Cc);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Bcc", Bcc);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("LocalCC", LocalCC);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Charset", Charset);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("Priority", Priority);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("UserID", UserID);
                nameValuePairs.add(mNameValuePair);

                int filecount = 0;
                for (int i = 0; i < mMailEditActivity.mMail.attachments.size(); i++) {
                    File mfile = new File(mMailEditActivity.mMail.attachments.get(i).mPath);
                    if (mfile.exists()) {

                        mNameValuePair = new NameValuePair("file" + String.valueOf(i), mMailEditActivity.mMail.attachments.get(i).mName);
                        mNameValuePair.isFile = true;
                        mNameValuePair.path = mfile.getPath();
                        filecount++;
                    }
                }

                mNameValuePair = new NameValuePair("filecount", String.valueOf(filecount));
                nameValuePairs.add(mNameValuePair);
                MailAsks.sendMail(mMailEditActivity,mMailEditHandler,nameValuePairs,issend);
            } else {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                NameValuePair mNameValuePair = new NameValuePair("token", NetUtils.getInstance().token);
                nameValuePairs.add(mNameValuePair);
                if (issend)
                mNameValuePair = new NameValuePair("send_state", "0");
                else
                    mNameValuePair = new NameValuePair("send_state", "4");
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("mail_box_id", MailManager.getInstance().mSelectMailBox.mRecordId);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("subject", Subject);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("content", mMailEditActivity.mContent.getText().toString());
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("from_address", mMailEditActivity.mFajian.getText().toString());
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("cc_address", Cc);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("to_address", To);
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("bcc_address", Bcc);
                nameValuePairs.add(mNameValuePair);
                if (mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
                    mNameValuePair = new NameValuePair("resent_address", mMailEditActivity.mMail.mFrom);
                    nameValuePairs.add(mNameValuePair);
                }
//                    jsonObject.put("label", new JSONArray());
                if (mMailEditActivity.mAction != MailManager.ACTION_EDIT) {
                    if(issend) {
                        mNameValuePair = new NameValuePair("type","2");
                        nameValuePairs.add(mNameValuePair);
                    }
                    else {
                        mNameValuePair = new NameValuePair("type","0");
                        nameValuePairs.add(mNameValuePair);
                    }
                } else {
                    mNameValuePair = new NameValuePair("state",String.valueOf(mMailEditActivity.mMail.state));
                    nameValuePairs.add(mNameValuePair);
                }

                if (mMailEditActivity.mAction == MailManager.ACTION_NEW) {
                    mNameValuePair = new NameValuePair("mail_type","1");
                    nameValuePairs.add(mNameValuePair);
                    mNameValuePair = new NameValuePair("submit_type","Add");
                    nameValuePairs.add(mNameValuePair);
                } else if (mMailEditActivity.mAction == MailManager.ACTION_EDIT) {
                    mNameValuePair = new NameValuePair("mail_type","1");
                    nameValuePairs.add(mNameValuePair);
                    mNameValuePair = new NameValuePair("submit_type","Set");
                    nameValuePairs.add(mNameValuePair);
                } else if (mMailEditActivity.mAction == MailManager.ACTION_REPEAT) {

                    if (mMailEditActivity.mMail.attachments.size() > 0)
                    {
                        mNameValuePair = new NameValuePair("submit_type","ReEnclosure");
                        nameValuePairs.add(mNameValuePair);
                    }
                    else {
                        mNameValuePair = new NameValuePair("submit_type","Re");
                        nameValuePairs.add(mNameValuePair);
                    }
                } else if (mMailEditActivity.mAction == MailManager.ACTION_REPEATALL) {
                    mNameValuePair = new NameValuePair("mail_type","3");
                    nameValuePairs.add(mNameValuePair);
                    if (mMailEditActivity.mMail.attachments.size() > 0)
                    {
                        mNameValuePair = new NameValuePair("submit_type","ReEnclosure");
                        nameValuePairs.add(mNameValuePair);
                    }
                    else {
                        mNameValuePair = new NameValuePair("submit_type","ReAll");
                        nameValuePairs.add(mNameValuePair);
                    }
                } else if (mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
                    mNameValuePair = new NameValuePair("mail_type","2");
                    nameValuePairs.add(mNameValuePair);
                    mNameValuePair = new NameValuePair("submit_type","Fw");
                    nameValuePairs.add(mNameValuePair);
                }
                mNameValuePair = new NameValuePair("isText", "false");
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("isCritical", "false");
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("isReceipt", "false");
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("isTrack", "false");
                nameValuePairs.add(mNameValuePair);
                mNameValuePair = new NameValuePair("priority", "1");
                nameValuePairs.add(mNameValuePair);
                if (mMailEditActivity.mAction == MailManager.ACTION_NEW || mMailEditActivity.mAction == MailManager.ACTION_EDIT) {

                } else {
                    mNameValuePair = new NameValuePair("raw_mail_id", mMailEditActivity.mMail.mRecordId);
                    nameValuePairs.add(mNameValuePair);
                    if (mMailEditActivity.mAction == MailManager.ACTION_RESEND) {
                        mNameValuePair = new NameValuePair("raw_mail_type", "2");
                        nameValuePairs.add(mNameValuePair);
                    }
                    else {
                        mNameValuePair = new NameValuePair("raw_mail_type", "3");
                        nameValuePairs.add(mNameValuePair);
                    }
                }

                if (issend==false || mMailEditActivity.mAction == MailManager.ACTION_EDIT) {
                    mNameValuePair = new NameValuePair("mail_user_id", mMailEditActivity.mMail.mRecordId);
                    nameValuePairs.add(mNameValuePair);
                }
                JSONObject object = new JSONObject();
                for(int i = 0 ; i < mMailEditActivity.mMail.attachments.size() ; i++)
                {
                    if(mMailEditActivity.mMail.attachments.get(i).mRecordid.length() > 0) {
                        try {
                            object.put(mMailEditActivity.mMail.attachments.get(i).mRecordid,mMailEditActivity.mMail.attachments.get(i).mailUploadJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mNameValuePair = new NameValuePair("attachments", object.toString());
                nameValuePairs.add(mNameValuePair);
                MailAsks.sendMailc(mMailEditActivity,mMailEditHandler,nameValuePairs,issend);
            }
            mMailEditActivity.waitDialog.show();
        }

    }


    private boolean checkMailAddress() {
        boolean iscurrect = true;
        for (int i = 0; i < mMailEditActivity.mToPesrons.size(); i++) {
            if (mMailEditActivity.mToPesrons.get(i).addressCurrect == false) {
                iscurrect = false;
            }
        }
        for (int i = 0; i < mMailEditActivity.mCcPesrons.size(); i++) {
            if (mMailEditActivity.mCcPesrons.get(i).addressCurrect == false) {
                iscurrect = false;
            }
        }
        for (int i = 0; i < mMailEditActivity.mBccPesrons.size(); i++) {
            if (mMailEditActivity.mBccPesrons.get(i).addressCurrect == false) {
                iscurrect = false;
            }
        }
        for (int i = 0; i < mMailEditActivity.mLccPesrons.size(); i++) {
            if (mMailEditActivity.mLccPesrons.get(i).addressCurrect == false) {
                iscurrect = false;
            }
        }
        return iscurrect;
    }

    private String measureMialPersonWord(ArrayList<MailContact> mPesrons, boolean locol) {
        String mail = "";
        for (int i = 0; i < mPesrons.size(); i++) {
            if (locol) {
                mail += (mPesrons.get(i).getMailAddress().toString() + ";");
            } else {
                mail += "\"" + mPesrons.get(i).mName + "\"" + "<"
                        + (mPesrons.get(i).getMailAddress().toString() + ">;");
            }

        }
        return mail;
    }



    private void onViewMeasure(MyLinearLayout mMyLinearLayout, ArrayList<MailContact> mPesrons, int type) {
        LayoutInflater mInflater = (LayoutInflater) mMailEditActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < mPesrons.size(); i++) {
            boolean has = false;

            switch (type) {
                case MailEditActivity.CONTENT_TYPE_SHOUJIAN:
                    for (int j = 0; j < mMailEditActivity.mToPesrons.size(); j++) {
                        if (mMailEditActivity.mToPesrons.get(j).getMailAddress().equals(mPesrons.get(i).getMailAddress())) {
                            has = true;
                            break;
                        }
                    }
                    if (has == false) {
                        View mView = mInflater.inflate(R.layout.mailperson, null);
                        MailContact tperson = new MailContact(mPesrons.get(i).mName,
                                mPesrons.get(i).getMailAddress(), false);
                        tperson.setmTextView((TextView) mView.findViewById(R.id.person_text));
                        mMailEditActivity.mToPesrons.add(tperson);
                        tperson.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener1);
                        tperson.getmTextView().setTag(tperson);
                        mMyLinearLayout.addView(mView, mMyLinearLayout.getChildCount() - 1);
                    }

                    break;
                case MailEditActivity.CONTENT_TYPE_CC:
                    for (int j = 0; j < mMailEditActivity.mCcPesrons.size(); j++) {
                        if (mMailEditActivity.mCcPesrons.get(j).getMailAddress().equals(mPesrons.get(i).getMailAddress())) {
                            has = true;
                            break;
                        }
                    }
                    if (has == false) {
                        View mView2 = mInflater.inflate(R.layout.mailperson, null);
                        MailContact tperson2 = new MailContact(mPesrons.get(i).mName,
                                mPesrons.get(i).getMailAddress(), false);
                        tperson2.setmTextView((TextView) mView2.findViewById(R.id.person_text));
                        mMailEditActivity.mCcPesrons.add(tperson2);
                        tperson2.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener2);
                        tperson2.getmTextView().setTag(tperson2);
                        mMyLinearLayout.addView(mView2, mMyLinearLayout.getChildCount() - 1);
                    }

                    break;
                case MailEditActivity.CONTENT_TYPE_BCC:
                    for (int j = 0; j < mMailEditActivity.mBccPesrons.size(); j++) {
                        if (mMailEditActivity.mBccPesrons.get(j).getMailAddress()
                                .equals(mPesrons.get(i).getMailAddress())) {
                            has = true;
                            break;
                        }
                    }
                    if (has == false) {
                        View mView3 = mInflater.inflate(R.layout.mailperson, null);
                        MailContact tperson3 = new MailContact(mPesrons.get(i).mName,
                                mPesrons.get(i).getMailAddress(), false);
                        tperson3.setmTextView((TextView) mView3.findViewById(R.id.person_text));
                        mMailEditActivity.mBccPesrons.add(tperson3);
                        tperson3.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener3);
                        tperson3.getmTextView().setTag(tperson3);
                        mMyLinearLayout.addView(mView3, mMyLinearLayout.getChildCount() - 1);
                    }
                    break;
                case MailEditActivity.CONTENT_TYPE_LCC:
                    for (int j = 0; j < mMailEditActivity.mLccPesrons.size(); j++) {
                        if (mMailEditActivity.mLccPesrons.get(j).getMailAddress()
                                .equals(mPesrons.get(i).getMailAddress())) {
                            has = true;
                            break;
                        }
                    }
                    if (has == false) {
                        View mView4 = mInflater.inflate(R.layout.mailperson, null);
                        MailContact tperson4 = new MailContact(mPesrons.get(i).mName,
                                mPesrons.get(i).getMailAddress(), true);
                        tperson4.mRName = mPesrons.get(i).mRName;
                        tperson4.setmTextView((TextView) mView4.findViewById(R.id.person_text));
                        mMailEditActivity.mLccPesrons.add(tperson4);
                        tperson4.getmTextView().setOnClickListener(mMailEditActivity.mPersonTextClickListener4);
                        tperson4.getmTextView().setTag(tperson4);
                        mMyLinearLayout.addView(mView4, mMyLinearLayout.getChildCount() - 1);
                    }
                    break;

            }

        }

    }

    final Html.TagHandler tagHandler = new Html.TagHandler() {

        int contentIndex = 0;

        /**
         *
         * opening : 是否为开始标签
         *
         * tag: 标签名称
         *
         * output:输出信息，用来保存处理后的信息
         *
         * xmlReader: 读取当前标签的信息，如属性等
         */

        public void handleTag(boolean opening, String tag, Editable output,

                              XMLReader xmlReader) {

            if ("mytag".equals(tag)) {

                if (opening) {// 获取当前标签的内容开始位置

                    contentIndex = output.length();

                    try {

                        final String color = (String) xmlReader.getProperty("color");

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } else {

                    final int length = output.length();

                    String content = output.subSequence(contentIndex, length).toString();

                    SpannableString spanStr = new SpannableString(content);

                    spanStr.setSpan(new ForegroundColorSpan(Color.GREEN), 0, content.length(),
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                    output.replace(contentIndex, length, spanStr);

                }

            }
            if ("style".equals(tag)) {

                if (output != null) {
                    final int length = output.length();
                    String content = output.subSequence(contentIndex, length).toString();
                    if (content.indexOf("{") != -1) {
                        String temp;
                        temp = content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1);
                        content = content.replace(temp, "");
                        content = content.replace("BLOCKQUOTE", "");
                        output.replace(contentIndex, length, content);
                    }
                }

            }

            System.out.println("opening:" + opening + ",tag:" + tag + ",output:" + output);

        }
    };
}
