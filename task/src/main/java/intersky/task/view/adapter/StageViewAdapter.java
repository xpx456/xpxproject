package intersky.task.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.task.R;
import intersky.task.entity.Stage;
import intersky.task.entity.Task;
import intersky.task.handler.ProjectStageViewHandler;
import intersky.task.view.activity.ProjectStageViewActivity;

/**
 * Created by xpx on 2017/11/10.
 */

public class StageViewAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    public static final int EVENT_DO_CHANGE = 2633302;

    public ArrayList<Stage> mStages;
    public Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public Handler mHandler;
    public DisplayMetrics metric;

    public StageViewAdapter(Context mContext, ArrayList<Stage> mStages, Handler mHandler)
    {
        this.mStages = mStages;
        this.mContext = mContext;
        this.mHandler = mHandler;
        metric = new DisplayMetrics();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType == 0)
        {
            MyViewHolder holder;
            holder = new MyViewHolder(LayoutInflater.from(
                    this.mContext).inflate(R.layout.template_task_view, parent,
                    false));
            return holder;
        }
        else
        {
            AddViewHolder holder;
            holder = new AddViewHolder(LayoutInflater.from(
                    this.mContext).inflate(R.layout.template_task_view_add, parent,
                    false));
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Stage mStage = mStages.get(position);
        if(mStage.type == 0)
        {
            MyViewHolder mMyViewHolder = (MyViewHolder) holder;
            mMyViewHolder.title.setText(mStage.name);
            if(mStage.isedit)
            {
                mMyViewHolder.add.setVisibility(View.GONE);
                mMyViewHolder.operlayer.setVisibility(View.VISIBLE);
                mMyViewHolder.editText.setVisibility(View.VISIBLE);
                mMyViewHolder.editText.setText("");
                mMyViewHolder.ok.setTag(mStage);
                mMyViewHolder.cancle.setTag(mStage);
            }
            else
            {
                mMyViewHolder.add.setVisibility(View.VISIBLE);
                mMyViewHolder.operlayer.setVisibility(View.GONE);
                mMyViewHolder.editText.setVisibility(View.GONE);
                mMyViewHolder.add.setTag(mStage);
            }
            mMyViewHolder.mTaskList.removeAllViews();
            mMyViewHolder.more.setTag(mStage);
            for(int i = 0 ; i < mStage.mTasks.size() ; i++)
            {
                addTaskView(mMyViewHolder.mTaskList,mStage.mTasks.get(i));
            }
        }
        else
        {
            //AddViewHolder mMyViewHolder = (AddViewHolder) holder;
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
        mStage.mViewview = holder.itemView;
    }

    public void addTaskView(LinearLayout layer, Task mTask)
    {

        View view = LayoutInflater.from(this.mContext).inflate(R.layout.stage_task_tiem_view, null);
        TextView title = (TextView) view.findViewById(R.id.taskname);
        TextView head = (TextView) view.findViewById(R.id.contact_img);
        ImageView imagetask = (ImageView) view.findViewById(R.id.task_son);
        ImageView imagelist = (ImageView) view.findViewById(R.id.task_list);
        TextView taskcount = (TextView) view.findViewById(R.id.task_son_count);
        TextView listcount = (TextView) view.findViewById(R.id.task_list_count);
        LinearLayout detiallayer = (LinearLayout) view.findViewById(R.id.detiallayer);
        title.setText(mTask.taskName);
        if (mTask.isComplete == 1) {
            title.setTextColor(Color.rgb(140, 140, 140));
            title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            title.setTextColor(Color.rgb(0, 0, 0));
        }
        String s = "";
        Contacts taskPerson = (Contacts) Bus.callData(mContext,"chat/getContactItem",mTask.leaderId);
        AppUtils.setContactCycleHead(head,taskPerson.getName(),taskPerson.colorhead);
        if(mTask.taskcount > 0)
        {
            taskcount.setVisibility(View.VISIBLE);
            imagetask.setVisibility(View.VISIBLE);
            taskcount.setText(String.valueOf(mTask.taskfinish)+"/"+String.valueOf(mTask.taskcount));
        }else
        {
            taskcount.setVisibility(View.GONE);
            imagetask.setVisibility(View.GONE);
        }
        if(mTask.listcount > 0)
        {
            listcount.setVisibility(View.VISIBLE);
            imagelist.setVisibility(View.VISIBLE);
            listcount.setText(String.valueOf(mTask.listfinish)+"/"+String.valueOf(mTask.listcount));
        }else
        {

            listcount.setVisibility(View.GONE);
            imagelist.setVisibility(View.GONE);
        }
        view.setTag(mTask);
        mTask.view = view;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (500 * metric.density), RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) (5* metric.density);
        layer.addView(view,layoutParams);
        view.setOnLongClickListener(mStartDrage);
        view.setOnClickListener(mStartDetial);
    }

    public View.OnLongClickListener mStartDrage = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            Message message = new Message();
            message.what = ProjectStageViewHandler.START_DRAGE;
            message.obj = v;
            mHandler.sendMessage(message);
            return true;
        }
    };

    public View.OnClickListener mStartDetial = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Message message = new Message();
            message.what = ProjectStageViewHandler.START_DETIAL;
            message.obj = v.getTag();
            mHandler.sendMessage(message);
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
        return mStages.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mStages.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onStart(int position) {
        ProjectStageViewActivity mProjectStageViewActivity = (ProjectStageViewActivity) mContext;
        mProjectStageViewActivity.startPosition = position;
        mProjectStageViewActivity.changePosition = -1;
    }

    @Override
    public void onEnd(int position) {
        ProjectStageViewActivity mProjectStageViewActivity = (ProjectStageViewActivity) mContext;
        mProjectStageViewActivity.mProjectStageViewPresenter.mProjectStageViewHandler.sendEmptyMessage(EVENT_DO_CHANGE);
    }

    @Override
    public void onItemMove(int from, int to) {

        ProjectStageViewActivity mProjectStageViewActivity = (ProjectStageViewActivity) mContext;
        mProjectStageViewActivity.changePosition = to;
    }


    @Override
    public int getItemViewType(int position) {
        return mStages.get(position).type;
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

    public View.OnClickListener doEditListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {

            Stage mStage = (Stage) v.getTag();
            mStage.isedit = true;
            doEdit(mStages.indexOf(mStage));
        }
    };

    public View.OnClickListener doCancleListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Stage mStage = (Stage) v.getTag();
            mStage.isedit = false;
            doEdit(mStages.indexOf(mStage));
        }
    };

    public View.OnClickListener doAddListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Stage mStage = (Stage) v.getTag();
            mStage.isedit = false;
            ProjectStageViewActivity mProjectStageViewActivity = (ProjectStageViewActivity) mContext;
            View bv = (View) v.getParent().getParent();
            EditText editText = (EditText) bv.findViewById(R.id.edit_text);
            mProjectStageViewActivity.mProjectStageViewPresenter.doCreatTask(editText.getText().toString(),mStage);
            doEdit(mStages.indexOf(mStage));
        }
    };

     public View.OnClickListener doMoreListener = new View.OnClickListener()
     {

         @Override
         public void onClick(View v) {
             ProjectStageViewActivity mProjectStageViewActivity = (ProjectStageViewActivity) mContext;
             mProjectStageViewActivity.mProjectStageViewPresenter.showMore((Stage) v.getTag());
         }
     };

    public void doEdit(int id)
    {
        if(id >= 0 && id < mStages.size() )
        notifyItemChanged(id);
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {

        public TextView title;
        public EditText editText;
        public ImageView more;
        public TextView ok;
        public TextView cancle;
        public RelativeLayout add;
        public RelativeLayout operlayer;
        public LinearLayout mTaskList;

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            ok = (TextView) view.findViewById(R.id.righto);
            more = (ImageView) view.findViewById(R.id.more);
            cancle = (TextView) view.findViewById(R.id.leftc);
            add = (RelativeLayout) view.findViewById(R.id.doadd);
            editText = (EditText) view.findViewById(R.id.edit_text);
            operlayer = (RelativeLayout) view.findViewById(R.id.operlayer);
            add.setOnClickListener(doEditListener);
            ok.setOnClickListener(doAddListener);
            cancle.setOnClickListener(doCancleListener);
            more.setOnClickListener(doMoreListener);
            mTaskList = (LinearLayout) view.findViewById(R.id.templateList);
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder
    {

        public RelativeLayout add;


        public AddViewHolder(View view)
        {
            super(view);
            add = (RelativeLayout) view.findViewById(R.id.addstage);
        }
    }
}
