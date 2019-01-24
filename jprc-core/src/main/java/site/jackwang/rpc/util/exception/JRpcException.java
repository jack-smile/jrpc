package site.jackwang.rpc.util.exception;

import lombok.Getter;

/**
 * rpc异常
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
@Getter
public class JRpcException extends RuntimeException {
    private static final long serialVersionUID = -7255863963021578972L;

    private int errorCode;

    private String errorMessage;

    private Throwable targetException;

    public JRpcException(String msg) {
        super(msg);
    }

    public JRpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JRpcException(Throwable cause) {
        super(cause);
    }

    public JRpcException(ErrorCodes errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = String.format("%08d : %s.", getErrorCode(), errorCode.getDescription());
    }

    public JRpcException(ErrorCodes errorCode, Object...parameters) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = String.format("%08d : %s.", getErrorCode(),
            String.format(errorCode.getDescription(), parameters));
    }

    public JRpcException(Throwable targetException, ErrorCodes errorCode, Object...parameters) {
        this(errorCode, parameters);
        this.targetException = targetException;
    }
}