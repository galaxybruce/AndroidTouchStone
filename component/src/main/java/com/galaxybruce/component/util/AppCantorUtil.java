package com.galaxybruce.component.util;


/**
 * @date 2019/3/20 15:21
 * @author bruce.zhang
 * @description 两个整型值生成唯一值，然后再拆分。在两个值生成唯一标示时很有用
 * <p>
 * modification history:
 */
public class AppCantorUtil {

    /**
     * @param k1
     * @param k2
     * @return cantor pair for k1 and k2
     */
    public static long getCantor(long k1, long k2) {
        return (k1 + k2) * (k1 + k2 + 1) / 2 + k2;
    }

    /**
     * reverse cantor pair to origin number k1 and k2, k1 is stored in result[0], and k2 is stored in result[1]
     * @param cantor a computed cantor number
     * @param result the array to store output values
     */
    public static void reverseCantor(long cantor, long[] result) {
        if (result == null || result.length < 2) {
            result = new long[2];
        }
        // reverse Cantor Function
        long w = (long) (Math.floor(Math.sqrt(8 * cantor + 1) - 1) / 2);
        long t = (w * w + w) / 2;

        long k2 = cantor - t;
        long k1 = w - k2;
        result[0] = k1;
        result[1] = k2;
    }

}
