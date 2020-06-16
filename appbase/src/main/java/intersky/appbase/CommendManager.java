package intersky.appbase;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.Commend;

public class CommendManager {

    public HashMap<String,Commend> commends = new HashMap<String,Commend>();
    public ArrayList<String> keys = new ArrayList<String>();
    private volatile static CommendManager commendManager = null;

    public static CommendManager init() {

        if (commendManager == null) {
            synchronized (CommendManager.class) {
                if (commendManager == null) {
                    commendManager = new CommendManager();
                }
            }
        }
        return commendManager;
    }

    public void registerComment(String cname, Commend.CommendFun fun) {
        Commend commend = new Commend(cname,fun);
        if(!commends.containsKey(cname))
        {
            keys.add(cname);
            commends.put(cname,commend);
        }

    }

    public void doCommend(Context context,String word) {
        Commend commendfun = null;
        if(word.length() > 0)
        {
            String commend[] = word.split(";");
            for(int i = 0 ; i < commend.length ; i++)
            {
                Commend commend1 = checkKey(commend[i]);
                if(commend1 != null)
                {
                    commendfun = commend1;
                    break;
                }
            }
        }
        if(commendfun != null)
        {
            commendfun.commendFun.doCommend(context);
        }
    }

    public Commend checkKey(String com) {
        Commend commend = null;
        for(int i = 0 ; i < keys.size() ; i++)
        {

            if(com.contains(keys.get(i)))
            {
                if(commend != null)
                {
                    if(commend.cName.length() < keys.get(i).length())
                    commend = commends.get(keys.get(i));
                }
                else
                {
                    commend = commends.get(keys.get(i));
                }
            }
        }
        return commend;
    }

}
