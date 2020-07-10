package com.example.demo;

import com.example.demo.utils.fileUtils.FileFunc;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Testy {

    public static void main(String[] args) {

    }

    @Test
    public void t() {
        String prefix = "file:/";
        String subfix = ".jar";
        String path = "file:/abc.jar";
        String classPath = path.substring(path.indexOf(prefix)+prefix.length());
        String jarFilePath = classPath.substring(0, classPath.indexOf(subfix)).concat(subfix);
        System.out.println(jarFilePath);
    }

    @Test
    public void tt() {

    }

}