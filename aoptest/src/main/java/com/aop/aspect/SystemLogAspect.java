package com.aop.aspect;

import com.aop.annotation.SystemControllerLog;
import com.aop.annotation.SystemServiceLog;
import com.aop.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 切点类
 */
@Aspect
@Named
public class SystemLogAspect implements Serializable {
    private static final long serialVersionUID = 8764566919257351950L;
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    //service层切点
    @Pointcut("@annotation(com.aop.annotation.SystemServiceLog)")
    public void serviceAspect() {
    }

    //controller层切点
    @Pointcut("@annotation(com.aop.annotation.SystemControllerLog)")
    public void controllerAspect() {
    }

    /**
     * 前置通知,用于拦截controller层记录用户的操作
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //请求的IP
        String ip = request.getRemoteAddr();
        //*===============控制台输出===============*//
        System.out.println("==========前置通知开始==========");
        System.out.println("请求方" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
        try {
            System.out.println("方法描述:" + getControllerMethodDescription(joinPoint));
            Object[] args = joinPoint.getArgs();
            System.out.println("请求人:" + args[0]);
            System.out.println("请求IP:" + ip);
        } catch (ClassNotFoundException ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
    }

    /**
     * 异常通知,用于拦截service层记录异常日志
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //请求的IP
        String ip = request.getRemoteAddr();
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    params = params + (objectMapper.writeValueAsString(arg) + ";");
                    System.out.println("=====异常通知开始=====");
                    System.out.println("异常代码:" + e.getClass().getName());
                    System.out.println("异常信息:" + e.getMessage());
                    System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
                    System.out.println("方法描述:" + getServiceMethodDescription(joinPoint));
                    System.out.println("请求人:" + args[0]);
                    System.out.println("请求IP:" + ip);
                    System.out.println("请求参数:" + params);
                } catch (JsonProcessingException | ClassNotFoundException ex) {
                    //记录本地异常日志
                    logger.error("==异常通知异常==");
                    logger.error("异常信息:{}", ex.getMessage());

                }
                 /*==========记录本地异常日志==========*/
                logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
            }
        }
    }


    /**
     * 获取注解中对方法的描述信息 用于controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    private String getControllerMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    private String getServiceMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    description = method.getAnnotation(SystemServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 指定切点
     * @param user
     */
    @Before("execution(* com.aop.services.UserService.add(..))&&args(user)")
    public void addBefore(User user) {
        System.out.println("指定切点, name:"+user.getName()+" ,pwd:"+user.getPwd());
    }

}
