package com.example.demo;

import com.example.demo.utils.fileUtils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Testy {

    public static void main(String[] args) {

    }

    @Test
    public void ttt() {
        String packageName = "com.example.demo.utils.fileUtils";
//        String packageName = "com.example.demo.common.task";
        try {
            Set<Class<?>> classes = ClassUtils.getClasses(packageName);
            classes.forEach(t-> System.out.println(t.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void tt() throws ClassNotFoundException {
        String packageName = "com.example.demo.utils.fileUtils.FileMonitor$FileMonitorTimer";
        Class<?> aClass = Class.forName(packageName, false, Thread.currentThread().getContextClassLoader());
        System.out.println(aClass.getName());
        Arrays.stream(aClass.getMethods()).forEach(System.out::println);
    }

}