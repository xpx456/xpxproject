package intersky.mail.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.handler.MailShowHandler;
import intersky.mail.receiver.MaiShowReceiver;
import intersky.mail.view.activity.MailContactsActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.mail.view.activity.MailFengFaActivity;
import intersky.mail.view.activity.MailListActivity;
import intersky.mail.view.activity.MailShowActivity;
import intersky.mywidget.ItemOnClickListener;
import intersky.xpxnet.net.CookieUtil;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailShowPresenter implements Presenter {

    public MailShowHandler mMailShowHandler;
    public MailShowActivity mMailShowActivity;
    public MailShowPresenter(MailShowActivity mMailShowActivity)
    {
        this.mMailShowActivity = mMailShowActivity;
        this.mMailShowHandler = new MailShowHandler(mMailShowActivity);
        this.mMailShowActivity.setBaseReceiver(new MaiShowReceiver(mMailShowHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mMailShowActivity.setContentView(R.layout.activity_mail_show);
        ImageView back = mMailShowActivity.findViewById(R.id.back);
        back.setOnClickListener(mMailShowActivity.mBackListener);
        mMailShowActivity.mMail = mMailShowActivity.getIntent().getParcelableExtra("mail");
        mMailShowActivity.buttonArea = mMailShowActivity.findViewById(R.id.mail_buttom_content_layer);
        mMailShowActivity.buttonArea1 = mMailShowActivity.findViewById(R.id.mail_buttom_content_layer1);
        mMailShowActivity.buttonArea2 = mMailShowActivity.findViewById(R.id.mail_buttom_content_layer2);
        mMailShowActivity.mShade = (RelativeLayout) mMailShowActivity.findViewById(R.id.shade);
        mMailShowActivity.mScrollView = (ScrollView) mMailShowActivity.findViewById(R.id.scrollView1);
        mMailShowActivity.mWebView = (WebView) mMailShowActivity.findViewById(R.id.mail_webview);
        mMailShowActivity.mTitle = (TextView) mMailShowActivity.findViewById(R.id.mail_title);
        mMailShowActivity.mTitle.setText(mMailShowActivity.mMail.mSubject);
        mMailShowActivity.mBtnDetial = (TextView) mMailShowActivity.findViewById(R.id.open_detial_imf);
        mMailShowActivity.mDtialLayer = (RelativeLayout) mMailShowActivity.findViewById(R.id.imf_lyaer);
        mMailShowActivity.mSimpleDtialLayer = (RelativeLayout) mMailShowActivity.findViewById(R.id.imf_simple_lyaer);
        mMailShowActivity.mFujianLayer = (LinearLayout) mMailShowActivity.findViewById(R.id.mail_fujian_lyaer);
        mMailShowActivity.mFujianimg = (ImageView) mMailShowActivity.findViewById(R.id.fujianicon);
        mMailShowActivity.mSFujianimg = (ImageView) mMailShowActivity.findViewById(R.id.detial_fujianicon);
        TextView mtext = (TextView) mMailShowActivity.findViewById(R.id.detial_imf_text);
        mMailShowActivity.mBtnDetial.setOnClickListener(mMailShowActivity.mDetialListener);

        String username = "";
        if(mMailShowActivity.mMail.mFrom.toString().startsWith("\""))
        {
            username = Html.fromHtml(mMailShowActivity.mMail.mFrom).toString();
        }
        else{
            username = mMailShowActivity.mMail.mFrom;
        }
        if(username.length() == 0)
        {
            username = mMailShowActivity.mMail.mFrom;
        }
        mtext.setText(getuser(username));
        RelativeLayout mRelativeLayout = (RelativeLayout) mMailShowActivity.findViewById(R.id.time_layer);
        TextView mtextView;
        if (mMailShowActivity.mMail.mCc.length() == 0 && mMailShowActivity.mMail.mLcc.length() == 0) {
            RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mRelativeLayout.getLayoutParams();
            mLayoutParams.addRule(RelativeLayout.BELOW, R.id.shoujian_content);
            mRelativeLayout.setLayoutParams(mLayoutParams);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.cc_title);
            mtextView.setVisibility(View.GONE);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.cc_content);
            mtextView.setVisibility(View.GONE);

            mtextView = (TextView) mMailShowActivity.findViewById(R.id.lcc_title);
            mtextView.setVisibility(View.GONE);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.lcc_content);
            mtextView.setVisibility(View.GONE);
        }
        else if (mMailShowActivity.mMail.mCc.length() == 0 && mMailShowActivity.mMail.mLcc.length() > 0) {
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.lcc_title);
            RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mtextView.getLayoutParams();
            mLayoutParams.addRule(RelativeLayout.BELOW, R.id.shoujian_content);
            mtextView.setLayoutParams(mLayoutParams);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.lcc_content);
            RelativeLayout.LayoutParams mLayoutParams2 = (RelativeLayout.LayoutParams) mtextView.getLayoutParams();
            mLayoutParams.addRule(RelativeLayout.BELOW, R.id.shoujian_content);
            mtextView.setLayoutParams(mLayoutParams2);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.cc_title);
            mtextView.setVisibility(View.GONE);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.cc_content);
            mtextView.setVisibility(View.GONE);

        }
        else if (mMailShowActivity.mMail.mCc.length() > 0 && mMailShowActivity.mMail.mLcc.length() == 0) {
            RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mRelativeLayout.getLayoutParams();
            mLayoutParams.addRule(RelativeLayout.BELOW, R.id.cc_content);
            mRelativeLayout.setLayoutParams(mLayoutParams);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.lcc_title);
            mtextView.setVisibility(View.GONE);
            mtextView = (TextView) mMailShowActivity.findViewById(R.id.lcc_content);
            mtextView.setVisibility(View.GONE);
        }
        if (mMailShowActivity.mMail.haveAttachment == false) {
            mMailShowActivity.mFujianimg.setVisibility(View.GONE);
            mMailShowActivity.mSFujianimg.setVisibility(View.GONE);
        }
        else
        {
            mMailShowActivity.mFujianimg.setOnClickListener(mMailShowActivity.mShowfujianListener);
            mMailShowActivity.mSFujianimg.setOnClickListener(mMailShowActivity.mShowfujianListener);
        }

        if(MailManager.getInstance().account.iscloud == false)
        {
            if(mMailShowActivity.getIntent().getIntExtra("type",0) == MailListActivity.GET_MAIL_SHENGPIXIANG)
            {
                mMailShowActivity.buttonArea1.addView(initButtom(mMailShowActivity.getString(R.string.button_word_mailgo),mMailShowActivity.acceptListener));
                mMailShowActivity.buttonArea2.addView(initButtom(mMailShowActivity.getString(R.string.button_word_mailback),mMailShowActivity.vetoListener));
            }
            else if(mMailShowActivity.getIntent().getIntExtra("type",0) == MailListActivity.GET_MAIL_CAOGAOXIAN)
            {
                mMailShowActivity.buttonArea1.addView(initButtom(R.drawable.write,mMailShowActivity.writeListener));
                mMailShowActivity.buttonArea2.addView(initButtom(R.drawable.deletemail,mMailShowActivity.deleteListener));
            }
            else
            {
                mMailShowActivity.buttonArea1.addView(initButtom(R.drawable.resendicon,mMailShowActivity.repeatShowListener));
                mMailShowActivity.buttonArea2.addView(initButtom(R.drawable.deletemail,mMailShowActivity.deleteListener));
            }
        }
        else
        {
            if(mMailShowActivity.getIntent().getIntExtra("type",0) == MailListActivity.GET_MAIL_SHENGPIXIANG_C)
            {
                mMailShowActivity.buttonArea1.addView(initButtom(mMailShowActivity.getString(R.string.button_word_mailgo),mMailShowActivity.acceptListener));
                mMailShowActivity.buttonArea2.addView(initButtom(mMailShowActivity.getString(R.string.button_word_mailback),mMailShowActivity.vetoListener));
            }
            else if(mMailShowActivity.getIntent().getIntExtra("type",0) == MailListActivity.GET_MAIL_CAOGAOXIAN_C)
            {
                mMailShowActivity.buttonArea1.addView(initButtom(R.drawable.write,mMailShowActivity.writeListener));
                mMailShowActivity.buttonArea2.addView(initButtom(R.drawable.deletemail,mMailShowActivity.deleteListener));
            }
            else
            {
                mMailShowActivity.buttonArea1.addView(initButtom(R.drawable.repeats,mMailShowActivity.repeatShowListener));
                mMailShowActivity.buttonArea2.addView(initButtom(R.drawable.deletemail,mMailShowActivity.deleteListener));
            }
        }

        if(mMailShowActivity.getIntent().getBooleanExtra("push",false) == false)
        MailAsks.getMailView(mMailShowActivity,mMailShowHandler,mMailShowActivity.mMail);
        else
            MailAsks.getMailPushView(mMailShowActivity,mMailShowHandler,mMailShowActivity.mMail);
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

    public void showFujian() {
        mMailShowActivity.mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void startNewMail(String mail) {
        MailManager.getInstance().sendMail(mMailShowActivity,mail);
    }

    public void startAttachment(Attachment attachment) {
        Bus.callData(mMailShowActivity,"filetools/startAttachment",attachment);
    }

    public void initAttachmentView() {

        for(int i = 0 ; i < mMailShowActivity.mMail.attachments.size() ; i++)
        {
            addFujianViewItem(mMailShowActivity.mMail.attachments.get(i));
        }
    }

    public void repeatShow()
    {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        MenuItem mMenuItem = new MenuItem();
        mMenuItem.mListener = mMailShowActivity.repeatListener;
        mMenuItem.btnName = mMailShowActivity.getString(R.string.xml_mail_repeat);
        menuItems.add(mMenuItem);
        if (mMailShowActivity.mMail.mCc.length() > 0 || mMailShowActivity.mMail.mTo.contains(";")
                || mMailShowActivity.mMail.mLcc.contains(";")) {
            mMenuItem = new MenuItem();
            mMenuItem.mListener = mMailShowActivity.repeatAllListener;
            mMenuItem.btnName = mMailShowActivity.getString(R.string.xml_mail_repeatall);
            menuItems.add(mMenuItem);
        }
        mMenuItem = new MenuItem();
        mMenuItem.mListener = mMailShowActivity.resendListener;
        mMenuItem.btnName = mMailShowActivity.getString(R.string.xml_mail_resend);
        menuItems.add(mMenuItem);
        if(MailManager.getInstance().account.iscloud == false)
        {
            mMenuItem = new MenuItem();
            mMenuItem.mListener = mMailShowActivity.fenfaListener;
            mMenuItem.btnName = mMailShowActivity.getString(R.string.xml_mail_psend);
            menuItems.add(mMenuItem);
        }
        mMailShowActivity.popupWindow = AppUtils.creatButtomMenu(mMailShowActivity,mMailShowActivity.mShade,menuItems,mMailShowActivity.findViewById(R.id.activity_mail_show));
    }

    public void doRepert() {
        Intent intent = new Intent(mMailShowActivity, MailEditActivity.class);
        intent.putExtra("action",MailManager.ACTION_REPEAT);
        intent.putExtra("maildata",mMailShowActivity.mMail);
        mMailShowActivity.popupWindow.dismiss();
        mMailShowActivity.startActivity(intent);
    }

    public void doFenfa() {
        if(MailManager.getInstance().account.iscloud == false)
        {
            Intent intent = new Intent(mMailShowActivity, MailFengFaActivity.class);
            intent.putExtra("maildata",mMailShowActivity.mMail);
            mMailShowActivity.popupWindow.dismiss();
            mMailShowActivity.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(mMailShowActivity, MailContactsActivity.class);
            intent.putExtra("mail",mMailShowActivity.mMail);
            mMailShowActivity.popupWindow.dismiss();
            mMailShowActivity.startActivity(intent);
        }

    }

    public void doRepertAll() {
        Intent intent = new Intent(mMailShowActivity, MailEditActivity.class);
        intent.putExtra("action",MailManager.ACTION_REPEATALL);
        intent.putExtra("maildata",mMailShowActivity.mMail);
        mMailShowActivity.popupWindow.dismiss();
        mMailShowActivity.startActivity(intent);
    }

    public void doResend() {
        Intent intent = new Intent(mMailShowActivity, MailEditActivity.class);
        intent.putExtra("action",MailManager.ACTION_RESEND);
        intent.putExtra("maildata",mMailShowActivity.mMail);
        mMailShowActivity.popupWindow.dismiss();
        mMailShowActivity.startActivity(intent);
    }

    public void doEdit() {
        Intent intent = new Intent(mMailShowActivity, MailEditActivity.class);
        intent.putExtra("action",MailManager.ACTION_EDIT);
        intent.putExtra("maildata",mMailShowActivity.mMail);
        mMailShowActivity.startActivity(intent);
    }

    public void doShowDelete() {
        AppUtils.creatDialogTowButton(mMailShowActivity,"",mMailShowActivity.getString(R.string.button_delete)
                ,mMailShowActivity.getString(R.string.button_word_cancle),mMailShowActivity.getString(R.string.button_word_ok)
                ,null, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Mail> mails = new ArrayList<Mail>();
                        mails.add(mMailShowActivity.mMail);
                        mMailShowActivity.waitDialog.show();
                        MailAsks.deleteMails(mMailShowActivity,mMailShowHandler,mails);
                    }
                });
    }

    public void doAccept() {
        mMailShowActivity.popupWindow.dismiss();
        if(MailManager.getInstance().account.iscloud == false)
        MailAsks.sendShenpi(mMailShowActivity,mMailShowHandler,2,"",mMailShowActivity.mMail);
        else
            MailAsks.sendShenpi(mMailShowActivity,mMailShowHandler,1,"",mMailShowActivity.mMail);
    }

    public void doVote() {
        mMailShowActivity.popupWindow.dismiss();
        AppUtils.creatDialogTowButtonEdit(mMailShowActivity,"",mMailShowActivity.getString(R.string.keyword_applovedetial),
                mMailShowActivity.getString(R.string.button_word_cancle),mMailShowActivity.getString(R.string.button_word_ok),null,new EditDialogListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(MailManager.getInstance().account.iscloud == false)
                        MailAsks.sendShenpi(mMailShowActivity,mMailShowHandler,3,this.editText.getText().toString(),mMailShowActivity.mMail);
                        else
                            MailAsks.sendShenpi(mMailShowActivity,mMailShowHandler,3,this.editText.getText().toString(),mMailShowActivity.mMail);
                    }
                },mMailShowActivity.getString(R.string.keyword_applovedetial));
    }

    public void doDetial() {
        if (mMailShowActivity.showDetial) {
            setSimplayDetiallyaer();
        }
        else {
            setDetiallayer();
        }
    }

    private void setSimplayDetiallyaer() {
        mMailShowActivity.showDetial = false;
        mMailShowActivity.mSimpleDtialLayer.setVisibility(View.VISIBLE);
        mMailShowActivity.mDtialLayer.setVisibility(View.GONE);
        mMailShowActivity.mBtnDetial.setText(mMailShowActivity.getString(R.string.mail_mailshowdetial));
        TextView mtext = (TextView) mMailShowActivity.findViewById(R.id.detial_imf_text);
        if (mMailShowActivity.mMail.isLocal) {
            if (mMailShowActivity.mMail.mFrom.startsWith("\"")) {
                mtext.setText(getuser(Html.fromHtml(mMailShowActivity.mMail.mFrom).toString()));
            }
            else {
                mtext.setText(mMailShowActivity.mMail.mFrom);
            }
        }
        else {
            mtext.setText(getuser(Html.fromHtml(mMailShowActivity.mMail.mFrom).toString()));
        }

    }

    private void setDetiallayer() {
        mMailShowActivity.showDetial = true;
        mMailShowActivity.mSimpleDtialLayer.setVisibility(View.GONE);
        mMailShowActivity.mDtialLayer.setVisibility(View.VISIBLE);
        mMailShowActivity.mBtnDetial.setText(mMailShowActivity.getString(R.string.mail_mailhidedetial));
        TextView mtext = (TextView) mMailShowActivity.findViewById(R.id.fajian_content);
        mtext.setText(mMailShowActivity.mMail.mFrom.replaceAll("\"", ""));
        mtext = (TextView) mMailShowActivity.findViewById(R.id.shoujian_content);
        mtext.setText(mMailShowActivity.mMail.mTo.replaceAll("\"", ""));
        mtext = (TextView) mMailShowActivity.findViewById(R.id.cc_content);
        mtext.setText(mMailShowActivity.mMail.mCc.replaceAll("\"", ""));
        mtext = (TextView) mMailShowActivity.findViewById(R.id.lcc_content);
        mesurelcc(mMailShowActivity.mMail.mLcc, mtext);
        mtext = (TextView) mMailShowActivity.findViewById(R.id.time_content);
        mtext.setText(mMailShowActivity.mMail.mDate);
    }

    private void mesurelcc(String lcc, TextView mtext) {
        mtext.setText(lcc);
    }

    private String getuser(String word) {
        String mword = "";
        if(MailManager.getInstance().account.iscloud == false)
        {
            if (word.length() > 2 && word != null) {
                mword = word.substring(1, word.length() - 2);
            }
        }
        else
        {
            mword = word;
            if(mword.startsWith("<"))
            {
                mword = mword.substring(1,mword.length());
                if(mword.equals("@")) {
                    mword = mword.substring(0,mword.indexOf("@")-1);
                }
                if(mword.endsWith(">"))
                    mword = mword.substring(0,mword.length()-1);
            }
        }

        return mword;
    }

    public void initWebView() {
        mMailShowActivity.mWebView.setVisibility(View.VISIBLE);
        mMailShowActivity.mWebView.getSettings().setJavaScriptEnabled(true);
        mMailShowActivity.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mMailShowActivity.mWebView.setBackgroundColor(0);
        mMailShowActivity.mWebView.setBackgroundResource(R.drawable.empty);
        mMailShowActivity.mWebView.getSettings().setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMailShowActivity.mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if(MailManager.getInstance().account.iscloud == false)
        {
//            String html = measureHtml(mMailShowActivity.mMail.mContentHtml);
            CookieUtil.getInstance().syncCookie(mMailShowActivity.mWebView,"http://" + MailManager.getInstance().service.sAddress + ":"
                    + MailManager.getInstance().service.sPort);
            mMailShowActivity.mWebView.loadDataWithBaseURL(
                    "http://" + MailManager.getInstance().service.sAddress + ":"
                            + MailManager.getInstance().service.sPort,
                    mMailShowActivity.mMail.mContentHtml, "text/html", "utf-8", null);
        }
        else
        {
//            String html = measureHtml(mMailShowActivity.mMail.mContentHtml);
            CookieUtil.getInstance().syncCookie(mMailShowActivity.mWebView,"https://" + MailManager.getInstance().service.sAddress + ":"
                    + MailManager.getInstance().service.sPort);
            mMailShowActivity.mWebView.loadDataWithBaseURL(
                    "https://" + MailManager.getInstance().service.sAddress + ":"
                            + MailManager.getInstance().service.sPort,
                    mMailShowActivity.mMail.mContentHtml, "text/html", "utf-8", null);
        }
        mMailShowActivity.mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mMailShowActivity.mWebView.getSettings().setAllowFileAccess(true);
        mMailShowActivity.mWebView.getSettings().setAppCacheEnabled(true);
        mMailShowActivity.mWebView.getSettings().setDomStorageEnabled(true);
        mMailShowActivity.mWebView.getSettings().setDatabaseEnabled(true);
        mMailShowActivity.mWebView.addJavascriptInterface(new JavascriptInterface(mMailShowActivity), "imagelistener");
        mMailShowActivity.mWebView.setWebViewClient(new MailWebViewClient());
//		mMailShowActivity.mWebView.loadUrl("http://192.168.100.237:8081/webapp/index.html");
    }

    public String measureHtml(String html) {
        if (MailManager.getInstance().service.https == false)
            return html.replaceAll("/img", "http://" + MailManager.getInstance().service.sAddress + ":" + MailManager.getInstance().service.sPort + "/image/" + MailManager.getInstance().account.mCompanyId);
        else
            return html.replaceAll("/img", "https://" + MailManager.getInstance().service.sAddress + ":" + MailManager.getInstance().service.sPort + "/image/" + MailManager.getInstance().account.mCompanyId);
    }

    private void addFujianViewItem(Attachment mFuJianItem) {
        LayoutInflater mInflater = (LayoutInflater) mMailShowActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.mail_attachment_item, null);
        if (mView != null) {
            TextView mTextView = (TextView) mView.findViewById(R.id.fujian_name);
            mTextView.setText(mFuJianItem.mName);
            mTextView = (TextView) mView.findViewById(R.id.fujian_size);
            mTextView.setText(AppUtils.getSizeText(Long.valueOf(mFuJianItem.mSize)));
            mView.setOnClickListener(mMailShowActivity.mAttachmentListener);
            mView.setTag(mFuJianItem);
            mMailShowActivity.mFujianLayer.addView(mView);
        }

    }

    private View initButtom(int id, View.OnClickListener listener) {
        View view = mMailShowActivity.getLayoutInflater().inflate(R.layout.button_image,null);
        ImageView imageView = view.findViewById(R.id.buttom);
        imageView.setImageResource(id);
        view.setOnClickListener(listener);
        imageView.setOnClickListener(listener);
        return view;
    }

    private View initButtom(String id, View.OnClickListener listener) {
        View view = mMailShowActivity.getLayoutInflater().inflate(R.layout.button_text,null);
        TextView textView = view.findViewById(R.id.buttom);
        textView.setText(id);
        view.setOnClickListener(listener);
        textView.setOnClickListener(listener);
        return view;
    }

    private class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            showSavePicture(img);

        }

    }

    private void showSavePicture(final String url) {
        ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
        mMenuItems.add(new MenuItem(mMailShowActivity.getString(R.string.button_word_save), saveListener,url));
        mMenuItems.add(new MenuItem(mMailShowActivity.getString(R.string.button_word_share),shareListener,url));
        mMailShowActivity.popupWindow = AppUtils.creatButtomMenu(mMailShowActivity,mMailShowActivity.mShade,mMenuItems,mMailShowActivity.findViewById(R.id.activity_mail_show));
    }


    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag();
            MailManager.getInstance().xpxShare.doShare(mMailShowActivity,"","",url);
            mMailShowActivity.popupWindow.dismiss();
        }
    };

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag();
            String mPath = Bus.callData(MailManager.getInstance().context,"filetools/getfilePath", "mail/temp")+"/"+url.substring(url.lastIndexOf("/"),url.length()) ;
            Bus.callData(mMailShowActivity,"filetools/savenet",new File(mPath),url);
            mMailShowActivity.popupWindow.dismiss();
        }
    };


    private class MailWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            String mail = url.substring(url.indexOf("mailto:") + 7);
            if (matchMail(mail)) {
                startNewMail(mail);
                return true;
            }
            else {

//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                mMailShowActivity.startActivity(intent);

                return true;
            }
            // return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CookieUtil.getInstance().addCookie(url);
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
        }

        private void addImageClickListener(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
                    "})()");
        }

        private boolean matchMail(String mailAddress) {
            Pattern pattern = Pattern.compile(
                    "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

            Matcher matcher = pattern.matcher(mailAddress);
            return matcher.matches();

        }
    }

}
