package com.qq1833111108.common.utils;

import java.util.Calendar;

/**
 * Author: qq183311108
 * Email: 183311108@qq.com
 * Date: 2017/9/1
 * Time: 00:00
 * Describe: 获取日期时间工具类
 */
public class DateUtil {

    /**
     * 获取年份
     * @return
     */
    public static int getYear() {

        Calendar cal = Calendar.getInstance();

        return  cal.get(Calendar.YEAR);
    }

    /**
     * 获取日期
     * @return
     */
    public static int getMonth() {
        Calendar cal = Calendar.getInstance();

        return  cal.get(Calendar.MONTH)+1;
    }

}
