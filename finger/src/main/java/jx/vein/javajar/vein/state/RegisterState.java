package jx.vein.javajar.vein.state;

import android.util.Log;

import jx.vein.javajar.vein.subject.NotifyContent;


public class RegisterState {
    private Node ready = new Node(NotifyContent.Step.Ready, "");
    private Node firstWait = new Node(NotifyContent.Step.First_Wait, "请放入手指");
    private Node firstPress = new Node(NotifyContent.Step.First_Press, "请移开手指");
    private Node secWait = new Node(NotifyContent.Step.Sec_Wait, "请放入手指");
    private Node secPress = new Node(NotifyContent.Step.Sec_Press, "请移开手指");
    private Node finalWait = new Node(NotifyContent.Step.Final_Wait, "请放入手指");
    private Node done = new Node(NotifyContent.Step.Done, "完成录入");
    private Node failed = new Node(NotifyContent.Step.Done, "手指移开太快");
    private Node reset = new Node(NotifyContent.Step.Reset, "重新录入, 请放入手指");

    private Node currentNode;

    public RegisterState() {
        ready.setNext(firstWait, null, null);
        firstWait.setNext(firstPress, ready, null);
        firstPress.setNext(secWait, null, reset);

        secWait.setNext(secPress, firstPress, reset);
        secPress.setNext(finalWait, null, reset);

        finalWait.setNext(done, secPress, reset);
        done.setNext(done, null, null);

        currentNode = ready;
    }

    public Node onNext() {
        currentNode = currentNode.moveToNext();
        return currentNode;
    }

    public Node onFailedNext(String message) {
        currentNode = currentNode.failedToNext(message);
        return currentNode;
    }

    public Node onFailedNext() {
        currentNode = currentNode.failedToNext();
        return currentNode;
    }

    public Node onResetNext() {
        currentNode = currentNode.resetToNext();
        return currentNode;
    }


    public Node getCurrentStep() {
        return currentNode;
    }

    public void onReset() {
        currentNode = ready;
    }



}
