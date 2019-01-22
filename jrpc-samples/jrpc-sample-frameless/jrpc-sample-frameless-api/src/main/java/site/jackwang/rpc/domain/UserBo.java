package site.jackwang.rpc.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjie<http://www.jackwang.site/>
 * @date 2019/1/17
 */
@Data
@Builder
public class UserBo implements Serializable {
    private static final long serialVersionUID = -4341178281514862942L;

    private String name;
    private String word;

    public UserBo(String name, String word) {
        this.name = name;
        this.word = word;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "name='" + name + '\'' +
            ", word='" + word + '\'' +
            '}';
    }
}
