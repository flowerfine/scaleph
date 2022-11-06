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

package cn.sliew.scaleph.system.vo;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "通用响应体", description = "通用响应体")
public class ResponseVO<T> {
    /**
     * operate result
     */
    private Boolean success;

    /**
     * result data
     */
    private T data;
    /**
     * error code
     */
    private String errorCode;
    /**
     * error message
     */
    private String errorMessage;
    /**
     * ErrorShowTypeEnum
     */
    private Integer showType;

    private ResponseVO() {
    }

    public static ResponseVO sucess() {
        ResponseVO info = new ResponseVO();
        info.setSuccess(true);
        return info;
    }

    public static ResponseVO sucess(Object data) {
        ResponseVO info = new ResponseVO();
        info.setSuccess(true);
        info.setData(data);
        return info;
    }

    /**
     * default error info
     *
     * @param message message
     * @return ResponseVO
     */
    public static ResponseVO error(String message) {
        ResponseVO info = new ResponseVO();
        info.setSuccess(false);
        info.setErrorCode(ResponseCodeEnum.ERROR.getCode());
        info.setErrorMessage(message);
        info.setShowType(ErrorShowTypeEnum.NOTIFICATION.getCode());
        return info;
    }

    public static ResponseVO error(ResponseCodeEnum info) {
        return error(info.getCode(), info.getValue());
    }

    public static ResponseVO error(String code, String message) {
        ResponseVO info = new ResponseVO();
        info.setSuccess(false);
        info.setErrorCode(code);
        info.setErrorMessage(message);
        info.setShowType(ErrorShowTypeEnum.NOTIFICATION.getCode());
        return info;
    }

    public static ResponseVO error(String code, String message, ErrorShowTypeEnum showType) {
        ResponseVO info = new ResponseVO();
        info.setSuccess(false);
        info.setErrorCode(code);
        info.setErrorMessage(message);
        info.setShowType(showType.getCode());
        return info;
    }


    @SneakyThrows
    @Override
    public String toString() {
        return JacksonUtil.toJsonString(this);
    }
}
