package intersky.mail.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.handler.MailLableHandler;
import intersky.mail.view.NewLableView;
import intersky.mail.view.activity.MailLableActivity;
import intersky.mail.view.adapter.MailLable2Adapter;
import intersky.mail.view.adapter.MailLable3Adapter;
import intersky.select.entity.Select;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailLablePresenter implements Presenter {

    public MailLableActivity mMailLableActivity;
    public RecyclerView lableview;
    public NewLableView newLableView;
    public RelativeLayout shade;
    public MailLableHandler mailLableHandler;
    public MailLable2Adapter mailLable2Adapter;
    public MailLable3Adapter mailLable3Adapter;
    public ArrayList<Mail> mails;
    public ArrayList<Select> olds;
    public RelativeLayout buttom;
    public MailLablePresenter(MailLableActivity mMailLableActivity) {
        this.mMailLableActivity = mMailLableActivity;
        this.mailLableHandler = new MailLableHandler(mMailLableActivity);

    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mMailLableActivity.setContentView(R.layout.activity_mail_lable);
        buttom = mMailLableActivity.findViewById(R.id.buttomlayer);
        if(mMailLableActivity.getIntent().hasExtra("mails"))
            mails = mMailLableActivity.getIntent().getParcelableArrayListExtra("mails");

        if(mMailLableActivity.getIntent().hasExtra("selects"))
            olds = mMailLableActivity.getIntent().getParcelableArrayListExtra("selects");

        initSelects();
        mailLable2Adapter = new MailLable2Adapter(MailManager.getInstance().mMyLabols,mMailLableActivity);
        mailLable3Adapter = new MailLable3Adapter(MailManager.getInstance().mGropLabols,mMailLableActivity);
        mailLable3Adapter.setOnItemClickListener(onItemClickListener2);
        mailLable2Adapter.setOnItemClickListener(onItemClickListener);
        lableview = (RecyclerView)mMailLableActivity.findViewById(R.id.lablelist);
        shade = mMailLableActivity.findViewById(R.id.shade);
        newLableView = new NewLableView(mMailLableActivity,shade);
        lableview.setLayoutManager(new LinearLayoutManager(mMailLableActivity));
        TextView creat = mMailLableActivity.findViewById(R.id.newtab);
        if(mMailLableActivity.getIntent().getBooleanExtra("push",false) == false)
        {
            lableview.setAdapter(mailLable2Adapter);
            buttom.setVisibility(View.VISIBLE);
        }
        else
        {
            lableview.setAdapter(mailLable3Adapter);
            buttom.setVisibility(View.INVISIBLE);
        }


        TextView ok = mMailLableActivity.findViewById(R.id.ok);
        TextView cancle = mMailLableActivity.findViewById(R.id.cancle);
        cancle.setOnClickListener(mMailLableActivity.mBackListener);
        ok.setOnClickListener(okListener);
        creat.setOnClickListener(creatListener);
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

    }

    public View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mails != null)
            {
                mMailLableActivity.waitDialog.show();
                ArrayList<Select> selects = new ArrayList<Select>();
                for(int i = 0 ; i < MailManager.getInstance().mMyLabols.size() ; i++)
                {
                    if(MailManager.getInstance().mMyLabols.get(i).iselect)
                        selects.add(MailManager.getInstance().mMyLabols.get(i));
                }
                mMailLableActivity.waitDialog.hide();
                MailAsks.lableMail(mMailLableActivity,mailLableHandler,mails,selects);
            }
            else
            {
                ArrayList<Select> selects = new ArrayList<Select>();
                for(int i = 0 ; i < MailManager.getInstance().mMyLabols.size() ; i++)
                {
                    if(MailManager.getInstance().mMyLabols.get(i).iselect)
                        selects.add(MailManager.getInstance().mMyLabols.get(i));
                }
                Intent intent = new Intent(MailLableActivity.ACTION_SET_LABLE);
                intent.putParcelableArrayListExtra("selects",selects);
                mMailLableActivity.sendBroadcast(intent);
                mMailLableActivity.finish();
            }
        }
    };

    public View.OnClickListener creatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            newLableView.show(mMailLableActivity.findViewById(R.id.activity_mail),lableOkListener);
        }
    };

    public NewLableView.OkListener lableOkListener = new NewLableView.OkListener() {

        @Override
        public void OkListener(Select select) {
            mMailLableActivity.waitDialog.hide();
            MailAsks.lableSet(mMailLableActivity,mailLableHandler,select);
            newLableView.hide();
        }
    };

    public void updataLable(Select select) {
        boolean updata = false;
        for(int i = 0; i < MailManager.getInstance().mMyLabols.size() ; i++)
        {
            Select select1 = MailManager.getInstance().mMyLabols.get(i);
            if(select1.mId.equals(select.mId))
            {
                select1.mName = select.mName;
                select1.mColor = select.mColor;
                updata = true;

            }
        }
        if(updata == false)
        {
            MailManager.getInstance().mMyLabols.add(0,select);
        }

        if(mMailLableActivity.getIntent().getBooleanExtra("push",false) == false)
        {
            lableview.setAdapter(mailLable2Adapter);
        }
        else
        {
            lableview.setAdapter(mailLable3Adapter);
        }
        Intent intent = new Intent(MailManager.ACTION_MAIL_LABLE_UPDATE);
        mMailLableActivity.sendBroadcast(intent);
    }

    public MailLable2Adapter.OnItemClickListener onItemClickListener = new MailLable2Adapter.OnItemClickListener()
    {

        @Override
        public void onItemClick(Select lable, int position, View view) {
            if(lable.iselect == false)
            {
                lable.iselect = true;
            }
            else
            {
                lable.iselect = false;
            }
            mailLable2Adapter.notifyDataSetChanged();
        }
    };

    public MailLable3Adapter.OnItemClickListener onItemClickListener2 = new MailLable3Adapter.OnItemClickListener()
    {

        @Override
        public void onItemClick(Select lable, int position, View view) {
            if(lable.iselect == false)
            {
                cleanSelect();
                lable.iselect = true;
            }
            else
            {
                lable.iselect = false;
            }
            ArrayList<Select> selects = new ArrayList<Select>();
            for(int i = 0 ; i < MailManager.getInstance().mGropLabols.size() ; i++)
            {
                if(MailManager.getInstance().mGropLabols.get(i).iselect)
                    selects.add(MailManager.getInstance().mGropLabols.get(i));
            }
            Intent intent = new Intent(MailLableActivity.ACTION_SET_LABLE);
            intent.putParcelableArrayListExtra("selects",selects);
            mMailLableActivity.sendBroadcast(intent);
            mMailLableActivity.finish();
        }
    };

    public void cleanSelect() {
        if(mMailLableActivity.getIntent().getBooleanExtra("push",false) == false)
        {
            for(int i = 0 ; i < MailManager.getInstance().mMyLabols.size() ; i++)
            {
                MailManager.getInstance().mMyLabols.get(i).iselect = false;
            }
        }
        else
        {
            for(int i = 0 ; i < MailManager.getInstance().mGropLabols.size() ; i++)
            {
                MailManager.getInstance().mGropLabols.get(i).iselect = false;
            }
        }


    }

    public void initSelects() {
        if(olds != null)
        {
            for(int i = 0 ; i < olds.size() ; i++)
            {
                Select select = olds.get(i);
                Select select1 = null;
                if(mMailLableActivity.getIntent().getBooleanExtra("push",false) == false)
                    select1 = MailManager.getInstance().getLable(select.mId);
                else
                    select1 = MailManager.getInstance().getGLable(select.mId);
                if(select1 != null)
                {
                    select1.iselect = true;
                }
            }
        }
    }
}
