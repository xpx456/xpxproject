package intersky.talk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import intersky.apputils.AppUtils;


/**
 * *****************************************
 *
 * @author 廖乃波
 * @文件名称    : FaceRelativeLayout.java
 * @创建时间    : 2013-1-27 下午02:34:17
 * @文件描述    : 带表情的自定义输入框
 * *****************************************
 */
public class ImFaceRelativeLayout extends RelativeLayout implements
        OnItemClickListener, OnClickListener {

    public static final int EVENT_INPUT_SHOW_ADD = 2201;
    public static final int EVENT_INPUT_SHOW_FACE = 2202;
    public DisplayMetrics metric;
    private Context context;
    /**
     * 表情页的监听事件
     */
    private OnCorpusSelectedListener mListener;

    /**
     * 显示表情页的viewpager
     */
    private ViewPager vp_face;

    /**
     * 表情页界面集合
     */
    private ArrayList<View> pageViews;

    /**
     * 游标显示布局
     */
    private LinearLayout layout_point;

    /**
     * 游标点集合
     */
    private ArrayList<ImageView> pointViews;

    /**
     * 表情集合
     */
    private List<List<ChatEmoji>> emojis;

    /**
     * 表情区域
     */
    private View view;

    /**
     * 输入框
     */
    public EditText et_sendmessage;

    /**
     * 表情数据填充器
     */
    private List<FaceAdapter> faceAdapters;

    /**
     * 当前表情页
     */
    private int current = 0;


    private MyLinearLayout addLayer;

    private TextView mVolueView;

    public boolean ispress = false;

    public TextView btnSend;

    private RelativeLayout messageeArer;

    public ImageView btnAdd;
    public ImageView btnface;
    public ImageView btnvoice;
    public View pick;
    public View takephoto;
    public View location;
    public View card;
    private Handler mHandler;
    public boolean faseshow = false;
    private View input;
    public AudioLayout audioLayout;
    public boolean canadudio = false;
    public ImFaceRelativeLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ImFaceRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        input = mInflater.inflate(R.layout.im_facerelativelayout, null);
        this.addView(input);
    }


    public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
        mListener = listener;
    }

    public void setAudioLayout(AudioLayout audioLayout) {
        this.audioLayout = audioLayout;
    }

    /**
     * 表情选择监听
     *
     * @author naibo-liao
     * @时间： 2013-1-15下午04:32:54
     */
    public interface OnCorpusSelectedListener {

        void onCorpusSelected(ChatEmoji emoji);

        void onCorpusDeleted();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        emojis = FaceConversionUtil.getInstace().emojiLists;
        onCreate();
    }

    private void onCreate() {
        Init_View();
        Init_viewPager();
        Init_Point();
        Init_Data();
    }

    public void showVoice()
    {
        if(canadudio)
        {
            mVolueView.setVisibility(View.VISIBLE);
            messageeArer.setVisibility(View.INVISIBLE);
            btnvoice.setImageResource(R.drawable.imkeyboard);
        }
        else
        {
            AppUtils.showMessage(context,context.getString(R.string.record_permisstion));
        }
    }

    public void showAdd()
    {
        addLayer.setVisibility(View.VISIBLE);
    }

    public void hidAdd()
    {
        addLayer.setVisibility(View.GONE);
    }

    public void showKeyboard()
    {
        checkText();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            et_sendmessage.setShowSoftInputOnFocus(true);
        } else {
            et_sendmessage.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        mVolueView.setVisibility(View.INVISIBLE);
        messageeArer.setVisibility(View.VISIBLE);
        btnvoice.setImageResource(R.drawable.im_btn_voice);
    }

    public void hidInput()
    {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
    }

    public void showInput()
    {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_sendmessage, InputMethodManager.SHOW_FORCED);
        et_sendmessage.setInputType(InputType.TYPE_CLASS_TEXT);
        et_sendmessage.requestFocus();
        hidAdd();
    }

    public void showFace()
    {
        view.setVisibility(View.VISIBLE);
        btnface.setImageResource(R.drawable.imkeyboard);
        faseshow = true;
    }

    public void hidFace()
    {
        view.setVisibility(View.GONE);
        btnface.setImageResource(R.drawable.imfacebtn);
        faseshow = false;
    }

    public void checkText()
    {
        if(et_sendmessage.getText().length() > 0)
        {
            btnAdd.setVisibility(View.INVISIBLE);
            btnSend.setVisibility(View.VISIBLE);
            viewhidadd();
        }
        else
        {
            btnAdd.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.INVISIBLE);
            viewshowadd();
        }
    }


    public void viewshowadd()
    {
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) btnface.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF,R.id.btn_add);
        btnface.setLayoutParams(layoutParams);
    }

    public void viewhidadd()
    {
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) btnface.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF,R.id.btn_send);
        btnface.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_face) {// 隐藏表情选择框
            if (view.getVisibility() == View.VISIBLE) {
                hidFace();
                showInput();
                checkText();
            } else {
//                mHandler.sendEmptyMessageDelayed(EVENT_INPUT_SHOW_FACE, 100);
                showKeyboard();
                hidInput();
                checkText();
                hidAdd();
                showFace();
            }

        } else if (i == R.id.et_sendmessage_arer) {// 隐藏表情选择框
            if (view.getVisibility() == View.VISIBLE) {
                hidFace();
                showInput();
            } else if (addLayer.getVisibility() == View.VISIBLE) {
                hidAdd();
                showInput();
            }

        } else if (i == R.id.btn_add) {
            if (addLayer.getVisibility() == View.VISIBLE) {
                hidAdd();
                showInput();
            } else {
//                mHandler.sendEmptyMessageDelayed(EVENT_INPUT_SHOW_ADD, 100);
                showKeyboard();
                hidInput();
                hidFace();
                showAdd();
            }

        } else if (i == R.id.btn_volue) {
            if (mVolueView.getVisibility() == View.VISIBLE) {
                showKeyboard();
                showInput();
                checkText();
            } else {
                showVoice();
                hidInput();
                hidAdd();
                hidFace();
                btnAdd.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.INVISIBLE);
            }

        }
    }

    /**
     * 隐藏表情选择框
     */
    public boolean hideFaceView() {
        // 隐藏表情选择框
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    /**
     * 初始化控件
     */
    private void Init_View() {
        vp_face = (ViewPager) input.findViewById(R.id.vp_contains);
        et_sendmessage = (EditText) input.findViewById(R.id.et_sendmessage);
        et_sendmessage.addTextChangedListener(mTextWatcher);
        et_sendmessage.setOnTouchListener(mtoche);
        layout_point = (LinearLayout) input.findViewById(R.id.iv_image);
        addLayer = (MyLinearLayout) input.findViewById(R.id.ll_add);
        mVolueView = (TextView) input.findViewById(R.id.et_voice);
        mVolueView.setOnTouchListener(mOnTouchListener);
        btnSend = (TextView) input.findViewById(R.id.btn_send);
        btnAdd = (ImageView) input.findViewById(R.id.btn_add);
        btnface = (ImageView) input.findViewById(R.id.btn_face);
        btnvoice = (ImageView) input.findViewById(R.id.btn_volue);
        messageeArer = (RelativeLayout) input.findViewById(R.id.et_sendmessage_arer);
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pick = mInflater.inflate(R.layout.item_add_pic_photo,null);
        takephoto = mInflater.inflate(R.layout.item_add_take_photo,null);
        location = mInflater.inflate(R.layout.item_add_location,null);
        card = mInflater.inflate(R.layout.item_add_card,null);
        addLayer.addView(pick);
        addLayer.addView(takephoto);
        addLayer.addView(location);
        addLayer.addView(card);
        messageeArer.setOnClickListener(this);
        input.findViewById(R.id.btn_face).setOnClickListener(this);
        input.findViewById(R.id.btn_add).setOnClickListener(this);
        input.findViewById(R.id.btn_volue).setOnClickListener(this);
        view = findViewById(R.id.ll_facechoose);

    }

    public void hidLocation() {
        location.setVisibility(INVISIBLE);
    }

    public void hidCard() {
        card.setVisibility(INVISIBLE);
    }

    public View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        public float pos;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                audioLayout.doRecord();
                setPress();
                audioLayout.showRecordView();
                pos = event.getY();

            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE)
            {
                float pos2 = event.getY();
                if(pos - pos2 > 100* metric.density)
                {
                    if(audioLayout != null)
                        audioLayout.showCancleVoice();
                }
                else
                {
                    if(audioLayout != null)
                    audioLayout.hidCancleVoice();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP) {
                float pos2 = event.getY();
                if(pos - pos2 > 100* metric.density)
                {
                    setRelease();
                    audioLayout.mAudioRecoderUtils.cancelRecord();
                    audioLayout.hidRecordView();
                }
                else
                {
                    audioLayout.hidRecordView();
                    audioLayout.mAudioRecoderUtils.stopRecord(true);
                    setRelease();
                }

            }

            return true;
        }
    };

    public OnTouchListener mtoche = new OnTouchListener()
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                hidAdd();
            }
            return false;
        }
    };

    public void setPress()
    {
        ispress = true;
        mVolueView.setBackgroundResource(R.drawable.shape_im_bg_input_voice_s);
        mVolueView.setText(context.getString(R.string.input_voice_end));
    }

    public void setRelease()
    {
        ispress = false;
        mVolueView.setBackgroundResource(R.drawable.shape_im_bg_input_voice);
        mVolueView.setText(context.getString(R.string.input_voice_start));
    }

    private TextWatcher mTextWatcher = new TextWatcher()
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkText();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 初始化显示表情的viewpager
     */
    private void Init_viewPager() {
        pageViews = new ArrayList<View>();
        // 左侧添加空页
        View nullView1 = new View(context);
        // 设置透明背景
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        pageViews.add(nullView1);

        // 中间添加表情页

        faceAdapters = new ArrayList<FaceAdapter>();
        for (int i = 0; i < emojis.size(); i++) {
            GridView view = new GridView(context);
            FaceAdapter adapter = new FaceAdapter(context, emojis.get(i));
            view.setAdapter(adapter);
            faceAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing((int) (8 * metric.density));
            view.setVerticalSpacing((int) (25 * metric.density));
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }

        // 右侧添加空页面
        View nullView2 = new View(context);
        // 设置透明背景
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        pageViews.add(nullView2);
    }

    /**
     * 初始化游标
     */
    private void Init_Point() {

        pointViews = new ArrayList<ImageView>();
        ImageView imageView;
        for (int i = 0; i < pageViews.size(); i++) {
            imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.shape_im_bg_round2);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = (int) (8 * metric.density);
            layoutParams.rightMargin = (int) (8 * metric.density);
            layoutParams.width = (int) (8 * metric.density);
            layoutParams.height = (int) (8 * metric.density);
            layout_point.addView(imageView, layoutParams);
            if (i == 0 || i == pageViews.size() - 1) {
                imageView.setVisibility(View.GONE);
            }
            if (i == 1) {
                imageView.setBackgroundResource(R.drawable.shape_im_bg_round);
            }
            pointViews.add(imageView);

        }
    }

    /**
     * 填充数据
     */
    private void Init_Data() {
        vp_face.setAdapter(new ViewPagerAdapter(pageViews));

        vp_face.setCurrentItem(1);
        current = 0;
        vp_face.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                current = arg0 - 1;
                // 描绘分页点
                draw_Point(arg0);
                // 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
                if (arg0 == pointViews.size() - 1 || arg0 == 0) {
                    if (arg0 == 0) {
                        vp_face.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
                        pointViews.get(1).setBackgroundResource(R.drawable.shape_im_bg_round);
                    } else {
                        vp_face.setCurrentItem(arg0 - 1);// 倒数第二屏
                        pointViews.get(arg0 - 1).setBackgroundResource(
                                R.drawable.shape_im_bg_round);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    /**
     * 绘制游标背景
     */
    public void draw_Point(int index) {
        for (int i = 1; i < pointViews.size(); i++) {
            if (index == i) {
                pointViews.get(i).setBackgroundResource(R.drawable.shape_im_bg_round);
            } else {
                pointViews.get(i).setBackgroundResource(R.drawable.shape_im_bg_round2);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ChatEmoji emoji = (ChatEmoji) faceAdapters.get(current).getItem(arg2);
        if (emoji.getId() == R.drawable.im_face_del_icon) {
            int selection = et_sendmessage.getSelectionStart();
            String text = et_sendmessage.getText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    et_sendmessage.getText().delete(start, end);
                    return;
                }
                et_sendmessage.getText().delete(selection - 1, selection);
            }
        }
        if (!TextUtils.isEmpty(emoji.getCharacter())) {
            if (mListener != null)
                mListener.onCorpusSelected(emoji);
            SpannableString spannableString = FaceConversionUtil.getInstace()
                    .addFace(getContext(), emoji.getId(), emoji.getCharacter());
            et_sendmessage.append(spannableString);
        }

    }

    public void setmHandler(Handler mHandler)
    {
        this.mHandler = mHandler;
    }

    public void setPicListener(OnClickListener listener)
    {
        pick.setOnClickListener(listener);
    }

    public void setTakephotoListener(OnClickListener listener)
    {
        takephoto.setOnClickListener(listener);
    }


    public void setLocationListener(OnClickListener listener)
    {
        location.setOnClickListener(listener);
    }

    public void setCardListener(OnClickListener listener)
    {
        card.setOnClickListener(listener);
    }
}
