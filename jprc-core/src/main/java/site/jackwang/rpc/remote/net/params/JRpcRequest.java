package site.jackwang.rpc.remote.net.params;

import lombok.Data;

/**
 * 请求包
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/13
 */
@Data
public class JRpcRequest {
    /**
     * 包唯一id
     */
    private String id;

    /**
     * 调用的服务名
     */
    private String serviceName;

    /**
     * 调用的服务{@link JRpcRequest.serviceName}中的方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 参数值
     */
    private Object[] params = new Object[2];
}
