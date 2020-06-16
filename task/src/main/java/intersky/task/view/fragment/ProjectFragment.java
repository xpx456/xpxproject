package intersky.task.view.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import intersky.appbase.BaseFragment;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;
import intersky.task.R;
import intersky.task.entity.Project;
import intersky.task.presenter.TaskManagerPresenter;
import intersky.task.view.activity.TaskManagerActivity;

@SuppressLint("ValidFragment")
public class ProjectFragment extends BaseFragment {


    public TaskManagerActivity mTaskManagerActivity;
    public ListView projectList;
    public PullToRefreshView mPullToRefreshView;
    public SearchViewLayout mSearchViewLayout;
    public ProjectFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTaskManagerActivity = (TaskManagerActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_project, container, false);
        mSearchViewLayout = (SearchViewLayout) mView.findViewById(R.id.top_layer);
        mSearchViewLayout.mSetOnSearchListener(mOnEditorActionListener);
        mSearchViewLayout.setDotextChange(doTextChange);
        mSearchViewLayout.sethit(mTaskManagerActivity.getString(R.string.task_search_hit2));
        projectList = (ListView) mView.findViewById(R.id.project_list);
        mPullToRefreshView = (PullToRefreshView) mView.findViewById(R.id.task_pull_refresh_view);
        projectList.setAdapter(mTaskManagerActivity.mProjectAdapter);
        mPullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mPullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mPullToRefreshView.setOnHeaderRefreshListener(mTaskManagerActivity);
        mPullToRefreshView.setOnFooterRefreshListener(mTaskManagerActivity);
        projectList.setAdapter(mTaskManagerActivity.mProjectAdapter);
        projectList.setOnItemClickListener(clickProject);
        return mView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }


    public AdapterView.OnItemClickListener clickFunction = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                if(mSearchViewLayout.getText().length() > 0)
                    mTaskManagerActivity.mTaskManagerPresenter.onSearch(mSearchViewLayout.getText());
                else
                    projectList.setAdapter(mTaskManagerActivity.mProjectAdapter);
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
                projectList.setAdapter(mTaskManagerActivity.mProjectAdapter);
            }
        }
    };

    public AdapterView.OnItemClickListener clickProject = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTaskManagerActivity.mTaskManagerPresenter.startDetial((Project) parent.getAdapter().getItem(position));
        }
    };
}
