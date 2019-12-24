package com.example.demo.utils.ds;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面类，对mapper层拦截，包括拦截了mybatisplus的公共BaseMapper
 */
@Component
@Aspect
@Order(-1)
public class DataSourceAspect {

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
        String dataSource = DataSourceEnum.getByMethod(method);
        // 设置数据源
        DataSourceContextHolder.setDataSource(dataSource);
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
        // 获取数据源
        String dataSource = DataSourceEnum.getByMethod(method);
        // 设置数据源
        DataSourceContextHolder.setDataSource(dataSource);
        // 调用批处理方法
        Object res = proceedingJoinPoint.proceed();
        // 清空数据源上下文
        DataSourceContextHolder.clear();
        return res;
    }

}
