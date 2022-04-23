package com.liyu.breeze.api.aspect;

import com.liyu.breeze.api.util.SecurityUtil;
import com.liyu.breeze.service.dto.BaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * 拦截service层的insert 和update方法，修改参数中的人员和时间字段数据
 *
 * @author gleiyu
 */
@Slf4j
@Aspect
@Component
public class InsertUpdateAspect {
    @Pointcut("execution(* com.liyu.breeze.service..*.insert*(..))")
    public void insertPointCut() {
    }

    @Pointcut("execution(* com.liyu.breeze.service..*.update*(..))")
    public void updatePointCut() {

    }

    @Around("insertPointCut()")
    public Object beforeInsert(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("before insert method:" + joinPoint.getSignature().getDeclaringTypeName());
        Object[] args = joinPoint.getArgs();
        String userName = SecurityUtil.getCurrentUserName();
        for (Object arg : args) {
            Date date = new Date();
            if (!arg.getClass().isPrimitive() && arg.getClass().getSuperclass() == BaseDTO.class) {
                //非基础数据类型
                Field[] fields = arg.getClass().getSuperclass().getDeclaredFields();
                for (Field field : fields) {
                    try {
                        if ("creator".equals(field.getName())) {
                            BeanUtils.setProperty(arg, field.getName(), StringUtils.isEmpty(userName) ? "sys" : userName);
                        }
                        if ("editor".equals(field.getName())) {
                            BeanUtils.setProperty(arg, field.getName(), StringUtils.isEmpty(userName) ? "sys" : userName);
                        }
                        if ("createTime".equals(field.getName())) {
                            BeanUtils.setProperty(arg, field.getName(), date);
                        }
                        if ("updateTime".equals(field.getName())) {
                            BeanUtils.setProperty(arg, field.getName(), date);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }

    @Around("updatePointCut()")
    public Object beforeUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("before update method" + joinPoint.getSignature().getDeclaringTypeName());
        Object[] args = joinPoint.getArgs();
        String userName = SecurityUtil.getCurrentUserName();
        for (Object arg : args) {
            Date date = new Date();
            if (!arg.getClass().isPrimitive() && arg.getClass().getSuperclass() == BaseDTO.class) {
                //非基础数据类型
                Field[] fields = arg.getClass().getSuperclass().getDeclaredFields();
                for (Field field : fields) {
                    try {
                        if ("editor".equals(field.getName())) {
                            BeanUtils.setProperty(arg, field.getName(), StringUtils.isEmpty(userName) ? "sys" : userName);
                        }
                        if ("updateTime".equals(field.getName())) {
                            BeanUtils.setProperty(arg, field.getName(), date);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
