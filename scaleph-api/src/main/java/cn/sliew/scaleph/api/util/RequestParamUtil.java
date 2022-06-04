package cn.sliew.scaleph.api.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public enum RequestParamUtil {
    ;

    private static final String SPLIT_STRING_M = "=";
    private static final String SPLIT_STRING_DOT = ", ";

    private static final List<String> IGNORE_PATH = Arrays.asList("/webjars/**",
            "/doc.html", "/swagger-resources", "/v3/api-docs", "/favicon.ico");
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * query param
     */
    public static String getRequestParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> enu = request.getParameterNames();
        //获取请求参数
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            sb.append(name + SPLIT_STRING_M).append(request.getParameter(name));
            if (enu.hasMoreElements()) {
                sb.append(SPLIT_STRING_DOT);
            }
        }
        return sb.toString();
    }

    /**
     * body param
     */
    public static String getRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    payload = "[unknown]";
                }
                return payload.replaceAll("\\n", "");
            }
        }
        return "";
    }

    public static boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    public static boolean ignorePath(String uri) {
        return IGNORE_PATH.stream()
                .filter(pattern -> ANT_PATH_MATCHER.match(pattern, uri))
                .findAny()
                .isPresent();
    }
}