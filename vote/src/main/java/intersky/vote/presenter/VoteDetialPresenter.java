package intersky.vote.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.WebEdit;
import intersky.oa.OaAsks;
import intersky.oa.OaUtils;
import intersky.vote.R;
import intersky.vote.VoteManager;
import intersky.vote.asks.VoteAsks;
import intersky.vote.entity.ShowPicItem;
import intersky.vote.entity.VoteSelect;
import intersky.vote.handler.VoteDetialHandler;
import intersky.vote.receicer.VoteDetialReceiver;
import intersky.vote.view.activity.VoteDetialActivity;
import intersky.vote.view.activity.VoteRecordActivity;
import xpx.com.toolbar.utils.ToolBarHelper;


public class VoteDetialPresenter implements Presenter {

    public VoteDetialActivity mVoteDetialActivity;
    public VoteDetialHandler mVoteDetialHandler;

    public VoteDetialPresenter(VoteDetialActivity mVoteDetialActivity) {
        this.mVoteDetialActivity = mVoteDetialActivity;
        mVoteDetialHandler = new VoteDetialHandler(mVoteDetialActivity);
        mVoteDetialActivity.setBaseReceiver(new VoteDetialReceiver(mVoteDetialHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mVoteDetialActivity.setContentView(R.layout.activity_vote_detial);
        ImageView back = mVoteDetialActivity.findViewById(R.id.back);
        back.setOnClickListener(mVoteDetialActivity.mBackListener);
        mVoteDetialActivity.more = mVoteDetialActivity.findViewById(R.id.more);
        mVoteDetialActivity.more.setOnClickListener(mVoteDetialActivity.mMoreListenter);
        mVoteDetialActivity.mAnsallLayer = (RelativeLayout) mVoteDetialActivity.findViewById(R.id.answer);
        mVoteDetialActivity.mVoteItemlayer = (LinearLayout) mVoteDetialActivity.findViewById(R.id.itemlayer);
        mVoteDetialActivity.mAnswerlayer = (LinearLayout) mVoteDetialActivity.findViewById(R.id.answeritem);
        mVoteDetialActivity.mAnswer = (TextView) mVoteDetialActivity.findViewById(R.id.answertitle);
        mVoteDetialActivity.mRelativeLayout = (RelativeLayout) mVoteDetialActivity.findViewById(R.id.shade);
        mVoteDetialActivity.mTypeText = (TextView) mVoteDetialActivity.findViewById(R.id.type);
        mVoteDetialActivity.mContent = (TextView) mVoteDetialActivity.findViewById(R.id.vote_content);
        mVoteDetialActivity.head = mVoteDetialActivity.findViewById(R.id.contact_img);
        mVoteDetialActivity.mTime = (TextView) mVoteDetialActivity.findViewById(R.id.vote_dete);
        mVoteDetialActivity.mSubmit = (TextView) mVoteDetialActivity.findViewById(R.id.creat);
        mVoteDetialActivity.mNameType = (TextView) mVoteDetialActivity.findViewById(R.id.vote_name_type);
        mVoteDetialActivity.mEditTextContent = (EditText) mVoteDetialActivity.findViewById(R.id.et_sendmessage);
        mVoteDetialActivity.vote = mVoteDetialActivity.getIntent().getParcelableExtra("vote");
        mVoteDetialActivity.mEditTextContent.setOnEditorActionListener(mVoteDetialActivity.sendtext);
        VoteAsks.getDetial(mVoteDetialActivity,mVoteDetialHandler,mVoteDetialActivity.vote);
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

    public void Destroy() {
        // TODO Auto-generated method stub
        mVoteDetialHandler = null;
    }

    public void showEdit() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        if (mVoteDetialActivity.vote.is_close == 1) {
            MenuItem item = new MenuItem();
            item.btnName = mVoteDetialActivity.getString(R.string.xml_vote_record);
            item.mListener = mVoteDetialActivity.mRecordListenter;
            menuItems.add(item);
            item = new MenuItem();
            item.btnName = mVoteDetialActivity.getString(R.string.button_delete);
            item.mListener = mVoteDetialActivity.mDeleteListenter;
            menuItems.add(item);

        } else {
            if (mVoteDetialActivity.vote.visiable == true && mVoteDetialActivity.vote.is_incognito == 0)
            {
                MenuItem item = new MenuItem();
                item.btnName = mVoteDetialActivity.getString(R.string.button_delete);
                item.mListener = mVoteDetialActivity.mDeleteListenter;
                menuItems.add(item);
                item = new MenuItem();
                item.btnName = mVoteDetialActivity.getString(R.string.xml_vote_close);
                item.mListener = mVoteDetialActivity.mCloseListenter;
                menuItems.add(item);
                item = new MenuItem();
                item.btnName = mVoteDetialActivity.getString(R.string.xml_vote_record);
                item.mListener = mVoteDetialActivity.mRecordListenter;
                menuItems.add(item);
            }
            else
            {
                MenuItem item = new MenuItem();
                item.btnName = mVoteDetialActivity.getString(R.string.button_delete);
                item.mListener = mVoteDetialActivity.mDeleteListenter;
                menuItems.add(item);
                item = new MenuItem();
                item.btnName = mVoteDetialActivity.getString(R.string.xml_vote_close);
                item.mListener = mVoteDetialActivity.mCloseListenter;
                menuItems.add(item);
            }
        }
        mVoteDetialActivity.popupWindow1 = AppUtils.creatButtomMenu(mVoteDetialActivity,mVoteDetialActivity.mRelativeLayout,menuItems,mVoteDetialActivity.findViewById(R.id.activity_about));
    }

    public void doDelete() {
      VoteAsks.deleteVote(mVoteDetialActivity,mVoteDetialHandler,mVoteDetialActivity.vote);
    }


    public void doCloseVote() {
        VoteAsks.closeVote(mVoteDetialActivity,mVoteDetialHandler,mVoteDetialActivity.vote);
    }

    private void deleteData() {
        AppUtils.creatDialogTowButton(mVoteDetialActivity,mVoteDetialActivity.getString(R.string.xml_vote_delete_msg),""
                ,mVoteDetialActivity.getString(R.string.button_word_cancle),mVoteDetialActivity.getString(R.string.button_word_ok),
                null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        doDelete();
                    }
                });

    }

    public void showRecord() {
        Intent intent = new Intent(mVoteDetialActivity, VoteRecordActivity.class);
        intent.putExtra("vote", mVoteDetialActivity.vote);
        mVoteDetialActivity.startActivity(intent);
        mVoteDetialActivity.popupWindow1.dismiss();
    }

    public void onClose() {
        doCloseVote();
        mVoteDetialActivity.popupWindow1.dismiss();
    }

    public void onDelete() {
        deleteData();
        mVoteDetialActivity.popupWindow1.dismiss();
    }


    public void showPic(View v) {
        ShowPicItem mShowPicItemModel = (ShowPicItem) v.getTag();
        Bus.callData(mVoteDetialActivity,"filetools/showSeriasPic",mShowPicItemModel.mAttachments,mShowPicItemModel.mAttachment,true,false,VoteManager.ACTION_DELETE_PIC);
    }


    public void getid(String ids) {
        String[] strs = ids.split(",");
        for (int i = 0; i < strs.length; i++) {
            mVoteDetialActivity.mSelectItemModels.get(Integer.valueOf(strs[i]) - 1).count++;
        }
    }

    public void praseDetial() {
        try {
            LayoutInflater mInflater = (LayoutInflater) mVoteDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            AppUtils.setContactCycleHead(mVoteDetialActivity.head,mVoteDetialActivity.vote.userName);
            mVoteDetialActivity.mTime.setText(mVoteDetialActivity.vote.creatTime.substring(5, 16) +
                    mVoteDetialActivity.getString(R.string.xml_vote_between) + mVoteDetialActivity.vote.endTime.substring(5, 16));
            mVoteDetialActivity.mTime.setText(mVoteDetialActivity.mTime.getText().toString().replaceAll("-", "/"));
            String type = mVoteDetialActivity.getString(R.string.xml_vote_1);
            if (mVoteDetialActivity.vote.type == 1) {
                type = mVoteDetialActivity.getString(R.string.xml_vote_2);
            }
            mVoteDetialActivity.mContent.setText(mVoteDetialActivity.vote.mContent + "(" + type + ")");
            if (TimeUtils.measureBefore(TimeUtils.getDateAndTime(), mVoteDetialActivity.vote.endTime) == false) {

            }
            if(mVoteDetialActivity.vote.is_incognito == 0)
            {

                String value = mVoteDetialActivity.getString(R.string.xml_vote_name_type_yes)
                        + String.valueOf(mVoteDetialActivity.vote.personCount)+mVoteDetialActivity.getString(R.string.unit1);
                mVoteDetialActivity.mNameType.setText(AppUtils.highlight(value,String.valueOf(mVoteDetialActivity.vote.totalCount),
                        Color.parseColor("#3370FF")));
            }
            else
            {
                String value = mVoteDetialActivity.getString(R.string.xml_vote_name_type_no)
                        + String.valueOf(mVoteDetialActivity.vote.personCount)+mVoteDetialActivity.getString(R.string.unit1);
                mVoteDetialActivity.mNameType.setText(AppUtils.highlight(value,String.valueOf(mVoteDetialActivity.vote.totalCount),
                        Color.parseColor("#3370FF")));
            }

            if(mVoteDetialActivity.vote.is_close == 1  )
            {
                if(mVoteDetialActivity.vote.is_incognito == 0 && VoteManager.getInstance().oaUtils.mAccount.mRecordId.equals(mVoteDetialActivity.vote.userid))
                    mVoteDetialActivity.more.setVisibility(View.VISIBLE);
            }
            else if(VoteManager.getInstance().oaUtils.mAccount.mRecordId.equals(mVoteDetialActivity.vote.userid))
            {
                mVoteDetialActivity.more.setVisibility(View.VISIBLE);
            }
            else
                mVoteDetialActivity.more.setVisibility(View.INVISIBLE);

            if (mVoteDetialActivity.vote.myvoteid.equals("0") && mVoteDetialActivity.vote.is_close == 0) {

                RelativeLayout sendlayer = (RelativeLayout) mVoteDetialActivity.findViewById(R.id.sendlayer);
                sendlayer.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVoteDetialActivity.mAnsallLayer.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.sendlayer);
                mVoteDetialActivity.mAnsallLayer.setLayoutParams(params);
                sendlayer.setOnClickListener(mVoteDetialActivity.mSendListener);
            } else {
                RelativeLayout sendlayer = (RelativeLayout) mVoteDetialActivity.findViewById(R.id.sendlayer);
                sendlayer.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVoteDetialActivity.mAnsallLayer.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.itemlayer);
                mVoteDetialActivity.mAnsallLayer.setLayoutParams(params);
            }
            mVoteDetialActivity.mVoteItemlayer.removeAllViews();
            mVoteDetialActivity.mSelectItemModels.clear();
            String votes[] = mVoteDetialActivity.vote.myvoteid.split(",");
            if (mVoteDetialActivity.vote.selectJson.length() > 0) {
                JSONArray ja = new JSONArray(mVoteDetialActivity.vote.selectJson);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject item = ja.getJSONObject(i);
                    VoteSelect temp = new VoteSelect(item.getString("item_name"), mInflater.inflate(R.layout.vote_select_item_d, null));
                    temp.hash = item.getString("attence");
                    temp.count = item.getInt("total");
                    temp.selectid = item.getString("vote_item_id");
                    temp.hash = item.getString("attence");
                    if(mVoteDetialActivity.vote.myvoteid.length() > 0)
                    {
                        for(int j =0 ; j <votes.length ; j++)
                        {
                            if (temp.selectid.equals(votes[j])) {
                                temp.iselect = true;
                                break;
                            }
                        }
                    }
                    mVoteDetialActivity.vote.totalCount += item.getInt("total");
                    mVoteDetialActivity.mSelectItemModels.add(temp);
                    initVoteItemView(temp, i);
                }
            }

            if (mVoteDetialActivity.vote.is_close == 0) {
                if(mVoteDetialActivity.vote.visiable)
                {
                    String value = mVoteDetialActivity.getString(R.string.xml_vote_doing) + ","
                            + mVoteDetialActivity.getString(R.string.xml_vote_total) + String.valueOf(mVoteDetialActivity.vote.totalCount)
                            + mVoteDetialActivity.getString(R.string.xml_vote_tic);
                    mVoteDetialActivity.mTypeText.setText(AppUtils.highlight(value,String.valueOf(mVoteDetialActivity.vote.totalCount),
                            Color.parseColor("#3370FF")));
                }
                else
                    mVoteDetialActivity.mTypeText.setText(mVoteDetialActivity.getString(R.string.xml_vote_doing));
            } else {
                String value = mVoteDetialActivity.getString(R.string.xml_vote_closed) + ","
                        + mVoteDetialActivity.getString(R.string.xml_vote_total) + String.valueOf(mVoteDetialActivity.vote.totalCount)
                        + mVoteDetialActivity.getString(R.string.xml_vote_tic);
                mVoteDetialActivity.mTypeText.setText(AppUtils.highlight(value,String.valueOf(mVoteDetialActivity.vote.totalCount),
                        Color.parseColor("#3370FF")));
            }

            if(mVoteDetialActivity.vote.replyJson.length() > 0)
            {
                XpxJSONArray ja2 = new XpxJSONArray(mVoteDetialActivity.vote.replyJson);
                mVoteDetialActivity.mReplys.clear();
                mVoteDetialActivity.mAnswerlayer.removeAllViews();
                for (int i = 0; i < ja2.length(); i++) {
                    XpxJSONObject jo2 = ja2.getJSONObject(i);
                    Reply mReplyModel = new Reply(jo2.getString("vote_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), mVoteDetialActivity.vote.voteid, jo2.getString("create_time"));
                    mVoteDetialActivity.mReplys.add(mReplyModel);
                }
            }
            ReplyUtils.praseReplyViews(mVoteDetialActivity.mReplys,mVoteDetialActivity,mVoteDetialActivity.mAnswer
                    ,mVoteDetialActivity.mAnswerlayer,mVoteDetialActivity.mDeleteReplyListenter,mVoteDetialHandler);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initVoteItemView(VoteSelect mSelectItemModel, int id) {
        View mView = mSelectItemModel.mView;
        mView.setTag(mSelectItemModel);
        if (mVoteDetialActivity.vote.myvoteid.length() == 0)
        {
            mView.setOnClickListener(mVoteDetialActivity.mOnItemListenter);
        }
        else if(mVoteDetialActivity.vote.myvoteid.equals("0"))
        {
            mView.setOnClickListener(mVoteDetialActivity.mOnItemListenter);
        }
        TextView item_id = (TextView) mView.findViewById(R.id.item_id);
        TextView item_count = (TextView) mView.findViewById(R.id.item_count);
        TextView title = (TextView) mView.findViewById(R.id.item_content);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.image_cion);
        ProgressBar roundProgressBar = (ProgressBar) mView.findViewById(R.id.roundProgressBar);
        item_id.setText(String.valueOf(id+1)+"、");
        item_count.setText(String.valueOf(mSelectItemModel.count) + mVoteDetialActivity.getString(R.string.xml_vote_tic));
        title.setText(mSelectItemModel.name);
        if (mSelectItemModel.iselect) {
            mImageView.setImageResource(R.drawable.checkbox_checked);
        } else {
            mImageView.setImageResource(R.drawable.bunselect);
        }
        roundProgressBar.setMax(mVoteDetialActivity.vote.personCount);
        roundProgressBar.setProgress(mSelectItemModel.count);
        if(mVoteDetialActivity.vote.visiable == true) {
            roundProgressBar.setVisibility(View.VISIBLE);
            item_count.setVisibility(View.VISIBLE);
        } else {
            roundProgressBar.setVisibility(View.GONE);
            item_count.setVisibility(View.INVISIBLE);
        }
        if (mSelectItemModel.hash.length() > 0) {
            MyLinearLayout imagelayer = (MyLinearLayout) mView.findViewById(R.id.image_content);
            imagelayer.setVisibility(View.VISIBLE);
            OaAsks.getAttachments(mSelectItemModel.hash,mVoteDetialHandler,mVoteDetialActivity,mSelectItemModel);
        }
        mVoteDetialActivity.mVoteItemlayer.addView(mSelectItemModel.mView);
    }


    public void addPicView(VoteSelect mSelectItemModel) {
        LayoutInflater mInflater = (LayoutInflater) mVoteDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int position = mVoteDetialActivity.mSelectItemModels.indexOf(mSelectItemModel);
        View mView = null;
        for (int i = 0; i < mSelectItemModel.mPics.size(); i++) {
            File mfile = new File(mSelectItemModel.mPics.get(i).mPath);
            mView = mInflater.inflate(R.layout.fujian_image40, null);
            ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
            if ((int)Bus.callData(mVoteDetialActivity,"filetools/getFileType",mSelectItemModel.mPics.get(i).mName)== Actions.FILE_TYPE_PICTURE) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.plugin_camera_no_pictures);
                Glide.with(mVoteDetialActivity).load(VoteManager.getInstance().oaUtils.praseClodAttchmentUrl(mSelectItemModel.mPics.get(i).mRecordid, (int) (40 * mVoteDetialActivity.mBasePresenter.mScreenDefine.density))).apply(options).into(new MySimpleTarget(mImageView));
            }
            mView.setTag(new ShowPicItem(mSelectItemModel.mPics.get(i),mSelectItemModel.mPics));
            mView.setOnClickListener(mVoteDetialActivity.mShowPicListener);
            MyLinearLayout mImageLayer = (MyLinearLayout) mSelectItemModel.mView.findViewById(R.id.image_content);
            mImageLayer.setVisibility(View.VISIBLE);
            mImageLayer.addView(mView);
        }
    }

    public void mdoVote() {

        boolean has = false;
        for(int i = 0 ; i < mVoteDetialActivity.mSelectItemModels.size() ; i++)
        {
            if(mVoteDetialActivity.mSelectItemModels.get(i).iselect == true)
            {
                has = true;
                break;
            }
        }
        if(has == false)
        {
            AppUtils.showMessage(mVoteDetialActivity,mVoteDetialActivity.getString(R.string.select_empty));
            return;
        }
        String ids = "";
        for (int i = 0; i < mVoteDetialActivity.mSelectItemModels.size(); i++) {
            if (mVoteDetialActivity.mSelectItemModels.get(i).iselect == true) {
                if (ids.length() == 0)
                    ids += mVoteDetialActivity.mSelectItemModels.get(i).selectid;
                else
                    ids += "," + mVoteDetialActivity.mSelectItemModels.get(i).selectid;
            }
        }
        mVoteDetialActivity.vote.myvoteid = ids;
        mVoteDetialActivity.waitDialog.show();
        VoteAsks.doVote(mVoteDetialActivity,mVoteDetialHandler,mVoteDetialActivity.vote);
    }





    public void clickVote(View mView) {
        VoteSelect mSelectItemModel = (VoteSelect) mView.getTag();

        if (mVoteDetialActivity.vote.type == 1) {
            if (mSelectItemModel.iselect == false) {
                mSelectItemModel.iselect = true;
                ImageView image = (ImageView) mView.findViewById(R.id.image_cion);
                image.setImageResource(R.drawable.checkbox_checked);
            } else {
                mSelectItemModel.iselect = false;
                ImageView image = (ImageView) mView.findViewById(R.id.image_cion);
                image.setImageResource(R.drawable.bunselect);
            }
        } else {

            if (mSelectItemModel.iselect == false) {
                for (int i = 0; i < mVoteDetialActivity.mVoteItemlayer.getChildCount(); i++) {
                    View v = mVoteDetialActivity.mVoteItemlayer.getChildAt(i);
                    VoteSelect temp = (VoteSelect) v.getTag();
                    temp.iselect = false;
                    ImageView image = (ImageView) v.findViewById(R.id.image_cion);
                    image.setImageResource(R.drawable.bunselect);
                }
                mSelectItemModel.iselect = true;
                ImageView image = (ImageView) mView.findViewById(R.id.image_cion);
                image.setImageResource(R.drawable.checkbox_checked);
            }
        }
    }

    public void senderReply() {
        if (mVoteDetialActivity.mEditTextContent.getText().length() == 0) {
            AppUtils.showMessage(mVoteDetialActivity, mVoteDetialActivity.getString(R.string.reply_mesage_send_content));
            return;
        }
        mVoteDetialActivity.waitDialog.show();
        VoteAsks.sendReply(mVoteDetialActivity,mVoteDetialHandler,mVoteDetialActivity.vote,mVoteDetialActivity.mEditTextContent.getText().toString());
    }

    public void showDeleteReply(Reply mReplyModel) {
        AppUtils.creatDialogTowButton(mVoteDetialActivity,mVoteDetialActivity.getString(R.string.xml_reply_delete),"",
                mVoteDetialActivity.getString(R.string.button_word_cancle),mVoteDetialActivity.getString(R.string.button_word_ok),null,new ReplyUtils.DeleteReplyDialogListener(mReplyModel,doDelete));
    }

    public ReplyUtils.DoDelete doDelete = new ReplyUtils.DoDelete(){
        @Override
        public void doDeltet(Reply reply) {
            deleteReply(reply);
        }
    };

    public void deleteReply(Reply mReplyModel) {
        mVoteDetialActivity.waitDialog.show();
        VoteAsks.deleteReply(mVoteDetialActivity,mVoteDetialHandler,mReplyModel);
    }

}
