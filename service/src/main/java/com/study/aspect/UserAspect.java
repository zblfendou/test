package com.study.aspect;

import com.study.models.user.User;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.inject.Named;

/**
 * Created by zbl on 2017/8/21.
 */
@Named
@Aspect
public class UserAspect {
    @Before("execution(* com.study.services.user.UserService.save(..))&&args(user)")
    public void beforeAddUser(User user) {
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        System.out.println("beforeAddUser");
    }

    @Pointcut("execution(* com.study.services.user.UserService.save(..))")
    public void pointCut() {
    }

    @AfterReturning(pointcut = "pointCut()", returning = "returnUser")
    public void afterReturn(Object returnUser) {
        User user = (User)returnUser;
        System.out.println(user.getPassword());
        System.out.println(user.getName());
        System.out.println(user.getId());
        System.out.println("afterAddUser");
    }

    /*通知方法参数注入：
    在spring AOP中，除了execution和bean指示符不能传递参数给通知方法，其他指示符都可以将匹配的相应参数或对象自动传递给通知方法。
            1、获取匹配的参数
    通过"argNames"属性指定参数名。
            [html] view plain copy
    @Before(value="args(param)", argNames="param") //明确指定了
    public void beforeTest(String param) {
        System.out.println("param:" + param);
    }
    args、argNames的参数名与beforeTest()方法中参数名 保持一致，即param
    使用args指示符，可以不利用argNames声明参数
[html] view plain copy
    @Before("execution(public * com.learn..*.show(..)) && args(userId,..)")
    public void beforeTest(int userId) {
        System.out.println("userId:" + userId);
    }
    args的参数名与beforeTest()方法中参数名 保持一致，即param
2、获取匹配的对象
    例：
            [html] view plain copy
    @Pointcut(value="@annotation(t)",argNames = "t")
    public void annotationTest(Table t)
    {
    }

    //引用时带上类名
    @After(value = "annotationTest(t)")
    public void after(Table t)
    {
        System.out.println("调用LearnAspect的切入点pointcut()");
    }
    Table是一个自定义注解。要获取该对象，annotationTest方法要先在方法中定义添加该参数，再利用argNames声明，切入点表达式也要改为参数名。即t

    JoinPoint获取参数：
    Spring AOP提供使用org.aspectj.lang.JoinPoint类型获取连接点数据，任何通知方法的第一个参数都可以是JoinPoint(环绕通知是ProceedingJoinPoint，JoinPoint子类)。
            （1）JoinPoint：提供访问当前被通知方法的目标对象、代理对象、方法参数等数据
（2）ProceedingJoinPoint：只用于环绕通知，使用proceed()方法来执行目标方法
    如参数类型是JoinPoint、ProceedingJoinPoint类型，可以从“argNames”属性省略掉该参数名（可选，写上也对），这些类型对象会自动传入的，但必须作为第一个参数。
    例：
    切面：
            [html] view plain copy
    @Pointcut(value="args(p)")
    public void argsTest(int p)
    {
    }

    @Before(value="argsTest(p)",argNames="p")
    public void afterTest(JoinPoint jp,int p)
    {
        Object[] o = jp.getArgs();
        System.out.println(o[0]);
        System.out.println(p);
        System.out.println("args");
    }
    结果：
            [html] view plain copy
1  //利用JoinPoint获取参数
        1  //利用参数注入获取参数
    args
    This is showTime
    AfterReturning 获取请求返回值：
            [html] view plain copy
    @Pointcut("execution(public * com.learn.spring..*.*(*))")
    public void pointcut() {
    }

    *//*
     *配置后置通知返回，使用在方法pointcut()上注册的切入点
     *//*
    @AfterReturning(pointcut="pointcut()", returning="returnValue")
    public void afterReturn(Object returnValue)
    {
        System.out.println("afterReturn  "+returnValue);
    }
    注意：
            1、returning参数名与afterReturn（）方法参数名相同，即都为returnedValue.
2、AfterRetruning含有返回值时，不能和Around指示符使用同一个pointcut。返回值会为null，报错：
    org.springframework.aop.AopInvocationException:
    Null return value from advice does not match primitive return type for: ....

    AfterThrowing 获取请求抛出异常：
            [html] view plain copy
    @Pointcut("execution(public * com.learn.spring..*.*(*))")
    public void pointcut() {
    }
    *//*
     * 配置抛出异常后通知，使用在方法pointcut()上注册的切入点
     *//*
    @AfterThrowing(pointcut="pointcut()",throwing="ex")
    public void afterThrow(Exception ex)
    {
        System.out.println("afterThrow "+ex.getMessage());
    }

    throwing参数名与afterThrow()方法参数名相同，即ex*/
}
