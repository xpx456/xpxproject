package intersky.mail.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.entity.Mail;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;

@SuppressLint("InflateParams")
public class MailItemAdapter extends BaseAdapter
{
	private List<Mail> mMailItems;
	private Context mContext;
	private LayoutInflater mInflater;
	public boolean issend = false;
	public MailItemAdapter(Context context, List<Mail> mMailItems,boolean issend)
	{
		this.mContext = context;
		this.mMailItems = mMailItems;
		this.issend = issend;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	public MailItemAdapter(Context context, List<Mail> mMailItems)
	{
		this.mContext = context;
		this.mMailItems = mMailItems;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mMailItems.size();
	}

	@Override
	public Mail getItem(int position)
	{
		// TODO Auto-generated method stub
		return mMailItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		Mail mMailItem = getItem(position);
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_item, null);
			holder.mUser = (TextView) convertView.findViewById(R.id.username_text);
			holder.mName = (TextView) convertView.findViewById(R.id.mialname_text);
			holder.mDate = (TextView) convertView.findViewById(R.id.date_text);
			holder.micon = (ImageView) convertView.findViewById(R.id.fujianicon);
			holder.micon2 = (ImageView) convertView.findViewById(R.id.fujianicon2);
			holder.mlayer = (RelativeLayout) convertView.findViewById(R.id.mial_item);
			holder.mContent = (TextView) convertView.findViewById(R.id.mialcontent_text);
			holder.mhead = (TextView) convertView.findViewById(R.id.head);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mName.setText(mMailItem.mSubject);
		holder.mDate.setText(TimeUtils.measureDeteForm(mMailItem.mDate));
		holder.mContent.setText(mMailItem.mSContent);
		if (mMailItem.haveAttachment)
		{
			holder.micon.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.micon.setVisibility(View.INVISIBLE);
		}
		if (mMailItem.isSelect == false)
		{
			holder.mlayer.setBackgroundColor(Color.rgb(255, 255, 255));
		}
		else
		{
			holder.mlayer.setBackgroundColor(Color.rgb(174, 204, 243));
		}


		if(MailManager.getInstance().account.iscloud == true) {

			if(issend)
			{
				holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mTo));
				Drawable read = null;
				if(mMailItem.sendstate == 0) {
					read = mContext.getResources().getDrawable(R.drawable.mail_send0);
				}
				else if(mMailItem.sendstate == 1) {
					read = mContext.getResources().getDrawable(R.drawable.mail_send1);
				}
				else if(mMailItem.sendstate == 2) {
					read = mContext.getResources().getDrawable(R.drawable.mail_send2);
				}
				else if(mMailItem.sendstate == 3) {
					read = mContext.getResources().getDrawable(R.drawable.mail_send3);
				}
				if(read != null)
				{
					read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
					holder.mUser.setCompoundDrawables(read, null, null, null);
				}
			}
			else
			{
				if (mMailItem.isReaded == false)
				{
					holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
					Drawable read = mContext.getResources().getDrawable(R.drawable.unread);
					read.setBounds( 0,0, read.getMinimumWidth() * 1 / 2, read.getMinimumHeight() * 1/ 2);
					holder.mUser.setCompoundDrawables(read, null, null, null);
				}
				else if(mMailItem.mailtype == 6)
				{
					holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
					Drawable read = mContext.getResources().getDrawable(R.drawable.fworre);
					read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
					holder.mUser.setCompoundDrawables(read, null, null, null);
				}

				else if(mMailItem.mailtype == 2 || mMailItem.mailtype == 5)
				{
					holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
					Drawable read = mContext.getResources().getDrawable(R.drawable.repeaticon);
					read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
					holder.mUser.setCompoundDrawables(read, null, null, null);
				}
				else if(mMailItem.mailtype == 3 )
				{
					holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
					Drawable read = mContext.getResources().getDrawable(R.drawable.resendicon);
					read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
					holder.mUser.setCompoundDrawables(read, null, null, null);
				}
				else
				{
					holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
					holder.mUser.setCompoundDrawables(null, null, null, null);

				}
				if(mMailItem.isRepeat == true)
				{
					if(mMailItem.haveAttachment == false)
					{
						holder.micon2.setVisibility(View.INVISIBLE);
						holder.micon.setImageResource(R.drawable.no_reply);
						holder.micon.setVisibility(View.VISIBLE);
					}
				}
				else
				{
					holder.micon2.setVisibility(View.INVISIBLE);
				}
			}
		}
		else {
			holder.micon2.setVisibility(View.INVISIBLE);
			holder.micon2.setVisibility(View.INVISIBLE);
			if (mMailItem.isReaded == false)
			{
				holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
				Drawable read = mContext.getResources().getDrawable(R.drawable.unread);
				read.setBounds( 0,0, read.getMinimumWidth() * 1 / 2, read.getMinimumHeight() * 1/ 2);
				holder.mUser.setCompoundDrawables(read, null, null, null);
			}
			else if(mMailItem.isForwarded && mMailItem.isRepeat)
			{
				holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
				Drawable read = mContext.getResources().getDrawable(R.drawable.fworre);
				read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
				holder.mUser.setCompoundDrawables(read, null, null, null);
			}

			else if(mMailItem.isForwarded)
			{
				holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
				Drawable read = mContext.getResources().getDrawable(R.drawable.repeaticon);
				read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
				holder.mUser.setCompoundDrawables(read, null, null, null);
			}
			else if(mMailItem.isRepeat)
			{
				holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
				Drawable read = mContext.getResources().getDrawable(R.drawable.resendicon);
				read.setBounds(0, 0, read.getMinimumWidth() * 2 / 3, read.getMinimumHeight() * 2 / 3);
				holder.mUser.setCompoundDrawables(read, null, null, null);
			}
			else
			{
				holder.mUser.setText(MailManager.praseMailAddress(mMailItem.mFrom));
				holder.mUser.setCompoundDrawables(null, null, null, null);

			}
		}

		String s = "";
		if(holder.mUser.getText().toString().length() > 0)
		{
			if(holder.mUser.getText().toString().length() > 2)
			{
				s = holder.mUser.getText().toString().substring(0,2);
				if(s.substring(0, 1).toCharArray()[0]>='a'&&s.substring(0, 1).toCharArray()[0]<='z'){
					s=(s.substring(0,1).toUpperCase()+s.substring(1,2));
				}
				if(AppUtils.strlen(s))
				{
					holder.mhead.setText(holder.mUser.getText().toString().substring(0,1));
				}
				else
				{
					holder.mhead.setText(holder.mUser.getText().toString().substring(0,2));
				}
			}
			else
			{
				s = holder.mUser.getText().toString();
				if(s.substring(0, 1).toCharArray()[0]>='a'&&s.substring(0, 1).toCharArray()[0]<='z'){
					s=(s.substring(0,1).toUpperCase());
				}
				if(AppUtils.strlen(s))
				{
					holder.mhead.setText(holder.mUser.getText().toString().substring(0,1));
				}
				else
				{
					holder.mhead.setText(holder.mUser.getText().toString());
				}
			}
		}

		return convertView;
	}

	private static class ViewHolder
	{
		private RelativeLayout mlayer;
		private TextView mName;
		private TextView mUser;
		private TextView mDate;
		private TextView mContent;
		private ImageView micon;
		private ImageView micon2;
		private TextView mhead;
	}


}
