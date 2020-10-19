package intersky.mywidget;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class XpxSpinnerAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<String> list;
    TextView snipertext;
    public XpxSpinnerAdapter(Context context, ArrayList<String> list, TextView snipertext) {
        super();
        this.context = context;
        this.list = list;
        this.snipertext = snipertext;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.xpx_item_data_spinner, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.date_text);
            viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,this.snipertext.getTextSize());
            viewHolder.textView.setTextColor(this.snipertext.getTextColors());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position));
        return convertView;
    }
    public class ViewHolder {
        TextView textView;
    }
    public void refresh(List<String> l) {
        this.list.clear();
        list.addAll(l);
        notifyDataSetChanged();
    }
    public void add(String str) {
        list.add(str);
        notifyDataSetChanged();
    }
    public void add(ArrayList<String> str) {
        list.addAll(str);
        notifyDataSetChanged();
    }
}