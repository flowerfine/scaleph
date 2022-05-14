package cn.sliew.breeze.api.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.OS;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.breeze.api.util.SecurityUtil;
import cn.sliew.breeze.common.constant.Constants;
import cn.sliew.breeze.common.constant.DictConstants;
import cn.sliew.breeze.common.enums.LoginTypeEnum;
import cn.sliew.breeze.service.admin.ActionLogService;
import cn.sliew.breeze.service.admin.LoginLogService;
import cn.sliew.breeze.service.dto.admin.LogActionDTO;
import cn.sliew.breeze.service.dto.admin.LogLoginDTO;
import cn.sliew.breeze.service.vo.DictVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 使用aop记录用户操作日志数据
 *
 * @author gleiyu
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Autowired
    private ActionLogService actionLogService;
    @Autowired
    private LoginLogService loginLogService;

    /**
     * 配置拦截Logging注解所有方法
     */
    @Pointcut("@annotation(cn.sliew.breeze.api.annotation.Logging)")
    public void actionPointCut() {

    }

    /**
     * 登录切面
     */
    @Pointcut("execution(* cn.sliew.breeze.api.controller.admin.UserController.login(..))")
    public void loginPointCut() {

    }

    /**
     * 登出切面
     */
    @Pointcut("execution(* cn.sliew.breeze.api.controller.admin.UserController.logout(..))")
    public void logoutPointCut() {

    }

    /**
     * 记录操作日志
     *
     * @param joinPoint join point
     */
    @Around("actionPointCut()")
    public Object actionLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        insertActionLog(joinPoint, startTime);
        return result;
    }

    /**
     * 记录登录日志
     *
     * @param joinPoint joinPoint
     */
    @Around("loginPointCut()")
    public Object loginLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        insertLoginLog(result, startTime, DictVO.toVO(DictConstants.LOGIN_TYPE, LoginTypeEnum.LOGIN.getValue()));
        return result;
    }

    /**
     * 记录登出日志
     *
     * @param joinPoint joinPoint
     */
    @Around("logoutPointCut()")
    public Object logoutLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        insertLoginLog(result, startTime, DictVO.toVO(DictConstants.LOGIN_TYPE, LoginTypeEnum.LOGOUT.getValue()));
        return result;
    }

    /**
     * 获取当前请求
     *
     * @return httpServletRequest
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        return sra.getRequest();
    }

    /**
     * 获取请求header中的token数据
     *
     * @param request 请求
     * @return token
     */
    private String resolveToken(HttpServletRequest request) {
        return request.getHeader(Constants.TOKEN_KEY);
    }

    /**
     * 获取请求中的参数信息
     *
     * @param args 参数
     * @return 参数字符串 json格式
     * @throws JsonProcessingException 异常
     */
    private String getParameter(Object[] args) throws JsonProcessingException {
        List<Object> argList = new ArrayList<>();
        for (Object o : args) {
            if (o instanceof ServletRequest || o instanceof ServletResponse || o instanceof MultipartFile) {
                continue;
            }
            argList.add(o);
        }
        return argList.size() < 1 ? "" : JSONUtil.toJsonStr(argList);
    }

    /**
     * 插入操作日志
     *
     * @param joinPoint joinPoint返回结果或者异常
     * @param startTime 开始时间
     */
    private void insertActionLog(JoinPoint joinPoint, long startTime) {
        try {
            long endTime = System.currentTimeMillis();
            LogActionDTO log = new LogActionDTO();
            HttpServletRequest request = getRequest();
            log.setUserName(SecurityUtil.getCurrentUserName());
            log.setActionTime(new Date(startTime));
            String agentStr = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgentUtil.parse(agentStr);
            OS os = userAgent.getOs();
            Browser browser = userAgent.getBrowser();
            log.setIpAddress(ServletUtil.getClientIP(request));
            log.setActionUrl(request.getRequestURL().toString());
            log.setToken(resolveToken(request));
            log.setClientInfo(userAgent.getPlatform().getName());
            log.setOsInfo(os.getName());
            log.setBrowserInfo(browser.toString());
            //action info
            Map<String, Object> actionInfo = new HashMap<>(3);
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length >= 1 && !(args[0] instanceof Throwable)) {
                actionInfo.put("params", getParameter(joinPoint.getArgs()));
                actionInfo.put("result", "success");
            } else if (args != null && args.length >= 1) {
                actionInfo.put("result", "error");
                actionInfo.put("exception", JSONUtil.toJsonStr(((Throwable) args[0]).getMessage()));
            } else {
                actionInfo.put("result", "success");
            }
            actionInfo.put("method", request.getMethod());
            actionInfo.put("elapsed_time", endTime - startTime);
            log.setActionInfo(JSONUtil.toJsonStr(actionInfo));
            this.actionLogService.insert(log);
        } catch (Exception e) {
            log.error("操作日志记录失败！");
        }
    }

    /**
     * 插入登录日志
     *
     * @param result    操作返回结果或者异常
     * @param startTime 开始时间
     * @param loginType 登录/登出
     */
    private void insertLoginLog(Object result, long startTime, DictVO loginType) {
        try {
            long endTime = System.currentTimeMillis();
            LogLoginDTO log = new LogLoginDTO();
            HttpServletRequest request = getRequest();
            log.setUserName(SecurityUtil.getCurrentUserName());
            log.setLoginTime(new Date(startTime));
            String agentStr = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgentUtil.parse(agentStr);
            OS os = userAgent.getOs();
            Browser browser = userAgent.getBrowser();
            log.setIpAddress(ServletUtil.getClientIP(request));
            log.setLoginType(loginType);
            log.setClientInfo(userAgent.getPlatform().getName());
            log.setOsInfo(os.getName());
            log.setBrowserInfo(browser.toString());
            //action info
            Map<String, Object> actionInfo = new HashMap<>(3);
            actionInfo.put("data", JSONUtil.toJsonStr(result));
            actionInfo.put("method", request.getMethod());
            actionInfo.put("elapsed_time", endTime - startTime);
            log.setActionInfo(JSONUtil.toJsonStr(actionInfo));
            this.loginLogService.insert(log);
        } catch (Exception e) {
            log.error("操作日志记录失败！");
        }
    }
}
