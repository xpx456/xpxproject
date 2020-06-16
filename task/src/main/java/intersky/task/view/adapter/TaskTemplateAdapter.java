package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.entity.Template;
import intersky.xpxnet.net.NetUtils;


@SuppressLint("InflateParams")
public class TaskTemplateAdapter extends BaseAdapter {

    private ArrayList<Template> mTemplates;
    private Context mContext;
    private LayoutInflater mInflater;

    public TaskTemplateAdapter(Context context, ArrayList<Template> mTemplates) {
        this.mContext = context;
        this.mTemplates = mTemplates;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTemplates.size();
    }

    @Override
    public Template getItem(int position) {
        // TODO Auto-generated method stub
        return mTemplates.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Template mTemplate = mTemplates.get(position);
        if(mTemplate.mId.equals("0"))
        {
            convertView = mInflater.inflate(R.layout.task_empty_template_item, null);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.task_template_item, null);

            ImageView mImageView = (ImageView) convertView.findViewById(R.id.img);
            if(mTemplate.mImage.length() > 0)
            {

                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.shape_bg_template_item);
                Glide.with(mContext).load("http://"+ TaskManager.getInstance().oaUtils.mAccount.cloudServer+"/"+mTemplate.mImage).apply(options).into(new MySimpleTarget(mImageView));
            }
            else
                mImageView.setImageResource(R.drawable.shape_bg_template_item);
        }
        TextView title = (TextView) convertView.findViewById(R.id.name);
        title.setText(mTemplate.name);



        return convertView;
    }


}
