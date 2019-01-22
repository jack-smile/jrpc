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
    private static final long serialVersionUID = -8709202145603520018L;

    private String name;
    private String word;

    public UserBo() {
    }

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
