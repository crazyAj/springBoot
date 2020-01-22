package com.example.demo.common.schedule;

import com.example.demo.utils.DateFormatTool;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@Component
public class TestSchedule {

    /**
     * 固定 5s 定时任务
     */
    @Scheduled(fixedRate = 5000)
    private void testFixedRateSchedule() {
        System.out.println(String.join("","fixed 5s --> testFixedRateSchedule [ ",
                        LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(DateFormatTool.DATE_TIME_PATTON_1)),
                        " ]"));
    }

    /**
     * 通过cron来设置定时规则，每隔10秒
     * 对应含义为：
     *   字段         允许值               允许的特殊字符
     *   秒           0-59                - * /
     *   分           0-59                - * /
     *   小时         0-23                - * /
     *   日期         1-31                - * ? / L W C
     *   月份         1-12 或者 JAN-DEC    - * /
     *   星期         1-7 或者 SUN-SAT     - * ? / L C #
     *   年（可选）   留空, 1970-2099       - * /
     */
    @Scheduled(cron = "0/8 * * * * ?")
    private void testCronSchedule() {
        System.out.println(String.join("","cron 8s --> testCronSchedule [ ",
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(DateFormatTool.DATE_TIME_PATTON_1)),
                " ]"));
    }

}
