package intersky.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhoneCode extends RelativeLayout {
    private Context context;
    public ArrayList<TextView> textViews = new ArrayList<TextView>();
    private EditText et_code;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;
    public DisplayMetrics metric;
    public int count;
    public int eachwidth = 0;

    public PhoneCode(Context context) {
        super(context);
        metric = new DisplayMetrics();
        ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        this.context = context;
        loadView();
    }

    public PhoneCode(Context context, AttributeSet attrs) {
        super(context, attrs);
        metric = new DisplayMetrics();
        ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PhoneCode);
        count = a.getInt(R.styleable.PhoneCode_count,4);
        eachwidth = metric.widthPixels/this.count;
        loadView();
    }

    private void loadView(){
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.phone_code, this);
        initView(view);
        initEvent();
    }

    private void initView(View view){

        LinearLayout layout = view.findViewById(R.id.ll_code);
        for(int i = 0 ; i < count ; i++)
        {
            View item = LayoutInflater.from(context).inflate(R.layout.phone_code_item, null);
            TextView textView = item.findViewById(R.id.tv_code);
            textViews.add(textView);
            layout.addView(item,eachwidth, LayoutParams.MATCH_PARENT);
        }
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_code.setFilters( new InputFilter[]{ new  InputFilter.LengthFilter( count )});
    }

    private void initEvent(){
        //验证码输入
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length()>0) {
                    et_code.setText("");
                    if(codes.size() < count){
                        codes.add(editable.toString());
                        showCode();
                    }
                }
            }
        });
        // 监听验证码删除按键
        et_code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size()>0) {
                    codes.remove(codes.size()-1);
                    showCode();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 显示输入的验证码
     */
    private void showCode(){
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        if(codes.size()>=1){
            code1 = codes.get(0);
        }
        if(codes.size()>=2){
            code2 = codes.get(1);
        }
        if(codes.size()>=3){
            code3 = codes.get(2);
        }
        if(codes.size()>=4){
            code4 = codes.get(3);
        }
        for(int i = 0 ; i < count ; i++)
        {
            TextView textView = textViews.get(i);
            if(codes.size() > i)
            {
                textView.setText(codes.get(i));
                textView.setBackgroundResource(R.drawable.phone_code_bgs);
            }
            else
            {
                textView.setText("");
                textView.setBackgroundResource(R.drawable.phone_code_bgs);
            }
        }
        callBack();//回调
    }


    /**
     * 回调
     */
    private void callBack(){
        if(onInputListener==null){
            return;
        }
        if(codes.size()==4){
            onInputListener.onSucess(getPhoneCode());
        }else{
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener{
        void onSucess(String code);
        void onInput();
    }
    private OnInputListener onInputListener;
    public void setOnInputListener(OnInputListener onInputListener){
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput(){
        //显示软键盘
        if(imm!=null && et_code!=null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(et_code, 0);
                }
            },200);
        }
    }

    /**
     * 获得手机号验证码
     * @return 验证码
     */
    public String getPhoneCode(){
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }
}
