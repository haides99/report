package me.quanli.commons.util;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExcelUtils {

    private final static Log LOGGER = LogFactory.getLog(ExcelUtils.class);

    public static String encodeFilename(String filename,
            HttpServletRequest request) {
        String agent = request.getHeader("USER-AGENT");
        String finalfilename = "";
        try {
            if (agent != null
                    && StringUtils.contains(agent, "Mozilla")
                    && (StringUtils.contains(agent, "Firefox")
                            || StringUtils.contains(agent, "Chrome") || StringUtils
                                .contains(agent, "Safari"))) {// google,Firefox,Safari
                finalfilename = new String(filename.getBytes(), "ISO8859-1");
            } else {
                finalfilename = URLEncoder.encode(filename, "UTF8");
            }
            return finalfilename;
        } catch (Exception ex) {
            LOGGER.error(ex, ex);
            return filename;
        }
    }

}
