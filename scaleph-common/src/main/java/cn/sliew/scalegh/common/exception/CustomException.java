package cn.sliew.scalegh.common.exception;

/**
 * @author gleiyu
 */
public class CustomException extends Exception {

    private static final long serialVersionUID = 8895017577371254716L;

    private String exceptionCode;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String code, String message) {
        super(message);
        this.exceptionCode = code;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
