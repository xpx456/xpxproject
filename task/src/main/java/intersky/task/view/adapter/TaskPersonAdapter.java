package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.task.R;

@SuppressLint("InflateParams")
public class TaskPersonAdapter extends BaseAdapter
{

	public ArrayList<Contacts> mContacts;
	private Context mContext;
	private LayoutInflater mInflater;
	public TaskPersonAdapter(Context context, ArrayList<Contacts> mContacts)
	{
		this.mContext = context;
		this.mContacts = mContacts;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mContacts.size();
	}

	@Override
	public Contacts getItem(int position)
	{
		// TODO Auto-generated method stub
		return mContacts.get(position);
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
		Contacts mContact = mContacts.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.task_contact_item, null);
			holder.mhead = (TextView) convertView.findViewById(R.id.contact_img);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		AppUtils.setContactCycleHead(holder.mhead,mContact.getName(),mContact.colorhead);
		return convertView;
	}

	private static class ViewHolder {
		private TextView mhead;
	}

}
