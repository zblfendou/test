package com.dynamicDataSource.aspect;

import com.dynamicDataSource.config.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.inject.Named;

/**
 * 切换数据源Advice
 */
@Aspect
@Named
@Order(-10)//保证该AOP在@Transactional之前执行
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        //获取当前指定的数据源
        String dsId = targetDataSource.value();
        //获取执行方法
        final Signature signature = joinPoint.getSignature();
        //如果不在我们注入的数据源范围内,那么输出警告信息,系统自动使用默认的数据源
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            logger.error("数据源[{}]不存在,请使用默认数据源>{}" , dsId , signature);
        } else {
            logger.error("使用数据源:{}>{}" , dsId , signature);
            DynamicDataSourceContextHolder.setDataSourceType(dsId);
        }
    }

    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        logger.debug("revert DataSource:{}>{}" , targetDataSource.value(), joinPoint.getSignature());
        //执行完成以后销毁当前数据源信息,进行垃圾回收
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
