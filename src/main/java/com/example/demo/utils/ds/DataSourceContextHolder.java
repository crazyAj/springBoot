package com.example.demo.utils.ds;

/**
 * 设置、获取、清除当前数据源
 * <p>
 * InheritableThreadLocal 父子线程可以共享参数，但是要注意线程只有在初始化才会调用 InheritableThreadLocal 复用
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static String getDataSource() {
        return contextHolder.get();
    }

    /**
     * 清空上下文数据
     */
    public static void clear() {
        contextHolder.remove();
    }

}
