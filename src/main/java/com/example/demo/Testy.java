package com.example.demo;

import com.example.demo.utils.ChineseCharacterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Testy {
    public static void main(String[] args) {
    }

    @Test
    public void temp() throws Exception {
        // 字符首字母
        String pyIndexStr = ChineseCharacterUtil.convertHanzi2Pinyin("鬼", false);
        pyIndexStr = StringUtils.isBlank(pyIndexStr) ? "" : pyIndexStr.substring(0, 1);
        if (pyIndexStr.matches("\\d")) { // 数字特殊处理
            pyIndexStr = Arrays.asList("l", "y", "e", "s", "s", "w", "l", "q", "b", "j").get(Integer.valueOf(pyIndexStr).intValue());
        }
        System.out.println(pyIndexStr.toUpperCase());
    }

    public void tesy() throws Exception {
    }


}
