package com.mall4j.cloud.common.cloud.util;

/**
 * 时间日期格式化
 *
 * @author lixingwu
 */
public class DateUtil {

    /**
     * <p> 方法描述：毫秒转分钟秒数. </p>
     * <p> 创建时间：2017-12-08 14:07:01 </p>
     * <p> 创建作者：李兴武 </p>
     *
     * @param ms 毫秒数
     * @return xx分钟xx秒
     * @author "lixingwu"
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分钟");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond).append("毫秒");
        }
        return sb.toString();
    }
}
