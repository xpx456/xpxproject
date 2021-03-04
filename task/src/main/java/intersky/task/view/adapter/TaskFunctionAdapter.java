package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.task.R;
import intersky.task.entity.TaskFunction;


@SuppressLint("InflateParams")
public class TaskFunctionAdapter extends BaseAdapter {
    private ArrayList<TaskFunction> mFunctions;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mHandler;
    private boolean isMore;

    public TaskFunctionAdapter(Context context, ArrayList<TaskFunction> mFunctions) {
        this.mContext = context;
        this.mFunctions = mFunctions;
        this.isMore = isMore;
        this.mHandler = mHandler;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFunctions.size();
    }

    @Override
    public TaskFunction getItem(int position) {
        // TODO Auto-generated method stub
        return mFunctions.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TaskFunction mFunction = getItem(position);
        convertView = mInflater.inflate(R.layout.funcs_grid_item3, null);
        ImageView mimage = (ImageView) convertView.findViewById(R.id.fun_img);
        TextView title = (TextView) convertView.findViewById(R.id.fun_text);
        if(mFunction.onoff == false)
        {
            title.setText(mFunction.name);
            mimage.setImageResource(mFunction.id);
        }
        else
        {
            title.setText(mFunction.name2);
            mimage.setImageResource(mFunction.id2);
        }
        return convertView;
    }

}
