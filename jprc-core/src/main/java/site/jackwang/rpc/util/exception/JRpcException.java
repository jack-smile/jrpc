package site.jackwang.rpc.util.exception;

/**
 * rpc异常
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
public class JRpcException extends RuntimeException {
    private static final long serialVersionUID = -7255863963021578972L;

    public JRpcException(String msg) {
        super(msg);
    }

    public JRpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JRpcException(Throwable cause) {
        super(cause);
    }

}