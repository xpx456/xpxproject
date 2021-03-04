package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class XpxSpinnerView extends LinearLayout {
    private Context context;
    private LinearLayout layout;
    private ListView listView;
    private XpxSpinnerAdapter adapter;
    private PopupWindow popupWindow;
    public TextView mSpinnerText;
    public DoItemClick itemClick;
    public int lastid = 0;
    public float height;
    private ArrayList<String> listData = new ArrayList<>();

    public XpxSpinnerView(Context context) {
        super(context);
        this.context = context;
    }

    public XpxSpinnerView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_xpx_spinnerview, this);
        mSpinnerText = findViewById(R.id.text_spinner);
        RelativeLayout relativeLayout = findViewById(R.id.spinnerid);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinnerView);
//        height = a.getDimension(R.styleable.XpxSpinnerView_xheight,mSpinnerText.getHeight());
//        mSpinnerText.setTextSize(a.getDimension(R.styleable.XpxSpinnerView_xtextsize,mSpinnerText.getTextSize()));
//        mSpinnerText.setTextColor(a.getColor(R.styleable.XpxSpinnerView_xtextcolor,mSpinnerText.getCurrentTextColor()));
        relativeLayout.setBackgroundResource(a.getResourceId(R.styleable.XpxSpinnerView_xbg,R.drawable.shape_search_bg));
        mSpinnerText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(v);
            }
        });
    }

    public XpxSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setMyData(ArrayList<String> data){
        this.listData = data;
        adapter = new XpxSpinnerAdapter(context, listData,mSpinnerText);    // 默认设置下拉框的标题为数据的第一个
        mSpinnerText.setText((CharSequence) adapter.getItem(lastid));    // 点击右侧按钮，弹出下拉框
    }

    public void setMyData(ArrayList<String> data,int index){
        this.listData = data;
        this.lastid = index;
        adapter = new XpxSpinnerAdapter(context, listData,mSpinnerText);    // 默认设置下拉框的标题为数据的第一个
        mSpinnerText.setText((CharSequence) adapter.getItem(lastid));    // 点击右侧按钮，弹出下拉框
    }

    public void setdoItemClick(DoItemClick doItemClick){
        itemClick = doItemClick;
    }

    public String getText()
    {
        return mSpinnerText.getText().toString();
    }

    public void showWindow(View v) {
        // 找到布局文件
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xpxpinner_dropdown, null);
        // 实例化listView
        listView = (ListView) layout.findViewById(R.id.listView);
        // 设置listView的适配器
        listView.setAdapter(adapter);
        // 实例化一个PopuWindow对象
        popupWindow = new PopupWindow(v);
        // 设置弹框的宽度为布局文件的宽
        popupWindow.setWidth(getWidth());
        // 高度设置的300
        popupWindow.setHeight(35*listData.size());
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        popupWindow.setOutsideTouchable(true);
        // 设置焦点
        popupWindow.setFocusable(true);
        // 设置所在布局
        popupWindow.setContentView(layout);
        // 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度
        popupWindow.showAsDropDown(v, 0, 0);
        // listView的item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mSpinnerText.setText(listData.get(position));// 设置所选的item作为下拉框的标题
                if(itemClick != null && lastid != position)
                {
                    lastid = position;
                    itemClick.doItemClick(listData.get(position));
                }
                // 弹框消失
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
    }

    public interface DoItemClick {
        void doItemClick(String item);
    }

}
