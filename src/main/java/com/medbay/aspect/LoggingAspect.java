package com.medbay.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.medbay.util.Helper.asJsonString;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("within((com.medbay..*..*Controller))")
    public void logBefore(JoinPoint joinPoint) {
        if(joinPoint.getSignature().getName().equals("updateProfilePicture")) {
            return;
        } else if(joinPoint.getSignature().getName().equals("getLoggedInPatient")){
            return;
        }
        log.info("REQUEST RECEIVED - " + getRequest(joinPoint));
    }

    private String getRequest(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName() + arrToString(joinPoint.getArgs());
    }

    private String arrToString(Object[] args) {
        if(args == null | args.length == 0 || args[0] == null) {
            return "";
        }
        return " - parametars: " + asJsonString(args);
    }

    @AfterReturning(pointcut = "within((com.medbay..*..*Controller))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        if(joinPoint.getSignature().getName().equals("updateProfilePicture")) {
            return;
        } else if(joinPoint.getSignature().getName().equals("getLoggedInPatient")){
            return;
        }
        log.info("RESPONSE RETURNED - " + getResponse(joinPoint, result));
    }

    private String getResponse(JoinPoint joinPoint, Object result) {
        return joinPoint.getSignature().getName() + resultToString(result);
    }

    private String resultToString(Object result) {
        if(result == null) {
            return "";
        }
        return " - result: " + asJsonString(result);
    }
}
