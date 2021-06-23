package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 获取字符首字母
 */
@Slf4j
public class WordUtils {

    @Test
    public void temp() throws Exception {
        String str = "112";
        boolean up_case = false;
        System.out.println("中文首字母：" + getPYIndexStr(str, up_case));
        String c = getPYIndexStr(str, up_case).substring(0, 1);
        if (c.matches("\\d")) c = Arrays.asList("l", "y", "e", "s", "s", "w", "l", "q", "b", "j").get(Integer.valueOf(c).intValue());
        System.out.println("中文首字母：" + c);
    }

    /**
     * 返回首字母
     *
     * @param strChinese
     * @param bUpCase
     * @return
     */
    public static String getPYIndexStr(String strChinese, boolean bUpCase) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            byte b[] = strChinese.getBytes("GBK");// 把中文转化成byte数组
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 255) > 128) {
                    int char1 = b[i++] & 255;
                    char1 <<= 8;// 左移运算符用“<<”表示，是将运算符左边的对象，向左移动运算符右边指定的位数，并且在低位补零。其实，向左移n位，就相当于乘上2的n次方
                    int chart = char1 + (b[i] & 255);
                    buffer.append(getPYIndexChar((char) chart, bUpCase));
                    continue;
                }
                char c = (char) b[i];
                // 确定指定字符是否可以是 Java 标识符中首字符以外的部分。
                if (!Character.isJavaIdentifierPart(c)) c = 'A';
                buffer.append(c);
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new Exception("获取字符首字母异常");
        }
    }

    /**
     * 得到首字母
     *
     * @param strChinese
     * @param bUpCase
     * @return
     */
    private static char getPYIndexChar(char strChinese, boolean bUpCase) {
        int charGBK = strChinese;
        char result;
        if (charGBK >= 45217 && charGBK <= 45252) result = 'A';
        else if (charGBK >= 45253 && charGBK <= 45760) result = 'B';
        else if (charGBK >= 45761 && charGBK <= 46317) result = 'C';
        else if (charGBK >= 46318 && charGBK <= 46825) result = 'D';
        else if (charGBK >= 46826 && charGBK <= 47009) result = 'E';
        else if (charGBK >= 47010 && charGBK <= 47296) result = 'F';
        else if (charGBK >= 47297 && charGBK <= 47613) result = 'G';
        else if (charGBK >= 47614 && charGBK <= 48118) result = 'H';
        else if (charGBK >= 48119 && charGBK <= 49061) result = 'J';
        else if (charGBK >= 49062 && charGBK <= 49323) result = 'K';
        else if (charGBK >= 49324 && charGBK <= 49895) result = 'L';
        else if (charGBK >= 49896 && charGBK <= 50370) result = 'M';
        else if (charGBK >= 50371 && charGBK <= 50613) result = 'N';
        else if (charGBK >= 50614 && charGBK <= 50621) result = 'O';
        else if (charGBK >= 50622 && charGBK <= 50905) result = 'P';
        else if (charGBK >= 50906 && charGBK <= 51386) result = 'Q';
        else if (charGBK >= 51387 && charGBK <= 51445) result = 'R';
        else if (charGBK >= 51446 && charGBK <= 52217) result = 'S';
        else if (charGBK >= 52218 && charGBK <= 52697) result = 'T';
        else if (charGBK >= 52698 && charGBK <= 52979) result = 'W';
        else if (charGBK >= 52980 && charGBK <= 53688) result = 'X';
        else if (charGBK >= 53689 && charGBK <= 54480) result = 'Y';
        else if (charGBK >= 54481 && charGBK <= 55289) result = 'Z';
        else result = (char) (65 + (new Random()).nextInt(25));
        if (!bUpCase) result = Character.toLowerCase(result);
        return result;
    }

}
