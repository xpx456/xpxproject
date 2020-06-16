package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.select.entity.CustomSelect;
import intersky.select.view.adapter.CustomSelectAdapter;
import intersky.task.R;
import intersky.task.entity.Task;

@SuppressLint("InflateParams")
public class TaskSelectAdapter extends CustomSelectAdapter {


    public TaskSelectAdapter(Context context, ArrayList<CustomSelect> mProjectItemModels) {
        super(context, mProjectItemModels);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSelectMores.size();
    }

    @Override
    public CustomSelect getItem(int position) {
        // TODO Auto-generated method stub
        return mSelectMores.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        CustomSelect mTaskItemModel = mSelectMores.get(position);
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.task_select_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.taskname);
            holder.head = (TextView) convertView.findViewById(R.id.contact_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = "";
        Task task = (Task) mTaskItemModel.object;
        Contacts taskPerson = (Contacts) Bus.callData(mContext,"chat/getContactItem",task.leaderId);
        AppUtils.setContactCycleHead(holder.head,taskPerson.getName(),taskPerson.colorhead);
        holder.title.setText(mTaskItemModel.mName);
        return convertView;
    }


    private static class ViewHolder {
        private TextView title;
        private TextView head;
    }
}
