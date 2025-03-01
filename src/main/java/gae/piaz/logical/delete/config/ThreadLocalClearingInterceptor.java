package gae.piaz.logical.delete.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class ThreadLocalClearingInterceptor implements HandlerInterceptor {

    private final CustomStatementInspector customStatementInspector;

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        log.trace("Clearing thread local request events");
        customStatementInspector.clear(); // Clear ThreadLocal events after request completion
    }
}
