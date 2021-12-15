package waeng.bootrestapi.common;

import java.lang.annotation.*;

/**
 * 响应结果被包装为ResponseResult
 *
 * @author waeng
 * @since 2021/12/15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ResponseResultWrapper {

    /**
     * 是否忽略
     */
    boolean ignore() default false;
}
