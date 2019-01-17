package site.jackwang.rpc.test.impl;

import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.jackwang.rpc.test.HelloService;
import site.jackwang.rpc.test.domain.UserBo;

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
