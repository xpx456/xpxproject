package intersky.mail.view.activity;

import android.os.Bundle;

import intersky.mail.presenter.MailLablePresenter;


/**
 * Created by xpx on 2017/8/18.
 */

public class MailLableActivity extends BaseActivity {

    public static String ACTION_SET_LABLE = "ACTION_SET_LABLE";

    public MailLablePresenter mMailLablePresenter = new MailLablePresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMailLablePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMailLablePresenter.Destroy();
        super.onDestroy();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMailLablePresenter.Resume();
    }



}
