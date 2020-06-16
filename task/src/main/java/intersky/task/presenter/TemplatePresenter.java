package intersky.task.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.mywidget.NoScrollViewPager;
import intersky.task.R;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TampaleteAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Template;
import intersky.task.handler.TemplateHandler;
import intersky.task.view.activity.ProjectStageDetialActivity;
import intersky.task.view.activity.TemplateActivity;
import intersky.task.view.adapter.LoderPageAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class TemplatePresenter implements Presenter {

    public TemplateHandler mTemplateHandler;
    public TemplateActivity mTemplateActivity;


    public TemplatePresenter(TemplateActivity mTemplateActivity) {
        this.mTemplateActivity = mTemplateActivity;
        this.mTemplateHandler = new TemplateHandler(mTemplateActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mTemplateActivity.setContentView(R.layout.activity_template);
        ToolBarHelper.setTitle(mTemplateActivity.mActionBar, mTemplateActivity.getString(R.string.task_project_new_title));
        mTemplateActivity.mTabHeadView = mTemplateActivity.findViewById(R.id.head);
        mTemplateActivity.mViewPager = mTemplateActivity.findViewById(R.id.load_pager1);
        mTemplateActivity.mViewPager.setNoScroll(true);
        TampaleteAsks.getTampaleteType(mTemplateActivity,mTemplateHandler);
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


    public void startProjects(Project project)
    {
        Intent intent = new Intent(mTemplateActivity, ProjectStageDetialActivity.class);
        intent.putExtra("project",project);
        mTemplateActivity.startActivity(intent);
    }

    public void onitemcick(Template mTemplateModel) {

        AppUtils.creatDialogTowButtonEdit(mTemplateActivity,"",mTemplateActivity.getString(R.string.task_project_new_title),
                mTemplateActivity.getString(R.string.button_word_cancle),mTemplateActivity.getString(R.string.button_word_ok),null,
                new CreatListener(mTemplateModel),mTemplateModel.name);
    }

    public class CreatListener extends EditDialogListener
    {
        public Template template;

        public CreatListener(Template template) {
            this.template = template;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                Project project = new Project();
                project.mName = editText.getText().toString();
                project.templateId = template.mId;
                ProjectAsks.creatProject(mTemplateActivity,mTemplateHandler,project, template);
            } else {
                AppUtils.showMessage(mTemplateActivity, mTemplateActivity.getString(R.string.task_project_new_name));
            }
        }
    };


    public void initTabs() {
        String[] names = new String[mTemplateActivity.mTemplateTypes.size()];
        for(int i = 0 ; i < mTemplateActivity.mTemplateTypes.size() ; i++)
        {
            View mView = mTemplateActivity.getLayoutInflater().inflate(R.layout.templatepager, null);
            mTemplateActivity.mViews.add(mView);
            names[i] = mTemplateActivity.mTemplateTypes.get(i).name;
            mTemplateActivity.mTemplateTypes.get(i).gridView = mView.findViewById(R.id.busines_List);
            mTemplateActivity.mTemplateTypes.get(i).gridView.setAdapter(mTemplateActivity.mTemplateTypes.get(i).mTaskTemplateAdapter);
            mTemplateActivity.mTemplateTypes.get(i).gridView.setOnItemClickListener(mTemplateActivity.clickAdapterListener);
        }
        mTemplateActivity.mLoderPageAdapter = new LoderPageAdapter(mTemplateActivity.mViews,names);
        mTemplateActivity.mViewPager.setAdapter(mTemplateActivity.mLoderPageAdapter);
        mTemplateActivity.mTabHeadView.setViewPager(mTemplateActivity.mViewPager);
        mTemplateActivity.mViewPager.setCurrentItem(0);
//        mTemplateActivity.mTabHeadView.setmOnTabLisener(onTabLisener);
    }


}
