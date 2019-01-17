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
     * 调用的serviceName的方法名
     */
    private String methodName;

    private double param1;
    private double param2;
}
