package com.game.utils.encription;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * DSE 加密和解密工具类
 * Created by Administrator on 14-3-28.
 */
public class DESADESecuritys {
    /**
     * 密钥算法
     * */
    private static final String SECRET_KEY = "ecp88888";

    private static final String ALGORITHM = "DESede";
    /**
     * 加密/解密算法/工作模式/填充方式
     * */
    public static final String CIPHER_ALGORITHM="DESede/ECB/NoPadding";

    /**
     * 3DES 加密
     * @param src
     * @return
     */
    public static String encryptEde( String src){
        String plainText = null;
        try
        {
            byte[] keyBytes = new byte[24]; // DES3 为24Bytes密钥
            asciiToBcdBytes(SECRET_KEY, keyBytes, Math.min(32, SECRET_KEY.length()) / 2);
            for (int i = 0; i < 8; ++i)
                keyBytes[16 + i] = keyBytes[i];
            // 从原始密匙数据创建一个DESKeySpec对象
            KeySpec dks = new DESedeKeySpec(keyBytes);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKey secKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secKey, sr);
            byte[] srcBytes = src.getBytes("UTF-8");
            int srcLen = srcBytes.length;
            int encLen = ((srcLen % 8) == 0) ? srcLen : ((srcLen / 8 + 1) * 8);
            byte[] encBytes = new byte[encLen];
            System.arraycopy(srcBytes, 0, encBytes, 0, srcLen);
            // 正式执行解密操作
            byte[] encryptBytes = cipher.doFinal(encBytes);
            plainText = bcdBytesToAscii(encryptBytes, encLen);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return plainText;
    }

    /**
     * 3DES 解密
     * @param src
     * @return
     */
    public static String decryptEde(String src)  {
        String plainText = null;
        try
        {
            byte[] keyBytes = new byte[24]; // DES3 为24Bytes密钥
            asciiToBcdBytes(SECRET_KEY, keyBytes, Math.min(32, SECRET_KEY.length()) / 2);
            for (int i = 0; i < 8; ++i)
                keyBytes[16 + i] = keyBytes[i];
            // 从原始密匙数据创建一个DESKeySpec对象
            KeySpec dks = new DESedeKeySpec(keyBytes);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKey secKey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secKey, sr);
            int count = (src.length() + 1) / 2;
            byte[] inputBytes = new byte[count];
            asciiToBcdBytes(src, inputBytes, count);
            // 正式执行解密操作
            byte[] decryptBytes = cipher.doFinal(inputBytes);
            int olen = decryptBytes.length - 1;
            for (; decryptBytes[olen] == 0 && olen >= 0; --olen){}
            plainText = new String(decryptBytes, 0, olen + 1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return plainText;
    }

    /**
     * BASE64 加密
     * @param str
     * @return
     */
    public static String encodeBASE64(String str){
       // return new String(new BASE64Encoder().encode(str.getBytes()));
    	byte[] b=str.getBytes();  
        Base64 base64=new Base64();  
        b=base64.encode(b);  
        return new String(b);  
    }

    /**
     * BASE64 解密
     * @param str
     * @return
     */
    public static String decodeBASE64(String str)throws Exception{
        // return new String(new BASE64Decoder().decodeBuffer(str));
    	byte[] b=str.getBytes();  
        Base64 base64=new Base64();  
        b=base64.decode(b);  
        return new String(b);  
    }

    private static String bcdBytesToAscii(byte[] hex, int count){
        byte[] ascii = new byte[2 * count];
        int t;
        for (int i = 0; i < count; i++)
        {
            t = hex[i] & 0xf0;
            t = t >> 4;
            if (t <= 9)
                t += '0';
            else if (t >= 10 && t <= 15)
                t += 'A' - 10;
            else
                t = '0';
            ascii[2 * i] = (byte) t;

            t = hex[i] & 0x0f;
            if (t <= 9)
                t += '0';
            else if (t >= 10 && t <= 15)
                t += 'A' - 10;
            else
                t = '0';
            ascii[2 * i + 1] = (byte) t;
        }
        return (new String(ascii));
    }


    private static void asciiToBcdBytes(String str, byte[] hex, int count) {
        byte[] inputBytes = str.getBytes();
        for (int i = 0, j = 0; j < inputBytes.length && i < count; ++i) {
            byte v = inputBytes[j];
            ++j;
            if (v <= 0x39)
                hex[i] = (byte) (v - 0x30);
            else
                hex[i] = (byte) (v - 0x41 + 10);

            hex[i] <<= 4;

            if (j >= inputBytes.length)
                break;
            v = inputBytes[j];
            ++j;
            if (v <= 0x39)
                hex[i] += (byte) (v - 0x30);
            else
                hex[i] += (byte) (v - 0x41 + 10);
        }
    }
    public static void main(String[] args) throws Exception {

        //加密数据
        String jm=DESADESecuritys.encryptEde("/w/w/ww/asa.txt");
        System.out.println("加密后："+ jm);
        System.out.println("加密后64 加密："+new String(new Base64().encode(jm.getBytes())));
        System.out.println("加密后64 解密："+new String(new Base64().decode(new Base64().encode(jm.getBytes()))));
        //解密数据
        jm=DESADESecuritys.decryptEde(jm);
        System.out.println("解密后："+jm);

}


}
