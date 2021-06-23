package com.example.demo.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ObjectUtils {

    /**
     * 深复制对象，包括对象的引用都复制了，并不是地址复制
     * 需要实现序列化接口
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T clone(T obj) {
        T cloneObj = null;
        try {
            //写入字节流
            @Cleanup
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            @Cleanup
            ObjectOutputStream oop = new ObjectOutputStream(out);
            oop.writeObject(obj);

            //分配内存，写入原始对象，生成新对象
            @Cleanup
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            @Cleanup
            ObjectInputStream oip = new ObjectInputStream(in);
            cloneObj = (T) oip.readObject();
        } catch (Exception e) {
            log.error("-- ObjectUtils.clone 复制对象异常 -- {}", e);
        }
        return cloneObj;
    }

    /**
     * 数组分组
     *
     * @param data 原集合
     * @param step 每组个数
     * @param <T>  集合泛型
     * @return
     */
    private <T> List<List<T>> splitByFive(List<T> data, int step) {
        List<List<T>> res = new ArrayList<>();
        if (data != null && data.size() > 0) {
            int len = data.size();
            int count = len / step;
            int more = len % step;
            for (int i = 0; i < count; i++) {
                res.add(data.subList(step * i, step * (i + 1)));
            }
            if (more != 0) {
                res.add(data.subList(step * count, len));
            }
        }
        return res;
    }

}
