package intersky.conversation.entity;

import intersky.conversation.NotifictionManager;

public class NotificationData {

    public NotificationData(String data, String title, String content, Channel channel) {
        this.data = data;
        this.title = title;
        this.content = content;
        this.channel = channel;
    }

    public boolean show = true;
    public String data = "";
    public String title = "";
    public String detialid = "";
    public Channel channel;
    public String content = "";

}
