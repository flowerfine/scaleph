package cn.sliew.breeze.api.vo;

import cn.hutool.json.JSONUtil;
import cn.sliew.breeze.common.enums.ErrorShowTypeEnum;
import cn.sliew.breeze.common.enums.ResponseCodeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author gleiyu
 */
@Data
@ApiModel(value = "通用响应体", description = "通用响应体")
public class ResponseVO {
    /**
     * operate result
     */
    private Boolean success;

    /**
     * result data
     */
    private Object data;
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
        return JSONUtil.toJsonStr(this);
    }
}
