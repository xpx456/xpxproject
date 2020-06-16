package intersky.riche;

import android.content.Context;
import android.content.Intent;

import intersky.appbase.entity.Account;

public class RicheManager {

    public static RicheManager mRicheManager;
    public Context context;
    public Account account;
    public static RicheManager init(Context context) {

        mRicheManager = new RicheManager(context);
        return mRicheManager;
    }

    public static RicheManager getInstance() {
        return mRicheManager;
    }

    public RicheManager(Context context) {
        this.context = context;
    }

    public void startEdit(Context context,String action,String html) {
        Intent intent = new Intent(context, RichEditActivity.class);
        intent.setAction(action);
        intent.putExtra("value",html);
        context.startActivity(intent);
    }
}
