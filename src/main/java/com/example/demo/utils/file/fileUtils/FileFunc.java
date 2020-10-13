package com.example.demo.utils.file.fileUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class FileFunc {

    public static Map<String, Properties> propsCache = new ConcurrentHashMap<>();
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
    public static String getPropValue(String resPath, String key) {
        try {
            //读取文件
            Resource resource = getResource(resPath);
            if (!resource.exists()) {
                log.error("资源不存在 filePath = {}", resPath);
                return "";
            }

            //把文件加入监听器
            fileMonitor.addRes(resource);

            //文件加入缓存
            if (!propsCache.containsKey(resource.getURL().toString())) {
                cacheProps(resource);
            }

            Properties props = propsCache.get(resource.getURL().toString());
            return props == null ? "" : props.getProperty(key, "");
        } catch (Exception e) {
            log.error("获取参数异常 {}", e);
            return "";
        }
    }

    /**
     * 动态加载 properties 或 xml 配置
     */
    public static Properties getProp(String resPath) {
        Properties prop = new Properties();
        try {
            //读取文件
            Resource resource = getResource(resPath);
            if (!resource.exists()) {
                log.error("资源不存在 filePath = {}", resPath);
                return prop;
            }

            //把文件加入监听器
            fileMonitor.addRes(resource);

            //文件加入缓存
            if (!propsCache.containsKey(resource.getURL().toString())) {
                cacheProps(resource);
            }

            prop = propsCache.get(resource.getURL().toString());
        } catch (Exception e) {
            log.error("获取属性异常 {}", e);
        }
        return prop;
    }

    /**
     * 缓存配置
     */
    public static void cacheProps(Resource resource) {
        try {
            propsCache.put(resource.getURL().toString(), getProperties(resource));
        } catch (Exception e) {
            log.error("FileFunc 读取配置文件异常 {}", e);
        }
    }

    /**
     * 获取资源 (分为：绝对路径 和 相对路径)
     *
     * @param filePath
     * @return
     */
    public static Resource getResource(String filePath) {
        // 读取绝对路径
        Resource resource = new FileSystemResource(filePath);
        // 读取相对路径
        if (!resource.exists()) {
            String path;
            if (filePath.indexOf("classes") == -1) path = filePath;
            else {
                String classesPath = filePath.substring(filePath.indexOf("classes"));
                path = classesPath.substring(classesPath.indexOf(File.separator) + 1);
            }
            resource = new ClassPathResource(path);
        }
        return resource;
    }

    /**
     * 读取配置文件
     * jar包内 resource.getURL().openStream()会有缓存，所以用 JarFile 读取
     * getClass().getClassLoader().getResourceAsStream() 也会有缓存
     *
     * @param resource
     * @return
     * @throws IOException
     */
    private static Properties getProperties(Resource resource) throws Exception {
        Properties props;
        String url = resource.getURL().toString();
        // 读取jar包外部配置
        if (!url.startsWith("jar")) {
            try (InputStream is = resource.getURL().openStream()) {
                props = parseFile(resource.getFilename(), is);
            }
        } else { // 读取jar包内部配置
            // 获取 jar
            String prefix = "file:";
            String subfix = ".jar";
            String path = FileFunc.class.getProtectionDomain().getCodeSource().getLocation().toString();
            String classPath = path.substring(path.indexOf(prefix) + prefix.length());
            String jarFilePath = classPath.substring(0, classPath.indexOf(subfix)).concat(subfix);
            JarFile jarFile = new JarFile(jarFilePath);

            // 获取文件
            String temp = url.substring(url.indexOf(subfix));
            String entity = temp.substring(temp.indexOf("/") + 1).replaceAll("\\!", "");
            JarEntry jarFileEntry = jarFile.getJarEntry(entity);

            InputStream is = null;
            try {
                is = jarFile.getInputStream(jarFileEntry);
                props = parseFile(resource.getFilename(), is);
            } finally {
                if (is != null) is.close();
                if (jarFile != null) jarFile.close();
            }
        }
        return props;
    }

    private static Properties parseFile(String fileName, InputStream is) throws IOException {
        Properties props = new Properties();
        if (fileName.endsWith(".xml")) props.loadFromXML(is);
        else if (fileName.endsWith(".properties")) props.load(is);
        return props;
    }
}