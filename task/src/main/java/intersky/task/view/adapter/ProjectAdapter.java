package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.task.R;
import intersky.task.entity.Project;


@SuppressLint("InflateParams")
public class ProjectAdapter extends BaseAdapter {



    private ArrayList<Project> mProjects;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mHandler;

    public ProjectAdapter(Context context, ArrayList<Project> mProjects, Handler mHandler) {
        this.mContext = context;
        this.mProjects = mProjects;
        this.mHandler = mHandler;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mProjects.size();
    }

    @Override
    public Project getItem(int position) {
        // TODO Auto-generated method stub
        return mProjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Project mProject = mProjects.get(position);
        ViewHolder holder;
        holder = new ViewHolder();

        if(mProject.type == 0)
        {
            if(mProject.fileid.equals("0"))
            convertView = mInflater.inflate(R.layout.project_item, null);
            else
                convertView = mInflater.inflate(R.layout.project_item2, null);

            holder.title = (TextView) convertView.findViewById(R.id.taskname);
            holder.head = (TextView) convertView.findViewById(R.id.contact_img);
            holder.more = (ImageView) convertView.findViewById(R.id.imagemore);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.more.setTag(mProject);
            holder.more.setOnClickListener(doMoreListener);
            String s = "";
            Contacts taskPerson = (Contacts) Bus.callData(mContext,"chat/getContactItem",mProject.leaderId);
            AppUtils.setContactCycleHead( holder.head,taskPerson.getName());
            holder.title.setText(mProject.mName);
            holder.date.setText(mProject.creattime.substring(0,16));
        }
        else
        {
            convertView = mInflater.inflate(R.layout.project_item_head, null);
            ImageView array = (ImageView) convertView.findViewById(R.id.arraw);
            if(mProject.expend)
            {
                array.setImageResource(R.drawable.tip);
            }
            else
            {
                array.setImageResource(R.drawable.tip2);
            }
            holder.more = (ImageView) convertView.findViewById(R.id.imagemore);
            holder.more.setTag(mProject);
            holder.more.setOnClickListener(doMoreListener);
            holder.title = (TextView) convertView.findViewById(R.id.taskname);
            holder.title.setText(mProject.mName);
        }

        return convertView;
    }


    public void doMore(Project item)
    {
        Message msg = new Message();
        msg.what = Project.EVENT_PROJECT_MORE;
        msg.obj = item;
        mHandler.sendMessage(msg);
    }

    public View.OnClickListener doMoreListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            doMore((Project) v.getTag());
        }
    };

    private static class ViewHolder {
        private TextView title;
        private TextView head;
        private TextView date;
        private ImageView more;
    }
}
