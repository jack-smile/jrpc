package site.jackwang.rpc.registry;

/**
 * 注册
 *
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/30
 */
public interface RegistryServer {
    /**
     * 向注册中心注册服务器信息
     *
     * @param serverName 服务器名称（唯一）
     * @param address    服务器地址，格式：ip:port
     * @return true：注册成功；失败返回异常
     */
    boolean register(String serverName, String address);

    /**
     * 从注册中心剔除注册服务器信息
     *
     * @param serverName 服务器名称（唯一）
     * @param address    服务器地址，格式：ip:port
     * @return true：剔除成功；失败返回异常
     */
    boolean remove(String serverName, String address);
}
