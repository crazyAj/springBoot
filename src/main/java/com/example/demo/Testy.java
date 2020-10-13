package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Testy {

    public static void main(String[] args) {

    }

    @Test
    public void t() {
        // LocalDateTime -> String
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = time.format(dateTimeFormatter);
        System.out.println("LocalDateTime -> String = " + format);

        // String -> LocalDateTime
        time = LocalDateTime.parse(format, dateTimeFormatter);
        System.out.println("LocalDateTime -> time = " + time);

    }

    @Test
    public void tt() {
    }

}