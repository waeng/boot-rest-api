package waeng.bootrestapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import waeng.bootrestapi.common.ErrorCode;

/**
 * @author waeng
 * @since 2021/12/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class ResponseResult<T> {
    /**
     * 调用链ID
     */
    private String traceId;

    /**
     * 错误码
     *
     * @see ErrorCode
     */
    private int errCode;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * <p>
     * 提示给用户的信息
     */
    private String tips;

    /**
     * 响应数据
     */
    private T data;
}

