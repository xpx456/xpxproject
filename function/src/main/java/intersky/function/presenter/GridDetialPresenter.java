package intersky.function.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.EditDialogListener;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.asks.FunAsks;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.receiver.entity.Function;
import intersky.function.handler.GridDetialHandler;
import intersky.function.prase.FunPrase;
import intersky.function.receiver.GrideDetialReceiver;
import intersky.function.view.activity.GridActivity;
import intersky.function.view.activity.GridAttachmentActivity;
import intersky.function.view.activity.GridDetialActivity;
import intersky.mywidget.InnerScrollView;
import intersky.mywidget.TableCloumArts;
import intersky.scan.ScanUtils;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class GridDetialPresenter implements Presenter {

    public GridDetialHandler mGridDetialHandler;
    public GridDetialActivity mGridDetialActivity;
    public GridDetialPresenter(GridDetialActivity mGridDetialActivity) {
        this.mGridDetialActivity = mGridDetialActivity;
        this.mGridDetialHandler = new GridDetialHandler(mGridDetialActivity);
        mGridDetialActivity.setBaseReceiver(new GrideDetialReceiver(mGridDetialHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mGridDetialActivity.setContentView(R.layout.activity_grid_detial);
        mGridDetialActivity.mFunction = mGridDetialActivity.getIntent().getParcelableExtra("function");
        mGridDetialActivity.isNew = mGridDetialActivity.getIntent().getBooleanExtra("isnew",false);
        mGridDetialActivity.mActions = mGridDetialActivity.findViewById(R.id.actions);
        mGridDetialActivity.mTable = mGridDetialActivity.findViewById(R.id.warnList);
        mGridDetialActivity.mTableArea = mGridDetialActivity.findViewById(R.id.listscoll);
        mGridDetialActivity.mHorizontalScrollView = mGridDetialActivity.findViewById(R.id.scrollView1);
        mGridDetialActivity.mClassManager = mGridDetialActivity.findViewById(R.id.tebs);
        mGridDetialActivity.back = (Button) mGridDetialActivity.findViewById(R.id.back);
        mGridDetialActivity.summit = (Button) mGridDetialActivity.findViewById(R.id.summit);
        mGridDetialActivity.accept = (Button) mGridDetialActivity.findViewById(R.id.accept);
        mGridDetialActivity.veto = (Button) mGridDetialActivity.findViewById(R.id.veto);
        mGridDetialActivity.accept.setOnClickListener(mGridDetialActivity.acceptListener);
        mGridDetialActivity.veto.setOnClickListener(mGridDetialActivity.vetoListener);
        mGridDetialActivity.imScrollView = (ScrollView) mGridDetialActivity.findViewById(R.id.listscoll);
        mGridDetialActivity.btnSave = mGridDetialActivity.findViewById(R.id.save_btn);
        mGridDetialActivity.btnSave.setOnClickListener(mGridDetialActivity.doSaveListener);
        ToolBarHelper.setTitle(mGridDetialActivity.mActionBar,mGridDetialActivity.mFunction.mCaption);
        initPopupWindow2();
        if (mGridDetialActivity.mFunction.isWorkFlowDetial) {
            WorkFlowAsks.getWorkFlowTask(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction.modulflag);
            mGridDetialActivity.waitDialog.show();
        }
        else if(mGridDetialActivity.mFunction.modulflag.length() > 0)
        {
            FunAsks.getSubGride(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction);
            mGridDetialActivity.waitDialog.show();
        }
        else
        {
            FunPrase.praseGrideData(mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial);
            initData();
        }

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

    public void initData() {
        if(mGridDetialActivity.getIntent().getBooleanExtra("edit",false) == false)
        {
            mGridDetialActivity.btnSave.setVisibility(View.GONE);
            if (mGridDetialActivity.mTableDetial.mHeads.size() > 0) {
                if (mGridDetialActivity.mFunction.showAction == true) {

                    mGridDetialActivity.mHorizontalScrollView.setVisibility(View.VISIBLE);
                    mGridDetialActivity.mActions.setVisibility(View.VISIBLE);
                    initAciton();
                } else {
                    mGridDetialActivity.mHorizontalScrollView.setVisibility(View.GONE);
                    mGridDetialActivity.mActions.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mGridDetialActivity.mTableArea
                            .getLayoutParams();
                    mLayoutParams.addRule(RelativeLayout.BELOW, R.id.scrollView1);
                    mLayoutParams.bottomMargin = 0;
                    mGridDetialActivity.mTableArea.setLayoutParams(mLayoutParams);
                }
                initHead();
            }
            else {
                if (mGridDetialActivity.mFunction.showAction == true) {
                    mGridDetialActivity.mHorizontalScrollView.setVisibility(View.GONE);
                    mGridDetialActivity.mActions.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mGridDetialActivity.mTableArea
                            .getLayoutParams();
                    mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mLayoutParams.bottomMargin = (int) (50 * mGridDetialActivity.mBasePresenter.mScreenDefine.density);
                    mGridDetialActivity.mTableArea.setLayoutParams(mLayoutParams);
                    initAciton();
                } else {
                    mGridDetialActivity.mHorizontalScrollView.setVisibility(View.GONE);
                    mGridDetialActivity.mActions.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mGridDetialActivity.mTableArea
                            .getLayoutParams();
                    mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mLayoutParams.bottomMargin = 0;
                    mGridDetialActivity.mTableArea.setLayoutParams(mLayoutParams);
                }

            }
            if (mGridDetialActivity.mTableDetial.attachmentData.length() > 0) {
                if (mGridDetialActivity.mFunction.canEdit == true) {
                    ToolBarHelper.setRightBtnText(mGridDetialActivity.mActionBar, mGridDetialActivity.mMoreListenter, mGridDetialActivity.getString(R.string.menu_more));
                } else {
                    ToolBarHelper.setRightBtnText(mGridDetialActivity.mActionBar, mGridDetialActivity.showAttachmentListener, mGridDetialActivity.getString(R.string.menu_attchment));
                }
            } else {
                if (mGridDetialActivity.mFunction.canEdit == true) {
                    ToolBarHelper.setRightBtnText(mGridDetialActivity.mActionBar, mGridDetialActivity.mMoreListenter, mGridDetialActivity.getString(R.string.menu_more));
                }
            }
        }
        else {
            mGridDetialActivity.btnSave.setVisibility(View.VISIBLE);
            mGridDetialActivity.mHorizontalScrollView.setVisibility(View.GONE);
            mGridDetialActivity.mActions.setVisibility(View.GONE);
            RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mGridDetialActivity.mTableArea
                    .getLayoutParams();
            mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mLayoutParams.bottomMargin = (int) (40 * mGridDetialActivity.mBasePresenter.mScreenDefine.density);
            mGridDetialActivity.mTableArea.setLayoutParams(mLayoutParams);
            ToolBarHelper.setRightBtnText(mGridDetialActivity.mActionBar, mGridDetialActivity.doCreatListener, mGridDetialActivity.getString(R.string.button_new));
        }


        mGridDetialActivity.mTable.removeAllViews();
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        JSONObject data = null;
        try {
             data = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < mGridDetialActivity.mTableDetial.tableCloums.size(); i++) {
            TableCloumArts mTableCloumArts = mGridDetialActivity.mTableDetial.tableCloums.get(i);

            if (mTableCloumArts.isVisiable == true ) {
                if (mGridDetialActivity.getIntent().getBooleanExtra("edit", false)) {
                    if(mTableCloumArts.dataType.toLowerCase().equals("cimage") || mTableCloumArts.dataType.toLowerCase().equals("dtimage")
                            || mTableCloumArts.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_IMAGE))
                    {
                        View mView = initImage(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                    else if(mTableCloumArts.dataType.toLowerCase().equals("barcode"))
                    {
                        View mView = initBoard(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                    else if(mTableCloumArts.dataType.toLowerCase().equals("dtdatetime")|| mTableCloumArts.dataType.toLowerCase().equals("date")
                            ||mTableCloumArts.dataType.toLowerCase().equals("sex")||mTableCloumArts.mAttributes.contains("ValueList"))
                    {
                        View mView = initNomal(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                    else
                    {
                        View mView = initNomalEdit(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                }
                else {
                    if (mTableCloumArts.dataType.toLowerCase().equals("dtmemo"))
                    {
                        View mView = initMemo(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                    else if(mTableCloumArts.dataType.toLowerCase().equals("dtimage") || mTableCloumArts.dataType.toLowerCase().equals("cimage")
                            || mTableCloumArts.dataType.equals(TableCloumArts.GRIDE_DATA_TYPE_IMAGE))
                    {
                        View mView = initImage(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                    else
                    {
                        View mView = initNomal(mTableCloumArts,data);
                        if(mView != null)
                        {
                            View line = mInflater.inflate(R.layout.bussiness_warn_cell_line, null);
                            mGridDetialActivity.mTable.addView(mView);
                            mGridDetialActivity.mTable.addView(line);
                        }
                    }
                }
            }
        }
    }

    public void updateView() {
        JSONObject data = null;
        try {
            data = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
            if(mGridDetialActivity.getIntent().getBooleanExtra("edit", false))
            {
                for (int i = 0; i < mGridDetialActivity.mTableDetial.tableCloums.size(); i++) {
                    TableCloumArts mTableCloumArts = mGridDetialActivity.mTableDetial.tableCloums.get(i);
                    if(mTableCloumArts.dataType.toLowerCase().equals("cimage") || mTableCloumArts.dataType.toLowerCase().equals("dtimage"))
                    {
                        if(mTableCloumArts.view != null)
                        {
                            ImageView imageView = (ImageView) mTableCloumArts.view;
                            File file = new File(data.getString(mTableCloumArts.mFiledName));
                            if(file.exists())
                            {
                                Glide.with(mGridDetialActivity).load(file).into(imageView);
                            }
                            else
                            {
                                String url = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,data.getString(mTableCloumArts.mFiledName));
                                Glide.with(mGridDetialActivity).load(url).into(imageView);
                            }

                        }

                    }
                    else
                    {
                        if(mTableCloumArts.view != null)
                        {
                            TextView textView = (EditText) mTableCloumArts.view;
                            textView.setText(String.valueOf(data.get(mTableCloumArts.mFiledName)));
                        }
                    }
                }
            }
            else
            {
                for (int i = 0; i < mGridDetialActivity.mTableDetial.tableCloums.size(); i++) {
                    TableCloumArts mTableCloumArts = mGridDetialActivity.mTableDetial.tableCloums.get(i);
                    if(mTableCloumArts.dataType.toLowerCase().equals("cimage") || mTableCloumArts.dataType.toLowerCase().equals("dtimage"))
                    {
                        if(mTableCloumArts.view != null)
                        {
                            ImageView imageView = (ImageView) mTableCloumArts.view;
                            File file = new File(data.getString(mTableCloumArts.mFiledName));
                            if(file.exists())
                            {
                                Glide.with(mGridDetialActivity).load(file).into(imageView);
                            }
                            else
                            {
                                String url = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,data.getString(mTableCloumArts.mFiledName));
                                Glide.with(mGridDetialActivity).load(url).into(imageView);
                            }

                        }

                    }
                    else if(mTableCloumArts.dataType.toLowerCase().equals("dtdatetime")|| mTableCloumArts.dataType.toLowerCase().equals("date")
                            ||mTableCloumArts.dataType.toLowerCase().equals("sex")||mTableCloumArts.mAttributes.contains("ValueList"))
                    {
                        if(mTableCloumArts.view != null)
                        {
                            TextView textView = (TextView) mTableCloumArts.view;
                            textView.setText(String.valueOf(data.get(mTableCloumArts.mFiledName)));
                        }

                    }
                    else
                    {
                        if(mTableCloumArts.view != null)
                        {
                            EditText textView = (EditText) mTableCloumArts.view;
                            textView.setText(String.valueOf(data.get(mTableCloumArts.mFiledName)));
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showMore() {
        ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
        if (mGridDetialActivity.mTableDetial.attachmentData.length() > 0)
            mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.menu_attchment), mGridDetialActivity.showAttachmentListener));
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.menu_new), mGridDetialActivity.newListener));
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.menu_edit), mGridDetialActivity.editListener));
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.menu_delete), mGridDetialActivity.deleteListener));
        mGridDetialActivity.mPopupWindow = AppUtils.creatButtomMenu(mGridDetialActivity, (RelativeLayout) mGridDetialActivity.findViewById(R.id.shade), mMenuItems, mGridDetialActivity.findViewById(R.id.grid_detail_activity));
    }

    public void simpleoper(View v) {
        NetObject item = (NetObject) v.getTag();
        Intent intent = null;
        TableCloumArts mTableCloumArts = (TableCloumArts) item.item;
        if(mTableCloumArts.dataType.equals("email"))
        {

        }
        else if(mTableCloumArts.dataType.equals("phone"))
        {
            Uri uri = Uri.parse("del:" + item.result);
            intent = new Intent(Intent.ACTION_DIAL, uri);
            mGridDetialActivity.startActivity(intent);
        }
        else if(mTableCloumArts.dataType.equals("city"))
        {
            mGridDetialActivity.address = item.result;
            showPopupWindow2(v);
        }

    }

    public void doAttachment() {
        Intent intent = new Intent(mGridDetialActivity, GridAttachmentActivity.class);
        Function function = new Function();
        function.modulflag = mGridDetialActivity.mFunction.modulflag;
        function.mRecordId = mGridDetialActivity.mFunction.mRecordId;
        function.mCellValue = mGridDetialActivity.mTableDetial.attachmentData;
        if(function.mCellValue != null)
        mGridDetialActivity.startActivity(intent);
        if(mGridDetialActivity.mPopupWindow != null)
        {
            mGridDetialActivity.mPopupWindow.dismiss();
        }
    }

    public void doSelectList(TextView v) {
        ArrayList<Select> selects = new ArrayList<Select>();
        TableCloumArts mTableCloumArts = (TableCloumArts) v.getTag();
        mGridDetialActivity.mTableCloumArts = mTableCloumArts;
        String[] value = mTableCloumArts.mValues.split(",");
        for(int i = 0 ; i < value.length ; i++)
        {
            Select mSelect = new Select(mTableCloumArts.mFiledName,value[i]);
            selects.add(mSelect);
        }
        SelectManager.getInstance().startSelectView(mGridDetialActivity,selects,mTableCloumArts.mCaption, GridDetialActivity.ACTION_SET_SELECT_LIST,true,false);
    }

    public void showPopupWindow2(View mTextView) {

        if (!mGridDetialActivity.popupWindow2.isShowing()) {
            mGridDetialActivity.popupWindow2.showAsDropDown(mTextView, mTextView.getLayoutParams().width / 2, 0);
        } else {
            mGridDetialActivity.popupWindow2.dismiss();
        }
    }

    public void onGaode() {
        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse("androidamap://keywordNavi?sourceApplication=intersky&keyword="
                        + Uri.encode(mGridDetialActivity.address) + " &style=2"));
        intent.setPackage("com.autonavi.minimap");
        intent.addCategory("android.intent.category.DEFAULT");
        mGridDetialActivity.startActivity(intent);

        mGridDetialActivity.popupWindow2.dismiss();
    }

    public void onGaodeDown() {
        String url = "http://mobile.amap.com/download.html";
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mGridDetialActivity.startActivity(it);
        mGridDetialActivity.popupWindow2.dismiss();
    }

    public void onBaidu() {
        Intent intent = new Intent();
        try {

            // 移动APP调起Android百度地图方式举例
            intent = Intent.getIntent("intent://map/geocoder?city=" + mGridDetialActivity.address
                    + "&src=intersky#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 调起百度地图客户端（Android）展示上海市"28"路公交车的检索结果
        mGridDetialActivity.startActivity(intent); // 启动调用

        mGridDetialActivity.popupWindow2.dismiss();
    }

    public void onBaiduDown() {
        String url = "http://shouji.baidu.com/soft/item?docid=7943539&from=web_alad_6";
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mGridDetialActivity.startActivity(it);
        mGridDetialActivity.popupWindow2.dismiss();
    }

    public void setTime(TextView time) {
        String otime = time.getText().toString();
        TableCloumArts tableCloumArts = (TableCloumArts) time.getTag();
        if(otime.length() == 0)
        {
            otime = TimeUtils.getDate()+TimeUtils.getTime();
        }
        AppUtils.creatDataPicker(mGridDetialActivity,otime
                , mGridDetialActivity.getString(R.string.schdule_select_time),new DataPickerListener(tableCloumArts));
    }

    public void doPick(TableCloumArts mTableCloumArts) {
        mGridDetialActivity.mTableCloumArts  = mTableCloumArts;
        Bus.callData(mGridDetialActivity,"filetools/getPhotos",false,1,"intersky.function.view.activity.GridDetialActivity", GridDetialActivity.ACTION_GRID_PHOTO_SELECT);
    }

    public void takePhoto(TableCloumArts mTableCloumArts) {
        mGridDetialActivity.mTableCloumArts  = mTableCloumArts;
        mGridDetialActivity.permissionRepuest = (PermissionResult) Bus.callData(mGridDetialActivity,"filetools/checkPermissionTakePhoto",mGridDetialActivity,Bus.callData(mGridDetialActivity,"filetools/getfilePath","grid/image"));
    }

    public void doDelete(TableCloumArts mTableCloumArts) {
        try {
            JSONObject data2 = null;
            ImageView mImageView = mTableCloumArts.view.findViewById(R.id.value);
            mTableCloumArts.mValues = "";
            data2 = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
            data2.put(mTableCloumArts.mFiledName,"");
            mGridDetialActivity.mTableDetial.tempData = data2.toString();
            mImageView.setImageResource(R.drawable.imgadd);
            mImageView.setOnClickListener(new ItemClickListener(mTableCloumArts));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void takePhotoResult() {
        File mFile = new File((String) Bus.callData(mGridDetialActivity,"filetools/takePhotoUri",""));
        if (mFile.exists()) {
            try {
                JSONObject data2 = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
                data2.put(mGridDetialActivity.mTableCloumArts.mFiledName,mFile.getPath());
                mGridDetialActivity.mTableDetial.tempData = data2.toString();
                ImageView imageView = (ImageView) mGridDetialActivity.mTableCloumArts.view;
                Glide.with(mGridDetialActivity).load(mFile).into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPicResult(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        if(attachments.size() > 0)
        {
            File mFile = new File(attachments.get(0).mPath);
            if (mFile.exists()) {
                try {
                    JSONObject data2 = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
                    data2.put(mGridDetialActivity.mTableCloumArts.mFiledName,mFile.getPath());
                    mGridDetialActivity.mTableDetial.tempData = data2.toString();
                    ImageView imageView = (ImageView) mGridDetialActivity.mTableCloumArts.view;
                    Glide.with(mGridDetialActivity).load(mFile).into(imageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setSelectValue() {
        try {
            JSONObject data = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
            if(SelectManager.getInstance().mSignal != null && mGridDetialActivity.mTableCloumArts != null)
            data.put(mGridDetialActivity.mTableCloumArts.mFiledName, SelectManager.getInstance().mSignal.mName);
            mGridDetialActivity.mTableDetial.tempData = data.toString();
            if(mGridDetialActivity.mTableCloumArts != null)
            {
                if(mGridDetialActivity.mTableCloumArts.view != null)
                {
                    TextView textView = (TextView) mGridDetialActivity.mTableCloumArts.view;
                    if(SelectManager.getInstance().mSignal != null)
                    textView.setText(SelectManager.getInstance().mSignal.mName);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setScan(String text) {
        try {
            JSONObject data = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
            data.put(mGridDetialActivity.mTableCloumArts.mFiledName,text);
            TextView textView = (TextView) mGridDetialActivity.mTableCloumArts.view;
            textView.setText(text);
            mGridDetialActivity.mTableDetial.tempData = data.toString();
            FunAsks.getFill(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial, mGridDetialActivity.mTableCloumArts);
            mGridDetialActivity.waitDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startCreat() {
        Intent intent = new Intent(mGridDetialActivity, GridDetialActivity.class);
        Function function = new Function();
        function.mCaption = mGridDetialActivity.mFunction.mCaption;
        function.mName = mGridDetialActivity.mFunction.mName;
        function.modulflag = mGridDetialActivity.mFunction.modulflag;
        function.canEdit = mGridDetialActivity.mFunction.canEdit;
        function.series = mGridDetialActivity.mFunction.series;
        function.mRecordId = mGridDetialActivity.mFunction.mRecordId;
        function.mColName = mGridDetialActivity.mFunction.mColName;
        try {
            JSONObject jsonObject = new JSONObject();
            for(int i = 0; i < mGridDetialActivity.mTableDetial.tableCloums.size() ; i++)
            {
                TableCloumArts tableCloumArts = mGridDetialActivity.mTableDetial.tableCloums.get(i);
                jsonObject.put(tableCloumArts.mFiledName,tableCloumArts.mDefault);
            }
            function.mCellValue = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent.putExtra("function",function);
        intent.putExtra("isnew",true);
        intent.putExtra("edit",true);
        mGridDetialActivity.startActivity(intent);
        if(mGridDetialActivity.mPopupWindow != null)
        {
            mGridDetialActivity.mPopupWindow.dismiss();
        }
    }

    public void startEdit() {
        Intent intent = new Intent(mGridDetialActivity, GridDetialActivity.class);
        Function function = new Function();
        function.mCaption = mGridDetialActivity.mFunction.mCaption;
        function.mName = mGridDetialActivity.mFunction.mName;
        function.modulflag = mGridDetialActivity.mFunction.modulflag;
        function.canEdit = mGridDetialActivity.mFunction.canEdit;
        function.series = mGridDetialActivity.mFunction.series;
        function.mRecordId = mGridDetialActivity.mFunction.mRecordId;
        function.mColName = mGridDetialActivity.mFunction.mColName;
        function.mCellValue = mGridDetialActivity.mFunction.mCellValue;
        intent.putExtra("function",function);
        intent.putExtra("edit",true);
        mGridDetialActivity.startActivity(intent);
        if(mGridDetialActivity.mPopupWindow != null)
        {
            mGridDetialActivity.mPopupWindow.dismiss();
        }
    }

    public void doDelete() {
        AppUtils.creatDialogTowButton(mGridDetialActivity,mGridDetialActivity.getString(R.string.menu_delete),mGridDetialActivity.getString(R.string.menu_delete)
        ,mGridDetialActivity.getString(R.string.button_word_cancle),mGridDetialActivity.getString(R.string.button_word_ok),null,new DeleteListener());
        if(mGridDetialActivity.mPopupWindow != null)
        {
            mGridDetialActivity.mPopupWindow.dismiss();
        }
    }

    public void doCreat(boolean isnew) {
        AppUtils.creatDialogTowButton(mGridDetialActivity, mGridDetialActivity.getString(R.string.title_forgavesave), mGridDetialActivity.getString(R.string.title_forgavesave)
                , mGridDetialActivity.getString(R.string.button_no), mGridDetialActivity.getString(R.string.button_saveandnew), mGridDetialActivity.getString(R.string.button_yes)
        ,null,new SaveAndNewListener(isnew),new NewListener());

    }

    public void doEdit(boolean isnew) {
        FunAsks.editGride(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial,isnew,false);
    }

    public void doReCreat() {
        try {
            JSONObject jsonObject = new JSONObject();
            for(int i = 0; i < mGridDetialActivity.mTableDetial.tableCloums.size() ; i++)
            {
                TableCloumArts tableCloumArts = mGridDetialActivity.mTableDetial.tableCloums.get(i);
                jsonObject.put(tableCloumArts.mFiledName,tableCloumArts.mDefault);
            }
            mGridDetialActivity.mTableDetial.tempData = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mGridDetialActivity.isNew = true;
        mGridDetialActivity.btnSave.setEnabled(false);
        mGridDetialActivity.btnSave.setBackgroundColor(Color.rgb(255, 67, 68));
    }

    public void doEditEnd() {
        mGridDetialActivity.btnSave.setEnabled(false);
        mGridDetialActivity.btnSave.setBackgroundColor(Color.GRAY);
    }

    public void doAccept() {
        AppUtils.creatDialogTowButtonEdit(mGridDetialActivity, mGridDetialActivity.getString(R.string.approvemsg), ""
                , mGridDetialActivity.getString(R.string.button_word_cancle), mGridDetialActivity.getString(R.string.button_word_summit),null,new AcceptListener()
                , mGridDetialActivity.getString(R.string.approvemsg_hit));
    }

    public void doVote() {
        AppUtils.creatDialogTowButtonEdit(mGridDetialActivity, mGridDetialActivity.getString(R.string.title_approvedonot), mGridDetialActivity.getString(R.string.title_approvedonot)
                , mGridDetialActivity.getString(R.string.button_word_cancle), mGridDetialActivity.getString(R.string.button_word_summit),null,new VetoListener()
                , mGridDetialActivity.getString(R.string.hit_approvedonot));
    }

    private void initPopupWindow2() {
        initPopupWindowView2();
        mGridDetialActivity.popupWindow2 = new PopupWindow(mGridDetialActivity.mPopupWindowView2,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mGridDetialActivity.popupWindow2.setFocusable(true);
        mGridDetialActivity.popupWindow2.setOutsideTouchable(true);
        mGridDetialActivity.popupWindow2.setTouchable(true);
        mGridDetialActivity.popupWindow2
                .setBackgroundDrawable(mGridDetialActivity.getResources().getDrawable(R.color.toolBarBgColor));
        mGridDetialActivity.popupWindow2.update();
        mGridDetialActivity.popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mGridDetialActivity.popupWindow2.dismiss();
            }
        });
    }

    private void initPopupWindowView2() {
        mGridDetialActivity.mPopupWindowView2 = LayoutInflater.from(mGridDetialActivity).inflate(R.layout.mapmenu,
                null);
        TextView mtext = (TextView) mGridDetialActivity.mPopupWindowView2.findViewById(R.id.textview_gaoded);
        if (AppUtils.checkApkExist(mGridDetialActivity, "com.autonavi.minimap")) {
            mtext.setVisibility(View.INVISIBLE);
            RelativeLayout mRelativeLayout = (RelativeLayout) mGridDetialActivity.mPopupWindowView2
                    .findViewById(R.id.gaodelayer);
            mRelativeLayout.setOnClickListener(mGridDetialActivity.mOpenGaodeListener);
        } else {
            mtext.setVisibility(View.VISIBLE);
            mtext.setOnClickListener(mGridDetialActivity.mOpenGaodeDownListener);
        }
        mtext = (TextView) mGridDetialActivity.mPopupWindowView2.findViewById(R.id.textview_baidud);
        if (AppUtils.checkApkExist(mGridDetialActivity, "com.baidu.BaiduMap")) {
            mtext.setVisibility(View.INVISIBLE);
            RelativeLayout mRelativeLayout = (RelativeLayout) mGridDetialActivity.mPopupWindowView2
                    .findViewById(R.id.baidulayer);
            mRelativeLayout.setOnClickListener(mGridDetialActivity.mOpenBaiduListener);
        } else {
            mtext.setVisibility(View.VISIBLE);
            mtext.setOnClickListener(mGridDetialActivity.mOpenBaiduDownListener);
        }
    }

    private void initAciton() {

        if (!mGridDetialActivity.mTableDetial.backEnabled) {
            mGridDetialActivity.back.setClickable(false);
            mGridDetialActivity.back.setTextColor(Color.rgb(231, 231, 231));
        }
        else
        {
            mGridDetialActivity.back.setClickable(true);
            mGridDetialActivity.back.setTextColor(Color.rgb(140, 140, 140));
        }

        if (!mGridDetialActivity.mTableDetial.transmitEnabled) {
            mGridDetialActivity.summit.setClickable(false);
            mGridDetialActivity.summit.setTextColor(Color.rgb(231, 231, 231));
        }
        else
        {
            mGridDetialActivity.summit.setClickable(true);
            mGridDetialActivity.summit.setTextColor(Color.rgb(140, 140, 140));
        }

        if (!mGridDetialActivity.mTableDetial.approvalEnabled) {
            mGridDetialActivity.accept.setTextColor(Color.rgb(231, 231, 231));
            mGridDetialActivity.accept.setClickable(false);

            mGridDetialActivity.veto.setTextColor(Color.rgb(231, 231, 231));
            mGridDetialActivity.veto.setClickable(false);
        }
        else
        {
            mGridDetialActivity.accept.setClickable(true);
            mGridDetialActivity.accept.setTextColor(Color.rgb(140, 140, 140));
            mGridDetialActivity.veto.setClickable(true);
            mGridDetialActivity.veto.setTextColor(Color.rgb(140, 140, 140));
        }
    }


    public void updataAction(Intent intent) {

        if(mGridDetialActivity.mFunction.showAction == true)
        {
            if(intent.hasExtra("taskid"))
            {
                if(intent.getStringExtra("taskid").equals(mGridDetialActivity.mFunction.modulflag))
                {
                    if(intent.hasExtra("actionsuccess"))
                    {
                        closeAciton();
                    }
                }
            }
        }
    }

    private void closeAciton(){
        mGridDetialActivity.back.setClickable(false);
        mGridDetialActivity.back.setTextColor(Color.rgb(231, 231, 231));
        mGridDetialActivity.accept.setTextColor(Color.rgb(231, 231, 231));
        mGridDetialActivity.accept.setClickable(false);
        mGridDetialActivity.summit.setClickable(false);
        mGridDetialActivity.summit.setTextColor(Color.rgb(231, 231, 231));
        mGridDetialActivity.veto.setTextColor(Color.rgb(231, 231, 231));
        mGridDetialActivity.veto.setClickable(false);
    }

    private void initHead() {
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < mGridDetialActivity.mTableDetial.mHeads.size(); ++i) {
            View mView = mInflater.inflate(R.layout.headtab, null);
            TextView mtext = (TextView) mView.findViewById(R.id.tebtext);
            mtext.setText(" " + mGridDetialActivity.mTableDetial.mHeads.get(i) + " ");
            mtext.setOnClickListener(new HeadTeblistener(mGridDetialActivity.mTableDetial.mHeads.get(i)));
            mGridDetialActivity.mClassManager.addView(mView);
        }
    }

    private void getSubdetial(String head) {
        Intent intent = new Intent(mGridDetialActivity, GridActivity.class);
        Function mFunctionModel = new Function();
        mFunctionModel.typeName = Function.GRID;
        mFunctionModel.isSecond = true;
        mFunctionModel.isWorkFlowDetial = mGridDetialActivity.mFunction.isWorkFlowDetial;
        mFunctionModel.subgirde = true;
        mFunctionModel.mCaption = head;
        mFunctionModel.mName = head;
        mFunctionModel.mGrop = head;
        mFunctionModel.modulflag = mGridDetialActivity.mFunction.mName;
        mFunctionModel.mRecordId = mGridDetialActivity.mFunction.mRecordId;
        mFunctionModel.showAction = mGridDetialActivity.mFunction.showAction;
        intent.putExtra("function",mFunctionModel);
        intent.putExtra("actionjson",mGridDetialActivity.mFunction.actionjson);
        mGridDetialActivity.startActivity(intent);
    }

    private View initMemo(TableCloumArts mTableCloumArts, JSONObject data) {
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.bussiness_warn_detail_memo_cell, null);
        if (mGridDetialActivity.getIntent().getBooleanExtra("edit", true) == false)
        {

            InnerScrollView ms = (InnerScrollView) mView.findViewById(R.id.sss);
            ms.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //允许ScrollView截断点击事件，ScrollView可滑动
                        mGridDetialActivity.imScrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        //不允许ScrollView截断点击事件，点击事件由子View处理
                        TextView mTextView = (TextView) v.findViewById(R.id.value);
                        if (mTextView.getHeight() > mTextView.getMinHeight()) {
                            mGridDetialActivity.imScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    return false;
                }
            });
            TextView name = (TextView) mView.findViewById(R.id.name);
            TextView value = (EditText) mView.findViewById(R.id.value);
            name.setText(mTableCloumArts.mCaption + ":");
            try {
                value.setText(data.getString(mTableCloumArts.mFiledName));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mTableCloumArts.view = value;
            return mView;
        }
        return null;

    }

    private View initImage(TableCloumArts mTableCloumArts, JSONObject data) {
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.bussiness_warn_detail_image_cell, null);
        String mvalue = "";
        try {
            mvalue = data.getString(mTableCloumArts.mFiledName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView name = (TextView) mView.findViewById(R.id.name);
        name.setText(mTableCloumArts.mCaption + ":");
        ImageView value2 = (ImageView) mView.findViewById(R.id.value);
        if (mGridDetialActivity.getIntent().getBooleanExtra("edit", false) == false)
        {

            String url = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,mvalue);
            Glide.with(mGridDetialActivity).load(url).into(value2);
            Attachment attachment = new Attachment();
            attachment.mUrl = url;
            attachment.mPath = (String) Bus.callData(mGridDetialActivity,"filetools/getfilePath","function/"+url.substring(url.lastIndexOf("/"),url.length()));
//            Bus.callData(mGridDetialActivity,"filetools/startAttachment",)
            mView.setTag(attachment);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bus.callData(mGridDetialActivity,"filetools/startAttachment",v.getTag());
                }
            });
        }
        else
        {
            if(mvalue.length() > 0)
            {
                File file = new File(mvalue);
                if(file.exists())
                {
                    Glide.with(mGridDetialActivity).load(file).into(value2);
                }
                else
                {
                    String url = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,mvalue);
                    Glide.with(mGridDetialActivity).load(url).into(value2);
                }

            }
            else
            {
                value2.setImageResource(R.drawable.imgadd);
            }

            if(mTableCloumArts.dataType.toLowerCase().equals("cimage"))
            {
                value2.setOnClickListener(new ItemClickListener(mTableCloumArts));
            }
        }
        mTableCloumArts.view = value2;
        return mView;
    }

    private View initNomal(TableCloumArts mTableCloumArts, JSONObject data) {
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.bussiness_warn_detail_cell, null);
        if (mGridDetialActivity.getIntent().getBooleanExtra("edit", false) == false) {
            InnerScrollView ms = (InnerScrollView) mView.findViewById(R.id.sss);
            ms.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //允许ScrollView截断点击事件，ScrollView可滑动
                        mGridDetialActivity.imScrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        //不允许ScrollView截断点击事件，点击事件由子View处理
                        TextView mTextView = (TextView) v.findViewById(R.id.value);
                        if (mTextView.getHeight() > mTextView.getMinHeight()) {
                            mGridDetialActivity.imScrollView.requestDisallowInterceptTouchEvent(true);
                        }

                    }
                    return false;
                }
            });
            TextView name = mView.findViewById(R.id.name);
            TextView value = mView.findViewById(R.id.value);
            name.setText(mTableCloumArts.mCaption + ":");
            String mvalue = "";
            try {
                mvalue = data.getString(mTableCloumArts.mFiledName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mTableCloumArts.dataType.toLowerCase().equals("email") || mTableCloumArts.dataType.toLowerCase().equals("phone") ||
                    mTableCloumArts.dataType.toLowerCase().equals("city")) {
                value.setText(Html.fromHtml("<u>" + mvalue + "</u>"));
                value.setTextColor(Color.rgb(79, 191, 232));
                NetObject item = new NetObject();
                item.result = mvalue;
                item.item = mTableCloumArts;
                value.setTag(item);
                value.setOnClickListener(mGridDetialActivity.msimpleoperListener);
            } else if (mTableCloumArts.dataType.toLowerCase().equals("dtboolean") || mTableCloumArts.dataType.toLowerCase().equals("boolean")) {
                if (mvalue.equals("true")) {
                    value.setText(mGridDetialActivity.getString(R.string.boolean_true));
                } else {
                    value.setText(mGridDetialActivity.getString(R.string.boolean_false));
                }
                if (mvalue.equals("1")) {
                    value.setText(mGridDetialActivity.getString(R.string.boolean_true));
                } else if (mvalue.equals("0")) {
                    value.setText(mGridDetialActivity.getString(R.string.boolean_false));
                }
            } else {
                value.setText(mvalue);
            }
            mTableCloumArts.view = value;
            return mView;

        }else {
            if(mTableCloumArts.dataType.toLowerCase().equals("dtdatetime")|| mTableCloumArts.dataType.toLowerCase().equals("date"))
            {
                String mvalue = "";
                try {
                    mvalue = data.getString(mTableCloumArts.mFiledName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView name = (TextView) mView.findViewById(R.id.name);
                TextView value = (TextView) mView.findViewById(R.id.value);
                value.setOnClickListener(mGridDetialActivity.mTextTimeListener);
                name.setText(mTableCloumArts.mCaption + ":");
                value.setText(mvalue);
                value.setTag(mTableCloumArts);
                if (mTableCloumArts.isReadOnly == true || mTableCloumArts.mAttributes.contains("ReadOnly")) {
                    value.setFocusable(false);
                }
                mTableCloumArts.view = value;
                return mView;
            }
            else if(mTableCloumArts.dataType.toLowerCase().equals("sex")||mTableCloumArts.mAttributes.contains("ValueList"))
            {
                String mvalue = "";
                try {
                    mvalue = data.getString(mTableCloumArts.mFiledName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView name = (TextView) mView.findViewById(R.id.name);
                TextView value = (TextView) mView.findViewById(R.id.value);
                Drawable drawable= mGridDetialActivity.getResources().getDrawable(R.drawable.selectmore);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                value.setCompoundDrawables(null, null, drawable, null);
                value.setOnClickListener(mGridDetialActivity.mSelectListener);
                name.setText(mTableCloumArts.mCaption + ":");
                value.setText(mvalue);
                value.setTag(mTableCloumArts);
                value.addTextChangedListener(new TextTextWatcher(value, mTableCloumArts));
                if (mTableCloumArts.isReadOnly == true || mTableCloumArts.mAttributes.contains("ReadOnly")) {
                    value.setEnabled(false);
                }
                if(mTableCloumArts.isLinkage&& mvalue.length() == 0 && mTableCloumArts.mValues.length() == 0)
                {
                    FunAsks.getLinkValue(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial,mTableCloumArts);
                    mGridDetialActivity.waitDialog.show();
                }
                mTableCloumArts.view = value;
                return mView;
            }
        }
       return null;

    }

    private View initNomalEdit(TableCloumArts mTableCloumArts, JSONObject data) {
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.bussiness_warn_edit_detail_cell, null);
        String mvalue = "";
        try {
            mvalue = data.getString(mTableCloumArts.mFiledName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mTableCloumArts.dataType.toLowerCase().equals("dtinteger") || mTableCloumArts.dataType.toLowerCase().equals("dtfloat")||mTableCloumArts.dataType.toLowerCase().equals("number")
                ||mTableCloumArts.dataType.toLowerCase().equals("phone") || mTableCloumArts.dataType.toLowerCase().equals("mobile")|| mTableCloumArts.dataType.toLowerCase().equals("fax"))
        {

            TextView name2 = (TextView) mView.findViewById(R.id.name);
            EditText value2 = (EditText) mView.findViewById(R.id.value);
            value2.addTextChangedListener(new EditTextWatcher(value2, mTableCloumArts));
            name2.setText(mTableCloumArts.mCaption + ":");
            value2.setText(mvalue);
            value2.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            if(mTableCloumArts.mAttributes.contains("Password"))
            {
                value2.setInputType(EditorInfo.TYPE_CLASS_NUMBER| EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
            }
            if (mTableCloumArts.isReadOnly == true|| mTableCloumArts.mAttributes.contains("ReadOnly")) {
                value2.setEnabled(false);
            }
            mTableCloumArts.view = value2;
            return mView;
        }
        else {
            TextView name3 = (TextView) mView.findViewById(R.id.name);
            EditText value3 = (EditText) mView.findViewById(R.id.value);
            value3.addTextChangedListener(new EditTextWatcher(value3, mTableCloumArts));
            name3.setText(mTableCloumArts.mCaption + ":");
            value3.setText(mvalue);
            if (mTableCloumArts.isReadOnly == true|| mTableCloumArts.mAttributes.contains("ReadOnly")) {
                value3.setFocusable(false);
            }
            mTableCloumArts.view = value3;
            return mView;
        }
    }

    private View initBoard(TableCloumArts mTableCloumArts, JSONObject data) {
        LayoutInflater mInflater = (LayoutInflater) mGridDetialActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.bussiness_warn_edit_detail_scan_cell, null);
        String mvalue = "";
        try {
            mvalue = data.getString(mTableCloumArts.mFiledName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView name5 = (TextView) mView.findViewById(R.id.name);
        EditText value5 = (EditText) mView.findViewById(R.id.value);
        TextView scan1 = (TextView) mView.findViewById(R.id.scan);
        scan1.setOnClickListener(new MyScanListener(mTableCloumArts));
        value5.setOnEditorActionListener(new ItemOnEditorActionListener(mTableCloumArts));
        name5.setText(mTableCloumArts.mCaption + ":");
        value5.setText(mvalue);
        value5.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        value5.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        value5.setSingleLine(true);
        if(mTableCloumArts.mAttributes.contains("Password"))
        {
            value5.setInputType(EditorInfo.TYPE_CLASS_NUMBER| EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        if (mTableCloumArts.isReadOnly == true|| mTableCloumArts.mAttributes.contains("ReadOnly")) {
            value5.setFocusable(false);
        }
        mTableCloumArts.view = value5;
        return mView;
    }

    private void addImageMenu(TableCloumArts mTableCloumArts) {
        ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.button_word_takephoto), new TakePhotoClickListener((mTableCloumArts))));
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.button_word_album), new DoPicClickListener(mTableCloumArts)));
        mGridDetialActivity.mPopupWindow = AppUtils.creatButtomMenu(mGridDetialActivity,  (RelativeLayout) mGridDetialActivity.findViewById(R.id.shade), mMenuItems, mGridDetialActivity.findViewById(R.id.grid_detail_activity));
    }

    private void deleteAddImageMenu(TableCloumArts mTableCloumArts) {
        ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.button_word_takephoto), new TakePhotoClickListener((mTableCloumArts))));
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.button_word_album), new DoPicClickListener(mTableCloumArts)));
        mMenuItems.add(new MenuItem(mGridDetialActivity.getString(R.string.menu_delete), new DoDeleteClickListener(mTableCloumArts)));
        mGridDetialActivity.mPopupWindow = AppUtils.creatButtomMenu(mGridDetialActivity, (RelativeLayout) mGridDetialActivity.findViewById(R.id.shade), mMenuItems, mGridDetialActivity.findViewById(R.id.grid_detail_activity));
    }

    private void uplinkValues(TableCloumArts mTableCloumArts) {
        for (int i = 0; i < mGridDetialActivity.mTableDetial.tableCloums.size(); i++)
        {
            TableCloumArts temp = mGridDetialActivity.mTableDetial.tableCloums.get(i);
            if(mTableCloumArts.mToLinkageFields.contains(temp.mFiledName))
            {
                FunAsks.getLinkValue(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial,temp);
            }
        }
    }

    class HeadTeblistener implements View.OnClickListener {
        private String head;

        public HeadTeblistener(String head) {
            this.head = head;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            getSubdetial(head);
        }

    }

    class MyScanListener implements View.OnClickListener {
        public TableCloumArts mTableCloumArts;

        public MyScanListener(TableCloumArts mTableCloumArts) {
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridDetialActivity.mTableCloumArts = mTableCloumArts;
            mGridDetialActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mGridDetialActivity, "");

        }

    }

    class ItemOnEditorActionListener implements TextView.OnEditorActionListener{

        public TableCloumArts tableCloumArts;

        public ItemOnEditorActionListener(TableCloumArts tableCloumArts) {
            this.tableCloumArts = tableCloumArts;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                FunAsks.getFill(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial,tableCloumArts);
                mGridDetialActivity.waitDialog.show();

            }
            return true;
        }
    }

    class DataPickerListener implements DoubleDatePickerDialog.OnDateSetListener {

        public TableCloumArts tableCloumArts;

        public DataPickerListener(TableCloumArts tableCloumArts) {
            this.tableCloumArts = tableCloumArts;
        }

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            if (tableCloumArts.view != null) {
                TextView textView = (TextView) tableCloumArts.view;
                textView.setText(String.format("%04d-%02d-%02d", startYear, startMonthOfYear, startDayOfMonth));
                try {
                    JSONObject data = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
                    data.put(tableCloumArts.mFiledName, String.format("%04d-%02d-%02d", startYear, startMonthOfYear, startDayOfMonth));
                    mGridDetialActivity.mTableDetial.tempData = data.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class ItemClickListener implements View.OnClickListener {
        public TableCloumArts mTableCloumArts;

        public ItemClickListener(TableCloumArts mTableCloumArts) {
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String mvalue = "";
            try {
                JSONObject data = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
                mvalue = data.getString(mTableCloumArts.mFiledName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(mvalue.length() == 0)
            {
                addImageMenu(mTableCloumArts);
            }
            else
            {
                deleteAddImageMenu(mTableCloumArts);
            }
        }
    }

    class TakePhotoClickListener implements View.OnClickListener {
        public TableCloumArts mTableCloumArts;

        public TakePhotoClickListener(TableCloumArts mTableCloumArts) {
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            takePhoto(mTableCloumArts);
        }
    }

    class DoPicClickListener implements View.OnClickListener {
        public TableCloumArts mTableCloumArts;

        public DoPicClickListener(TableCloumArts mTableCloumArts) {
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            doPick(mTableCloumArts);
        }
    }

    class DoDeleteClickListener implements View.OnClickListener {
        public TableCloumArts mTableCloumArts;

        public DoDeleteClickListener(TableCloumArts mTableCloumArts) {
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            doDelete(mTableCloumArts);
        }
    }

    class TextTextWatcher implements TextWatcher {

        private TextView mEditView;
        private TableCloumArts mTableCloumArts;

        private TextTextWatcher(TextView mEditView, TableCloumArts mTableCloumArts) {
            this.mEditView = mEditView;
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub\
            try {
                JSONObject data2 = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
                data2.put(mTableCloumArts.mFiledName, mEditView.getText().toString());
                mGridDetialActivity.mTableDetial.tempData = data2.toString();
                uplinkValues(mTableCloumArts);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class EditTextWatcher implements TextWatcher {

        private EditText mEditView;
        private TableCloumArts mTableCloumArts;

        private EditTextWatcher(EditText mEditView, TableCloumArts mTableCloumArts) {
            this.mEditView = mEditView;
            this.mTableCloumArts = mTableCloumArts;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            try {
                JSONObject data2 = new JSONObject(mGridDetialActivity.mTableDetial.tempData);
                data2.put(mTableCloumArts.mFiledName,mEditView.getText().toString());
                mGridDetialActivity.mTableDetial.tempData = data2.toString();
                uplinkValues(mTableCloumArts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class SaveAndNewListener implements DialogInterface.OnClickListener {

        public boolean isnew = false;

        public SaveAndNewListener(boolean isnew)
        {
            this.isnew = isnew;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            FunAsks.editGride(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction, mGridDetialActivity.mTableDetial,isnew,true);
        }
    }

    class NewListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            doReCreat();
        }
    }

    class DeleteListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            FunAsks.deleteGride(mGridDetialActivity,mGridDetialHandler,mGridDetialActivity.mFunction,mGridDetialActivity.mFunction.mRecordId);
        }
    }

    class VetoListener extends EditDialogListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mGridDetialActivity.waitDialog.show();
            if(this.editText != null)
            WorkFlowAsks.doVeto(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction.modulflag,this.editText.getText().toString());
            else
                WorkFlowAsks.doVeto(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction.modulflag,"");
        }
    }

    class AcceptListener extends EditDialogListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mGridDetialActivity.waitDialog.show();
            if(this.editText != null)
                WorkFlowAsks.doAccept(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction.modulflag,editText.getText().toString());
            else
                WorkFlowAsks.doAccept(mGridDetialActivity, mGridDetialHandler, mGridDetialActivity.mFunction.modulflag,"");
        }
    }
}
