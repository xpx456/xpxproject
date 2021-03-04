package jx.vein.javajar.vein.subject;

import android.content.Intent;


public class Constant {

    public static class ItemList {
        public final static String IntentKey = "itemListKey";
        public final static Integer Reserve = 1;
        public final static Integer Candidate = 2;

        public final static Integer MAX_VISITOR_TABLE_ROW = 10;

        public static class Approve {
            public final static String Yes = "审核通过";
        }
    }


    public static class RegisterEntry {
        public final static String IntentKey = "registerKey";
        public final static Integer MakeAppoint = 1;
        public final static Integer ScanQRCode = 2;
        public final static Integer InterviewList = 3;
        public final static Integer ReserveList = 4;
        public final static Integer FastTunnel = 5;
    }

    public static class NetSettings {
        public final static String classTAG = "NetSettings";

        public final static String serverAddressKey = "serverAddress";
        public final static String serverPortKey = "serverPort";
        public final static String serverWebSocketPortKey = "serverWebSocketPort";

        public final static String useDHCPKey = "useDHCP";

        public final static String hostIPKey = "hostIP";
        public final static String hostMaskKey = "hostMask";
        public final static String hostGatewayKey = "hostGateway";


        public final static String defaultServerAddress = "10.100.1.46";
        public final static String defaultServerPort = "5000";

        public final static String defaultHostMask = "255.255.255.0";
    }

    public static class Leave {
        public final static String IntentKey = "LeaveKey";
        public final static Integer Tick = 1;
        public final static Integer Face = 2;
        public final static Integer Vein = 3;
        public final static Integer IdCard = 4;

        public final static Integer visitorCntType = 1;
        public final static Integer leaveCntType = 2;
        public final static Integer stayCntType = 3;
    }

    public static class IDCardType {
        // 居民身份证
        public final static Integer NormalIDCard = 1;
        // 外国人永居证
        public final static Integer PRPFIDCard = 2;
        // 港澳台居住证
        public final static Integer HMTIDCard = 3;

        public static class ErrorCode {
            public final static Integer OK = 0;
            public final static Integer NAME_NOT_MATCH = 1;
            public final static Integer ID_NOT_MATCH = 2;
        }
    }

    public static class SOUND {
        public final static Integer FACE_MATCHED = 0;
        public final static Integer LEAVE_OK = 1;
        public final static Integer PUT_DOWN_ID_CARD = 2;
        public final static Integer REGISTER_COMPLETED = 3;
        public final static Integer RESERVE_COMPLETED = 4;
        public final static Integer SCAN = 5;
        public final static Integer START_FACE_MATCH = 6;
    }

    public static class FACE_SDK {
        public final static String APP_ID = "JDATxcPRk6eyAjFCZ98VbDeadXxFR9p9URd1oytk9vHr";
        public final static String SDK_KEY = "888aA5MKGmzJFEGrB93DKe5pfBycoudDaa7W9dxVpwXy";
        public final static long DETECT_TIMEOUT = 10000;
        public final static long DETECT_FAILED = 10000;
    }

    public static class FACE_MODE {
        public final static Integer lToN = 0;
        public final static Integer lTol = 1;
        public final static Integer PHOTOGRAPH = 2;
    }

    public static class PRINTER {
        public static final Integer OK = 0;
        public static final Integer NOT_CONNECT_DEVICE = 1;
        public static final Integer NO_PAGER = 2;
    }

    public static class DEVICE_NODE_TYPE {
        public static final Integer FACE = 0;
        public static final Integer VEIN = 1;
        public static final Integer QR_CODE = 2;
        public static final Integer ID_CARD = 3;
        public static final Integer None = -1;
    }


    public static class VEIN_WORK_MODE {
        public static final Integer WAIT = 0;
        public static final Integer REGIST = 1;
        public static final Integer VERICATE = 2;
        public static final Integer DELETE = 3;
        public static final Integer SELF_CHECKING = 4;
    }

    public static class VEIN_STATUS_CODE {
        // Record failed code
        public static final Integer OUTOFTIME_WHEN_CAPIMG = 0;
        public static final Integer FINGER_LEFT_TOO_EARLY = 1;
        public static final Integer SAMPLE_QUALITY_NOT_GOOD = 2;
        public static final Integer VEIN_NO_MATCH = 3;


        // COMMON
        public static final Integer DR_OK = 0;
        public static final Integer UNKNOW_ERROR = -100;

        // initUSBDriver try
        public static final Integer FAILED_TO_INIT_USB = 0;

        // isFVDConnected try
        public static final Integer NO_DEVICE_FIND_WHEN_CHECK = 0;
        public static final Integer FOUND_THE_DEVICE_WHEN_CHECK = 1;

        // connFVD
        public static final Integer NO_DEVICE_FIND_WHEN_CONNECTTING = -1;
        public static final Integer NO_USB_PRIVIRLEGES_WHEN_CONNECTTING = -2;
        public static final Integer DEVICE_NOT_AUTH_WHEN_CONNECTTION = -3;

        // isFingerDetected & isFingerTouched
        public static final Integer NO_FINGER_DETECTED = 0;
        public static final Integer FINGER_DETECTED = 1;


        //deInitCapEnv
        public static final Integer ERROR_WHEN_DEINIT_CAP_ENV = -1;

        // isVeinImgOK
        public static final Integer ADJUST_BRIGHTNESS = 0;
        public static final Integer WAIT_FOR_FINGER_STABLE = 1;
        public static final Integer FINISH_SAMPLE_COLLECTION = 2;
        public static final Integer FINGER_LEFT = 3;

        // checkVeinSampleQuality
        public static final Integer INVALID_BRIGHTNESS = -1;
        public static final Integer FINGER_ROTATED = -2;
        public static final Integer SAMPLE_LESS_INFO = -3;
        public static final Integer SAMPLE_FORMAT_INVALID_WHEN_CHECK = -4;
        public static final Integer FINGER_POSTION_INVALID = -4;

        // grabVeinFeature
        public static final Integer SAMPLE_FORMAT_INVALID_WHEN_GRAB = -1;

        // vericateTwoVeinFeature
        public static final Integer COMPARE_MATCH = 1;
        public static final Integer COMPARE_NOT_MATCH = 0;
        public static final Integer SAMPLE_FORMAT_INVALID_WHEN_COMPARE = -1;

        // custom
        public static final int WELCOME_INFO = 0;
        public static final int SUCCESS_INFO = 1;
        public static final int FAILED_INFO = 2;
        public static final int CRASH_INFO = 3;
    }

    public static class RegisterMethod {
        public static final Integer WITH_CARD = 0;
        public static final Integer SCAN_CARD = 1;
        public static final Integer WITHOUT_CARD = 2;
        public static final Integer SIGN_IN_WITH_CARD = 3;
        public static final Integer SIGN_IN_SCAN_CARD = 4;
        public static final Integer SIGN_IN_WITHOUT_CARD = 5;
    }


    public static class ResponseCode {
        public final static Integer OK = 200;
        public final static Integer SERVER_EXCEPTION = 500;
        public final static Integer PARAMS_INCORRECT = 501;
        public final static Integer SERVER_OP_DB_ERROR = 502;
        public final static Integer SERVER_QUERY_ERROR = 503;
        public final static Integer IN_BLOCKED_LIST = 601;
    }
}
