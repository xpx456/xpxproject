package intersky.mail.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import intersky.apputils.TimeUtils;
import intersky.mail.R;
import intersky.mywidget.RoundProgressBar;
import intersky.select.entity.Select;


public class MailLable2Adapter extends RecyclerView.Adapter
{
	private ArrayList<Select> mLables;
	private Context mContext;
	private LayoutInflater mInflater;
	public MailLable2Adapter(ArrayList<Select> mLables, Context context)
	{
		this.mContext = context;
		this.mLables = mLables;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Select getItem(int position)
	{
		// TODO Auto-generated method stub
		return mLables.get(position);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View convertView = mInflater.inflate(R.layout.mail_lable_item, null);
		return new ViewHolder(convertView);
	}

	public interface OnItemClickListener{
		void onItemClick(Select lable, int position, View view);
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener mListener) {
		this.mListener = mListener;
	}


	public LableFunction LableionFunction;
	public void setLableFunction(LableFunction LableionFunction) {
		this.LableionFunction = LableionFunction;
	}

	public interface LableFunction {
		void delete(Select mLable);
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder iholder, final int position) {
		final Select lable = mLables.get(position);
		ViewHolder holder = (ViewHolder) iholder;
		RelativeLayout relativeLayout = holder.itemView.findViewById(R.id.mail_person_item);
		relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null)
					mListener.onItemClick(lable,position,iholder.itemView);
			}
		});

		holder.title.setText(lable.mName);
		holder.color.setBackgroundColor(Color.parseColor(lable.mColor));
		if(lable.iselect)
		holder.select.setVisibility(View.VISIBLE);
		else
			holder.select.setVisibility(View.INVISIBLE);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemCount() {
		return mLables.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView title;
		View color;
		ImageView select;
		public ViewHolder(@NonNull View convertView) {
			super(convertView);
			color = convertView.findViewById(R.id.color);
			title = convertView.findViewById(R.id.item_mial_address);
			select = convertView.findViewById(R.id.select);
		}
	}



}
