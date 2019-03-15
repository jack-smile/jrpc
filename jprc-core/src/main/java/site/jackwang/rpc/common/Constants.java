package site.jackwang.rpc.common;

import java.util.regex.Pattern;

/**
 * @author jie.wang001@bkjk.com
 * @date 2019/3/15
 */
public class Constants {
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

    public static final String GROUP_KEY = "group";

    public static final String INTERFACE_KEY = "interface";

    public static final String VERSION_KEY = "version";

    public static final String COMMA_SEPARATOR = ",";

}
