package com.example.demo.utils.fileUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

@Slf4j
public class FileMonitor {

    private Timer timer = new Timer(true);
    public static Map<Resource, Long> resCache = new HashMap<>();

    public FileMonitor() {
    }

    /**
     * 初始化定时器
     *
     * @param pollingInterval
     */
    public FileMonitor(long pollingInterval) {
        timer.schedule(new FileMonitorTimer(), 0L, pollingInterval);
    }

    /**
     * 在监听器里面添加资源
     */
    public void addRes(Resource resource) throws IOException {
        if (!resCache.containsKey(resource)) {
            Long modifiedTime = resource.exists() ? new Long(resource.lastModified()) : -1L;
            resCache.put(resource, modifiedTime);
        }
    }

    /**
     * 定义监听任务
     */
    private class FileMonitorTimer extends TimerTask {
        public FileMonitorTimer() {
        }

        public void run() {
            Iterator<Resource> it = resCache.keySet().iterator();
            while (it.hasNext()) {
                Resource resource = it.next();
                long lastModifiedTime = resCache.get(resource).longValue();
                long nowModifiedTime = -1L;
                try {
                    if (resource.exists()) nowModifiedTime = resource.lastModified();
                } catch (IOException e) {
                    continue;
                }
                if (lastModifiedTime == nowModifiedTime) continue;
                // 更新文件时间
                resCache.put(resource, new Long(nowModifiedTime));
                // 重新缓存文件属性
                FileFunc.cacheProps(resource);
            }
        }
    }

}
