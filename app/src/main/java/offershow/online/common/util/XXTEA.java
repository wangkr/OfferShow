package offershow.online.common.util;

/**
 * Created by Kairong on 2015/12/28.
 * mail:wangkrhust@gmail.com
 */

/*
 * XXTEA 加密算法
 */
/* XXTEA.java
*
* Author:       Ma Bingyao <andot@ujn.edu.cn>
* Copyright:    CoolCode.CN
* Version:      1.6
* LastModified: 2006-08-09
* This library is free.  You can redistribute it and/or modify it.
* http://www.coolcode.cn/?p=169
*/

public class XXTEA {
    private XXTEA() {}

    /**
     * Encrypt data with key.
     *
     * @param data
     * @return
     */
    public static byte[] encrypt(byte[] data) {
        if (data.length == 0) {
            return data;
        }
        if (data.length != 16) {
            byte[] newdata = new byte[16];
            for (int i = 0;i < data.length && i < 16 ; i++) {
                newdata[i] = data[i];
            }

            if (data.length < 16) {
                for (int j = data.length; j < 16 ; j++) {
                    newdata[j] = 0x00;
                }
            }

            return toByteArray(
                    encrypt(toIntArray(newdata, false), toIntArray(KEY_INTS, false)), false);

        }
        return toByteArray(
                encrypt(toIntArray(data, false), toIntArray(KEY_INTS, false)), false);
    }

    /**
     * Decrypt data with key.
     *
     * @param data
     * @return
     */
    public static byte[] decrypt(byte[] data) {
        if (data.length == 0) {
            return data;
        }

        if (data.length != 16) {
            byte[] newdata = new byte[16];
            for (int i = 0;i < data.length && i < 16 ; i++) {
                newdata[i] = data[i];
            }

            if (data.length < 16) {
                for (int j = data.length; j < 16 ; j++) {
                    newdata[j] = 0x00;
                }
            }

            return toByteArray(
                    decrypt(toIntArray(newdata, false), toIntArray(KEY_INTS, false)), false);

        }

        return toByteArray(
                decrypt(toIntArray(data, false), toIntArray(KEY_INTS, false)), false);
    }

    /**
     * Encrypt data with key.
     *
     * @param v
     * @param k
     * @return
     */
    public static int[] encrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        if (k.length < 4) {
            int[] key = new int[4];

            System.arraycopy(k, 0, key, 0, k.length);
            k = key;
        }
        int z = v[n], y = v[0], delta = DELTA, sum = 0, e;
        int p, q = 5 + 52 / (n + 1);

        while (q-- > 0) {
            sum = sum + delta;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v[p + 1];
                z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4)
                        ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            }
            y = v[0];
            z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4)
                    ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
        }
        return v;
    }

    /**
     * Decrypt data with key.
     *
     * @param v
     * @param k
     * @return
     */
    public static int[] decrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        if (k.length < 4) {
            int[] key = new int[4];

            System.arraycopy(k, 0, key, 0, k.length);
            k = key;
        }
        int z = v[n], y = v[0], delta = DELTA, sum, e;
        int p, q = 5 + 52 / (n + 1);

        sum = q * delta;
        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4)
                        ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            }
            z = v[n];
            y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4)
                    ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            sum = sum - delta;
        }
        return v;
    }

    /**
     * Convert byte array to int array.
     *
     * @param data
     * @param includeLength
     * @return
     */
    public static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (((data.length & 3) == 0)
                ? (data.length >>> 2)
                : ((data.length >>> 2) + 1));
        int[] result;

        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for (int i = 0; i < n; i++) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    /**
     * Convert int array to byte array.
     *
     * @param data
     * @param includeLength
     * @return
     */
    public static byte[] toByteArray(int[] data, boolean includeLength) {
        int n = data.length << 2;

        if (includeLength) {
            int m = data[data.length - 1];

            if (m > n) {
                return null;
            } else {
                n = m;
            }
        }
        byte[] result = new byte[n];

        for (int i = 0; i < n; i++) {
            result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
        }
        return result;
    }

    private static int DELTA = 0x79b95e3a;
    private static int MIN_LENGTH = 16;
    private static char SPECIAL_CHAR = '\0';
    private static byte[] KEY_INTS ={
            (byte)0xbf, (byte)0xc6, (byte)0xbc, (byte)0xbc,
            (byte)0xd6, (byte)0xc7, (byte)0xc4, (byte)0xfe,
            (byte)0xc3, (byte)0xc5, (byte)0xcb, (byte)0xf8,
            (byte)0xce, (byte)0xde, (byte)0xbf, (byte)0xd7};
}

//}
