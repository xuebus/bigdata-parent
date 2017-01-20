// ----------------------------------------------------------------------------
// Copyright 2010-2013 Yangling Tech. Co. Ltd. 
// All Rights Reserved
// ----------------------------------------------------------------------------
//
// ----------------------------------------------------------------------------
// Description:
//  加密与验证
// ----------------------------------------------------------------------------
// Change History:
// 2012-3-28 Zhbwang
//      -Initial release
//
// ----------------------------------------------------------------------------
package com.zhbwang.bigdata.lib.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 加密与验证方法<br>
 * 使用方法:
 * 
 * <pre>
 * {@code }
 * </pre>
 * 
 * @author Zhibin Wang
 * @version 1.00, 2012-3-28
 * @see
 * @since
 */
public class Encrypter {
    /**
     * 加密算法：MD5
     */
    private static final String TYPE_MD5 = "MD5";

    /**
     * 加密 <br>
     * MD5的算法在RFC1321 中定义 在RFC 1321中，给出了Test suite用来检验你的实现是否正确： MD5 ("") =
     * d41d8cd98f00b204e9800998ecf8427e MD5 ("a") =
     * 0cc175b9c0f1b6a831c399e269772661 MD5 ("abc") =
     * 900150983cd24fb0d6963f7d28e17f72 MD5 ("message digest") =
     * f96b697d7cb7938d525a2f31aaf161d0 MD5 ("abcdefghijklmnopqrstuvwxyz") =
     * c3fcd3d76192e4007dfb496cca67e13b eg:
     * 
     * <pre>
     * {@code string = Encrypter.encrypt(password);}
     * </pre>
     * 
     * @param password
     *            需要加密的密码
     * @return String
     * @see
     * @since
     */
    public static String encrypt(String password) {
        try {
            byte[] pwd = null;
            if (password != null) {
                MessageDigest md5 = MessageDigest.getInstance(TYPE_MD5);
                md5.update(password.getBytes("UTF-8"));
                pwd = md5.digest();
            } else {
                pwd = new byte[0];
            }
            return byte2hex(pwd).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 密码认证.将原密码再加密一次,与给定的加密后密码比较 <br>
     * eg:
     * 
     * <pre>
     * {@code flag = Encrypter.isEncrypt(passwd, encryptPwd);}
     * </pre>
     * 
     * @param password
     * @param encryptPwd
     * @return
     * @see
     * @since
     */
    public static boolean isEncrypt(String password,
                                    String encryptPwd) {
        try {
            // LogUtil.info("paramstr: " + password);
            // LogUtil.info("sign: " + encryptPwd);
            // LogUtil.info("encry params: " + encrypt(password));
            if (password != null && encryptPwd != null) {
                if (encryptPwd.toUpperCase()
                              .equals(encrypt(password).toUpperCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    /**
     * 生成随机密钥 <br>
     * eg:
     * 
     * <pre>
     * {@code key= Encrypter.generateKey(str);}
     * </pre>
     * 
     * @param str
     *            生成密钥所需要传入的参数
     * @return String
     */
    public static String generateKey(String str) {
        Date dt = new Date();
        Random random = new Random();
        random.nextInt();
        String randomString = encrypt(str + dt.getTime() + random.toString());
        return randomString.toUpperCase();
    }

    /**
     * 将请求参数签名 <br>
     * sign = MD5(key + paramsStr + key) eg:
     * 
     * <pre>
     * {@code sign = Encrypter.sign(params, key);}
     * </pre>
     * 
     * @param params
     *            请求参数treeMap
     * @param key
     *            签名密钥
     * @return 签名
     */
    @SuppressWarnings("rawtypes")
    public static String sign(TreeMap params,
                              String key) {
        if (key == null) {
            return null;
        }
        String paramsStr = treeMapStr(params, null);
        return encrypt(key.toUpperCase() + paramsStr + key.toUpperCase());
    }

    /**
     * 检查是否请求已经签名 <br>
     * params eg:
     * 
     * <pre>
     * flag = Encrypter.isSign(params, &quot;sign&quot;, key);
     * </pre>
     * 
     * @param params
     *            请求参数
     * @param signStr
     *            签名在params中的key
     * @param key
     *            签名密钥
     * @return
     * @see
     * @since
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean isSign(TreeMap params,
                                 String signStr,
                                 String key) {

        boolean isSign = false;
        if (key == null) {
            return isSign;
        }

        // 获取请求参数中的签名
        Object signObj = params.get(signStr);
        if (signObj == null) {
            return isSign;
        }
        String sign = null;
        if (signObj instanceof Object[]) {
            Object[] signArr = (Object[]) signObj;
            sign = signArr[0].toString();
        } else {
            sign = signObj.toString();
        }

        // 验证签名
        if (null != sign) {
            TreeMap temp = new TreeMap(params);
            temp.remove(signStr);
            String paramsStr = treeMapStr(temp, null);
            if (isEncrypt(key.toUpperCase() + paramsStr + key.toUpperCase(),
                          sign)) {
                isSign = true;
            }
        }
        return isSign;

    }

    /**
     * 将TreeMap转换为字符串的形式 <br>
     * eg:
     * 
     * <pre>
     * {@code str = StringUtil.treeMapStr(params, exclude);}
     * </pre>
     * 
     * @param params
     *            parmas
     * @param exclude
     *            字符串中排除某个key
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static String treeMapStr(TreeMap params,
                                     String exclude) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {

            Iterator<Map.Entry> it = params.entrySet()
                                           .iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = entry.getKey()
                                  .toString();

                if (exclude == null || !exclude.equals(key)) {
                    Object valueObj = entry.getValue();
                    String value = null;
                    if (valueObj instanceof Object[]) {
                        Object[] valueArr = (Object[]) valueObj;
                        value = valueArr[0].toString();
                    } else {
                        value = valueObj.toString();
                    }
                    sb.append(key + value);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 二进制向字符串的转化 <br>
     * eg:
     * 
     * <pre>
     * {@code string=byte2hex(_byte);}
     * </pre>
     * 
     * @param _byte
     *            需要转换的byte数组
     * @return String
     */
    private static String byte2hex(byte[] _byte) {

        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < _byte.length; i++) {
            if (Integer.toHexString(0xFF & _byte[i])
                       .length() == 1) {
                md5StrBuff.append("0")
                          .append(Integer.toHexString(0xFF & _byte[i]));
            } else
                md5StrBuff.append(Integer.toHexString(0xFF & _byte[i]));
        }
        return md5StrBuff.toString();
    }
    
    public static void main(String[] args){
//        System.out.println(Encrypter.generateKey("27000005"));
        System.out.println(Encrypter.encrypt("FD34CB5E922FDDDB773359FB2F89B02D" + "27000005" + "175567,175547d,175595d"));

        System.out.println(Encrypter.encrypt("98F97B79EBE839519BFCC1" + "27000001" + "1010051,175673"));
    }

}
