package waeng.bootrestapi.common;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import waeng.bootrestapi.domain.ResponseResult;
import waeng.bootrestapi.util.JsonUtil;

/**
 * @author waeng
 * @since 2021/12/15
 */
@RestControllerAdvice
public class ResponseResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ResponseResultWrapper annotation = returnType.getMethodAnnotation(ResponseResultWrapper.class);
        if (annotation == null) {
            annotation = returnType.getContainingClass().getAnnotation(ResponseResultWrapper.class);
        }
        return annotation != null && !annotation.ignore();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseResult) {
            return body;
        }

        ResponseResult<Object> result = new ResponseResult<>();
        result.setErrCode(ErrorCode.OK.getCode());
        result.setData(body);

        // todo 改为调用链 ID
        result.setTraceId(MDC.get(TraceIdFilter.TRACE_ID));

        if (String.class.isAssignableFrom(returnType.getMethod().getReturnType())) {
            return JsonUtil.toJson(result);
        } else {
            return result;
        }
    }
}
