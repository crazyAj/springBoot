package com.example.demo.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

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

}
