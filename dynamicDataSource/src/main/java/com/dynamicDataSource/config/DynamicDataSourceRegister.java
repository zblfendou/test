package com.dynamicDataSource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源注册
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
    private ConversionService conversionService = new DefaultConversionService();
    private PropertyValues dataSourcePropertyValues;
    //默认数据源
    private DataSource defaultDataSource;
    //自定义数据源
    private Map<String, DataSource> customDataSources = new HashMap<>();


    /**
     * 加载多数据源配置
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        logger.debug("DynamicDataSourceRegister.setEnvironment()");
        initDefaultDataSource(environment);
        initCustomDataSource(environment);
    }

    private void initCustomDataSource(Environment environment) {
        //读取配置文件获取更多数据源,也可以通过defaultDataSource读取数据库获取更多数据源
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "custom.datasource.");
        String names = propertyResolver.getProperty("names");
        String[] nameArray = names.split(",");
        Arrays.stream(nameArray).forEach(dePrefix -> {
            Map<String, Object> dsMap = propertyResolver.getSubProperties(dePrefix + ".");
            DataSource ds = buildDataSource(dsMap);
            customDataSources.put(dePrefix, ds);
            dataBinder(ds, environment);
        });
    }

    /**
     * 加载住数据源配置
     *
     * @param environment
     */
    private void initDefaultDataSource(Environment environment) {
        final RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "spring.dataSource.");
        HashMap<String, Object> dsMap = new HashMap<>();
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driverClassName", propertyResolver.getProperty("driverClassName"));
        dsMap.put("url", propertyResolver.getProperty("url"));
        dsMap.put("username", propertyResolver.getProperty("username"));
        dsMap.put("password", propertyResolver.getProperty("password"));
        defaultDataSource = buildDataSource(dsMap);
        dataBinder(defaultDataSource, environment);
    }

    /**
     * 创建DataSource
     *
     * @param dsMap
     * @return
     */
    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            String driverClassName = dsMap.get("driverClassName").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password);
            return factory.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为DataSource绑定更多数据
     *
     * @param dataSource
     * @param environment
     */
    private void dataBinder(DataSource dataSource, Environment environment) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);//false
        dataBinder.setIgnoreInvalidFields(false);//false
        dataBinder.setIgnoreUnknownFields(true);//true
        if (dataSourcePropertyValues == null) {
            Map<String, Object> rpr = new RelaxedPropertyResolver(environment, "spring.datasource").getSubProperties(".");
            HashMap<String, Object> values = new HashMap<>(rpr);
            //排除已经设置的属性
            values.remove("type");
            values.remove("driverClassName");
            values.remove("url");
            values.remove("username");
            values.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        logger.debug("DynamicDataSourceRegister.registerBeanDefinitions()");
        HashMap<String, Object> targetSources = new HashMap<>();
        //将主数据源添加到更多数据中
        targetSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        //添加更多数据源
        targetSources.putAll(customDataSources);
        DynamicDataSourceContextHolder.dataSourceIds.addAll(customDataSources.keySet());
        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //添加属性:AbstractRoutingDataSource.defaultTargetDataSource
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }

}
