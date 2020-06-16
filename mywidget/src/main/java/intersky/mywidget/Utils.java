package intersky.mywidget;


import org.apache.commons.lang3.StringEscapeUtils;

public class Utils {

    public static String unescapeHtml4(String value) {
        return StringEscapeUtils.unescapeHtml4(value);
    }

}
