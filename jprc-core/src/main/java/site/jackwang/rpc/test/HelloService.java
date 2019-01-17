package site.jackwang.rpc.test;

import site.jackwang.rpc.test.domain.UserBo;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public interface HelloService {
    public UserBo sayHi(String name);

}
