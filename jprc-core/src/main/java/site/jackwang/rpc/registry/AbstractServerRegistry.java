package site.jackwang.rpc.registry;

import java.util.Map;

/**
 * 服务器注册器
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public abstract class AbstractServerRegistry implements RegistryServer, LookupServer {
    /**
     * 初始化，主要是初始化参数配置
     *
     * @param param 参数配置
     */
    public abstract void init(Map<String, String> param);

    /**
     * 开始
     */
    public abstract void start();

    /**
     * 结束
     */
    public abstract void stop();
}
