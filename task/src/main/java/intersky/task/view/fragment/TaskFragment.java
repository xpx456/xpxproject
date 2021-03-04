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
    public LinearLayout topLayer;
    public ListView mTaskList;
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
        mShade = (RelativeLayout) mView.findViewById(R.id.shade);
        mTaskList = (ListView) mView.findViewById(R.id.task_list);
        mTaskList.setOnScrollListener(mscoll);
        topLayer = (LinearLayout) mView.findViewById(R.id.classlistlayer);
        mPullToRefreshView = (PullToRefreshView) mView.findViewById(R.id.task_pull_refresh_view);
        mPullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mPullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mPullToRefreshView.setOnHeaderRefreshListener(mTaskManagerActivity);
        mPullToRefreshView.setOnFooterRefreshListener(mTaskManagerActivity);
        mPullToRefreshView.setVisibility(View.VISIBLE);
        mTaskList.setAdapter(mTaskManagerActivity.mTaskAdapter);
        mTaskList.setOnItemClickListener(clickTask);
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




    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mTaskManagerActivity.mTaskManagerPresenter.onSearch(mSearchViewLayout.getText());
            }
            return true;
        }
    };

    public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {

        }
    };



    public AbsListView.OnScrollListener mscoll = new AbsListView.OnScrollListener()
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mSearchViewLayout.hidEdit();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

}
