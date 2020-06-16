package intersky.appbase;

import intersky.appbase.entity.Conversation;

public class Downloadobject {

    public Thread thread;
    public Conversation conversation;
    public Downloadobject(Thread object, Conversation conversation) {
        this.thread = object;
        this.conversation = conversation;
    }

    public void start()
    {
        thread.start();
    }
}
