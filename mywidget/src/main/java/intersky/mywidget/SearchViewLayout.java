package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by xpx on 2017/7/5.
 */

public class SearchViewLayout extends RelativeLayout {

    private View mSearch;
    private RelativeLayout mNomal;
    private RelativeLayout mEdit;
    private EditText eTxtSearch;
    public TextView btnCancle;
    private ImageView mImgClean;
    private Context mContext;
    public boolean ishow = false;
    public DoTextChange doTextChange;
    public DoCancle doCancle;
    public int type = 0;
    public boolean keepshow = false;
    public boolean cleanbtn = true;
    public SearchViewLayout(Context context) {
        super(context);

        //initView(context);
    }

    public SearchViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchViewLayout);
        type =a.getInt(R.styleable.SearchViewLayout_searchtype,0);
        keepshow =a.getBoolean(R.styleable.SearchViewLayout_keepshow,false);
        cleanbtn =a.getBoolean(R.styleable.SearchViewLayout_cleanbtn,true);
        initView(context,type);
    }

    public void sethit(String hit) {
        eTxtSearch.setHint(hit);
    }

    public void initView(Context context,int type)
    {
        this.mContext = context;
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(type == 0)
        mSearch = mInflater.inflate(R.layout.search_view, null);
        else
            mSearch = mInflater.inflate(R.layout.search_view2, null);
        mNomal = (RelativeLayout) mSearch.findViewById(R.id.nomal);
        mEdit = (RelativeLayout) mSearch.findViewById(R.id.search);
        btnCancle = mSearch.findViewById(R.id.cancle_btn);
        eTxtSearch = (EditText) mSearch.findViewById(R.id.search_edit);
        mImgClean = (ImageView) mSearch.findViewById(R.id.clean);
        btnCancle.setOnClickListener(mCancleListen);
        eTxtSearch.addTextChangedListener(searchTextChangeListener);
        mNomal.setOnClickListener(showEditText);
        mImgClean.setOnClickListener(mCleanListen);
        if(keepshow)
        {
            showEdit();
        }
        this.addView(mSearch);
    }

    public OnClickListener showEditText = new OnClickListener()
    {

        @Override
        public void onClick(View v) {
            showEdit();
        }
    };

    public OnClickListener mCleanListen = new OnClickListener()
    {

        @Override
        public void onClick(View v) {
            eTxtSearch.setText("");
        }
    };

    public OnClickListener mCancleListen = new OnClickListener()
    {

        @Override
        public void onClick(View v) {
            eTxtSearch.setText("");
            hidEdit();
            if(doCancle != null) {
                doCancle.doCancle();
            }
        }
    };

    public void setOnCancleListener(DoCancle mOnClickListener) {
        this.doCancle = mOnClickListener;
    }

    public interface DoCancle{
       void doCancle();
    }


    public void mSetOnSearchListener(TextView.OnEditorActionListener mCleanListen)
    {
        eTxtSearch.setOnEditorActionListener(mCleanListen);
    }

    public void showEdit()
    {

        if(ishow == false)
        {
            ishow=true;
            mNomal.setVisibility(INVISIBLE);
            mEdit.setVisibility(View.VISIBLE);
            eTxtSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(eTxtSearch, InputMethodManager.SHOW_FORCED);
        }

    }

    public void hidEdit()
    {
        if(ishow == true)
        {
            if(keepshow == false)
            {
                ishow = false;
                eTxtSearch.clearFocus();
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(eTxtSearch.getWindowToken(), 0);
                mNomal.setVisibility(VISIBLE);
                mEdit.setVisibility(INVISIBLE);
            }

            eTxtSearch.setText("");

        }
    }



    public TextWatcher searchTextChangeListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(eTxtSearch.getText().length() > 0)
            {
                if(mImgClean.getVisibility() == View.INVISIBLE && cleanbtn)
                {
                    mImgClean.setVisibility(View.VISIBLE);
                }
                if(doTextChange != null)
                {
                    doTextChange.doTextChange(true);
                }
            }
            else
            {    if(mImgClean.getVisibility() == View.VISIBLE && cleanbtn)
                {
                    mImgClean.setVisibility(View.INVISIBLE);
                }
                if(doTextChange != null)
                {
                    doTextChange.doTextChange(false);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setDotextChange(DoTextChange doTextChange) {
        this.doTextChange = doTextChange;
    }

    public String getText()
    {
        return eTxtSearch.getText().toString();
    }

    public void cleanText()
    {
         eTxtSearch.setText("");
    }

    public interface DoTextChange
    {
        void doTextChange(boolean visiable);
    }


}
