package com.liyu.breeze.api.security;

import com.liyu.breeze.api.util.I18nUtil;
import com.liyu.breeze.api.vo.ResponseVO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证后的用户访问无权限资源时返回403提示
 *
 * @author gleiyu
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException accessDeniedException) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        ResponseVO info = ResponseVO.error(String.valueOf(HttpServletResponse.SC_FORBIDDEN), I18nUtil.get("response.error.no.privilege"));
        out.write(info.toString());
        out.flush();
        out.close();
    }
}
