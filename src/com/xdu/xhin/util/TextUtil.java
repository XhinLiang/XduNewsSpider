package com.xdu.xhin.util;

import com.xdu.xhin.Config;

/**
 * Created by xhinliang on 15-10-4.
 * Util
 */
public class TextUtil {
    private static TextUtil sTextUtil;

    private TextUtil() {
    }

    public static TextUtil getInstance() {
        if (sTextUtil == null)
            sTextUtil = new TextUtil();
        return sTextUtil;
    }

    public boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public String getAbsoluteUrl(String source) {
        if (source.startsWith(Config.VALUE_HTTP))
            return source;
        if (source.startsWith(Config.VALUE_BACKSPLASH))
            return Config.XDU_NEWS_URL + source;
        return Config.XDU_NEWS_URL + Config.VALUE_BACKSPLASH + source;
    }

    public String newLine() {
        String os = System.getProperty(Config.VALUE_OS_NAME);
        if (os.toLowerCase().startsWith(Config.VALUE_WINDOWS))
            return Config.PRINT_CRLF;
        return Config.PRINT_LF;
    }
}
