package com.example.demo.utils.dataSource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面类，对mapper层拦截，包括拦截了mybatisplus的公共BaseMapper
 * <p>
 * 设置数据源，优先级：
 * 1. 手动 DataSourceContextHolder.getContextHolder().set(DataSourceEnum.SLAVE.getValue());
 * 2. 注解 @DataSource(DataSourceEnum.MASTER)
 * 3. 方法名
 */
@Component
@Aspect
@Order(-100)
public class DataSourceAspect {

    @Autowired
    private CustomSqlSessionTemplate sqlSessionTemplate;

    /**
     * 普通 CRUD 切面
     */
    @Pointcut("execution(* com.example.demo.dao.*Mapper.*(..))||execution(* com.baomidou.mybatisplus.core.mapper.*Mapper.*(..))")
    public void pointCut() {
    }

    /**
     * mybatisplus 批量操作切面
     */
    @Pointcut("execution(* com.baomidou.mybatisplus.extension.service.IService.*Batch*(..))")
    public void pointCutBatch() {
    }

    /**
     * 普通 CRUD
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object doSimple(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取数据源
        if (StringUtils.isEmpty(DataSourceContextHolder.getDataSource())) {
            String dataSource = DataSourceEnum.getByMethod(method);
            // 设置数据源
            DataSourceContextHolder.setDataSource(dataSource);
        }
        // 调用方法
        Object res = proceedingJoinPoint.proceed();
        // 清空数据源上下文
        DataSourceContextHolder.clear();
        return res;
    }

    /**
     * mybatisplus 批量操作
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCutBatch()")
    public Object doBatch(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        if (StringUtils.isEmpty(DataSourceContextHolder.getDataSource())) {
            // 获取数据源
            String dataSource = DataSourceEnum.getByMethod(method);
            // 设置数据源
            DataSourceContextHolder.setDataSource(dataSource);
        }
        // 调用批处理方法
        Object res = proceedingJoinPoint.proceed();
        // 清空数据源上下文
        DataSourceContextHolder.clear();
        return res;
    }

}
