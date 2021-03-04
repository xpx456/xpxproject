package intersky.task.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import intersky.appbase.BaseActivity;
import intersky.task.asks.ProjectAsks;
import intersky.task.entity.Project;
import intersky.task.presenter.AddProjectPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class AddProjectActivity extends BaseActivity {

    public static final int EVENT_APPLY_SUCCESS = 1000;
    public static final int EVENT_APPLY_FAIL = 1001;
    public AddProjectPresenter mAddProjectPresenter = new AddProjectPresenter(this);
    public TextView projectadd;
    public Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddProjectPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAddProjectPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mAddListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            ProjectAsks.doApply(mAddProjectPresenter.mAddProjectActivity,mAddProjectPresenter.mAddProjectHandler,project);
        }
    };
}
