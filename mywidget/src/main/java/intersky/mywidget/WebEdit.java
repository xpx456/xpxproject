package intersky.mywidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.apache.commons.lang3.StringEscapeUtils;


/**
 * Created by xpx on 2018/3/19.
 */

public class WebEdit extends WebView {

    public String html = "";
    public String hit = "";
    public String action = "action";
    public boolean editable = true;
    public long time;
    public int txtcolor = Color.BLACK;
    public int txtsize = 15;
    public String baseUrl;
    public WebEdit(Context context) {
        super(context);
    }

    public WebEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            time = System.currentTimeMillis();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(System.currentTimeMillis() - time < 1000)
            {
                if(editable == true)
                startEdit();
            }
        }
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public void setText(String text) {
        html = StringEscapeUtils.unescapeHtml4(text);
        show();
    }

    public String getText()
    {
        return html;
    }

    public void setEditable(boolean edit)
    {
        editable = edit;
    }

    public void setBaseUrl(String baseUrl)
    {
        baseUrl = baseUrl;
    }

    public void setHit(String color, String text) {
        hit = "<font color=\""+color+"\">"+text+"</font>";
        show();
    }

    public void setTxtColor(int color)
    {
        txtcolor = color;
    }

    public void setTxtSize(int size)
    {
        txtsize = size;
    }

    public void show() {
        if (html.length() > 0) {
            this.addjs(html);
        } else {
            this.addjs(hit);
        }
    }

    private void addjs(String value) {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.loadDataWithBaseURL(
                "http://" + baseUrl,
                value, "text/html", "utf-8", null);
        this.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.getSettings().setAllowFileAccess(true);
        this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setDatabaseEnabled(true);
//        mNewNoticeActivity.mWebView.addJavascriptInterface(new MailShowPresenter.JavascriptInterface(mNewNoticeActivity), "imagelistner");
//        mNewNoticeActivity.mWebView.setWebViewClient(new MailShowPresenter.MyWebViewClient());
////		mMailShowActivity.mWebView.loadUrl("http://192.168.100.237:8081/webapp/index.html");
    }

    public void startEdit() {
        Intent intent = null;
        try {
            intent = new Intent(this.getContext(), Class.forName("intersky.riche.RichEditActivity"));
            intent.setAction(action);
            if (html.length() > 0)
                intent.putExtra("value", html);
            else
                intent.putExtra("value", hit);
            intent.putExtra("color",txtcolor);
            intent.putExtra("size",txtsize);
            this.getContext().startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
