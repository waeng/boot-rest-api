package waeng.bootrestapi.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author waeng
 * @since 2021/12/15
 */
public enum ErrorCode {

    OK(0),

    // 客户端错误
    CLIENT_ERROR(20000),

    // 服务端错误
    SYSTEM_EXECUTION_ERROR(40000),

    ;

    @Getter
    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    private static final Map<Integer, ErrorCode> VALUES = new HashMap<>();

    static {
        for (ErrorCode v : ErrorCode.values()) {
            VALUES.put(v.getCode(), v);
        }
    }

    public static ErrorCode of(int code) {
        return VALUES.get(code);
    }
}

