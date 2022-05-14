package cn.sliew.breeze.api.security;

import cn.sliew.breeze.api.util.I18nUtil;
import cn.sliew.breeze.api.vo.ResponseVO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登录用户访问无权限资源时候返回未鉴权信息，前端跳转登录页面
 *
 * @author gleiyu
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException {

        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        ResponseVO info = ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), I18nUtil.get("response.error.unauthorized"));
        out.write(info.toString());
        out.flush();
        out.close();
    }
}
