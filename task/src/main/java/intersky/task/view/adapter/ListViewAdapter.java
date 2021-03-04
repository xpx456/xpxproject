package intersky.task.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.entity.TaskList;
import intersky.task.view.activity.TaskDetialActivity;

/**
 * Created by xpx on 2017/11/10.
 */

public class ListViewAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    public static final int EVENT_DO_CHANGE = 2633302;


    public ArrayList<TaskList> mTaskLists;
    public Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public Handler mHandler;
    public ListViewAdapter(Context mContext, ArrayList<TaskList> mTaskLists, Handler mHandler)
    {
        this.mTaskLists = mTaskLists;
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType == TaskList.LIST_TYPE_ADD)
        {
            AddViewHolder holder;
            holder = new AddViewHolder(LayoutInflater.from(
                    this.mContext).inflate(R.layout.task_list_son_item_add, parent,
                    false));
            return holder;
        }
        else if(viewType == TaskList.LIST_TYPE_ITEM)
        {
            ItemViewHolder holder;
            holder = new ItemViewHolder(LayoutInflater.from(
                    this.mContext).inflate(R.layout.list_son_item, parent,
                    false));
            return holder;
        }
        else
        {
            HeadViewHolder holder;
            holder = new HeadViewHolder(LayoutInflater.from(
                    this.mContext).inflate(R.layout.task_list_item_head, parent,
                    false));
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TaskList mTaskList = mTaskLists.get(position);
        TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
        if(mTaskList.type == TaskList.LIST_TYPE_HEAD)
        {
            HeadViewHolder mHeadViewHolder = (HeadViewHolder) holder;
            mHeadViewHolder.name.setTag(mTaskList);
            mHeadViewHolder.name.setText(mTaskList.name);
            mHeadViewHolder.name.setHint(mTaskList.name);
            mHeadViewHolder.progress.setMax(100);
            if(mTaskList.listcount > 0)
            {
                mHeadViewHolder.progress.setProgress(100*mTaskList.finishcount/mTaskList.listcount);
                mHeadViewHolder.precent.setText(String.valueOf(100*mTaskList.finishcount/mTaskList.listcount)+"%");
            }
            else
            {
                mHeadViewHolder.progress.setProgress(0);
                mHeadViewHolder.precent.setText(String.valueOf(0)+"%");
            }
            mHeadViewHolder.textView.setText(String.valueOf(mTaskList.finishcount)+"/"+String.valueOf(mTaskList.listcount));
            mHeadViewHolder.imageView.setTag(mTaskList);
            if(mHeadViewHolder.name.hasFocus() == true)
            {
                mHeadViewHolder.imageView.setImageResource(R.drawable.ntask_morew2);
                mHeadViewHolder.imageView.setOnClickListener(mTaskDetialActivity.listMore);
            }
            else
            {
                mHeadViewHolder.imageView.setOnClickListener(mTaskDetialActivity.listExpend);
                if(mTaskList.expend)
                {
                    mHeadViewHolder.imageView.setImageResource(R.drawable.sniper1_s);
                    mHeadViewHolder.progress2.setVisibility(View.VISIBLE);
                    mHeadViewHolder.black.setVisibility(View.GONE);

                }
                else
                {
                    mHeadViewHolder.imageView.setImageResource(R.drawable.sniper1);
                    mHeadViewHolder.progress2.setVisibility(View.GONE);
                    mHeadViewHolder.black.setVisibility(View.VISIBLE);
                }
            }
            mTaskList.mView= mHeadViewHolder.itemView;
        }
        else if(mTaskList.type == TaskList.LIST_TYPE_ITEM)
        {
            ItemViewHolder mItemViewHolder = (ItemViewHolder) holder;
            mItemViewHolder.name.setTag(mTaskList);
            mItemViewHolder.name.setText(mTaskList.name);
            mItemViewHolder.name.setHint(mTaskList.name);
            mItemViewHolder.name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TaskList mlist = (TaskList) v.getTag();
                    View mView = mlist.mView;
                    if(mView != null)
                    {
                        ImageView imageView = (ImageView) mView.findViewById(R.id.contact_img);
                        EditText name = (EditText) mView.findViewById(R.id.title);
                        if(hasFocus)
                        {
                            imageView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            imageView.setVisibility(View.INVISIBLE);
                            name.setText(name.getHint());
                        }
                    }

                }
            });
            mItemViewHolder.mCheck.setTag(mTaskList);

            if (mTaskList.isComplete == 0) {
                mItemViewHolder.name.setTextColor(Color.rgb(118, 118, 118));
                mItemViewHolder.mCheck.setImageResource(R.drawable.ntask_select);
                mItemViewHolder.name.getPaint().setFlags(0);
            }
            if (mTaskList.isComplete == 1) {
                mItemViewHolder.name.setTextColor(Color.rgb(140, 140, 140));
                mItemViewHolder.mCheck.setImageResource(R.drawable.ntask_selected);
                mItemViewHolder.name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            mItemViewHolder.imageView.setTag(mTaskList);
            mItemViewHolder.imageView.setOnClickListener(mTaskDetialActivity.listItemMore);
            if(mItemViewHolder.name.isFocused())
            {
                mItemViewHolder.imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                mItemViewHolder.imageView.setVisibility(View.INVISIBLE);
            }
            mTaskList.mView= mItemViewHolder.itemView;
        }
        else
        {
            AddViewHolder mAddViewHolder = (AddViewHolder) holder;
            mAddViewHolder.editText.setTag(mTaskList);
            mTaskList.mView= mAddViewHolder.itemView;

        }

        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return false;
                }
            });
        }
    }

    public void updata()
    {
        notifyDataSetChanged();
    }

    public View.OnClickListener editclick = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            EditText editText = (EditText) v;
            editText.requestFocus();
            editText.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            updata();
        }
    };



    public View.OnClickListener maddListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
//            ProjectStageViewActivity mProjectStageViewActivity = (ProjectStageViewActivity) mContext;
//            mProjectStageViewActivity.mProjectStageViewPresenter.doCreatSelect();
        }
    };

    @Override
    public int getItemCount() {
        return mTaskLists.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mTaskLists.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onStart(int position) {
        TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
        mTaskDetialActivity.startPosition = position;
        mTaskDetialActivity.changePosition = -1;
    }

    @Override
    public void onEnd(int position) {
        TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
        mTaskDetialActivity.mTaskDetialPresenter.mTaskDetialHandler.sendEmptyMessage(EVENT_DO_CHANGE);
    }

    @Override
    public void onItemMove(int from, int to) {

        TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
        mTaskDetialActivity.changePosition = to;
    }


    @Override
    public int getItemViewType(int position) {
        return mTaskLists.get(position).type;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(RecyclerView.ViewHolder view, int position);
    }



    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder
    {

        public EditText name;
        public ProgressBar progress;
        public TextView textView;
        public TextView precent;
        public ImageView imageView;
        public RelativeLayout progress2;
        public RelativeLayout black;
        public HeadViewHolder(View view)
        {
            super(view);
            final TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
            black = (RelativeLayout) itemView.findViewById(R.id.black);
            name = (EditText) itemView.findViewById(R.id.title);
            name.setOnEditorActionListener(mTaskDetialActivity.mOnListQuictClick);
//            if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(InterskyApplication.mApp.mUser.getUsernRecordid())
//                    || mTaskDetialActivity.mTask.userId.equals(InterskyApplication.mApp.mUser.getUsernRecordid()))
//            {
//                name.setEnabled(true);
//            }
//            else
//            {
//                name.setEnabled(false);
//            }

            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TaskList mlist = (TaskList) v.getTag();
                    View mView = mlist.mView;
                    if(mView != null)
                    {
                        ImageView imageView = (ImageView) mView.findViewById(R.id.contact_img);
                        if(hasFocus)
                        {
                            imageView.setImageResource(R.drawable.ntask_morew2);
                            imageView.setOnClickListener(mTaskDetialActivity.listMore);
                        }
                        else
                        {
                            EditText name = (EditText) mView.findViewById(R.id.title);
                            name.setText(name.getHint());
                            if(mlist.expend)
                            {
                                imageView.setImageResource(R.drawable.sniper1_s);
                                imageView.setOnClickListener(mTaskDetialActivity.listExpend);
                            }
                            else
                            {
                                imageView.setImageResource(R.drawable.sniper1);
                                imageView.setOnClickListener(mTaskDetialActivity.listExpend);
                            }
                        }
                    }

                }
            });
            name.setOnEditorActionListener(mTaskDetialActivity.mOnListQuictClick);
            progress = (ProgressBar) itemView.findViewById(R.id.roundProgressBar);
            progress.setMax(100);
            textView = (TextView) itemView.findViewById(R.id.size);
            precent = (TextView) itemView.findViewById(R.id.precent);
            imageView = (ImageView) itemView.findViewById(R.id.contact_img);
            progress2 = (RelativeLayout) itemView.findViewById(R.id.progress);
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder
    {

        public EditText editText;


        public AddViewHolder(View view)
        {
            super(view);
            final TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
            editText = (EditText) itemView.findViewById(R.id.title);
            editText.setOnEditorActionListener(mTaskDetialActivity.mOnQuictListCreatClick);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
    {

        public EditText name;
        public ImageView mCheck;
        public ImageView imageView;

        public ItemViewHolder(View view)
        {
            super(view);
            final TaskDetialActivity mTaskDetialActivity = (TaskDetialActivity) mContext;
            name = (EditText) itemView.findViewById(R.id.title);
            if (mTaskDetialActivity.mTask.isLocked == 0 || mTaskDetialActivity.mTask.leaderId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId)
                    || mTaskDetialActivity.mTask.userId.equals(TaskManager.getInstance().oaUtils.mAccount.mRecordId))
            {
                name.setEnabled(true);
            }
            else
            {
                name.setEnabled(false);
            }
            name.setOnEditorActionListener(mTaskDetialActivity.mOnListItemQuictClick);
            mCheck = (ImageView) itemView.findViewById(R.id.task_finish);
            mCheck.setOnClickListener(mTaskDetialActivity.setListCheckListener);
            imageView = (ImageView) itemView.findViewById(R.id.contact_img);
        }
    }
}
