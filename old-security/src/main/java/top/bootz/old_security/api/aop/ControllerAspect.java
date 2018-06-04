package com.yxt.oauth.api.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.qiniu.util.Auth;
import com.yxt.common.annotation.EventLog;
import com.yxt.common.exception.ApiException;
import com.yxt.common.util.APIUtil;

import top.bootz.old_security.api.common.APIConstants;
import top.bootz.old_security.api.controller.BaseController;

/**
 * ControllerAspect
 */
@Aspect
public class ControllerAspect extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(ControllerAspect.class);   
    private static final ThreadLocal<TimeBean> threadLocal = new ThreadLocal<TimeBean>();
    /**
     * controllerMethodCall
     */
    @Pointcut("execution(public * com.yxt.oauth.api.controller.*.*(..))")
    public void controllerMethodCall() {
    }

    /**
     * logSuccessEvent
     *
     * @param joinPoint JoinPoint
     */
    @AfterReturning(value = "controllerMethodCall()")
    public void logSuccessEvent(JoinPoint joinPoint) {
        Method method = getControllerMethod(joinPoint);
        EventLog eventLog = method.getAnnotation(EventLog.class);
        if (eventLog != null) {
            HttpServletRequest request = getRequestArg(joinPoint.getArgs());
            if (request != null) {
                String description = "";
                String paraName = eventLog.parameter();
                if (StringUtils.isNotBlank(paraName)) {
                    description = request.getParameter(paraName);
                    if (description == null) {
                        description = "";
                    }
                }
                String result = getEventStr(eventLog.action(), eventLog.value(), "", request, description);
                LOGGER.trace(result);
            }
        }
    }

    /**
     * logFailEvent
     *
     * @param joinPoint JoinPoint
     * @param ex        Exception
     */
    @AfterThrowing(value = "controllerMethodCall()", throwing = "ex")
    public void logFailEvent(JoinPoint joinPoint, Exception ex) {
        Method method = getControllerMethod(joinPoint);
        EventLog eventLog = method.getAnnotation(EventLog.class);
        if (eventLog != null && ex instanceof ApiException) {
            HttpServletRequest request = getRequestArg(joinPoint.getArgs());
            if (request != null) {
                String result = getEventStr(eventLog.action(), eventLog.value(),
                        ((ApiException) ex).getErrorKey(), request, "");
                LOGGER.trace(result);
            }
        }
    }

    private HttpServletRequest getRequestArg(Object[] args) {
        HttpServletRequest request = null;
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                    break;
                }
            }
        }
        return request;
    }

    /**
     * authorize
     *
     * @param joinPoint JoinPoint
     */
    @Before(value = "controllerMethodCall()")
    public void authorize(JoinPoint joinPoint) {
    	threadLocal.set(new TimeBean(System.currentTimeMillis(), APIUtil.now()));
        Method method = getControllerMethod(joinPoint);
        Auth auth = method.getAnnotation(Auth.class);
        if (auth != null) {
            HttpServletRequest request = getRequestArg(joinPoint.getArgs());
            if (request != null) {
                String token = request.getHeader(APIConstants.HEADER_NAME_TOKEN);
                verifyToken(token);
                if (auth.authorize()) {
                    checkPermission(auth.value(), auth.action(), token);
                }
            }
        }
    }

    @After(value = "controllerMethodCall()")
    public void logTime(JoinPoint joinPoint) {
        Method method = getControllerMethod(joinPoint);
        String methodName = method.getName();
        TimeBean tb = threadLocal.get();
        long exeCuteTime = System.currentTimeMillis() - tb.getStart();
        HttpServletRequest request = getRequestArg(joinPoint.getArgs());
        if (request != null) {
            LOGGER.debug(tb.getStartTime() + " " + request.getRequestURI() + " " + methodName + " " + exeCuteTime + "ms");
        }
        threadLocal.set(null);
    }
    
    private Method getControllerMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
