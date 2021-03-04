package intersky.mail.entity;

import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

public class MailType {

    public String group = "";
    public JSONObject where = new JSONObject();
    public String name = "";
    public String typename = "";
    public boolean getMails = false;
    public boolean isMail = false;
    public String mailAddress = "";
    public int leave = 0;
    public View view;
    public boolean show = false;
    public ArrayList<MailType> mailTypes = new ArrayList<MailType>();
}
