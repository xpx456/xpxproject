package intersky.task.view.activity;

import android.os.Bundle;
import android.webkit.WebView;

import intersky.task.presenter.ProjectSysPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectSysActivity extends BaseActivity {

    public static final String PROJECT_SYS = "index/Phone/mobile_statistics";
    public ProjectSysPresenter mProjectSysPresenter = new ProjectSysPresenter(this);
    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectSysPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mProjectSysPresenter.Destroy();
        super.onDestroy();
    }
}
