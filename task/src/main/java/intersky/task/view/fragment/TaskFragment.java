package intersky.task.view.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.BaseFragment;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.entity.CourseClass;
import intersky.task.entity.Task;
import intersky.task.presenter.TaskManagerPresenter;
import intersky.task.view.activity.TaskManagerActivity;

@SuppressLint("ValidFragment")
public class TaskFragment extends BaseFragment {
    public TaskManagerActivity mTaskManagerActivity;
    public PullToRefreshView mPullToRefreshView;
    public SearchViewLayout mSearchViewLayout;
    public RelativeLayout btnStatuhensive;
    public TextView txtStatuhens;
    public ImageView imgStatuhens;
    public RelativeLayout btnPersonhensive;
    public TextView txtPersonhens;
    public ImageView imgPersonhens;
    public RelativeLayout btnOrderhensive;
    public TextView txtOrderhens;
    public ImageView imgOrderhens;
    public RelativeLayout btnTaghensive;
    public TextView txtTaghens;
    public ImageView imgTaghens;
    public LinearLayout topLayer;
    public ListView mClassList;
    public ListView mTaskList;
    public boolean showStatu = false;
    public boolean showPerson = false;
    public boolean showOrder = false;
    public boolean showTag = false;
    public RelativeLayout mShade;
    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTaskManagerActivity = (TaskManagerActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_task, container, false);
        mSearchViewLayout = (SearchViewLayout) mView.findViewById(R.id.top_layer);
        mSearchViewLayout.mSetOnSearchListener(mOnEditorActionListener);
        mSearchViewLayout.setDotextChange(doTextChange);
        mSearchViewLayout.hidEdit();
        mSearchViewLayout.sethit(mTaskManagerActivity.getString(R.string.task_search_hit));
        btnStatuhensive = (RelativeLayout) mView.findViewById(R.id.statu_layer_head);
        txtStatuhens = (TextView) mView.findViewById(R.id.statu_head_title);
        txtStatuhens.setText(mTaskManagerActivity.mTaskType.mCatName);
        imgStatuhens = (ImageView) mView.findViewById(R.id.statu_img);
        btnPersonhensive = (RelativeLayout) mView.findViewById(R.id.person_layer_head);
        txtPersonhens = (TextView) mView.findViewById(R.id.person_head_title);
        txtPersonhens.setText(mTaskManagerActivity.mTaskFilter.mCatName);
        imgPersonhens = (ImageView) mView.findViewById(R.id.person_img);
        btnOrderhensive = (RelativeLayout) mView.findViewById(R.id.select_layer_head);
        txtOrderhens = (TextView) mView.findViewById(R.id.select_head_title);
        txtOrderhens.setText(mTaskManagerActivity.mTaskOrder.mCatName);
        imgOrderhens = (ImageView) mView.findViewById(R.id.select_img);
        btnTaghensive = (RelativeLayout) mView.findViewById(R.id.tag_layer_head);
        txtTaghens = (TextView) mView.findViewById(R.id.tag_head_title);
        txtTaghens.setText(mTaskManagerActivity.mTaskTag.mCatName);
        imgTaghens = (ImageView) mView.findViewById(R.id.tag_img);

        mShade = (RelativeLayout) mView.findViewById(R.id.shade);
        mClassList = (ListView) mView.findViewById(R.id.class_list);
        mTaskList = (ListView) mView.findViewById(R.id.task_list);
        mTaskList.setOnScrollListener(mscoll);
        topLayer = (LinearLayout) mView.findViewById(R.id.classlistlayer);
        mPullToRefreshView = (PullToRefreshView) mView.findViewById(R.id.task_pull_refresh_view);
        mPullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mPullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mPullToRefreshView.setOnHeaderRefreshListener(mTaskManagerActivity);
        mPullToRefreshView.setOnFooterRefreshListener(mTaskManagerActivity);
        mPullToRefreshView.setVisibility(View.VISIBLE);
        btnStatuhensive.setOnClickListener(mShowClassLinstener1);
        btnPersonhensive.setOnClickListener(mShowClassLinstener2);
        btnOrderhensive.setOnClickListener(mShowClassLinstener3);
        btnTaghensive.setOnClickListener(mShowClassLinstener4);
        mTaskList.setAdapter(mTaskManagerActivity.mTaskAdapter);
        mTaskList.setOnItemClickListener(clickTask);
        mShade.setOnClickListener(shadeClick);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }


    public AdapterView.OnItemClickListener clickTask = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskManagerActivity.mTaskManagerPresenter.startDetial((Task) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener mShowClassLinstener1 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerActivity.mTaskManagerPresenter.doClass1();
        }
    };

    public View.OnClickListener mShowClassLinstener2 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerActivity.mTaskManagerPresenter.doClass2();
        }
    };

    public View.OnClickListener mShowClassLinstener3 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerActivity.mTaskManagerPresenter.doClass3();
        }
    };

    public View.OnClickListener mShowClassLinstener4 = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerActivity.mTaskManagerPresenter.doClass4();
        }
    };


    public AdapterView.OnItemClickListener statuListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskManagerActivity.mTaskManagerPresenter.doSearchStatu((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener filterSelectListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskManagerActivity.mTaskManagerPresenter.doSearchFilter((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener classOrderListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskManagerActivity.mTaskManagerPresenter.doOrder((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener classTagListenter = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskManagerActivity.mTaskManagerPresenter.doTag((CourseClass) parent.getAdapter().getItem(position));
        }
    };

    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                if(mSearchViewLayout.getText().length()> 0)
                    mTaskManagerActivity.mTaskManagerPresenter.onSearch(mSearchViewLayout.getText());

            }
            return true;
        }
    };

    public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            if(visiable)
            {

            }
            else
            {
                mTaskList.setAdapter(mTaskManagerActivity.mTaskAdapter);
                TaskManager.getInstance().taskSearchAll = false;
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().mSearchTasks.clear();
            }
        }
    };

    public TextWatcher mTextchange = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(mSearchViewLayout.getText().length()> 0)
            {
                mSearchViewLayout.showEdit();

            }
            else
            {
                mSearchViewLayout.hidEdit();
                mTaskList.setAdapter(mTaskManagerActivity.mTaskAdapter);
                TaskManager.getInstance().taskSearchAll = false;
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().mSearchTasks.clear();
            }


        }
    };

    public View.OnClickListener shadeClick = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mTaskManagerActivity.mTaskManagerPresenter.doHidall();
        }
    };

    public AbsListView.OnScrollListener mscoll = new AbsListView.OnScrollListener()
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(mSearchViewLayout.ishow == true)
            {
                mTaskList.setAdapter(mTaskManagerActivity.mTaskAdapter);
                TaskManager.getInstance().taskSearchAll = false;
                TaskManager.getInstance().taskSearchPage = 1;
                TaskManager.getInstance().mSearchTasks.clear();
            }
            mSearchViewLayout.hidEdit();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

}
