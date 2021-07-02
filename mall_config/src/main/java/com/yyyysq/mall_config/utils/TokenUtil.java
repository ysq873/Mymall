package com.yyyysq.mall_config.utils;


import java.math.BigInteger;
import java.security.MessageDigest;

public class TokenUtil {

    public static String getToken(Long userId) {
        String src= System.currentTimeMillis() + userId.toString() + NumberUtil.genRandomNum(4);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
//    private static final char[] hexCode = "0123456789abcdef".toCharArray();
//    public static String createToken() {
//        return generateValue(UUID.randomUUID().toString());
//    }
//    private static String toHexString(byte[] data) {
//        if (data == null) {
//            return null;
//        }
//        StringBuilder r = new StringBuilder(data.length * 2);
//        for (byte b : data) {
//            r.append(hexCode[(b >> 4) & 0xF]);
//            r.append(hexCode[(b & 0xF)]);
//        }
//        return r.toString();
//    }
//    private static String generateValue(String param) {
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance("MD5");
//            algorithm.reset();
//            algorithm.update(param.getBytes());
//            byte[] messageDigest = algorithm.digest();
//            return toHexString(messageDigest);
//        } catch (Exception e) {
//            throw new RuntimeException("Token cannot be generated.", e);
//        }
//    }

}