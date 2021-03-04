package jx.vein.javajar.vein.subject;


import java.util.ArrayList;
import java.util.List;

import jx.vein.javajar.vein.state.Node;

public class NotifyContent {
    private List<Type> types;
    public enum Type {
        None, Screen, Vericate, Crash
    }

    public enum Step {
        Ready, First_Wait, First_Press, First_Left, Sec_Wait, Sec_Press, Sec_Left, Final_Wait, Final_Press, Final_Left, Done, Failed, Reset
    }

    private Node gatherStep;

    public void setGatherStep(Node node) {
        gatherStep = node;
    }

    private boolean deviceCrash;

    private boolean unlockSrceenSaverAndRestTimer;
    private boolean startTime;
    private int deviceStatus;
    private int postStatus;
//    private Visitor visitor;
    private int failedCode;

    public boolean isConsume;

    public NotifyContent() {
        types = new ArrayList<>();
        types.add(Type.Screen);
        types.add(Type.Vericate);
        deviceCrash = false;
        unlockSrceenSaverAndRestTimer = false;
        startTime = false;
        deviceStatus = 0;
        postStatus = 0;
        //visitor = new Visitor();

        isConsume = false;
    }

    public void clear() {
        unlockSrceenSaverAndRestTimer = false;
        startTime = false;
        deviceStatus = 0;
        postStatus = 0;
        //visitor.clear();
        isConsume = true;
    }

    public void addCrashType() {
        if (!types.contains(Type.Crash)) {
            types.add(Type.Crash);
        }
    }
}
