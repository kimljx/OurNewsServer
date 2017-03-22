package com.ournews.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class DateUtil {

    private static final String SECONDS = "刚刚";
    private static final String MINUTES = "分钟前";
    private static final String HOURS = "小时";
    private static final String DAYS = "天前";
    private static final String FORMAT = "yyyy年M月d日 HH:mm";


    public static String getTime(long time) {
        StringBuilder sb = new StringBuilder();
        long distance = (System.currentTimeMillis() - time) / 1000;
        if (distance < 60) {
            sb.append(SECONDS);
        } else if (distance < 60 * 60) {
            sb.append(distance / 60).append(MINUTES);
        } else if (distance < 60 * 60 * 24) {
            int hour = (int) (distance / 60 / 60);
            int minute = (int) ((distance / 60) - (hour * 60));
            sb.append(hour).append(HOURS).append(minute).append(MINUTES);
        } else if (distance < 60 * 60 * 24 * 4) {
            sb.append(distance / 60 / 60 / 24).append(DAYS);
        } else {
            Date date = new Date(time);
            return new SimpleDateFormat(FORMAT).format(date);
        }
        return sb.toString();
    }
}
