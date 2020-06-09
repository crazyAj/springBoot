package com.example.demo.utils.fileUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

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
    private static Resource getResource(String filePath) {
        // 读取绝对路径
        Resource resource = new FileSystemResource(filePath);
        // 读取相对路径
        if (!resource.exists()) {
            String path;
            if (filePath.indexOf("classes") == -1) {
                path = filePath;
            } else {
                String classesPath = filePath.substring(filePath.indexOf("classes"));
                path = classesPath.substring(classesPath.indexOf(File.separator) + 1);
            }
            resource = new ClassPathResource(path);
        }
        return resource;
    }

    /**
     * 读取配置文件
     * URL.openStream() 不会有缓存问题，而 Resource.getInputStream() 会有缓存问题。
     * 因为 resource = new ClassPathResource(path) 中会调用 Class.getResourceAsStream()
     * <p>
     * Class.getResourceAsStream() 会先到缓存中读取文件，若缓存中没有，才会到真正的路径下去读取文件。所以用getResourceAsStream方法获取配置文件时，获取的不是最新配置！！！！
     * URL.openStream() 直接读文件，所以在读文件频繁时会造成一定性能损耗；但能够确保获取的配置信息是最新的
     *
     * @param resource
     * @return
     * @throws IOException
     */
    private static Properties getProperties(Resource resource) throws IOException {
        Properties props = new Properties();

        String path = resource.getURL().toString();
        log.info("--- path = {}", path);
        URL url;
        if (!path.startsWith("jar")) {
            url = resource.getURL();
            log.info("11111111");
        } else {
            String classesPath = path.substring(path.indexOf("classes"));
            String relPath = classesPath.substring(classesPath.indexOf(File.separator) + 1);
            url = FileFunc.class.getClassLoader().getResource(relPath);


            Properties prop1 = new Properties();
            prop1.load(url.openStream());
            log.info("1  --- {}", prop1);

            Properties prop2 = new Properties();
            prop2.load(new ClassPathResource(relPath).getURL().openStream());
            log.info("2  --- {}", prop2);

            Properties prop3 = new Properties();
            prop3.load(FileFunc.class.getResourceAsStream(File.separator + relPath));
            log.info("3  --- {}", prop3);

            Properties prop4 = new Properties();
            File file = new File(File.separator + relPath);
            prop4.load(new FileInputStream(ResourceUtils.getFile("classpath:" + relPath)));
            log.info("4  --- {}", prop4);

        }

        InputStream is = url.openStream();
        try {
            String filename = resource.getFilename();
            if (filename != null && filename.endsWith(".xml")) {
                props.loadFromXML(is);
            } else {
                props.load(is);
            }
        } finally {
            is.close();
        }
        log.info("res  --- {}", props);
        return props;
    }

}
