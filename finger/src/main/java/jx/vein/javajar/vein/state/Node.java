package jx.vein.javajar.vein.state;

import android.util.Log;

import jx.vein.javajar.vein.subject.NotifyContent;


public class Node {
    private Node next;
    private Node failedNext;
    private Node resetNext;
    private NotifyContent.Step step;
    private String message;
    private String FailedMessage;

    private String defaultFailedMessage = "请放稳手指";

    public Node(NotifyContent.Step step, String message) {
        this.step = step;
        this.message = message;
    }

    public void setNext(Node next, Node failedNext, Node resetNext) {
        this.next = next;
        this.failedNext = failedNext;
        this.resetNext = resetNext;
    }

    public Node moveToNext() {
        if (next == null) return this;
        return next;
    }

    public void setFailedMessage(String message) {
        this.FailedMessage = message;
    }

    public Node failedToNext(String message) {
        if (failedNext == null) return this;
        failedNext.setFailedMessage(message);
        return failedNext;
    }

    public Node failedToNext() {
        if (failedNext == null) return this;
        failedNext.setFailedMessage(defaultFailedMessage);
        return failedNext;
    }

    public Node resetToNext() {
        if (resetNext == null) return this;
        return resetNext;
    }

    public Node getStep() {
        return this;
    }

    public NotifyContent.Step getStepValue() {
        return step;
    }

    public String getMessage() {
        return message;
    }

    public String getFailedMessage() {
        return FailedMessage;
    }

}
