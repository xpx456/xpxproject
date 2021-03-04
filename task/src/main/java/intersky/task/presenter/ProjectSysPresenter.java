package intersky.task.presenter;

import android.content.IntentFilter;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import intersky.appbase.Presenter;
import intersky.oa.OaUtils;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.view.activity.ProjectSysActivity;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectSysPresenter implements Presenter {

    public ProjectSysActivity mProjectSysActivity;
    public ProjectSysPresenter(ProjectSysActivity mProjectSysActivity)
    {
        this.mProjectSysActivity = mProjectSysActivity;
    }

    @Override
    public void Create() {
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(String.valueOf(""));
    }

    @Override
    public void initView() {
        mProjectSysActivity.setContentView(R.layout.activity_sysproject);
        ImageView back = mProjectSysActivity.findViewById(R.id.back);
        back.setOnClickListener(mProjectSysActivity.mBackListener);
        TextView title = mProjectSysActivity.findViewById(R.id.title);
        title.setText(mProjectSysActivity.getIntent().getStringExtra("projectname"));
        mProjectSysActivity.mWebView = (WebView) mProjectSysActivity.findViewById(R.id.webview);
        try {
            JSONObject project = new JSONObject(TaskManager.getInstance().oaUtils.mAccount.project.toString());
            JSONObject jo = project.getJSONObject("orgs");
            String urlString = OaUtils.getOaUtils().createURLStringoa(TaskManager.getInstance().oaUtils.service,ProjectSysActivity.PROJECT_SYS);
            String postDate = "user_id="+ TaskManager.getInstance().oaUtils.mAccount.mRecordId +"&company_id="+TaskManager.getInstance().oaUtils.mAccount.mCompanyId
                    +"&users="+jo.toString()+"&type=file&project_id="+mProjectSysActivity.getIntent().getStringExtra("projectid");
            String data = URLEncoder.encode(postDate, "UTF-8");
            mProjectSysActivity.mWebView.postUrl(urlString,postDate.getBytes());
//            mProjectSysActivity.mWebView.loadUrl("www.baidu.com");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
}
