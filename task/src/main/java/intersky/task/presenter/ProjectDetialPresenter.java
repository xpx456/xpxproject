package intersky.task.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.appbase.ReplyUtils;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Reply;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.mywidget.WebEdit;
import intersky.oa.OaUtils;
import intersky.task.R;
import intersky.task.TaskManager;
import intersky.task.asks.ProjectAsks;
import intersky.task.asks.ProjectLogAsks;
import intersky.task.asks.ProjectReplyAsks;
import intersky.task.entity.Log;
import intersky.task.handler.ProjectDetialHandler;
import intersky.task.receiver.ProjectDetialReceiver;
import intersky.task.view.activity.ProjectDetialActivity;
import intersky.task.view.activity.ProjectSettingActivity;
import intersky.task.view.activity.TaskMemberActivity;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ProjectDetialPresenter implements Presenter {

    public ProjectDetialHandler mProjectDetialHandler;
    public ProjectDetialActivity mProjectDetialActivity;

    public ProjectDetialPresenter(ProjectDetialActivity mProjectDetialActivity)
    {
        this.mProjectDetialActivity = mProjectDetialActivity;
        this.mProjectDetialHandler = new ProjectDetialHandler(mProjectDetialActivity);
        mProjectDetialActivity.setBaseReceiver(new ProjectDetialReceiver(mProjectDetialHandler));

    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mProjectDetialActivity.setContentView(R.layout.activity_project_detial);
        mProjectDetialActivity.project = mProjectDetialActivity.getIntent().getParcelableExtra("project");
        mProjectDetialActivity.projectName = (TextView) mProjectDetialActivity.findViewById(R.id.project_name);
        mProjectDetialActivity.projectDes = (WebEdit) mProjectDetialActivity.findViewById(R.id.project_des);
        mProjectDetialActivity.projectDes.setHit("#767676",mProjectDetialActivity.getString(R.string.task_detial_des_hit));
        mProjectDetialActivity.projectDes.setTxtColor(Color.rgb(118,118,118));
        mProjectDetialActivity.projectDes.setBackgroundColor(0);
        mProjectDetialActivity.projectDes.setAction(ProjectDetialActivity.ACTION_SET_PROJECT_DES);
        mProjectDetialActivity.headImg = (TextView) mProjectDetialActivity.findViewById(R.id.contact_img);
        mProjectDetialActivity.headTitle = (TextView) mProjectDetialActivity.findViewById(R.id.conversation_title);
        mProjectDetialActivity.mHorizontalListView = (LinearLayout) mProjectDetialActivity.findViewById(R.id.horizon_listview);
        mProjectDetialActivity.mLefttTeb = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.day);
        mProjectDetialActivity.mMiddeleTeb = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.week);
        mProjectDetialActivity.mRightTeb = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.month);
        mProjectDetialActivity.mLine1 = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.line13);
        mProjectDetialActivity.mLine2 = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.line23);
        mProjectDetialActivity.mLine3 = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.line33);
        mProjectDetialActivity.mLefttImg = (TextView) mProjectDetialActivity.findViewById(R.id.daytxt);
        mProjectDetialActivity.mMiddleImg = (TextView) mProjectDetialActivity.findViewById(R.id.weektxt);
        mProjectDetialActivity.mRightImg = (TextView) mProjectDetialActivity.findViewById(R.id.monthtxt);
        mProjectDetialActivity.mEditTextContent = (EditText) mProjectDetialActivity.findViewById(R.id.et_sendmessage);
        mProjectDetialActivity.pcount = (TextView) mProjectDetialActivity.findViewById(R.id.p_count);
        View mView1 = null;
        View mView2 = null;
        View mView3 = null;
        mView1 = mProjectDetialActivity.findViewById(R.id.answer);
        mProjectDetialActivity.mAnswerlayer = (LinearLayout) mView1.findViewById(R.id.answeritem);
        mView2 = mProjectDetialActivity.findViewById(R.id.attahmentlayer);
        mProjectDetialActivity.mAttchmentView = (LinearLayout) mView2.findViewById(R.id.attachment_listview);
//        mProjectDetialActivity.addAttachment =(LinearLayout) mView2.findViewById(R.id.attachmentadd);
//        mProjectDetialActivity.addAttachment.setOnClickListener(mProjectDetialActivity.mSelectshowPicListener2);
        mView3 = mProjectDetialActivity.findViewById(R.id.loglayer);
        mProjectDetialActivity.mLogsArea = (LinearLayout) mView3.findViewById(R.id.log_list);
        mProjectDetialActivity.mViews.add(mView1);
        mProjectDetialActivity.mViews.add(mView2);
        mProjectDetialActivity.mViews.add(mView3);
        mProjectDetialActivity.shade = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.shade);
        mProjectDetialActivity.memBer = (RelativeLayout) mProjectDetialActivity.findViewById(R.id.member);
        mProjectDetialActivity.mEditTextContent.setOnEditorActionListener(mProjectDetialActivity.sendtext);
        mProjectDetialActivity.mLefttTeb.setOnClickListener(mProjectDetialActivity.leftClickListener);
        mProjectDetialActivity.mMiddeleTeb.setOnClickListener(mProjectDetialActivity.middleClickListener);
        mProjectDetialActivity.mRightTeb.setOnClickListener(mProjectDetialActivity.rightClickListener);
        mProjectDetialActivity.projectName.setOnClickListener(mProjectDetialActivity.mUpdtatName);
        mProjectDetialActivity.headImg.setOnClickListener(mProjectDetialActivity.mSetLeader);
        mProjectDetialActivity.headTitle.setOnClickListener(mProjectDetialActivity.mSetLeader);
        mProjectDetialActivity.memBer.setOnClickListener(mProjectDetialActivity.mSetMembers);
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) mProjectDetialActivity.findViewById(R.id.person);
        horizontalScrollView.setOnClickListener(mProjectDetialActivity.mSetMembers);
        ProjectAsks.getProjectDetial(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project);
        initdata();
        mProjectDetialActivity.replyPage = 1;
        mProjectDetialActivity.isreplyall = false;
        mProjectDetialActivity.mReplys.clear();
        ProjectReplyAsks.getReplays(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,mProjectDetialActivity.replyPage);
//
        mProjectDetialActivity.logPage = 1;
        mProjectDetialActivity.islogall = false;
        mProjectDetialActivity.mLogModels.clear();
        ProjectLogAsks.getLogs(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,mProjectDetialActivity.logPage);
//
        mProjectDetialActivity.mAttachments.clear();
        mProjectDetialActivity.attachall = false;
        ProjectAsks.getAttachments(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,mProjectDetialActivity.attachmentPage);
        ToolBarHelper.setRightBtn(mProjectDetialActivity.mActionBar,mProjectDetialActivity.mdetial,R.drawable.task_project_option);
//        showLeft();
    }

    public void initdata()
    {
        ToolBarHelper.setTitle(mProjectDetialActivity.mActionBar,mProjectDetialActivity.project.mName);
        mProjectDetialActivity.projectName.setText(mProjectDetialActivity.project.mName);
        Contacts taskPerson = (Contacts) Bus.callData(mProjectDetialActivity,"chat/getContactItem",mProjectDetialActivity.project.leaderId);
        AppUtils.setContactCycleHead(mProjectDetialActivity.headImg,taskPerson.getName(),taskPerson.colorhead);
        mProjectDetialActivity.headTitle.setText(taskPerson.getName());
        mProjectDetialActivity.projectDes.setText(mProjectDetialActivity.project.des);
        if( mProjectDetialActivity.project.des.length() == 0)
        {
            mProjectDetialActivity.projectDes.setText(mProjectDetialActivity.getString(R.string.task_detial_des_hit));
        }
        if(mProjectDetialActivity.memberDetial.apply == 0)
            mProjectDetialActivity.pcount.setText(mProjectDetialActivity.getString(R.string.task_contacts_person)+String.valueOf(mProjectDetialActivity.memberDetial.admincount+mProjectDetialActivity.memberDetial.personcount));
        else
        mProjectDetialActivity.pcount.setText(mProjectDetialActivity.getString(R.string.task_contacts_wait)+String.valueOf(mProjectDetialActivity.memberDetial.apply));
        updataContactViews(mProjectDetialActivity.mContactModels);
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

        mProjectDetialActivity.projectDes.destroy();
    }


    public void showLeft() {

        mProjectDetialActivity.current = 0;
        mProjectDetialActivity.mViews.get(0).setVisibility(View.VISIBLE);
        mProjectDetialActivity.mViews.get(1).setVisibility(View.GONE);
        mProjectDetialActivity.mViews.get(2).setVisibility(View.GONE);
        mProjectDetialActivity.mLine1.setVisibility(View.VISIBLE);
        mProjectDetialActivity.mLine2.setVisibility(View.INVISIBLE);
        mProjectDetialActivity.mLine3.setVisibility(View.INVISIBLE);
        mProjectDetialActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mProjectDetialActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mProjectDetialActivity.mLefttImg.setTextColor(Color.rgb(98, 153, 243));
    }

    public void showMiddle() {

        mProjectDetialActivity.current = 1;
        mProjectDetialActivity.mViews.get(0).setVisibility(View.GONE);
        mProjectDetialActivity.mViews.get(1).setVisibility(View.VISIBLE);
        mProjectDetialActivity.mViews.get(2).setVisibility(View.GONE);
        mProjectDetialActivity.mLine1.setVisibility(View.INVISIBLE);
        mProjectDetialActivity.mLine2.setVisibility(View.VISIBLE);
        mProjectDetialActivity.mLine3.setVisibility(View.INVISIBLE);
        mProjectDetialActivity.mRightImg.setTextColor(Color.rgb(0, 0, 0));
        mProjectDetialActivity.mMiddleImg.setTextColor(Color.rgb(98, 153, 243));
        mProjectDetialActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));
        initAttachment();
    }

    public void showRight() {

        mProjectDetialActivity.current = 2;
        mProjectDetialActivity.mViews.get(0).setVisibility(View.GONE);
        mProjectDetialActivity.mViews.get(1).setVisibility(View.GONE);
        mProjectDetialActivity.mViews.get(2).setVisibility(View.VISIBLE);
        mProjectDetialActivity.mLine1.setVisibility(View.INVISIBLE);
        mProjectDetialActivity.mLine2.setVisibility(View.INVISIBLE);
        mProjectDetialActivity.mLine3.setVisibility(View.VISIBLE);
        mProjectDetialActivity.mRightImg.setTextColor(Color.rgb(98, 153, 243));
        mProjectDetialActivity.mMiddleImg.setTextColor(Color.rgb(0, 0, 0));
        mProjectDetialActivity.mLefttImg.setTextColor(Color.rgb(0, 0, 0));

        initLogViews();
    }

    public void updataLeader()
    {
        if(mProjectDetialActivity.memberDetial.leavlType == ProjectDetialActivity.TYPE_LEADER)
        {
            Bus.callData(mProjectDetialActivity,"chat/setContacts","负责人", ProjectDetialActivity.ACTION_SET_PROJECT_LEADER);
        }
        else
        {
            AppUtils.showMessage(mProjectDetialActivity,mProjectDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public void setMembers()
    {
        Intent intent = new Intent(mProjectDetialActivity, TaskMemberActivity.class);
        intent.putParcelableArrayListExtra("members",mProjectDetialActivity.mContactModels);
        intent.putExtra("project",mProjectDetialActivity.project);
        intent.putExtra("memberdetial",mProjectDetialActivity.memberDetial);
        intent.putExtra("title","成员");
        mProjectDetialActivity.startActivity(intent);
    }

    public void updtatName() {
        if(mProjectDetialActivity.memberDetial.leavlType == ProjectDetialActivity.TYPE_LEADER || mProjectDetialActivity.memberDetial.leavlType == ProjectDetialActivity.TYPE_ADMIN)
        {

            AppUtils.creatDialogTowButtonEdit(mProjectDetialActivity,"",mProjectDetialActivity.getString(R.string.project_detial_set_task_name),
                    mProjectDetialActivity.getString(R.string.button_word_cancle),mProjectDetialActivity.getString(R.string.button_word_ok),null,new SetNameListener(),mProjectDetialActivity.project.mName);
        }
        else
        {
            AppUtils.showMessage(mProjectDetialActivity,mProjectDetialActivity.getString(R.string.task_no_auth));
        }
    }

    public class SetNameListener extends EditDialogListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (editText.getText().length() > 0) {
                ProjectAsks.setProjectName(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,editText.getText().toString());
            } else {
                AppUtils.showMessage(mProjectDetialActivity, mProjectDetialActivity.getString(R.string.task_detial_task_new_name));
            }
        }
    }

    public void updataLeader(NetObject net) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(mProjectDetialActivity,AppUtils.getfailmessage(json));
            return;
        }
        String id = ((Contacts) net.item).mRecordid;
        mProjectDetialActivity.project.leaderId = id;
        Contacts taskPerson = (Contacts) Bus.callData(mProjectDetialActivity,"chat/getContactItem",mProjectDetialActivity.project.leaderId);
        AppUtils.setContactCycleHead(mProjectDetialActivity.headImg,taskPerson.getName(),taskPerson.colorhead);
        mProjectDetialActivity.headTitle.setText(taskPerson.getName());
        ProjectAsks.getProjectDetial(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project);
    }


    public void updataContactViews(ArrayList<Contacts> contacts)
    {
        mProjectDetialActivity.mHorizontalListView.removeAllViews();
        int n = 0;

        for(int i = 0 ; i < contacts.size()-mProjectDetialActivity.memberDetial.apply ; i++)
        {
            Contacts mContact = contacts.get(i);
            if(mContact.mType == Contacts.TYPE_PERSON)
            {
                View mView = mProjectDetialActivity.getLayoutInflater().inflate(R.layout.task_contact_item, null);
                mView.setOnClickListener(mProjectDetialActivity.mSetMembers);
                TextView mhead = (TextView) mView.findViewById(R.id.contact_img);
                AppUtils.setContactCycleHead(mhead,mContact.getName(),mContact.colorhead);
                if(n > 0)
                {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = -30;
                    mProjectDetialActivity.mHorizontalListView.addView(mView,layoutParams);
                }
                else
                {
                    mProjectDetialActivity.mHorizontalListView.addView(mView);
                }
                n++;
            }

        }
    }


    public void praseAttachment(NetObject net) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(mProjectDetialActivity,AppUtils.getfailmessage(json));
                return;
            }

            JSONObject jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if(jo.getString("hash").length() != 0)
                {
                    Attachment mAttachmentModel = new Attachment(jo.getString("hash"),jo.getString( "name")
                            ,Bus.callData(mProjectDetialActivity,"filetools/getfilePath","/project")+"/"+jo.getString("hash")+"."+jo.getString("name").substring(jo.getString("name").lastIndexOf("."), jo.getString("name").length())
                            , TaskManager.getInstance().oaUtils.praseClodAttchmentUrl(jo.getString("hash")),0,0,"");
                    mAttachmentModel.mPath2 = Bus.callData(mProjectDetialActivity,"filetools/getfilePath", "/project")+"/"+jo.getString("hash")+"."+jo.getString("name").substring(jo.getString("name").lastIndexOf("."), jo.getString("name").length());
                    mAttachmentModel.mDete = jo.getString("create_time");
                    mAttachmentModel.taskuserid = jo.getString("user_id");
//
                    mProjectDetialActivity.mAttachments.add(mAttachmentModel);
                }
            }
            mProjectDetialActivity.attachmentPage++;

            if(ja.length() == 0)
                mProjectDetialActivity.attachall = true;
            else
                ProjectAsks.getAttachments(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,mProjectDetialActivity.attachmentPage);

            if(mProjectDetialActivity.current == 1 && mProjectDetialActivity.attachall == true)
                initAttachment();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initLogViews()
    {
        mProjectDetialActivity.mLogsArea.removeAllViews();
        for(int i = 0 ; i < mProjectDetialActivity.mLogModels.size() ; i++)
        {
            Log model = mProjectDetialActivity.mLogModels.get(i);
            addlogView(model);
        }


    }

    public void praseLog(NetObject net) {
        JSONObject jsonObject = null;
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(mProjectDetialActivity,AppUtils.getfailmessage(json));
                return;
            }

            jsonObject = new JSONObject(json);
            JSONArray ja = jsonObject.getJSONArray("data");
            for (int j = 0; j < ja.length(); j++) {
                JSONObject jo = ja.getJSONObject(j);
                Log model = new Log();
                model.content = jo.getString("content");
                model.creater = jo.getString("create_id");
                model.creattime = jo.getString("create_time");
                model.type = jo.getInt("icon");
                mProjectDetialActivity.mLogModels.add(model);
            }
            mProjectDetialActivity.logPage++;
            if(ja.length() >= 40)
            {
                ProjectLogAsks.getLogs(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,mProjectDetialActivity.logPage);
            }
            else
            {
                if(mProjectDetialActivity.current == 2)
                    initLogViews();
                mProjectDetialActivity.islogall = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getRecordID(String content)
    {
        String regEx = "([0-9a-fA-F]{8})-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-([0-9a-fA-F]{12})";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content);
        String content2 = content;
        int n = 0;
        while (matcher.find()){
            if(n == 0)
            {
                content2 = content2.replace(matcher.group(0), (CharSequence) Bus.callData(mProjectDetialActivity,"chat/getContactName",matcher.group(0)));
            }
            else
            {
                content2 = content2.replace(matcher.group(0), (CharSequence) Bus.callData(mProjectDetialActivity,"chat/getContactName",matcher.group(0)));
            }
            n++;
            matcher = pattern.matcher(content2);
        }
        return content2;
    }

    public void addlogView(Log mLogModel)
    {
        View convertView = mProjectDetialActivity.getLayoutInflater().inflate(R.layout.log_item, null);
        TextView title = (TextView) convertView.findViewById(R.id.item_title);
        TextView mtime = (TextView) convertView.findViewById(R.id.item_time);
        String content = getRecordID(mLogModel.content);
        switch (mLogModel.type)
        {
            case 9:
                break;
        }
        Contacts contacts = (Contacts) Bus.callData(mProjectDetialActivity,"chat/getContactItem",mLogModel.creater);
        title.setText(contacts.getName()+": "+content);
        mtime.setText(mLogModel.creattime.substring(0,16));
        mProjectDetialActivity.mLogsArea.addView(convertView);
    }

    public void prasseReplys(NetObject net) {
        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
            {
                AppUtils.showMessage(mProjectDetialActivity,AppUtils.getfailmessage(json));
                return;
            }

            JSONObject object = new JSONObject(json);
            JSONArray ja2 = object.getJSONArray("data");
            for (int i = 0; i < ja2.length(); i++) {
                JSONObject jo2 = ja2.getJSONObject(i);
                Reply mReplyModel = new Reply(jo2.getString("project_reply_id"), jo2.getString("user_id"), jo2.getString("reply_content"), mProjectDetialActivity.project.projectId, jo2.getString("create_time"));
                mProjectDetialActivity.mReplys.add(mReplyModel);
            }
            mProjectDetialActivity.replyPage++;
            if (ja2.length() == 40)
                ProjectReplyAsks.getReplays(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project,mProjectDetialActivity.replyPage);
            else
            {
                ReplyUtils.praseReplyViews(mProjectDetialActivity.mReplys,mProjectDetialActivity,null
                        ,mProjectDetialActivity.mAnswerlayer,mProjectDetialActivity.mDeleteReplyListenter,mProjectDetialHandler);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showDeleteReport(final Reply mReplyModel) {


        AppUtils.creatDialogTowButton(mProjectDetialActivity,mProjectDetialActivity.getString(R.string.xml_workreport_reply_delete),"",
                mProjectDetialActivity.getString(R.string.button_word_cancle),mProjectDetialActivity.getString(R.string.button_word_ok),null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ProjectReplyAsks.deleteReply(mProjectDetialActivity,mProjectDetialActivity.mProjectDetialPresenter.mProjectDetialHandler,mReplyModel);
                    }
                } );

    }

    public void updataName() {
        mProjectDetialActivity.projectName.setText(mProjectDetialActivity.project.mName);
    }

    public void dodetial()
    {
        Intent intent = new Intent(mProjectDetialActivity, ProjectSettingActivity.class);
        intent.putExtra("project", mProjectDetialActivity.project);
        intent.putExtra("leavetype", mProjectDetialActivity.memberDetial.leavlType);
        mProjectDetialActivity.startActivity(intent);
    }

    public void updataDes()
    {
        mProjectDetialActivity.projectDes.setText(mProjectDetialActivity.project.des);
        if( mProjectDetialActivity.project.des.length() == 0)
        {
            mProjectDetialActivity.projectDes.setText(mProjectDetialActivity.getString(R.string.task_detial_des_hit));
        }
    }

    public void praseDetial(String json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            if(!data.isNull("name"))
            mProjectDetialActivity.project.mName = data.getString("name");
            mProjectDetialActivity.project.projectId = data.getString("project_id");
            mProjectDetialActivity.project.templateId = data.getString("template_id");
            if(!data.isNull("description"))
            mProjectDetialActivity.project.des = data.getString("description");
            mProjectDetialActivity.project.isPower = data.getInt("is_power");
            mProjectDetialActivity.project.isTop = data.getInt("is_top");
            mProjectDetialActivity.project.isLayer = data.getInt("is_layer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initAttachment()
    {
        mProjectDetialActivity.mAttchmentView.removeAllViews();
        for(int i = 0 ; i < mProjectDetialActivity.mAttachments.size() ; i++)
        {
            addAttachmentViews(mProjectDetialActivity.mAttachments.get(i));
        }
    }

    public void addAttachmentViews(Attachment mAttachmentModel)
    {
        LayoutInflater mInflater = (LayoutInflater) mProjectDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File mfile = new File(mAttachmentModel.mPath);
        View mView = mInflater.inflate(R.layout.fujian_long_item3, null);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        TextView textView = (TextView) mView.findViewById(R.id.fujian_title);
        textView.setText(mAttachmentModel.mName);
        TextView name = (TextView) mView.findViewById(R.id.user_name);
        Contacts contacts = (Contacts) Bus.callData(mProjectDetialActivity,"chat/getContactItem",mAttachmentModel.taskuserid);
        name.setText(contacts.getName());
        TextView dete = (TextView) mView.findViewById(R.id.user_data);
        dete.setText(mAttachmentModel.mDete.substring(0,16));
        ImageView close = (ImageView) mView.findViewById(R.id.close_image);
        Button button = (Button) mView.findViewById(R.id.close_image_b);
        button.setVisibility(View.INVISIBLE);
        close.setVisibility(View.INVISIBLE);
        Bus.callData(mProjectDetialActivity,"filetools/setfileimg",mImageView, mfile.getName());
        if ((int)Bus.callData(mProjectDetialActivity,"filetools/getFileType", mfile.getName()) == 301) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            Glide.with(mProjectDetialActivity).load(TaskManager.getInstance().oaUtils.praseClodAttchmentUrl(mAttachmentModel.mRecordid, (int) (70 * mProjectDetialActivity.mBasePresenter.mScreenDefine.density))).apply(options).into(new MySimpleTarget(mImageView));
        }
        mView.setTag(mAttachmentModel);
        mView.setOnClickListener(mProjectDetialActivity.mShowPicListener);
        mProjectDetialActivity.mAttchmentView.addView(mView);
    }

    public void projectDetialUpdata(Intent intent) {
        String id = intent.getStringExtra("projectid");
        if(id.equals(mProjectDetialActivity.project.projectId))
        {
            mProjectDetialActivity.waitDialog.show();
            ProjectAsks.getProjectDetial(mProjectDetialActivity,mProjectDetialHandler,mProjectDetialActivity.project);
        }
    }

}
