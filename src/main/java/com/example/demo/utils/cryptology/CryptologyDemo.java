package com.example.demo.utils.cryptology;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.DateFormatTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class CryptologyDemo {

    public static void main(String[] args) {

    }

    @Test
    public void test() {
        try {
//            KeyPair keyPair = RSAUtil.getKeyPair();
//            String privateKey = RSAUtil.getPrivateKey(keyPair);
//            String publicKey = RSAUtil.getPublicKey(keyPair);
//            System.out.println("privateKey -> " + privateKey);
//            System.out.println("publicKey -> " + publicKey);


            String signKey = "fanuc";
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxiCGsk24T9wpxFyBbYJFWhH791BvFohDi/YTQfGnIHoHQlAqBAU35iE0oraiJTlFU70HlhJLoKzxCs9XZpWm/WETV+AULTbtC2V2DzBuqVFBxlDKYQJgCwrwop+Kr179SSbBMJjk/bdkG6+kxBtPl2rn3VFNEGM5MC/4yqea95QIDAQAB";
            String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALGIIayTbhP3CnEXIFtgkVaEfv3UG8WiEOL9hNB8acgegdCUCoEBTfmITSitqIlOUVTvQeWEkugrPEKz1dmlab9YRNX4BQtNu0LZXYPMG6pUUHGUMphAmALCvCin4qvXv1JJsEwmOT9t2Qbr6TEG0+XaufdUU0QYzkwL/jKp5r3lAgMBAAECgYBvaSb4zmy+SsJaxlCgB7ItwWiQkpP7KwG98atvyyP3Wm3NH5qX6uGsP0Vsyc6CnIud6ahJCTHIUZm3onQJ6iy5wiNTQHcE8OeN7zPX6Cq0yPLl8VNF7x+6yRodaafDObPgQA7Z89kjQFB+8zITh7TPOwyB1D9dFyCyuckLXs8iAQJBAPMGqKoptIrVQ0C1/8KbtHWk8HjaMy2+9c2NW+fvT02Q5//ayNdIGJyQ7AhS/i4ervGRpeu6zl5w48b2iszJBpECQQC7AmN1VKhTxq8iVB0I1hOGe86QDxmePaBMWD03B6XUe2/kDp27rTxeumQQ2DxYrkysImlnypiLa9YnR0M6N/QVAkEAyjtYT4G1A3LELC1HT6JSYTOICIlK3V5zHUHsoOy7iUh0RqzBRKRyFpdiUur9KVPPMKaK88bXxC8c3Ix8pD0EgQJAR64CsVZhrrrZJi9XcauckE+kPVOMXjsA1kJ8NDuaNHeby65Oh0E/go+vC0XW26bzfIc0H7RoBAixbRzDh+0qgQJBAO/hODhxh8x7VW6WbSXdXyKeiJImXXJxAyc78jZ7qanZlDAqKhE36GEhZxzzh7J3qMvTbNTg3DytNWlOuaaV0Jk=";


            long timestamp = DateFormatTool.parse("2020-04-26 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
            String content = "123";

            PublicKey publicKey1 = RSAUtil.string2PublicKey(publicKey);
            String s = RSAUtil.publicEncrypt(content.getBytes("UTF-8"), publicKey1).toString();
            String s2 = RSAUtil.publicEncrypt(content.getBytes("UTF-8"), publicKey1).toString();
            String s3 = RSAUtil.publicEncrypt(content.getBytes("UTF-8"), publicKey1).toString();

            System.out.println("s ---->  " + s + "\n");
            System.out.println("s2 ---->  " + s2 + "\n");
            System.out.println("s3 ---->  " + s3 + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 加解密过程：
     *            |———————————————————— RSA 公钥 加密 —————————————————————————
     *           |                                                                                                   |
     *          |                                                                                                   ↓
     *     [[ send ]] ——> [生成 AES 密钥] —— sign加签 [格式：AESSign = sign(AESKey + timeStamp)] ——> [AESKey + timeStamp + AESSign] ——————————
     *        |                |                                                                                                                         |
     *       |                |————————————————————— AES 加密 ———————————————————————                             |
     *      |                                                                                                              ↓                             |
     *     |————> [在原始报文中添加 timeStamp]  —— sign加签 [格式：msgSign = sign(msgJSON + timeStamp)] ——> [msg + timeStamp + msgSign] ——————|
     *                                                                                                                                                  |
     *                                                                                                                             [[ receive ]] <———|
     *                                                                                                                                  |
     *                           AESKey <——— 验签 ——— [AESKey 报文] <——— RSA 私钥解密 ——— [RSA 加密的 AESKey 报文] <——————|
     *                              |                                                                                                  |
     *                             |————————————— AESKey 解密 —————————————> [AES 加密的 msg 报文]  <——————|
     *                                                                                                   ↓
     *                           [原始 send 发送的报文] <—————— sign 验签 ——————————— [msg 报文]
     */
    @Test
    public void demo() {
        try {
            // ---------------- 请求方 ----------------- //
            System.out.println("--- 请求方 ---");
            // AES密钥
            String aesKey = UUID.randomUUID().toString().replace("-", "");
            // RSA公钥
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTIKxNsk4TOr6kOn31lUj4c4s2vNXVZpvajGbrSWqe8j0z0/0efeoJpuvfifvTuLkTqBU8TtVB1WVoYoPCQmeTehDtDcdLjr36leGau7/DfbyP3qqj4HXMZiCKGDxGH9tDmpyS/6zjarD326t+culTfIozP7ZtMyAjaZzzLGs2WQIDAQAB";
            // 签名key
            String signKey = "123456";
            // 请求时间戳
            long timeStamp = System.currentTimeMillis();
            System.out.println("时间 -> " + DateFormatTool.format(new Date(timeStamp), "yyyy-MM-dd HH:mm:ss.SSS"));

            Map<String, Object> data = new HashMap<>();
            data.put("title", "测试标题");
            data.put("messageModule", "EMERG_ENCYREPAIR");
            data.put("msgContent", "测试文本");
            data.put("pushType", "4");
            data.put("userId", "aabbcc");
            data.put("remark", "123456");
            data.put("timeStamp", timeStamp);
            String msgJSON = JSONObject.toJSONString(data);

            // AES加密msg签名
            String msgSign = Md5Encrypt.sign(msgJSON + timeStamp, signKey, StandardCharsets.UTF_8.displayName());
            data.put("sign", msgSign);

            // 原始报文
            String msg = JSONObject.toJSONString(data);
            System.out.println("原始报文 -> " + msg + "\n");

            // 测试报文过期
//            Calendar c = Calendar.getInstance();
//            c.add(Calendar.MINUTE, -6);
//            long timeStamp = c.getTimeInMillis();

            System.out.println("AES密钥 -> " + aesKey + "\n");

            // AES加密报文
            String aesEncodeContent = AESUtil.encrypt(msg, aesKey);
            System.out.println("AES加密报文 -> " + aesEncodeContent + "\n");

            // AES加密签名
            String sign = Md5Encrypt.sign(aesKey + timeStamp, signKey, StandardCharsets.UTF_8.displayName());

            // 加签
            String aesKeyContent = JSONObject.toJSONString(new HashMap<String, Object>() {{
                put("aesKey", aesKey);
                put("timeStamp", timeStamp);
                put("sign", sign);
            }});
            System.out.println("RSA加密密钥前内容 -> " + aesKeyContent + "\n");

            PublicKey publicKey1 = RSAUtil.string2PublicKey(publicKey);

            // 公钥加密
            byte[] encodeBytes = RSAUtil.publicEncrypt(aesKeyContent.getBytes(StandardCharsets.UTF_8), publicKey1);
            String encode = RSAUtil.byte2Base64(encodeBytes);
            System.out.println("RSA加密密钥后内容 -> " + encode + "\n");


            // ---------------- 接收方 ----------------- //
            System.out.println("--- 接收方 ---");
            System.out.println("RSA解密密钥前内容 -> " + encode + "\n");

            // 私钥
            String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANMgrE2yThM6vqQ6ffWVSPhziza81dVmm9qMZutJap7yPTPT/R596gmm69+J+9O4uROoFTxO1UHVZWhig8JCZ5N6EO0Nx0uOvfqV4Zq7v8N9vI/eqqPgdcxmIIoYPEYf20OanJL/rONqsPfbq35y6VN8ijM/tm0zICNpnPMsazZZAgMBAAECgYAemDh/1dvci4G+2L8SDH0Ti+Lbnse58fGZ3Qi5Bd2OhPn7fbfLaGG23lqF/T/h8X3YbF4PbDkZHixg0Q3Y47iQchjdC4iETPy5vsSxUPdVdozO/5YCY+ElNRmGYcEekMaoTq+7ddF5CHbGgfUmNPCIkm7+ll27LIz4rdQInQLiRQJBAOrP6ZRO8tUEhid2fVaRzsbnWBHm+XcQUxtOdA7lHNQ+pWKYrBWMXhVHbbUSrpHZ+jnfUWHrcFJhs1Kv7KgX0uMCQQDmLafJbtzSmp/e8on0rNNfdiggu09blJ4Gyo1qoz/HhZ9XeMB17Aqvo4XDn8oG/m783QvvTj+WC5RS20bOMsqTAkEArw6AKmRNX4g259bLjK25hcpVe0tCcoSGINZ1aeFfg9CMhhwiNxJl1eFhpdwer3fduCWIS8M2AwT5psynV44jVwJAbA1wn3nAhcj6VaGLq2VaEI0aB0uG1Fnu2QdK3Y1nClWq2FgiGPlGhu7/gzbGkwPrvB5UVPFpnhkvdrnjAjcLqwJAJgHhC1jnL9NvHVBZHS9+K0/BfEFdCHh/QF4Hdd+y+dbIyY8MrEWwmo06ruJqfq4qmWSd94ihV5IPhSp487ZQbw==";
            PrivateKey privateKey1 = RSAUtil.string2PrivateKey(privateKey);

            // 私钥解密
            byte[] decodeBytes = RSAUtil.base642Byte(encode);
            String decode = new String(RSAUtil.privateDecrypt(decodeBytes, privateKey1), StandardCharsets.UTF_8);
            System.out.println("RSA解密密钥后内容 -> " + decode + "\n");

            // 解析报文
            JSONObject jsonObject = JSONObject.parseObject(decode);
            long testTimeStamp = jsonObject.getLong("timeStamp");

            // check timeout
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -5);
            long timeInMillis = cal.getTimeInMillis();
            // 报文5min过期
            if (testTimeStamp < timeInMillis) {
                System.out.println("报文过期");
                return;
            }

            String testSign = jsonObject.getString("sign");
            System.out.println("密钥报文签名 -> " + testSign);

            String testAesKey = jsonObject.getString("aesKey");
            String testSignTemp = Md5Encrypt.sign(testAesKey + testTimeStamp, signKey, StandardCharsets.UTF_8.displayName());
            System.out.println("加密密钥签名 -> " + testSignTemp + "\n");

            // AES密钥验签
            if (testSign.equals(testSignTemp)) {
                System.out.println("--- AES密钥 验签成功 ---");
                System.out.println("AES密钥 -> " + testAesKey + "\n");
                String resJSON = AESUtil.decrypt(aesEncodeContent, testAesKey);
                System.out.println("解密后报文 -> " + resJSON + "\n");

                Map<String, Object> res = JSONObject.parseObject(resJSON);
                if (res == null || res.size() < 1) {
                    System.out.println("数据集为空");
                    return;
                }

                String resSign = (String) res.remove("sign");
                System.out.println("报文sign -> " + testSign);

                String resSignJson = JSONObject.toJSONString(res);
                Long msgTimeStamp = (Long) res.get("timeStamp");
                String testMsgSignTemp = Md5Encrypt.sign(resSignJson + msgTimeStamp.longValue(), signKey, StandardCharsets.UTF_8.displayName());
                System.out.println("报文签名 -> " + testMsgSignTemp + "\n");

                if (resSign.equals(testMsgSignTemp)) {
                    System.out.println("--- msg 验签成功 ---");
                    System.out.println("最终 msg --> " + resJSON);

                } else {
                    System.out.println("msg验签失败");
                    return;
                }


            } else {
                System.out.println("AES密钥验签失败");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}