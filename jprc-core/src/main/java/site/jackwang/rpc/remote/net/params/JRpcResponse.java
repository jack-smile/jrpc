package site.jackwang.rpc.remote.net.params;

import lombok.Data;

import java.io.Serializable;

/**
 * 应答包
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
@Data
public class JRpcResponse implements Serializable {
    private static final long serialVersionUID = 5610328506366601035L;

    /**
     * 包唯一id
     */
    private String id;

    /**
     * 返回信息，包含错误信息
     */
    private String message;

    /**
     * 结果
     */
    private Object result;
}
