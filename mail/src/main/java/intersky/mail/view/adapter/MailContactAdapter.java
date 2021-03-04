package intersky.mail.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.entity.MailContact;
import intersky.apputils.AppUtils;

@SuppressLint("InflateParams")
public class MailContactAdapter extends BaseAdapter
{
	private ArrayList<MailContact> mMailContacts;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isLocal;
	public MailContactAdapter(Context context, ArrayList<MailContact> mMailContacts, boolean isLocal)
	{
		this.mContext = context;
		this.mMailContacts = mMailContacts;
		this.isLocal = isLocal;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mMailContacts.size();
	}

	@Override
	public MailContact getItem(int position)
	{
		// TODO Auto-generated method stub
		return mMailContacts.get(position);
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
		MailContact mMailContact = mMailContacts.get(position);
		if(MailManager.getInstance().account.iscloud == false) {
			if(isLocal == true)
			{
				if(mMailContact.type == 0)
				{
					convertView = mInflater.inflate(R.layout.mail_person_item_n, null);
					RelativeLayout mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mail_person_item);
					TextView mName = (TextView) convertView.findViewById(R.id.item_mial_name);
					TextView icon = (TextView) convertView.findViewById(R.id.item_mial_icon);
					ImageView select = (ImageView) convertView.findViewById(R.id.item_icon);
					if(mMailContact.mName.length() > 0)
					{
						mName.setText(mMailContact.mName+"("+mMailContact.mName+")");
					}
					else
					{
						mName.setText(mMailContact.mName);
					}
					if(mMailContact.isselect == true)
					{
						select.setImageResource(R.drawable.mailpersonselect);
					}
					else
					{
						select.setImageResource(R.drawable.bunselect);
					}
					MailManager.getInstance().setContactCycleHead(icon,mMailContact);
					if(position != mMailContacts.size()-1)
					{
						RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
						if(mMailContacts.get(position+1).type == 1)
						{
							line.setVisibility(View.INVISIBLE);
						}
					}
				}
				else
				{
					convertView = mInflater.inflate(R.layout.mail_person_letter_item, null);
					TextView mName = (TextView) convertView.findViewById(R.id.item_letter_name);
					mName.setText(mMailContact.mName);
				}
			}
			else
			{
				if(mMailContact.type == 0)
				{
					convertView = mInflater.inflate(R.layout.mail_person_item, null);
					RelativeLayout mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mail_person_item);
					TextView mName = (TextView) convertView.findViewById(R.id.item_mial_name);
					TextView mMail = (TextView) convertView.findViewById(R.id.item_mial_mial);
					TextView icon = (TextView) convertView.findViewById(R.id.item_mial_icon);
					ImageView select = (ImageView) convertView.findViewById(R.id.item_icon);
					mName.setText(mMailContact.mName);
					mMail.setText(mMailContact.getMailAddress());
					if(mMailContact.isselect == true)
					{
						select.setImageResource(R.drawable.mailpersonselect);
					}
					else
					{
						select.setImageResource(R.drawable.bunselect);
					}
					mMailContact.colorhead = AppUtils.setContactCycleHead(icon,mMailContact.mName,mMailContact.colorhead);
					if(position != mMailContacts.size()-1)
					{
						RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
						if(mMailContacts.get(position+1).type == 1)
						{
							line.setVisibility(View.INVISIBLE);
						}
					}
				}
				else
				{
					convertView = mInflater.inflate(R.layout.mail_person_letter_item, null);
					TextView mName = (TextView) convertView.findViewById(R.id.item_letter_name);
					mName.setText(mMailContact.mName);
				}
			}
		}
		else
		{
			if(mMailContact.mType == Contacts.TYPE_PERSON)
			{
				convertView = mInflater.inflate(R.layout.mail_person_item, null);
				RelativeLayout mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mail_person_item);
				TextView mName = (TextView) convertView.findViewById(R.id.item_mial_name);
				TextView mMail = (TextView) convertView.findViewById(R.id.item_mial_mial);
				TextView icon = (TextView) convertView.findViewById(R.id.item_mial_icon);
				ImageView select = (ImageView) convertView.findViewById(R.id.item_icon);
				mName.setText(mMailContact.mName);
				if(isLocal)
				{
					mMail.setText(mMailContact.mailAddress2);
				}
				else
				{
					mMail.setText(mMailContact.getMailAddress());
				}
				if(mMailContact.isselect == true)
				{
					select.setImageResource(R.drawable.mailpersonselect);
				}
				else
				{
					select.setImageResource(R.drawable.bunselect);
				}
				MailManager.getInstance().setContactCycleHead(icon,mMailContact);
				if(position != mMailContacts.size()-1)
				{
					RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
					if(mMailContacts.get(position+1).type == 1)
					{
						line.setVisibility(View.INVISIBLE);
					}
				}
			}
			else if(mMailContact.mType == Contacts.TYPE_CLASS)
			{
				convertView = mInflater.inflate(R.layout.contact_item_class, null);
				TextView mname;
				ImageView mhead;
				mhead = (ImageView) convertView.findViewById(R.id.contact_img);
				mname = (TextView) convertView.findViewById(R.id.conversation_title);
				mhead.setImageResource(R.drawable.mailfile);
				if(mMailContact.mailType != null)
				{
					mname.setText(mMailContact.mailType.typename);
				}
				else
				{
					mname.setText(mMailContact.getName());
				}
				if(position != mMailContacts.size()-1)
				{
					RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
					if(mMailContacts.get(position+1).mType != Contacts.TYPE_CLASS)
					{
						line.setVisibility(View.INVISIBLE);
					}
				}
				else
				{
					RelativeLayout line = (RelativeLayout) convertView.findViewById(R.id.buttomline);
					line.setVisibility(View.INVISIBLE);

				}
			}
			else
			{
				convertView = mInflater.inflate(R.layout.mail_person_letter_item, null);
				TextView mName = (TextView) convertView.findViewById(R.id.item_letter_name);
				mName.setText(mMailContact.mName);
			}
		}

		
		return convertView;
	}

}
