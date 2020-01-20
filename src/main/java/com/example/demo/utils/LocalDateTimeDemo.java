package com.example.demo.utils;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * java8 LocalDateTime 使用
 */
public class LocalDateTimeDemo {

    @Test
    public void testLocalDateTime() {

        ///////////////////////////// 转换 ///////////////////////////

        // now()
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("now -> " + localDateTime);

        // LocalDateTime -> LocalDate
        LocalDate localDate = localDateTime.toLocalDate();
        System.out.println("LocalDate -> " + localDate);

        // LocalDateTime -> LocalTime
        LocalTime localTime = localDateTime.toLocalTime();
        System.out.println("localTime -> " + localTime);

        // LocalDateTime -> LocalDateTime
        LocalDateTime time = LocalDateTime.of(localDateTime.toLocalDate(), localDateTime.toLocalTime());
        System.out.println("LocalDateTime -> LocalDateTime = " + time);

        // LocalDateTime -> Timestamp
//        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
//        long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Timestamp timestamp1 = Timestamp.valueOf(localDateTime);
        long timestamp = timestamp1.getTime();
        Timestamp timestamp2 = Timestamp.from(Instant.ofEpochMilli(timestamp));
        System.out.println("timestamp1 = " + timestamp1);
        System.out.println("timestamp2 = " + timestamp2);
        System.out.println("LocalDateTime -> Timestamp = " + timestamp);

        // Timestamp -> LocalDateTime
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime timestampToLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println("Timestamp -> LocalDateTime = " + timestampToLocalDateTime);

        // LocalDateTime -> String
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = time.format(dateTimeFormatter);
        System.out.println("LocalDateTime -> String = " + format);

        // Date -> LocalDateTime
        Date now = new Date();
//        LocalDateTime dateToLocalDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateToLocalDateTime = Instant.ofEpochMilli(now.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("LocalDateTime -> Date = " + dateToLocalDateTime);

        // LocalDateTime -> Date
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("Date -> LocalDateTime = " + date);

        ///////////////////////////// 计算 ///////////////////////////

        // LocalDate plus Time
        LocalDate localDate1 = localDate.plusDays(1);
        LocalDate localDate2 = localDate.plus(1, ChronoUnit.DAYS);
        System.out.println("LocalDate plus Time = " + localDate1);

        // LocalTime plus Time
        LocalTime localTime1 = localTime.plusHours(1);
        LocalTime localTime2 = localTime.plus(1, ChronoUnit.HOURS);
        System.out.println("LocalTime plus Time = " + localTime2);

        // first/last day of Time
        LocalDate withDayOfMonth = localDate.withDayOfMonth(1);
        LocalDate firstDayOfMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
//        LocalDateTime firstDayOfMonth2 = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        LocalDateTime firstDayOfMonth2 = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime lastDayOfMonth2 = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        System.out.println("firstDayOfMonth = " + firstDayOfMonth2);
        System.out.println("lastDayOfMonth = " + lastDayOfMonth2);

        // compate
        LocalDateTime compateTime = LocalDateTime.of(2020, 05, 02, 12, 13, 14);
        boolean isAfter = localDateTime.isAfter(compateTime);
        System.out.println(localDateTime + " isAfter " + compateTime + " = " + isAfter);
        boolean isBefore = localDateTime.isBefore(compateTime);
        System.out.println(localDateTime + " isBefore " + compateTime + " = " + isBefore);
        long between = Math.abs(ChronoUnit.DAYS.between(compateTime, localDateTime));
        System.out.println(localDateTime + " between " + compateTime + " = " + between);
    }

}
