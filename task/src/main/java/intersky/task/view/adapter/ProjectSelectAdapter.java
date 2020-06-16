package intersky.task.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.select.entity.CustomSelect;
import intersky.select.view.adapter.CustomSelectAdapter;
import intersky.task.R;
import intersky.task.entity.Project;

public class ProjectSelectAdapter extends CustomSelectAdapter {


    public ProjectSelectAdapter(Context context, ArrayList<CustomSelect> mSelectMores) {

        super(context, mSelectMores);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mSelectMores.size();
    }

    @Override
    public CustomSelect getItem(int position)
    {
        // TODO Auto-generated method stub
        return mSelectMores.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        CustomSelect mSelectMoreModel = mSelectMores.get(position);
        ViewHolder mview = null;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.project_item3, null);
            mview = new ViewHolder();
            mview.head =  convertView.findViewById(R.id.contact_img);
            mview.title = convertView.findViewById(R.id.taskname);
            mview.imageView = convertView.findViewById(R.id.select);
            convertView.setTag(mview);
        }
        else
        {
            mview = (ViewHolder) convertView.getTag();
        }
        Project project = (Project) mSelectMoreModel.object;
        mview.title.setText(mSelectMoreModel.mName);
        Contacts contacts = (Contacts) Bus.callData(mContext,"chat/getContactItem",project.leaderId);
        contacts.colorhead = AppUtils.setContactCycleHead(mview.head,contacts.getName(),contacts.colorhead);
        if(mSelectMoreModel.iselect)
        {
            mview.imageView.setVisibility(View.VISIBLE);
        }
        else
        {
            mview.imageView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView title;
        private TextView head;
        private ImageView imageView;
    }

}
