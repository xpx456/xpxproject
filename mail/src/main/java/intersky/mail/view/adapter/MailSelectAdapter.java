package intersky.mail.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.mail.R;
import intersky.mail.entity.MailBox;

@SuppressLint("InflateParams")
public class MailSelectAdapter extends BaseAdapter
{

	private ArrayList<MailBox> mMailBoxModels;
	private Context mContext;
	private LayoutInflater mInflater;
	public MailSelectAdapter(Context context, ArrayList<MailBox> mMailBoxModels)
	{
		this.mContext = context;
		this.mMailBoxModels = mMailBoxModels;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mMailBoxModels.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mMailBoxModels.get(position);
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
		MailBox mMailBoxModel = mMailBoxModels.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_mail_box, null);
			holder.mMail = (TextView) convertView.findViewById(R.id.item_mial_address);
			holder.micon = (ImageView) convertView.findViewById(R.id.item_mial_select_icon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mMail.setText(mMailBoxModel.mAddress);
		if(mMailBoxModel.isSelect == true)
		{
			holder.micon.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.micon.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView mMail;
		private ImageView micon;
	} 
}
