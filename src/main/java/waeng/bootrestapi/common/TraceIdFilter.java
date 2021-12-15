package waeng.bootrestapi.common;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author waeng
 * @since 2021/12/15
 */
@Component
// 高于 LogbookFilter 优先级（Ordered.LOWEST_PRECEDENCE）
@Order(value = Ordered.LOWEST_PRECEDENCE - 1)
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        MDC.put(TRACE_ID, generateId());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    private String generateId() {
        final Random random = ThreadLocalRandom.current();
        // set most significant bit to produce fixed length string
        return Long.toHexString(random.nextLong() | Long.MIN_VALUE);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }
}
