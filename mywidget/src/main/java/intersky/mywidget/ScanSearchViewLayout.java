package intersky.mywidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by xpx on 2017/7/5.
 */

public class ScanSearchViewLayout extends RelativeLayout {

    private View mSearch;
    private EditText eTxtSearch;
    private ImageView btnScan;
    public Context mContext;
    public ScanSearchViewLayout(Context context) {
        super(context);

        //initView(context);
    }

    public ScanSearchViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context)
    {
        this.mContext = context;
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSearch = mInflater.inflate(R.layout.scan_search_view, null);
        eTxtSearch = (EditText) mSearch.findViewById(R.id.searchView);
        btnScan = (ImageView) mSearch.findViewById(R.id.scan);

        this.addView(mSearch);
    }


    public void mSetOnSearchListener(TextView.OnEditorActionListener mCleanListen)
    {
        eTxtSearch.setOnEditorActionListener(mCleanListen);
    }

    public void setOnScanlistener(View.OnClickListener showScanListener) {
        btnScan.setOnClickListener(showScanListener);
    }

    public void setText(String search)
    {
        eTxtSearch.setText(search);
    }

    public String getText()
    {
        return eTxtSearch.getText().toString();
    }

    public void setSacn(boolean cansacn)
    {
        if(cansacn == true)
        {
            btnScan.setVisibility(VISIBLE);
        }
        else
        {
            btnScan.setVisibility(GONE);
        }
    }

    public void sethit(String hit) {
        eTxtSearch.setHint(hit);
    }

    public void showScan()
    {

    }
}
