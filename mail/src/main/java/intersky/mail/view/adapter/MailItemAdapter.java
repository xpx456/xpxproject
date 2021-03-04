package intersky.mail.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import intersky.json.XpxJSONArray;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.entity.Mail;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.select.entity.Select;

@SuppressLint("InflateParams")
public class MailItemAdapter extends BaseAdapter
{
	private List<Mail> mMailItems;
	private Context mContext;
	private LayoutInflater mInflater;
	public boolean issend = false;
	public boolean edit = false;
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
			holder.select = (ImageView) convertView.findViewById(R.id.selecticon);
			holder.mUser = (TextView) convertView.findViewById(R.id.username_text);
			holder.mName = (TextView) convertView.findViewById(R.id.mialname_text);
			holder.mDate = (TextView) convertView.findViewById(R.id.date_text);
			holder.micon = (ImageView) convertView.findViewById(R.id.fujianicon);
			holder.micon2 = (ImageView) convertView.findViewById(R.id.fujianicon2);
			holder.mlayer = (RelativeLayout) convertView.findViewById(R.id.mial_item);
			holder.mContent = (TextView) convertView.findViewById(R.id.mialcontent_text);
			holder.mhead = (TextView) convertView.findViewById(R.id.head);
			holder.myLinearLayout = convertView.findViewById(R.id.lable);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if(edit)
		{
			holder.select.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.select.setVisibility(View.GONE);
		}

		holder.mName.setText(mMailItem.mSubject);
		holder.mDate.setText(TimeUtils.measureDeteForm(mContext,mMailItem.mDate));
		holder.mContent.setText(mMailItem.mSContent);
		if(mMailItem.mSContent.length() == 0)
		{
			holder.mContent.setText(mContext.getString(R.string.mail_no_content));
		}
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
			//holder.mlayer.setBackgroundColor(Color.rgb(255, 255, 255));
			if(holder.select.getVisibility() == View.VISIBLE)
			holder.select.setImageResource(R.drawable.bunselect);
		}
		else
		{
			//holder.mlayer.setBackgroundColor(Color.rgb(174, 204, 243));
			if(holder.select.getVisibility() == View.VISIBLE)
			holder.select.setImageResource(R.drawable.mailpersonselect);
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
		holder.myLinearLayout.removeAllViews();
		String lable = mMailItem.lables;
		String[] lables = lable.split(",");
		for(int i = 0 ; i < lables.length ; i++)
		{
			if(lables[i].length() > 0)
			{
				Select select = MailManager.getInstance().getLable(lables[i]);
				if(select != null)
					measureConversationLable(holder.myLinearLayout,select,mInflater);
			}
		}
		AppUtils.setContactCycleHead(holder.mhead,holder.mUser.getText().toString());
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
		private ImageView select;
		private TextView mhead;
		private LinearLayout myLinearLayout;
	}

	public static void measureConversationLable(LinearLayout mMyLinearLayout, Select lable, LayoutInflater mInflater) {
		View view = mInflater.inflate(R.layout.conversation_labe_item,null);
		TextView textView = view.findViewById(R.id.conversation_lable);
		GradientDrawable myShape = (GradientDrawable)textView.getBackground();
		myShape.setColor(Color.parseColor(lable.mColor));//绿
		textView.setText(lable.mName);
		mMyLinearLayout.addView(view);
	}
}
