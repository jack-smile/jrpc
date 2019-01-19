package site.jackwang.rpc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.domain.UserBo;
import site.jackwang.rpc.service.HelloService;

import java.text.MessageFormat;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
public class HelloServiceImpl implements HelloService {
    private static Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public UserBo sayHi(String name) {
        String word = MessageFormat.format("Hi {0}, from {1} as {2}",
            name, HelloServiceImpl.class.getName(), String.valueOf(System.currentTimeMillis()));

        if ("error".equalsIgnoreCase(name)) throw new RuntimeException("test exception.");

        UserBo user = new UserBo(name, word);
        logger.info(user.toString());

        return user;
    }
}
