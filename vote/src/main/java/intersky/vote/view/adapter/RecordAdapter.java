package intersky.vote.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.MyLinearLayout;
import intersky.vote.R;
import intersky.vote.entity.Reocrd;

@SuppressLint("InflateParams")
public class RecordAdapter extends BaseAdapter
{

	private ArrayList<Reocrd> mReocrdModels;
	private Context mContext;
	private LayoutInflater mInflater;
	public RecordAdapter(Context context, ArrayList<Reocrd> mReocrdModels)
	{
		this.mContext = context;
		this.mReocrdModels = mReocrdModels;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mReocrdModels.size();
	}

	@Override
	public Reocrd getItem(int position)
	{
		// TODO Auto-generated method stub
		return mReocrdModels.get(position);
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
		Reocrd mReocrdModel = mReocrdModels.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.record_item, null);
			holder.mMyLinearLayout = (MyLinearLayout) convertView.findViewById(R.id.sender);
			holder.mTitle = (TextView) convertView.findViewById(R.id.vote_name);
			holder. mTitleid = (TextView) convertView.findViewById(R.id.vote_id);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTitle.setText(mReocrdModel.name+"("+ String.valueOf(mReocrdModel.mContacts.size())+")");
		holder. mTitleid.setText(String.valueOf(position+1));
		initselectView(mReocrdModel.mContacts,holder.mMyLinearLayout);

		return convertView;
	}

	public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer) {
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mlayer.removeAllViews();
		if (mselectitems.size() > 0) {
			for (int i = 0; i < mselectitems.size(); i++) {
				Contacts mContact = mselectitems.get(i);
				View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
				TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
				Bus.callData(mContext,"chat/setContactCycleHead",mhead,mContact);
				TextView name = (TextView) mview.findViewById(R.id.title);
				name.setText(mContact.mName);
				mview.setTag(mContact);
				mlayer.addView(mview);
			}

		}
	}

	private static class ViewHolder {
		private MyLinearLayout mMyLinearLayout;
		private TextView mTitle;
		private TextView mTitleid;
	} 
}
