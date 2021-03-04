package intersky.conversation.entity;

import android.content.Intent;

import intersky.conversation.NotifictionManager;

public class BrodcastData {

    public BrodcastData(String action,Intent intent) {
        this.intent = intent;
        this.action = action;
    }

    public Intent intent;
    public String action = "";

}
