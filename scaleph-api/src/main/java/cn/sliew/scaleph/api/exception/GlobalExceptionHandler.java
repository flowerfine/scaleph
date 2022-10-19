/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.api.exception;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.util.RequestParamUtil;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.system.util.I18nUtil;
import cn.sliew.scaleph.system.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gleiyu
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * springmvc request param convert to request mapping param exception
     */
    @Logging
    @ResponseBody
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ResponseVO> conversionFailed(ConversionFailedException e,
                                                       HandlerMethod method,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("{} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        final TypeDescriptor sourceType = e.getSourceType();
        final TypeDescriptor targetType = e.getTargetType();
        final Object value = e.getValue();
        ResponseVO errorInfo = ResponseVO.error(String.format("springmvc convert %s from %s to %s error",
                value, sourceType.getName(), targetType.getName()));
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    @Logging
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseVO> bindException(BindException e,
                                                    HandlerMethod method,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("[{}] {} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getFieldErrors()) {
            String message = String.format("server reject [%s] value [%s] with rules: %s;",
                    fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            sb.append(message);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        ResponseVO errorInfo = ResponseVO.error(sb.toString());
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    @Logging
    @ResponseBody
    @ExceptionHandler(ScalephException.class)
    public ResponseEntity<ResponseVO> custom(ScalephException e,
                                             HandlerMethod method,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("[{}] {} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        ResponseVO errorInfo;
        if (StringUtils.hasText(e.getExceptionCode())) {
            errorInfo = ResponseVO.error(e.getExceptionCode(), e.getMessage());
        } else {
            errorInfo = ResponseVO.error(e.getMessage());
        }
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    @Logging
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseVO> exception(Exception e,
                                                HandlerMethod method,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("[{}] {} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        ResponseVO errorInfo = ResponseVO.error(I18nUtil.get(ResponseCodeEnum.ERROR.getValue()));
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    @Logging
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseVO> accessDenied(AccessDeniedException e,
                                                   HandlerMethod method,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("[{}] {} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        ResponseVO errorInfo = ResponseVO.error(ResponseCodeEnum.ERROR_NO_PRIVILEGE.getCode(),
                I18nUtil.get(ResponseCodeEnum.ERROR_NO_PRIVILEGE.getValue()));
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    /**
     * primary key duplicate
     */
    @Logging
    @ResponseBody
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ResponseVO> duplicateKey(DuplicateKeyException e,
                                                   HandlerMethod method,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("[{}] {} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        ResponseVO errorInfo = ResponseVO.error(ResponseCodeEnum.ERROR_DUPLICATE_DATA.getCode(),
                I18nUtil.get(ResponseCodeEnum.ERROR_DUPLICATE_DATA.getValue()));
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

    @Logging
    @ResponseBody
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ResponseVO> mail(MailException e,
                                           HandlerMethod method,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        String params = RequestParamUtil.formatRequestParams(request);
        log.error("[{}] {} {} {}", SecurityUtil.getCurrentUserName(), request.getMethod(), request.getRequestURI(), params, e);
        ResponseVO errorInfo = ResponseVO.error(ResponseCodeEnum.ERROR_EMAIL.getCode(),
                I18nUtil.get(ResponseCodeEnum.ERROR_EMAIL.getValue()));
        return new ResponseEntity<>(errorInfo, HttpStatus.OK);
    }

}
