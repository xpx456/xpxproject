package intersky.vote.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.mywidget.MyLinearLayout;
import intersky.oa.OaUtils;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.vote.R;
import intersky.vote.VoteManager;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.VoteSelect;
import intersky.vote.handler.VoteHandler;
import intersky.vote.receicer.VoteReceiver;
import intersky.vote.view.activity.VoteActivity;
import intersky.xpxnet.net.NetObject;
import xpx.com.toolbar.utils.ToolBarHelper;

public class VotePresenter implements Presenter {

    public VoteActivity mVoteActivity;
    public VoteHandler mVoteHandler;

    public VotePresenter(VoteActivity mVoteActivity) {
        this.mVoteHandler = new VoteHandler(mVoteActivity);
        this.mVoteActivity = mVoteActivity;
        mVoteActivity.setBaseReceiver(new VoteReceiver(mVoteHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mVoteActivity.setContentView(R.layout.activity_vote);
        ToolBarHelper.setTitle(mVoteActivity.mActionBar, mVoteActivity.getString(R.string.xml_newvote));
        ToolBarHelper.setRightBtnText(mVoteActivity.mActionBar, mVoteActivity.mSaveListener, mVoteActivity.getString(R.string.button_word_summit));
        mVoteActivity.vote = mVoteActivity.getIntent().getParcelableExtra("vote");
        mVoteActivity.timeLayer = (RelativeLayout) mVoteActivity.findViewById(R.id.timelayer);
        mVoteActivity.content = (EditText) mVoteActivity.findViewById(R.id.answer_text);
        mVoteActivity.time = (TextView) mVoteActivity.findViewById(R.id.time_content);
        mVoteActivity.formLayer = (RelativeLayout) mVoteActivity.findViewById(R.id.vote_formlayer);
        mVoteActivity.voterLayer = (RelativeLayout) mVoteActivity.findViewById(R.id.voterlayer);
        mVoteActivity.mRelativeLayout = (RelativeLayout) mVoteActivity.findViewById(R.id.shade);
        mVoteActivity.form = (TextView) mVoteActivity.findViewById(R.id.vote_form_content);
        mVoteActivity.voter = (TextView) mVoteActivity.findViewById(R.id.voter_content);
        mVoteActivity.itemLayer = (LinearLayout) mVoteActivity.findViewById(R.id.item_content);
        mVoteActivity.mSwitch = (Switch) mVoteActivity.findViewById(R.id.swich);
        mVoteActivity.time.setText(TimeUtils.getDateAndTimeex(7));
        mVoteActivity.timeLayer.setOnClickListener(mVoteActivity.setDateTime);
        mVoteActivity.formLayer.setOnClickListener(mVoteActivity.setFrorm);
        mVoteActivity.voterLayer.setOnClickListener(mVoteActivity.setVoter);

        initVotetype();
        initVoteItem();
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        mVoteHandler = null;
    }


    public void initVotetype() {
        if (VoteManager.getInstance().mVoteTypes.size() == 0) {
            VoteManager.getInstance().mVoteTypes.add(new Select("0", mVoteActivity.getString(R.string.xml_vote_1)));
            VoteManager.getInstance().mVoteTypes.add(new Select("1", mVoteActivity.getString(R.string.xml_vote_2)));
        }
        mVoteActivity.mselectVoteType = VoteManager.getInstance().mVoteTypes.get(0);
        VoteManager.getInstance().mVoteTypes.get(0).iselect = true;
        mVoteActivity.form.setText(mVoteActivity.mselectVoteType.mName);
    }

    public void showEndTimeDialog() {
        AppUtils.creatDataAndTimePicker(mVoteActivity, mVoteActivity.vote.endTime, mVoteActivity.getString(R.string.xml_vote_time), mOnEndSetListener);
    }

    public DoubleDatePickerDialog.OnDateSetListener mOnEndSetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d %02d:%02d", startYear, startMonthOfYear + 1, startDayOfMonth, hour, miniute);
            mVoteActivity.vote.endTime = textString + ":00";
            if (TimeUtils.measureBefore(TimeUtils.getDateAndTime(), textString + ":00")) {
                mVoteActivity.time.setText(textString);
            } else {

                AppUtils.showMessage(mVoteActivity, mVoteActivity.getString(R.string.xml_vote_end));
            }

        }
    };

    public void doForm() {

        SelectManager.getInstance().startSelectView(mVoteActivity, VoteManager.getInstance().mVoteTypes, mVoteActivity.getString(R.string.xml_vote_form),
                VoteManager.ACTION_SET_VOTE_TYPE, true, false);
    }

    public void setVotetype() {
        mVoteActivity.mselectVoteType = SelectManager.getInstance().mSignal;
        mVoteActivity.form.setText(mVoteActivity.mselectVoteType.mName);
    }

    public void selectVoter() {
        Bus.callData(mVoteActivity,"chat/selectListContact", mVoteActivity.mVoters, VoteManager.ACTION_SET_VOTER, "选择联系人", false);
    }

    public void setVoter() {
        mVoteActivity.mVoters.clear();
        mVoteActivity.mVoters.addAll((ArrayList<Contacts>)Bus.callData(mVoteActivity,"chat/mselectitems", ""));
        String voters = "";
        for (int i = 0; i < mVoteActivity.mVoters.size(); i++) {
            if (i == 0) {
                voters += mVoteActivity.mVoters.get(i).mName;
            } else {
                voters += "," + mVoteActivity.mVoters.get(i).mName;
            }
        }
        mVoteActivity.voter.setText(voters);
    }

    public void initVoteItem() {
        setAddView();
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 2; i++) {

            VoteSelect temp = new VoteSelect("", mInflater.inflate(R.layout.vote_select_item, null));
            mVoteActivity.mVoteItems.add(temp);
            initVoteItemView(temp, i);
        }
    }

    public class MyTextWatcher implements TextWatcher {

        public VoteSelect mSelectItemModel;

        public MyTextWatcher(VoteSelect mSelectItemModel) {
            this.mSelectItemModel = mSelectItemModel;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mSelectItemModel.name = s.toString();
        }
    }

    public void initVoteItemView(VoteSelect mSelectItemModel, int id) {
        View mView = mSelectItemModel.mView;
        TextView itemid = (TextView) mView.findViewById(R.id.item_id);
        TextView title = (TextView) mView.findViewById(R.id.item_content);
        ImageView addimage = (ImageView) mView.findViewById(R.id.image_cion);
        addimage.setImageResource(R.drawable.crm_new_pic_icon);
        EditText mEditText = (EditText) mView.findViewById(R.id.item_content);
        mEditText.addTextChangedListener(new MyTextWatcher(mSelectItemModel));
        itemid.setText(String.valueOf(id + 1));
        addimage.setTag(mSelectItemModel);
        addimage.setOnClickListener(mVoteActivity.mShowAddListener);
        title.setText(mSelectItemModel.name);

        if (mSelectItemModel.mPics.size() > 0) {

            initPicView(mSelectItemModel);
        }
        mVoteActivity.itemLayer.addView(mSelectItemModel.mView, mVoteActivity.itemLayer.getChildCount() - 1);
    }

    public void setAddView() {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mVoteActivity.mVoteItems.size() > 2) {
            View mview = mInflater.inflate(R.layout.vote_delete_item, null);
            RelativeLayout addlayer = (RelativeLayout) mview.findViewById(R.id.add);
            addlayer.setOnClickListener(mVoteActivity.maddItemListener);
            RelativeLayout dellayer = (RelativeLayout) mview.findViewById(R.id.delete);
            dellayer.setOnClickListener(mVoteActivity.mdelItemListener);
            mVoteActivity.itemLayer.addView(mview);
        } else {
            View mview = mInflater.inflate(R.layout.vote_add_item, null);
            mview.setOnClickListener(mVoteActivity.maddItemListener);
            mVoteActivity.itemLayer.addView(mview);
        }
    }

    public void additem() {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mVoteActivity.mVoteItems.size() == 2) {

            View mview = mVoteActivity.itemLayer.getChildAt(mVoteActivity.itemLayer.getChildCount() - 1);
            mVoteActivity.itemLayer.removeView(mview);
            VoteSelect temp = new VoteSelect("", mInflater.inflate(R.layout.vote_select_item, null));
            mVoteActivity.mVoteItems.add(temp);
            setAddView();
            initVoteItemView(temp, mVoteActivity.mVoteItems.size() - 1);
        } else {
            VoteSelect temp = new VoteSelect("", mInflater.inflate(R.layout.vote_select_item, null));
            mVoteActivity.mVoteItems.add(temp);
            initVoteItemView(temp, mVoteActivity.mVoteItems.size() - 1);
        }


    }

    public void dodelete(View v) {

        VoteSelect voteSelect = (VoteSelect) v.getTag();
        mVoteActivity.isdelete = false;
        mVoteActivity.mVoteItems.remove(voteSelect);
        mVoteActivity.itemLayer.removeAllViews();
        setAddView();
        for (int i = 0; i < mVoteActivity.mVoteItems.size(); i++) {
            initVoteItemView(mVoteActivity.mVoteItems.get(i), i);
        }
    }

    public void deleteitem() {
        if (mVoteActivity.isdelete == false) {
            mVoteActivity.isdelete = true;
            for (int i = 2; i < mVoteActivity.itemLayer.getChildCount(); i++) {
                View mview = mVoteActivity.itemLayer.getChildAt(i);
                if (i != mVoteActivity.itemLayer.getChildCount() - 1) {
                    ImageView image = (ImageView) mview.findViewById(R.id.image_cion);
                    image.setImageResource(R.drawable.voteitemdelete);
                    image.setOnClickListener(mVoteActivity.mdodelItemListener);
                } else {
                    ImageView image = (ImageView) mview.findViewById(R.id.image_ciondelete);
                    image.setImageResource(R.drawable.icon_scx);
                }
            }
        } else {
            mVoteActivity.isdelete = false;
            for (int i = 2; i < mVoteActivity.itemLayer.getChildCount(); i++) {
                View mview = mVoteActivity.itemLayer.getChildAt(i);
                if (i != mVoteActivity.itemLayer.getChildCount() - 1) {
                    ImageView image = (ImageView) mview.findViewById(R.id.image_cion);
                    image.setImageResource(R.drawable.crm_new_pic_icon);
                    image.setOnClickListener(mVoteActivity.mShowAddListener);
                } else {
                    ImageView image = (ImageView) mview.findViewById(R.id.image_ciondelete);
                    image.setImageResource(R.drawable.icon_qxsc);
                }
            }
        }

    }

    public void showAdd(View view) {

        mVoteActivity.mSelectItemModel = (VoteSelect) view.getTag();
        if (mVoteActivity.mSelectItemModel.mPics.size() >= VoteManager.MAX_PIC_COUNT) {
            AppUtils.showMessage(mVoteActivity, mVoteActivity.getString(R.string.xml_vote_max_pic));
        } else {

            ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
            MenuItem mMenuItem = new MenuItem();
            mMenuItem.btnName = mVoteActivity.getString(R.string.button_word_takephoto);
            mMenuItem.mListener = mVoteActivity.mTakePhotoListenter;
            mMenuItem.item = view.getTag();
            menuItems.add(mMenuItem);
            mMenuItem = new MenuItem();
            mMenuItem.btnName = mVoteActivity.getString(R.string.button_word_album);
            mMenuItem.mListener = mVoteActivity.mAddPicListener;
            mMenuItem.item = view.getTag();
            menuItems.add(mMenuItem);
            mVoteActivity.popupWindow1 = AppUtils.creatButtomMenu(mVoteActivity, mVoteActivity.mRelativeLayout, menuItems, mVoteActivity.findViewById(R.id.detial));
        }

    }

    public void addPic(View v) {
        mVoteActivity.popupWindow1.dismiss();
        mVoteActivity.mSelectItemModel = (VoteSelect) v.getTag();
        Bus.callData(mVoteActivity,"filetools/getPhotos",false,VoteManager.MAX_PIC_COUNT,"intersky.vote.view.activity.VoteActivity",VoteManager.ACTION_SET_PIC);
    }


    public void takePhoto(View v) {
        mVoteActivity.popupWindow1.dismiss();
        mVoteActivity.mSelectItemModel = (VoteSelect) v.getTag();
        AppUtils.showMessage(mVoteActivity, mVoteActivity.getString(R.string.keyword_photoaddmax1)
                + String.valueOf(VoteManager.MAX_PIC_COUNT) + mVoteActivity.getString(R.string.keyword_photoaddmax2));
    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mVoteActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),"","",mFile.length(),mFile.length(),"");
                    mVoteActivity.mSelectItemModel.mPics.add(attachment);
                    addPicView(mVoteActivity.mSelectItemModel, attachment);
                }
                break;
        }
    }

    public void addpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        mVoteActivity.mSelectItemModel.mPics.addAll(attachments);
        addPicViews(attachments,mVoteActivity.mSelectItemModel);
    }

    public void initPicView(VoteSelect mSelectItemModel) {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = null;
        MyLinearLayout mImageLayer = (MyLinearLayout) mSelectItemModel.mView.findViewById(R.id.image_content);
        mImageLayer.removeAllViews();
        for (int i = 0; i < mSelectItemModel.mPics.size(); i++) {
            File mfile = new File(mSelectItemModel.mPics.get(i).mPath);
            mView = mInflater.inflate(R.layout.fujian_image40, null);
            ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            Glide.with(mVoteActivity).load(mfile).apply(options).into(new MySimpleTarget(mImageView));
            mView.setTag(mSelectItemModel.mPics.get(i));
            mView.setOnClickListener(mVoteActivity.mShowPicListener);
            mImageLayer.setVisibility(View.VISIBLE);
            mImageLayer.addView(mView);
        }
    }


    public void addPicView(VoteSelect mSelectItemModel, Attachment mPic) {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = null;
        File mfile = new File(mPic.mPath);
        mView = mInflater.inflate(R.layout.fujian_image40, null);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.plugin_camera_no_pictures);
        Glide.with(mVoteActivity).load(mfile).apply(options).into(new MySimpleTarget(mImageView));
        mView.setTag(mPic);
        mView.setOnClickListener(mVoteActivity.mShowPicListener);
        MyLinearLayout mImageLayer = (MyLinearLayout) mSelectItemModel.mView.findViewById(R.id.image_content);
        mImageLayer.setVisibility(View.VISIBLE);
        mImageLayer.addView(mView);
    }

    public void addPicViews(ArrayList<Attachment> mAttachmentModels, VoteSelect mSelectItemModel) {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = null;
        int position = mVoteActivity.mVoteItems.indexOf(mSelectItemModel);
        for (int i = 0; i < mAttachmentModels.size(); i++) {
            File mfile = new File(mAttachmentModels.get(i).mPath);
            mView = mInflater.inflate(R.layout.fujian_image40, null);
            ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            Glide.with(mVoteActivity).load(mfile).apply(options).into(new MySimpleTarget(mImageView));
            mView.setTag(mAttachmentModels.get(i));
            mView.setOnClickListener(mVoteActivity.mShowPicListener);
            MyLinearLayout mImageLayer = (MyLinearLayout) mSelectItemModel.mView.findViewById(R.id.image_content);
            mImageLayer.setVisibility(View.VISIBLE);
            mImageLayer.addView(mView);
        }
    }

    public void showPic(View v) {
        Bus.callData(mVoteActivity,"filetools/showSeriasPic",mVoteActivity.mSelectItemModel.mPics, (Attachment) v.getTag(),true,true,VoteManager.ACTION_DELETE_PIC);
    }

    public void doSave() {


        if (mVoteActivity.isuploading) {
            mVoteActivity.waitDialog.show();
        } else {
            if (mVoteActivity.content.getText().toString().length() == 0) {
                AppUtils.showMessage(mVoteActivity, "投票主题不能为空");
                return;
            }

            if (mVoteActivity.mVoteItems.size() > 0) {
                boolean has = false;
                for (int i = 0; i < mVoteActivity.mVoteItems.size(); i++) {
                    if (mVoteActivity.mVoteItems.get(i).name.length() == 0) {
                        AppUtils.showMessage(mVoteActivity, "第" + String.valueOf(i + 1) + "项项名必须输入");
                        return;
                    }


                    if (mVoteActivity.mVoteItems.get(i).mPics.size() > 0) {
                        has = true;
                    }
                }
                if (mVoteActivity.mVoters.size() == 0) {
                    AppUtils.showMessage(mVoteActivity, "请选择参与者");
                    return;
                }
                if (mVoteActivity.issub == false) {
                    mVoteActivity.issub = true;
                    ArrayList<Attachment> attachments = new ArrayList<Attachment>();
                    for(int i = 0 ; i < mVoteActivity.mVoteItems.size() ; i++) {
                        for(int j = 0 ; j < mVoteActivity.mVoteItems.get(i).mPics.size() ; j++) {
                            attachments.add(mVoteActivity.mVoteItems.get(i).mPics.get(j));
                        }
                    }
                    NetObject netObject = (NetObject) Bus.callData(mVoteActivity,"filetools/getUploadFiles",attachments);
                    if(((ArrayList<File>) netObject.item).size() > 0)
                    {
                        VoteManager.getInstance().oaUtils.uploadAttchments((ArrayList<File>) netObject.item,mVoteHandler,netObject.result);
                    }
                    else
                    {
                        saveVote(netObject.result);
                    }

                } else {
                    mVoteActivity.waitDialog.show();
                }
            }
        }

    }

    public void saveVote(String attach) {
        try {
            String[] attacha = attach.split(",");
            JSONArray items = new JSONArray();
            int count = 0;
            for (int i = 0; i < mVoteActivity.mVoteItems.size(); i++) {
                JSONObject item = new JSONObject();
                item.put("item_name", mVoteActivity.mVoteItems.get(i).name);
                String hash = "";
                for (int j = 0; j < mVoteActivity.mVoteItems.get(i).mPics.size(); j++) {
                    if (hash.length() == 0) {
                        hash += attacha[count];
                    } else {
                        hash += "," + attacha[count];
                    }
                    count++;
                }
                if (hash.length() > 0) ;
                item.put("attence", hash);
                items.put(item);
            }
            mVoteActivity.vote.selectJson = items.toString();
            if (mVoteActivity.mSwitch.isChecked())
                mVoteActivity.vote.is_incognito = 1;
            else
                mVoteActivity.vote.is_incognito = 0;
            mVoteActivity.vote.mContent = mVoteActivity.content.getText().toString();
            mVoteActivity.vote.endTime = mVoteActivity.time.getText().toString()+":00";
            mVoteActivity.vote.voterid = (String) Bus.callData(mVoteActivity,"chat/getMemberIds",mVoteActivity.mVoters);
            mVoteActivity.waitDialog.show();
            VoteAsks.saveVote(mVoteActivity,mVoteHandler,mVoteActivity.vote);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void deletePic(Intent intent)
    {
        MyLinearLayout mImageLayer = (MyLinearLayout) mVoteActivity.mSelectItemModel.mView.findViewById(R.id.image_content);
        View mview = mImageLayer.getChildAt(intent.getIntExtra("index",0));
        Attachment mAttachmentModel = (Attachment) mview.getTag();
        mImageLayer.removeView(mview);
        mVoteActivity.mSelectItemModel.mPics.remove(mAttachmentModel);
    }

    public void setSender(Intent intent) {
        mVoteActivity.mVoters.clear();
        mVoteActivity.mVoters.addAll((ArrayList<Contacts>)Bus.callData(mVoteActivity,"chat/mselectitems",""));
        String voter = "";
        for(int i = 0 ; i < mVoteActivity.mVoters.size() ; i++)
        {
            if(voter.length() == 0)
            {
                voter += mVoteActivity.mVoters.get(i).mName;
            }
            else
            {
                voter += ","+mVoteActivity.mVoters.get(i).mName;
            }
        }
        mVoteActivity.voter.setText(voter);
    }
}
