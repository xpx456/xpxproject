package intersky.vote.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import intersky.appbase.PermissionResult;
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
        ImageView back = mVoteActivity.findViewById(R.id.back);
        back.setOnClickListener(mBackListener);

        mVoteActivity.vote = mVoteActivity.getIntent().getParcelableExtra("vote");
        mVoteActivity.timeLayer = (RelativeLayout) mVoteActivity.findViewById(R.id.timelayer);
        mVoteActivity.content = (EditText) mVoteActivity.findViewById(R.id.answer_text);
        mVoteActivity.time = (TextView) mVoteActivity.findViewById(R.id.time_content);
        mVoteActivity.formLayer = (RelativeLayout) mVoteActivity.findViewById(R.id.vote_formlayer);
        mVoteActivity.formLayer2 = (RelativeLayout) mVoteActivity.findViewById(R.id.noname);
        mVoteActivity.voterLayer = (RelativeLayout) mVoteActivity.findViewById(R.id.voterlayer);
        mVoteActivity.btnpublish = (RelativeLayout) mVoteActivity.findViewById(R.id.buttombtn);
        mVoteActivity.mRelativeLayout = (RelativeLayout) mVoteActivity.findViewById(R.id.shade);
        mVoteActivity.form = (TextView) mVoteActivity.findViewById(R.id.vote_form_content);
        mVoteActivity.voter = (MyLinearLayout) mVoteActivity.findViewById(R.id.sender);
        mVoteActivity.itemLayer = (LinearLayout) mVoteActivity.findViewById(R.id.item_content);
        mVoteActivity.mSwitch = (TextView) mVoteActivity.findViewById(R.id.v_content);
        mVoteActivity.time.setText(TimeUtils.getDateAndTimeex(7));
        mVoteActivity.timeLayer.setOnClickListener(mVoteActivity.setDateTime);
        mVoteActivity.formLayer.setOnClickListener(mVoteActivity.setFrorm);
        mVoteActivity.formLayer2.setOnClickListener(mVoteActivity.setFrorm2);
        mVoteActivity.voterLayer.setOnClickListener(mVoteActivity.setVoter);
        mVoteActivity.btnpublish.setOnClickListener(mVoteActivity.mSave);
        initVotetype();
        initVoteItem();

        Bus.callData(mVoteActivity, "chat/getContacts", mVoteActivity.vote.voterid, mVoteActivity.mVoters);
        initselectView(mVoteActivity.mVoters, mVoteActivity.voter, mVoteActivity.setVoter, mVoteActivity.mDeleteSenderListener);
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
        if (VoteManager.getInstance().mVoteTypes2.size() == 0) {
            VoteManager.getInstance().mVoteTypes2.add(new Select("0", mVoteActivity.getString(R.string.xml_vote_hasname)));
            VoteManager.getInstance().mVoteTypes2.add(new Select("1", mVoteActivity.getString(R.string.xml_vote_noname)));
        }
        mVoteActivity.mselectVoteType = VoteManager.getInstance().mVoteTypes.get(0);
        VoteManager.getInstance().mVoteTypes.get(0).iselect = true;
        mVoteActivity.form.setText(mVoteActivity.mselectVoteType.mName);

        mVoteActivity.mselectVoteType2 = VoteManager.getInstance().mVoteTypes2.get(0);
        VoteManager.getInstance().mVoteTypes2.get(0).iselect = true;
        mVoteActivity.mSwitch.setText(mVoteActivity.mselectVoteType2.mName);
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

    public void doForm2() {

        SelectManager.getInstance().startSelectView(mVoteActivity, VoteManager.getInstance().mVoteTypes2, mVoteActivity.getString(R.string.xml_vote_name),
                VoteManager.ACTION_SET_VOTE_TYPE2, true, false);
    }

    public void setVotetype() {
        mVoteActivity.mselectVoteType = SelectManager.getInstance().mSignal;
        mVoteActivity.form.setText(mVoteActivity.mselectVoteType.mName);
    }

    public void setVotetype2() {
        mVoteActivity.mselectVoteType2 = SelectManager.getInstance().mSignal;
        mVoteActivity.mSwitch.setText(mVoteActivity.mselectVoteType2.mName);
    }

    public void selectVoter() {
        Bus.callData(mVoteActivity,"chat/selectListContact", mVoteActivity.mVoters, VoteManager.ACTION_SET_VOTER, mVoteActivity.getString(R.string.select_contact), false);
    }

    public void setVoter() {
        mVoteActivity.mVoters.clear();
        mVoteActivity.mVoters.addAll((ArrayList<Contacts>)Bus.callData(mVoteActivity,"chat/mselectitems", ""));
        initselectView(mVoteActivity.mVoters, mVoteActivity.voter, mVoteActivity.setVoter, mVoteActivity.mDeleteSenderListener);

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
        TextView itemid = (TextView) mSelectItemModel.mView.findViewById(R.id.item_id);
        TextView title = (TextView) mSelectItemModel.mView.findViewById(R.id.item_content);
        ImageView addimage = (ImageView) mSelectItemModel.mView.findViewById(R.id.image_cion);
        ImageView delete = mSelectItemModel.mView.findViewById(R.id.image_delete);
        delete.setTag(mSelectItemModel);
        addimage.setImageResource(R.drawable.crm_new_pic_icon);
        EditText mEditText = (EditText) mSelectItemModel.mView.findViewById(R.id.item_content);
        mEditText.addTextChangedListener(new MyTextWatcher(mSelectItemModel));
        itemid.setText(String.valueOf(id + 1));
        addimage.setTag(mSelectItemModel);
        addimage.setOnClickListener(mVoteActivity.mShowAddListener);
        delete.setOnClickListener(mVoteActivity.mdodelItemListener);
        title.setText(mSelectItemModel.name);
        if (mSelectItemModel.mPics.size() > 0) {

            initPicView(mSelectItemModel);
        }
        if(id == 0)
        {
            delete.setVisibility(View.INVISIBLE);
        }
        else if(id == 1)
        {
            View mView2 = mVoteActivity.itemLayer.getChildAt(0);
            ImageView delete2 = mView2.findViewById(R.id.image_delete);
            delete2.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
        else
        {
            delete.setVisibility(View.VISIBLE);
        }
        mVoteActivity.itemLayer.addView(mSelectItemModel.mView, mVoteActivity.itemLayer.getChildCount() - 1);
    }

    public void setAddView() {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mview = mInflater.inflate(R.layout.vote_add_item, null);
        mview.setOnClickListener(mVoteActivity.maddItemListener);
        mVoteActivity.itemLayer.addView(mview);
    }

    public void additem() {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        VoteSelect temp = new VoteSelect("", mInflater.inflate(R.layout.vote_select_item, null));
        mVoteActivity.mVoteItems.add(temp);
        initVoteItemView(temp, mVoteActivity.mVoteItems.size() - 1);
    }

    public void dodelete(View v) {

        VoteSelect voteSelect = (VoteSelect) v.getTag();
        int a = mVoteActivity.itemLayer.indexOfChild(voteSelect.mView);
        mVoteActivity.itemLayer.removeView(voteSelect.mView);
        mVoteActivity.mVoteItems.remove(a-1);
        if(mVoteActivity.itemLayer.getChildCount() == 2)
        {
            View view1 = mVoteActivity.itemLayer.getChildAt(0);
            ImageView delete = view1.findViewById(R.id.image_delete);
            delete.setVisibility(View.INVISIBLE);
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
        mVoteActivity.permissionRepuest = (PermissionResult) Bus.callData(mVoteActivity, "filetools/checkPermissionTakePhoto"
                ,mVoteActivity, Bus.callData(mVoteActivity, "filetools/getfilePath", "vote/photo"));
    }


    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mVoteActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    Attachment attachment = new Attachment("",mFile.getName(),mFile.getPath(),"",mFile.length(),mFile.length(),"");
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
                AppUtils.showMessage(mVoteActivity, mVoteActivity.getString(R.string.vote_title_empty));
                return;
            }

            if (mVoteActivity.mVoteItems.size() > 0) {
                boolean has = false;
                for (int i = 0; i < mVoteActivity.mVoteItems.size(); i++) {
                    if (mVoteActivity.mVoteItems.get(i).name.length() == 0) {
                        AppUtils.showMessage(mVoteActivity, mVoteActivity.getString(R.string.select_no)+ String.valueOf(i + 1) + mVoteActivity.getString(R.string.select_name_empty));
                        return;
                    }


                    if (mVoteActivity.mVoteItems.get(i).mPics.size() > 0) {
                        has = true;
                    }
                }
                if (mVoteActivity.mVoters.size() == 0) {
                    AppUtils.showMessage(mVoteActivity, mVoteActivity.getString(R.string.select_join));
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
            mVoteActivity.vote.type = Integer.valueOf(mVoteActivity.mselectVoteType.mId);
            mVoteActivity.vote.is_incognito = Integer.valueOf(mVoteActivity.mselectVoteType2.mId);
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
        initselectView(mVoteActivity.mVoters, mVoteActivity.voter, mVoteActivity.setVoter, mVoteActivity.mDeleteSenderListener);
    }

    public void initselectView(ArrayList<Contacts> mselectitems, MyLinearLayout mlayer, View.OnClickListener senderListener, View.OnClickListener itemListener) {
        LayoutInflater mInflater = (LayoutInflater) mVoteActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayer.removeAllViews();
        if (mselectitems.size() > 0) {
            for (int i = 0; i < mselectitems.size(); i++) {
                Contacts mContact = mselectitems.get(i);
                View mview = mInflater.inflate(R.layout.sample_contact_item_ex, null);
                TextView mhead = (TextView) mview.findViewById(R.id.contact_img);
                ImageView delete = mview.findViewById(R.id.delete);
                AppUtils.setContactCycleHead(mhead,mContact.getName());
                TextView name = (TextView) mview.findViewById(R.id.title);
                name.setText(mContact.getName());
                delete.setTag(mContact);
                delete.setOnClickListener(itemListener);
                mlayer.addView(mview);
            }

        }
        View mview = mInflater.inflate(R.layout.sample_contact_item_add, null);
        mview.setOnClickListener(senderListener);
        mlayer.addView(mview);

    }

    public void deleteSender(View v) {
        Contacts send = (Contacts) v.getTag();
        mVoteActivity.voter.removeView(v);
        mVoteActivity.mVoters.remove(send);
    }

    public void askFinish()
    {
        AppUtils.creatDialogTowButton(mVoteActivity, mVoteActivity.getString(R.string.save_ask),
                mVoteActivity.getString(R.string.save_ask_title),mVoteActivity.getString(R.string.button_word_cancle)
                ,mVoteActivity.getString(R.string.button_word_ok),null,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mVoteActivity.finish();
                    }
                });
    }

    public void chekcBack() {
        if (mVoteActivity.content.getText().toString().length() > 0) {
            askFinish();
            return;
        }

        if (mVoteActivity.mVoteItems.size() > 0) {
            boolean has = false;
            for (int i = 0; i < mVoteActivity.mVoteItems.size(); i++) {
                if (mVoteActivity.mVoteItems.get(i).name.length() > 0) {
                    askFinish();
                    return;
                }


                if (mVoteActivity.mVoteItems.get(i).mPics.size() > 0) {
                    askFinish();
                    return;
                }
            }
            if (mVoteActivity.mVoters.size() == 0) {
                askFinish();
                return;
            }
        }
        mVoteActivity.finish();
    }

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            chekcBack();
        }
    };

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        if (e1.getX() - e2.getX() > mVoteActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mVoteActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {
            return false;
        }
        else if (e2.getX() - e1.getX() > mVoteActivity.mBasePresenter.mScreenDefine.verticalMinDistance*mVoteActivity.mBasePresenter.mScreenDefine.density && Math.abs(velocityX) > 0)
        {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mVoteActivity.flagFillBack == true)
            {
                mVoteActivity.isdestory = true;
                chekcBack();
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }
}
