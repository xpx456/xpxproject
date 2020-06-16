package intersky.schedule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.schedule.R;
import intersky.schedule.entity.Event;

public class EventAdapter extends RecyclerView.Adapter
{
	private ArrayList<Event> mEvents;
	private Context mContext;
	private LayoutInflater mInflater;
	public EventAdapter(Context context, ArrayList<Event> mEvents)
	{
		this.mContext = context;
		this.mEvents = mEvents;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Event getItem(int position)
	{
		// TODO Auto-generated method stub
		return mEvents.get(position);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View convertView = mInflater.inflate(R.layout.event_item, null);
		return new ViewHolder(convertView);
	}

	public interface OnItemClickListener{
		void onItemClick(Event contacts, int position, View view);
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener mListener) {
		this.mListener = mListener;
	}


	public EventFunction eventionFunction;
	public void setEventFunction(EventFunction eventionFunction) {
		this.eventionFunction = eventionFunction;
	}

	public interface EventFunction {
		void delete(Event mEvent);
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder iholder, final int position) {
		final Event mEvent = mEvents.get(position);
		ViewHolder holder = (ViewHolder) iholder;
		RelativeLayout relativeLayout = holder.itemView.findViewById(R.id.conversationlayer);
		relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null)
					mListener.onItemClick(mEvent,position,iholder.itemView);
			}
		});
		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(eventionFunction != null)
					eventionFunction.delete(mEvent);
			}
		});
		holder.time.setText(mEvent.mTime.substring(11,16));
		holder.title.setText(mEvent.mContent);
		if(position == mEvents.size()-1)
			holder.mRelativeLayout.setVisibility(View.INVISIBLE);
		else
			holder.mRelativeLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemCount() {
		return mEvents.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView time;
		TextView title;
		RelativeLayout mRelativeLayout;
		TextView delete;
		public ViewHolder(@NonNull View convertView) {
			super(convertView);
			time = (TextView) convertView.findViewById(R.id.time_text);
			title = (TextView) convertView.findViewById(R.id.date_text);
			delete = convertView.findViewById(R.id.delete);
			mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.line);
		}
	}



}
