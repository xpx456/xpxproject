package intersky.vote.entity;

import java.util.ArrayList;

import intersky.appbase.entity.Contacts;

/**
 * Created by xpx on 2017/6/23.
 */

public class Reocrd {

    public ArrayList<Contacts> mContacts = new ArrayList<Contacts>();
    public String name = "";
    public String id = "";
    public Reocrd(String name, String id)
    {
        this.name = name;
        this.id = id;
    }
}
