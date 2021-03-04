package intersky.mail.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.logging.LogRecord;

import intersky.appbase.ScreenDefine;
import intersky.apputils.AppUtils;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.entity.LableIViewProvider;
import intersky.mywidget.scollpick.adapter.ScrollPickerAdapter;
import intersky.mywidget.scollpick.view.MyScrollPickerView;
import intersky.mywidget.scollpick.view.ScrollPickerView;
import intersky.select.entity.Select;

public class NewLableView {

    public PopupWindow popupWindow;
    public View view;
    public ScrollPickerView mScrollPickerView;
    public LableIViewProvider iViewProvider;
    public ScrollPickerAdapter scrollPickerAdapter;
    public RelativeLayout shade;
    public Activity context;
    public EditText editText;
    public OkListener oklistener;
    public TextView ok;
    public Select edit;
    public NewLableView(Activity context,RelativeLayout shade) {
        this.shade = shade;
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.lable_creat_menu, null);
        mScrollPickerView = view.findViewById(R.id.scroll_picker_view);
        editText = view.findViewById(R.id.edit);
        mScrollPickerView.setLayoutManager(new LinearLayoutManager(context));
//        mScrollPickerView.setLayoutManager(new SmoothScrollLayoutManager(context));
        iViewProvider = new LableIViewProvider();
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<Select> builder = new ScrollPickerAdapter.ScrollPickerAdapterBuilder<Select>(context)
                        .setDataList(MailManager.getInstance().mMySelectLabols)
                        .selectedItemOffset(1)
                        .visibleItemNumber(3)
                        .setOnScrolledListener(scrollListener)
                        .setDivideLineColor("#00E5E5E5")
                        .setItemViewProvider(iViewProvider)
                        .setOnClickListener(new ScrollPickerAdapter.OnClickListener() {
                            @Override
                            public void onSelectedItemClicked(View v) {
                                Select select = (Select) v.getTag();
//                                select.iselect = true;
                            }
                        });
        scrollPickerAdapter = builder.build();
        mScrollPickerView.setAdapter(scrollPickerAdapter);
        TextView calcle = view.findViewById(R.id.cancle);
        ok = view.findViewById(R.id.ok);
        calcle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                hide();
            }
        });
        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(oklistener != null)
                {

                    if(edit == null)
                    {
                        edit = new Select();
                    }
                    edit.mName = editText.getText().toString();
                    if(edit.mName.length() == 0)
                    {
                        edit.mName = editText.getHint().toString();
                    }
                    edit.mColor = iViewProvider.nowSelect.mColor;
                    oklistener.OkListener(edit);
                }
            }
        });

    }


    public void show(View location) {
        edit = null;
        popupWindow = AppUtils.creatButtomView(context,shade,location,view);
    }

    public void show(View location,OkListener oklistener) {
        this.oklistener = oklistener;
        edit = null;
        popupWindow = AppUtils.creatButtomView(context,shade,location,view);
    }

    public void show(View location,OkListener oklistener,Select select) {
        this.oklistener = oklistener;
        edit = null;
        edit = select;
        editText.setText(edit.mName);
        handler.sendEmptyMessageDelayed(1,200);
        popupWindow = AppUtils.creatButtomView(context,shade,location,view);
    }

    public void hide()
    {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
            editText.setText("");
            mScrollPickerView.setAdapter(scrollPickerAdapter);
        }
    }

    public interface OkListener{
        void OkListener(Select select);
    }

    public ScrollPickerAdapter.OnScrollListener scrollListener = new ScrollPickerAdapter.OnScrollListener() {

        @Override
        public void onScrolled(View currentItemView) {

        }
    };

    public class SmoothScrollLayoutManager extends LinearLayoutManager {
        public SmoothScrollLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                // 返回：滑过1px时经历的时间(ms)。
//                @Override
//                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                    return 0;
//                }
                @Override
                protected int getHorizontalSnapPreference() {
                    return SNAP_TO_START;//具体见源码注释
                }

                @Override
                protected int getVerticalSnapPreference() {
                    return SNAP_TO_START;//具体见源码注释
                }
            };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1)
            {
                Select color = MailManager.getInstance().closrhash.get(edit.mColor);
                int pos = scrollPickerAdapter.mDataList.indexOf(color);
                mScrollPickerView.scrollToPosition(pos+1);
            }
        }

    };
}
