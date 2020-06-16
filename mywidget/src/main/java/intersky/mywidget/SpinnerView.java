package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class SpinnerView extends LinearLayout {
    private Context context;
    private LinearLayout layout;
    private ListView listView;
    private MySpinnerAdapter adapter;
    private PopupWindow popupWindow;
    public EditText mSpinnerText;
    public ImageView mSpinnerImag;
    public DoItemClick itemClick;
    private ArrayList<String> listData = new ArrayList<>();

    public SpinnerView(Context context) {
        super(context);
        this.context = context;
    }

    public SpinnerView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_my_spinnerview, this);
        mSpinnerText = (EditText) findViewById(R.id.text_spinner);
        mSpinnerImag = (ImageView) findViewById(R.id.image_spinner);
        RelativeLayout relativeLayout = findViewById(R.id.spinnerid);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinnerView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSpinnerImag.getLayoutParams();
        layoutParams.width = (int) a.getDimension(R.styleable.SpinnerView_imagesize,mSpinnerText.getTextSize());
        layoutParams.height = (int) a.getDimension(R.styleable.SpinnerView_imagesize,mSpinnerText.getTextSize());
        mSpinnerImag.setLayoutParams(layoutParams);
        mSpinnerImag.setImageResource(a.getResourceId(R.styleable.SpinnerView_icon_src,R.drawable.sniper1));
        mSpinnerText.setTextSize(TypedValue.COMPLEX_UNIT_PX,a.getDimension(R.styleable.SpinnerView_textsize,mSpinnerText.getTextSize()));
        mSpinnerText.setTextColor(a.getColor(R.styleable.SpinnerView_textcolor,mSpinnerText.getCurrentTextColor()));
        relativeLayout.setBackgroundResource(a.getResourceId(R.styleable.SpinnerView_bg,R.drawable.shape_search_bg));
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setMyData(ArrayList<String> data){
        this.listData = data;
        adapter = new MySpinnerAdapter(context, listData,mSpinnerText);    // 默认设置下拉框的标题为数据的第一个
        mSpinnerText.setText((CharSequence) adapter.getItem(0));    // 点击右侧按钮，弹出下拉框
        mSpinnerImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(v);
            }
        });
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
        layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.mypinner_dropdown, null);
        // 实例化listView
        listView = (ListView) layout.findViewById(R.id.listView);
        // 设置listView的适配器
        listView.setAdapter(adapter);
        // 实例化一个PopuWindow对象
        popupWindow = new PopupWindow(v);
        // 设置弹框的宽度为布局文件的宽
        popupWindow.setWidth(getWidth());
        // 高度设置的300
        popupWindow.setHeight(300);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        popupWindow.setOutsideTouchable(true);
        // 设置焦点
        popupWindow.setFocusable(true);
        // 设置所在布局
        popupWindow.setContentView(layout);
        // 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度
        popupWindow.showAsDropDown(v, -mSpinnerText.getWidth(), 0);
        // listView的item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mSpinnerText.setText(listData.get(arg2));// 设置所选的item作为下拉框的标题
                if(itemClick != null)
                {
                    itemClick.doItemClick(listData.get(arg2));
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

    public void setEnable(boolean enable)
    {
        if(enable)
        {
            mSpinnerText.setEnabled(true);
            mSpinnerImag.setEnabled(true);
        }
        else
        {
            mSpinnerText.setEnabled(false);
            mSpinnerImag.setEnabled(false);
        }
    }
}
