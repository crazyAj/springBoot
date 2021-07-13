package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Temp {

    @Test
    public void temp() {
        List<T2> list = new ArrayList<T2>() {{
            add(new T2("a", 1));
            add(new T2("b", 2));
            add(new T2("c", 3));
        }};


        Integer reduce = list.stream().reduce(0, (sum, t) -> sum + t.getAge(), (v, v2) -> v + v2);
        System.out.println(reduce);
    }

}

class T2 {
    private String name;
    private Integer age;

    public T2(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}