package intersky.riche;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.ColorAdapter;
import intersky.mywidget.ColorModel;
import jp.wasabeef.richeditor.RichEditor;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class RichEditPresenter implements Presenter {

    public RichEditHandler mRichEditHandler;
    public RichEditActivity mRichEditActivity;

    public RichEditPresenter(RichEditActivity mRichEditActivity) {
        this.mRichEditActivity = mRichEditActivity;
        this.mRichEditHandler = new RichEditHandler(mRichEditActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mRichEditActivity.setContentView(R.layout.activity_richedit);
        ImageView back = mRichEditActivity.findViewById(R.id.back);
        back.setOnClickListener(mRichEditActivity.mBackListener);
        mRichEditActivity.mEditor = (RichEditor) mRichEditActivity.findViewById(R.id.editor);
        mRichEditActivity.mEditor.setEditorHeight(200);
        mRichEditActivity.mEditor.setEditorFontSize(15);
        if(mRichEditActivity.getIntent().getIntExtra("size",0) != 0)
        {
            mRichEditActivity.mEditor.setEditorFontSize(mRichEditActivity.getIntent().getIntExtra("size",15));
        }
        mRichEditActivity.mEditor.setEditorFontColor(Color.BLACK);
        if(mRichEditActivity.getIntent().getIntExtra("color",0) != 0)
        {
            mRichEditActivity.mEditor.setEditorFontColor(mRichEditActivity.getIntent().getIntExtra("color",0));
        }
        mRichEditActivity.mEditor.setHtml(mRichEditActivity.getIntent().getStringExtra("value"));
        TextView textView = mRichEditActivity.findViewById(R.id.finish);
        textView.setOnClickListener(mRichEditActivity.mFinishListener);

        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        ArrayList<ColorModel> mcolors = new ArrayList<ColorModel>();
        mcolors.add(new ColorModel(R.drawable.color_white,R.drawable.txt_color_white, Color.rgb(255,255,255)));
        mcolors.add(new ColorModel(R.drawable.color_black,R.drawable.txt_color_black, Color.rgb(0,0,0)));
        mcolors.add(new ColorModel(R.drawable.color_red,R.drawable.txt_color_red, Color.rgb(216,30,6)));
        mcolors.add(new ColorModel(R.drawable.color_gray,R.drawable.txt_color_gray, Color.rgb(112,112,112)));
        mcolors.add(new ColorModel(R.drawable.color_blue1,R.drawable.txt_color_blue1, Color.rgb(19,34,122)));
        mcolors.add(new ColorModel(R.drawable.color_blue2,R.drawable.txt_color_blue2, Color.rgb(18,150,219)));
        mcolors.add(new ColorModel(R.drawable.color_green,R.drawable.txt_color_green, Color.rgb(26,250,41)));
        mcolors.add(new ColorModel(R.drawable.color_yellow,R.drawable.txt_color_yellow, Color.rgb(244,234,42)));
        mcolors.add(new ColorModel(R.drawable.color_puple,R.drawable.txt_color_puple, Color.rgb(212,35,122)));
        mcolors.add(new ColorModel(R.drawable.color_puple1,R.drawable.txt_color_puple1, Color.rgb(229,107,164)));
        mcolors.add(new ColorModel(R.drawable.color_green1,R.drawable.txt_color_green1, Color.rgb(14,146,92)));
        mcolors.add(new ColorModel(R.drawable.color_green2,R.drawable.txt_color_green2, Color.rgb(3,231,185)));
        mcolors.add(new ColorModel(R.drawable.color_green3,R.drawable.txt_color_green3, Color.rgb(164,231,3)));
        mcolors.add(new ColorModel(R.drawable.color_pink,R.drawable.txt_color_pink, Color.rgb(67,3,231)));
        mcolors.add(new ColorModel(R.drawable.color_pink1,R.drawable.txt_color_pink1, Color.rgb(180,0,255)));
        mcolors.add(new ColorModel(R.drawable.color_yellow1,R.drawable.txt_color_yellow1, Color.rgb(231,94,3)));
        mcolors.add(new ColorModel(R.drawable.color_yellow2,R.drawable.txt_color_yellow2, Color.rgb(255,72,0)));
        mcolors.add(new ColorModel(R.drawable.color_yellow2,R.drawable.txt_color_red1, Color.rgb(255,0,0)));

        mRichEditActivity.mColorAdapter = new ColorAdapter(mRichEditActivity,mcolors);


        ArrayList<ColorModel> mcolors2 = new ArrayList<ColorModel>();
        mcolors2.add(new ColorModel(R.drawable.color_white,R.drawable.bg_color_white, Color.rgb(255,255,255)));
        mcolors2.add(new ColorModel(R.drawable.color_black,R.drawable.bg_color_black, Color.rgb(0,0,0)));
        mcolors2.add(new ColorModel(R.drawable.color_red,R.drawable.bg_color_red, Color.rgb(216,30,6)));
        mcolors2.add(new ColorModel(R.drawable.color_gray,R.drawable.bg_color_gray, Color.rgb(112,112,112)));
        mcolors2.add(new ColorModel(R.drawable.color_blue1,R.drawable.bg_color_blue1, Color.rgb(19,34,122)));
        mcolors2.add(new ColorModel(R.drawable.color_blue2,R.drawable.bg_color_blue2, Color.rgb(18,150,219)));
        mcolors2.add(new ColorModel(R.drawable.color_green,R.drawable.bg_color_green, Color.rgb(26,250,41)));
        mcolors2.add(new ColorModel(R.drawable.color_yellow,R.drawable.bg_color_yellow, Color.rgb(244,234,42)));
        mcolors2.add(new ColorModel(R.drawable.color_puple,R.drawable.bg_color_puple, Color.rgb(212,35,122)));
        mcolors2.add(new ColorModel(R.drawable.color_puple1,R.drawable.bg_color_puple1, Color.rgb(229,107,164)));
        mcolors2.add(new ColorModel(R.drawable.color_green1,R.drawable.bg_color_green1, Color.rgb(14,146,92)));
        mcolors2.add(new ColorModel(R.drawable.color_green2,R.drawable.bg_color_green2, Color.rgb(3,231,185)));
        mcolors2.add(new ColorModel(R.drawable.color_green3,R.drawable.bg_color_green3, Color.rgb(164,231,3)));
        mcolors2.add(new ColorModel(R.drawable.color_pink,R.drawable.bg_color_pink, Color.rgb(67,3,231)));
        mcolors2.add(new ColorModel(R.drawable.color_pink1,R.drawable.bg_color_pink1, Color.rgb(180,0,255)));
        mcolors2.add(new ColorModel(R.drawable.color_yellow1,R.drawable.bg_color_yellow1, Color.rgb(231,94,3)));
        mcolors2.add(new ColorModel(R.drawable.color_yellow2,R.drawable.bg_color_yellow2, Color.rgb(255,72,0)));
        mcolors2.add(new ColorModel(R.drawable.color_yellow2,R.drawable.bg_color_red1, Color.rgb(255,0,0)));
        mRichEditActivity.mColorAdapter2 = new ColorAdapter(mRichEditActivity,mcolors2);

        mRichEditActivity.mEditor.setPadding(10, 10, 10, 10);
//        mRichEditActivity.mPreview = mRichEditActivity.findViewById(R.id.preview);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mRichEditActivity.mRelativeLayout = (RelativeLayout) mRichEditActivity.findViewById(R.id.shade);
        mRichEditActivity.mEditor.setPlaceholder("Insert text here...");
        mRichEditActivity.mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
//                mRichEditActivity.mPreview.setText(text);
            }
        });

        mRichEditActivity.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.undo();
            }
        });

        mRichEditActivity.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.redo();
            }
        });

        mRichEditActivity.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRichEditActivity.isblod == false) {
                    mRichEditActivity.isblod = true;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_bold)).setImageResource(R.drawable.bolds);
                    mRichEditActivity.mEditor.setBold();
                } else {
                    mRichEditActivity.isblod = false;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_bold)).setImageResource(R.drawable.bold);
                    mRichEditActivity.mEditor.setBold();
                }
            }
        });

        mRichEditActivity.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRichEditActivity.isitalic == false) {
                    mRichEditActivity.isitalic = true;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_italic)).setImageResource(R.drawable.italics);
                    mRichEditActivity.mEditor.setItalic();
                } else {
                    mRichEditActivity.isitalic = false;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_italic)).setImageResource(R.drawable.italic);
                    mRichEditActivity.mEditor.setItalic();
                }
            }
        });

        mRichEditActivity.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRichEditActivity.isstrikethrough == false) {
                    mRichEditActivity.isstrikethrough = true;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_strikethrough)).setImageResource(R.drawable.strikethroughs);
                    mRichEditActivity.mEditor.setStrikeThrough();
                } else {
                    mRichEditActivity.isstrikethrough = false;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_strikethrough)).setImageResource(R.drawable.strikethrough);
                    mRichEditActivity.mEditor.setStrikeThrough();
                }
            }
        });

        mRichEditActivity.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRichEditActivity.isunderline == false) {
                    mRichEditActivity.isunderline = true;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_underline)).setImageResource(R.drawable.underlines);
                    mRichEditActivity.mEditor.setUnderline();
                } else {
                    mRichEditActivity.isunderline = false;
                    ((ImageView) mRichEditActivity.findViewById(R.id.action_underline)).setImageResource(R.drawable.underline);
                    mRichEditActivity.mEditor.setUnderline();
                }
            }
        });

        mRichEditActivity.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setHeading(1);
            }
        });

        mRichEditActivity.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setHeading(2);
            }
        });

        mRichEditActivity.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setHeading(3);
            }
        });

        mRichEditActivity.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setHeading(4);
            }
        });

        mRichEditActivity.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setHeading(5);
            }
        });

        mRichEditActivity.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setHeading(6);
            }
        });

        mRichEditActivity.findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
//                mRichEditActivity.mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//                isChanged = !isChanged;
                selectColor(true);
            }
        });

        mRichEditActivity.findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
//            private boolean isChanged;

            @Override
            public void onClick(View v) {
//                mRichEditActivity.mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//                isChanged = !isChanged;
                selectColor(false);
            }
        });

        mRichEditActivity.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setAlignLeft();
            }
        });

        mRichEditActivity.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setAlignCenter();
            }
        });

        mRichEditActivity.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.mEditor.setAlignRight();
            }
        });


//        mRichEditActivity.findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doInstertPic();
//            }
//        });

        mRichEditActivity.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLinkDialog();
            }
        });
    }

    public void odfinifsh()
    {
        Intent intent = new Intent();
        intent.setAction(mRichEditActivity.getIntent().getAction());
        intent.putExtra("value",mRichEditActivity.mEditor.getHtml());
        mRichEditActivity.sendBroadcast(intent);
        mRichEditActivity.finish();
    }

    public void addLinkDialog()
    {
        LinearLayout linearLayout = new LinearLayout(mRichEditActivity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText et = new EditText(mRichEditActivity);
        et.setHint("link text");
        final EditText et2 = new EditText(mRichEditActivity);
        et2.setHint("http://...");
        linearLayout.addView(et);
        linearLayout.addView(et2);
        AlertDialog.Builder builder = new AlertDialog.Builder(mRichEditActivity);
        builder.setView(linearLayout);
        builder.setTitle(mRichEditActivity.getString(R.string.link));
        builder.setNegativeButton(mRichEditActivity.getString(R.string.button_word_cancle), null);
        builder.setPositiveButton(mRichEditActivity.getString(R.string.button_word_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (et2.getText().length() > 0 && et.getText().length() > 0) {
                    addlink(et.getText().toString(), et2.getText().toString());
                } else {
                    AppUtils.showMessage(mRichEditActivity, mRichEditActivity.getString(R.string.keyword_inputname));
                }
            }
        });
        builder.show();
    }

    public void selectColor(boolean istext)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(mRichEditActivity);
        // 获取布局
        View view2 = View.inflate(mRichEditActivity, R.layout.buttom_window81, null);
        // 获取布局中的控件
        GridView mGrid = (GridView) view2.findViewById(R.id.more_grid);


        if(istext)
        {
            mGrid.setAdapter(mRichEditActivity.mColorAdapter);
            mGrid.setOnItemClickListener(mRichEditActivity.clorClick);
        }
        else
        {
            mGrid.setAdapter(mRichEditActivity.mColorAdapter2);
            mGrid.setOnItemClickListener(mRichEditActivity.clorClick2);
        }

        // 设置参数
        // 创建对话框
        builder.setView(view2);
        mRichEditActivity.alertDialog = builder.create();
        mRichEditActivity.alertDialog.show();
    }

    public void doSetColor(ColorModel item)
    {
        mRichEditActivity.mEditor.setTextColor(item.color);
        ((ImageView) mRichEditActivity.findViewById(R.id.action_txt_color)).setImageResource(item.pngId);
        mRichEditActivity.alertDialog.dismiss();
    }
    public void doSetColor2(ColorModel item)
    {
        mRichEditActivity.mEditor.setTextBackgroundColor(item.color);
        ((ImageView) mRichEditActivity.findViewById(R.id.action_bg_color)).setImageResource(item.pngId);
        mRichEditActivity.alertDialog.dismiss();
    }


    public void addlink(String name, String link)
    {
        mRichEditActivity.mEditor.insertLink(link, name);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
    }

    public void doInstertPic() {
        View popupWindowView = LayoutInflater.from(mRichEditActivity).inflate(R.layout.picpwindowmenu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mRichEditActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mRichEditActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRichEditActivity.popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mRichEditActivity.popupWindow1.setBackgroundDrawable(dw);
        Button textview_edit = (Button) popupWindowView.findViewById(R.id.btn_pick_photo3);
        textview_edit.setOnClickListener(mRichEditActivity.mTakePhotoListenter);
        Button textview_delete = (Button) popupWindowView.findViewById(R.id.btn_take_photo);
        textview_delete.setOnClickListener(mRichEditActivity.mAddPicListener);
        Button textview_new = (Button) popupWindowView.findViewById(R.id.btn_cancel);
        textview_new.setOnClickListener(mRichEditActivity.mCancleListenter);
        mRichEditActivity.mRelativeLayout.setVisibility(View.VISIBLE);
        mRichEditActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mRichEditActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        mRichEditActivity.popupWindow1.showAtLocation(mRichEditActivity.findViewById(R.id.main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    public Uri getOutputMediaFileUri() {
        // get the mobile Pictures directory
//        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//
//        // get the current time
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String path = CacheManager.APP_PATH + CacheManager.RICH_PATH +"/"+ timeStamp + ".jpg";
//        String dir = CacheManager.APP_PATH + CacheManager.RICH_PATH;
//        File doc = new File(dir);
//        if (!doc.exists()) {
//            doc.mkdirs();
//        }
//        File videoFile = new File(path);
//        return Uri.fromFile(videoFile);
        return null;
    }


    public void praseInsertPic(String json)
    {
        String path = "";
        if(!json.equals("net"))
        {
            JSONObject mjson = null;
            try {
                mjson = new JSONObject(json);
                JSONArray data = mjson.getJSONArray("data");
                if(data.length() > 0)
                {
                    JSONObject jo = data.getJSONObject(0);
                    if (jo.getInt("status") == 200) {
                        path = jo.getString("hash");
                    }
                }
                if(path.length() > 0)
                {
//                    String urlString = NetUtils.getInstance().createURLStringex(FunctionManager.ATTACHMENT_PATH, FunctionManager.TEST_HOST);
//                    urlString+="?"+"method=get.attachment.item&hash="+path;
//                    mRichEditActivity.mEditor.insertImage(urlString,
//                            "dachshund");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public static class RichEditHandler extends Handler {
        public RichEditActivity theActivity;

        RichEditHandler(RichEditActivity mRichEditActivity) {
            theActivity = mRichEditActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            switch (msg.what) {
//                case AppUtils.EVENT_UPLOAD_PIC:
//                    AppUtils.sendpic((MultipartBody.Builder)msg.obj,theActivity.mRichEditPresenter.mRichEditHandler
//                            ,AppUtils.EVENT_UPLOAD_PIC_SUCCESS,AppUtils.EVENT_UPLOAD_PIC_FAIL,theActivity);
//                    break;
//                case AppUtils.EVENT_UPLOAD_PIC_SUCCESS:
//                    theActivity.mRichEditPresenter.praseInsertPic((String) msg.obj);
//                    break;
//                case AppUtils.EVENT_UPLOAD_PIC_FAIL:
//                    break;
            }

        }
    }
}
