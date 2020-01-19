package com.example.demo;

import io.lettuce.core.output.StatusOutput;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Testy {

    public static void main(String[] args) {

    }

    @Test
    public void testy() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        System.out.println(list.poll());
        System.out.println(list);
        System.out.println("---");
        System.out.println(list.poll());
        System.out.println(list);
        System.out.println("---");
        System.out.println(list.poll()==null&&list.isEmpty());


    }

}