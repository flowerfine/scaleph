package cn.sliew.scaleph.common.exception;

/**
 * @author gleiyu
 */
public class CustomException extends Exception {

    private static final long serialVersionUID = 8895017577371254716L;

    private final String exceptionCode;

    public CustomException(String message) {
        this(null, message);
    }

    public CustomException(String code, String message) {
        super(message);
        this.exceptionCode = code;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }
}
