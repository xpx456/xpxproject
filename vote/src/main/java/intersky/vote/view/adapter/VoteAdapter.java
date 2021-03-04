package intersky.vote.view.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.StringUtils;
import intersky.vote.R;
import intersky.vote.entity.Vote;

@SuppressLint("InflateParams")
public class VoteAdapter extends BaseAdapter
{
    public ArrayList<Vote> mVotes;
	private Context mContext;
	private LayoutInflater mInflater;
    public int startPage = 1;
    public boolean isall = false;
	public int totalCount = -1;
	public int endPage = 1;
	public VoteAdapter(Context context, ArrayList<Vote> mVotes)
	{
		this.mContext = context;
		this.mVotes = mVotes;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mVotes.size();
	}

	@Override
	public Vote getItem(int position)
	{
		// TODO Auto-generated method stub
		return mVotes.get(position);
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
		ViewHolder holder;
		Vote mVoteItem = getItem(position);
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.vote_item, null);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.vote_name);
			holder.mTime = (TextView) convertView.findViewById(R.id.vote_dete);
			holder.mType = (TextView) convertView.findViewById(R.id.vote_type);
			holder.mVoteText = (TextView) convertView.findViewById(R.id.vote_sig);
			holder.mVotebg = (ImageView) convertView.findViewById(R.id.image_sig_cion);
			holder.mhead = (TextView) convertView.findViewById(R.id.contact_img);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mName.setText(StringUtils.htmlToString(mVoteItem.mContent));
		holder.mTime.setText(mVoteItem.creatTime.substring(5,7)+"/"+mVoteItem.creatTime.substring(8,10)
		+" "+mVoteItem.creatTime.substring(11,16)+"-"+mVoteItem.endTime.substring(5,7)+"/"+mVoteItem.endTime.substring(8,10)
				+" "+mVoteItem.endTime.substring(11,16));
		AppUtils.setContactCycleHead(holder.mhead,mVoteItem.userName);
		String type1;
		String type2;
		if(mVoteItem.is_incognito == 0)
		{
			type1 = mContext.getString(R.string.xml_vote_hasname);

		}
		else
		{
			type1 = mContext.getString(R.string.xml_vote_noname);
		}
		if(mVoteItem.is_close == 0)
		{
			type2 = mContext.getString(R.string.xml_vote_doing);
			holder.mType.setTextColor(Color.rgb(142,198,255));

		}
		else
		{
			type2 = mContext.getString(R.string.xml_vote_closed);
			holder.mType.setTextColor(Color.rgb(199,199,199));
		}
		holder.mType.setText(type1+":"+type2);
		if(mVoteItem.visiable == false)
		{
			holder.mVoteText.setVisibility(View.INVISIBLE);
			holder.mVotebg.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.mVoteText.setVisibility(View.VISIBLE);
			holder.mVotebg.setVisibility(View.VISIBLE);
		}
		if(mVoteItem.visiable == true)
		{
			if(mVoteItem.myvoteid.length() > 0)
			{
				holder.mVoteText.setText(mContext.getString(R.string.xml_vote_noed));
				holder.mVoteText.setTextColor(Color.rgb(199,199,199));
				holder.mVotebg.setVisibility(View.INVISIBLE);
			}
			else
			{
				holder.mVoteText.setText(mContext.getString(R.string.xml_vote_no));
				holder.mVoteText.setTextColor(Color.rgb(255,255,255));
				holder.mVotebg.setVisibility(View.VISIBLE);
			}
		}


		return convertView;
	}

	private static class ViewHolder
	{
		private TextView mhead;
		private TextView mName;
		private TextView mTime;
		private TextView mType;
		private TextView mVoteText;
		private ImageView mVotebg;
	}
 
}
