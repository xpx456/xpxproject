package intersky.mail.entity;

import org.json.JSONArray;

public class MailSend {

    public String send_state = "0";
    public String mail_box_id = "";
    public String subject = "";
    public String content = "";
    public String from_address = "";
    public String cc_address = "";
    public String to_address = "";
    public String bcc_address = "";
    public String resent_address = "";
    public String state = "";
    public String mail_type = "";
    public String submit_type = "";
    public String isText = "false";
    public String isCritical = "false";
    public String isReceipt = "false";
    public String isTrack = "false";
    public String priority = "1";
    public String raw_mail_id = "";
    public String raw_mail_type = "";
    public String mail_user_id = "";
    public String approval = "false";
    public String attachments = "{}";
    public JSONArray lable = new JSONArray();
}
