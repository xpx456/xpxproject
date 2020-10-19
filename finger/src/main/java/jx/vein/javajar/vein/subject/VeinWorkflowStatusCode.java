package jx.vein.javajar.vein.subject;

public class VeinWorkflowStatusCode {
    public static final Integer PROCESSING = 1;

    public static final Integer INTERRUPT = 6;

    // record
    public static final Integer OK = 0;
    public final static Integer PARAM_NOT_CORRECT = -1;
    public static final Integer BAD_QUALITY = -3;


    public static final Integer WAIT_TIMEOUT = -10;

    // match
    public static final Integer MATCHED = 2;
}
