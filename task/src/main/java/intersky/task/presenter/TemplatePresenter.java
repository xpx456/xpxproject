package intersky.task.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.mywidget.NoScrollViewPager;
import intersky.task.R;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.TampaleteAsks;
import intersky.task.entity.Project;
import intersky.task.entity.Template;
import intersky.task.entity.TemplateType;
import intersky.task.handler.TemplateHandler;
import intersky.task.prase.TampaletePrase;
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
        mTemplateActivity.flagFillBack = false;
        mTemplateActivity.setContentView(R.layout.activity_template);
        ImageView back = mTemplateActivity.findViewById(R.id.back);
        back.setOnClickListener(mTemplateActivity.mBackListener);
        mTemplateActivity.mTabHeadView = mTemplateActivity.findViewById(R.id.tabs);
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

    public void initHeadView(TemplateType headview) {
        View mView = mTemplateActivity.getLayoutInflater().inflate(R.layout.template_head, null);
        TextView textView = mView.findViewById(R.id.name);
        textView.setText(headview.name);
        textView.setOnClickListener(onTabClickListener);
        textView.setTag(headview);
        headview.mHeadView = textView;
        mTemplateActivity.mTabHeadView.addView(mView);
    }

    public void initTabs() {
        String[] names = new String[mTemplateActivity.mTemplateTypes.size()];
        for(int i = 0 ; i < mTemplateActivity.mTemplateTypes.size() ; i++)
        {
            View mView = mTemplateActivity.getLayoutInflater().inflate(R.layout.templatepager, null);
            initHeadView(mTemplateActivity.mTemplateTypes.get(i));
            mTemplateActivity.mViews.add(mView);
            names[i] = mTemplateActivity.mTemplateTypes.get(i).name;
            mTemplateActivity.mTemplateTypes.get(i).gridView = mView.findViewById(R.id.busines_List);
            mTemplateActivity.mTemplateTypes.get(i).gridView.setAdapter(mTemplateActivity.mTemplateTypes.get(i).mTaskTemplateAdapter);
            mTemplateActivity.mTemplateTypes.get(i).gridView.setOnItemClickListener(mTemplateActivity.clickAdapterListener);
        }

        mTemplateActivity.mLoderPageAdapter = new LoderPageAdapter(mTemplateActivity.mViews,names);
        mTemplateActivity.mViewPager.setAdapter(mTemplateActivity.mLoderPageAdapter);
        setSelect(mTemplateActivity.mTemplateTypes.get(0));
    }

    public View.OnClickListener onTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TemplateType templateType = (TemplateType) v.getTag();
            setSelect(templateType);
        }
    };

    public void setSelect(TemplateType headview) {
        if(mTemplateActivity.selectTemplateType != null)
        {
            mTemplateActivity.selectTemplateType.select = false;
            mTemplateActivity.selectTemplateType.mHeadView.setBackgroundResource(R.drawable.buttom_select_bg_gary);
            mTemplateActivity.selectTemplateType.mHeadView.setTextColor(Color.parseColor("#8F9093"));
        }
        mTemplateActivity.selectTemplateType = headview;
        mTemplateActivity.selectTemplateType.select = true;
        headview.mHeadView.setBackgroundResource(R.drawable.buttom_select_bg_blue);
        headview.mHeadView.setTextColor(Color.parseColor("#ffffff"));
        int a = mTemplateActivity.mTemplateTypes.indexOf(headview);
        mTemplateActivity.mViewPager.setCurrentItem(a);
        headview.mTamplates.clear();
        if(headview.type.equals("1"))
        {
            Template template = new Template();
            template.mId = "0";
            template.mType = "1";
            template.mTypeId = "0";
            template.name = mTemplateActivity.getString(R.string.task_template_empty);
            headview.mTamplates.add(0,template);
        }
        mTemplateActivity.waitDialog.show();
        TampaleteAsks.getTampalets(mTemplateActivity,mTemplateHandler,headview);
    }
}
