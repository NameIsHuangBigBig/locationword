package com.example.locationword.locationword.tool;



import java.security.Security;




import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by asus- on 2018/4/12.
 */

public class AesCbcUtils {
    // 加密
    public static String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
       Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      /*  Security.addProvider(new BouncyCastleProvider());*/
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return new BASE64Encoder().encode(encrypted).replaceAll("\r|\n", "");//此处使用BASE64做转码。
    }

    // 解密
    public static String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter){
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 =new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }


}
