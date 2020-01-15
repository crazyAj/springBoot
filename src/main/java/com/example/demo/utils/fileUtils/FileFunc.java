package com.example.demo.utils.fileUtils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FileFunc {

    private static Map<String, Properties> propsCache = new ConcurrentHashMap<>();
    private static FileMonitor fileMonitor;

    //初始化文件监听
    static {
        fileMonitor = new FileMonitor(1000L);
    }

    public FileFunc() {
    }

    /**
     * 加载 properties 或 xml 属性
     */
    public static String getPropValue(String filePath, String key) {
        //读取文件
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file = new File(FileUtil.class.getResource(filePath).getPath());
            } catch (Exception e) {
                log.error("文件不存在，或文件路径有误 filePath = {}", filePath);
                return "";
            }
        }

        //把文件加入监听器
        fileMonitor.addFile(file);
        //文件加入缓存
        if (!propsCache.containsKey(filePath)){
            cacheProps(file);
        }

        Properties props = propsCache.get(file.getPath());
        return props == null ? "" : props.getProperty(key, "");
    }

    /**
     * 动态加载 properties 或 xml 配置
     */
    public static Properties getProp(String filePath) {
        Properties prop = new Properties();

        //读取文件
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file = new File(FileUtil.class.getResource(filePath).getPath());
            } catch (Exception e) {
                log.error("文件不存在，或文件路径有误 filePath = {}", filePath);
                return prop;
            }
        }

        //把文件加入监听器
        fileMonitor.addFile(file);
        //文件加入缓存
        if (!propsCache.containsKey(filePath)){
            cacheProps(file);
        }

        prop = propsCache.get(file.getPath());
        return prop;
    }

    /**
     * 缓存配置文件
     */
    public static void cacheProps(File file) {
        try {
            String filePath = file.getPath();
            Resource resource = new FileSystemResource(filePath);
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            propsCache.put(filePath, props);
        } catch (Exception e) {
            log.error("----- FileFunc 读取配置文件异常 -----");
        }
    }

}
