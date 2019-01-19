package site.jackwang.rpc.service;


import site.jackwang.rpc.domain.UserBo;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public interface HelloService {
    public UserBo sayHi(String name);

}
