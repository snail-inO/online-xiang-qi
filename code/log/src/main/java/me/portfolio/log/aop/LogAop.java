package me.portfolio.log.aop;

import me.portfolio.log.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class LogAop {
    private final static Logger LOGGER = LoggerFactory.getLogger(LogAop.class);

    @Around(value = "@annotation(me.portfolio.log.aop.Logging)")
    public Object methodLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        HttpServletRequest req = attributes.getRequest();
        Log logData = setLog(joinPoint, req);
        Object res;
        LOGGER.info("Before method: " + logData);
        try {
            res = joinPoint.proceed();
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter out = new PrintWriter(stringWriter);
            e.printStackTrace(out);
            logData.setLogType(Log.EXCEPTION_LOG);
            logData.setExceptionDetail(stringWriter.toString());
            LOGGER.error(logData.toString());
            throw e;
        }

        LOGGER.info("After method: " + res);
        return res;
    }

    private Log setLog(ProceedingJoinPoint joinPoint, HttpServletRequest req) {
        Date time = new Date();
        String method = joinPoint.getSignature().getName();
        StringBuilder builder = new StringBuilder();

        Arrays.stream(joinPoint.getArgs()).sequential().forEach(arg -> {
            builder.append(arg.getClass().getName());
            builder.append(" = ");
            builder.append(arg);
            builder.append(", ");
        });
        String params = builder.toString();
        String ip = req.getRemoteAddr();
        String addr = req.getRequestURI();

        String browser = req.getHeader("User-Agent");

        return new Log(time.getTime(), method + " Log", Log.OPERATION_LOG, method, params, ip, time.getTime(), "test",
                addr, browser, null, time);
    }
}
