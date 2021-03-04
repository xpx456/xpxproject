package intersky.task.presenter;

import android.widget.ImageView;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.task.R;
import intersky.task.handler.AddProjectHandler;
import intersky.task.view.activity.AddProjectActivity;

/**
 * Created by xpx on 2017/8/18.
 */

public class AddProjectPresenter implements Presenter {

    public AddProjectHandler mAddProjectHandler;
    public AddProjectActivity mAddProjectActivity;

    public AddProjectPresenter(AddProjectActivity mAddProjectActivity)
    {
        this.mAddProjectActivity = mAddProjectActivity;
        this.mAddProjectHandler = new AddProjectHandler(mAddProjectActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mAddProjectActivity.setContentView(R.layout.activity_addproject);
        ImageView back = mAddProjectActivity.findViewById(R.id.back);
        back.setOnClickListener(mAddProjectActivity.mBackListener);

        mAddProjectActivity.project = mAddProjectActivity.getIntent().getParcelableExtra("project");
        mAddProjectActivity.projectadd = (TextView) mAddProjectActivity.findViewById(R.id.addbtn);
        mAddProjectActivity.projectadd.setOnClickListener(mAddProjectActivity.mAddListener);
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
