package intersky.appbase.entity;

import android.content.Context;

public class Commend {

    public String cName;
    public CommendFun commendFun;

    public Commend(String cName,CommendFun commendFun) {
        this.cName = cName;
        this.commendFun = commendFun;
    }


    public interface CommendFun{
        void doCommend(Context context);
    }
}
