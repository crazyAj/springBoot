package com.example.demo.utils.cryptology;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.DateFormatTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class Testy {

    public static void main(String[] args) {

    }

    @Test
    public void t() {
        try {
            KeyPair keyPair = RSAUtil.getKeyPair();
            String privateKey = RSAUtil.getPrivateKey(keyPair);
            String publicKey = RSAUtil.getPublicKey(keyPair);
            System.out.println("privateKey -> " + privateKey);
            System.out.println("publicKey -> " + publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void tt() {
        try {
            // ---------------- 请求方 ----------------- //
            System.out.println("--- 请求方 ---");
            // AES密钥
            String aesKey = UUID.randomUUID().toString().replace("-", "");
            System.out.println("AES密钥 -> " + aesKey + "\n");

            Map<String, Object> data = new HashMap<>();
            data.put("title", "测试标题");
            data.put("messageModule", "EMERG_ENCYREPAIR");
            data.put("msgContent", "测试文本");
            data.put("pushType", "4");
            // 原始报文
            String msg = JSONObject.toJSONString(data);
            System.out.println("原始报文 -> " + msg + "\n");

            // RSA公钥
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTIKxNsk4TOr6kOn31lUj4c4s2vNXVZpvajGbrSWqe8j0z0/0efeoJpuvfifvTuLkTqBU8TtVB1WVoYoPCQmeTehDtDcdLjr36leGau7/DfbyP3qqj4HXMZiCKGDxGH9tDmpyS/6zjarD326t+culTfIozP7ZtMyAjaZzzLGs2WQIDAQAB";
            // 签名key
            String signKey = "123456";
            // 请求时间戳
            long timeStamp = System.currentTimeMillis();
            System.out.println("时间 -> " + DateFormatTool.format(new Date(timeStamp),"yyyy-MM-dd HH:mm:ss"));

            // 测试报文过期
//            Calendar c = Calendar.getInstance();
//            c.add(Calendar.MINUTE, -6);
//            long timeStamp = c.getTimeInMillis();

            // AES加密报文
            String aesEncodeContent = AESUtil.encrypt(msg, aesKey);
            System.out.println("AES加密报文 -> " + aesEncodeContent + "\n");

            // AES加密签名
            String sign = Md5Encrypt.sign(aesKey + timeStamp, signKey, StandardCharsets.UTF_8.displayName());
            System.out.println("AES加密密钥签名 -> " + aesKey + "\n");

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
            System.out.println("密钥报文签名 -> " + testSign + "\n");

            String testAesKey = jsonObject.getString("aesKey");
            String testSignTemp = Md5Encrypt.sign(testAesKey + testTimeStamp, signKey, StandardCharsets.UTF_8.displayName());
            System.out.println("加密密钥签名 -> " + testSignTemp + "\n");

            // 验签
            if (testSign.equals(testSignTemp)) {
                System.out.println("--- 验签成功 ---");
                System.out.println("AES密钥 -> " + testAesKey + "\n");
                String res = AESUtil.decrypt(aesEncodeContent, testAesKey);
                System.out.println("解密后报文 -> " + res);
            } else {
                System.out.println("验签失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}