package com.example.certificationboard.common.util;

import java.time.LocalDateTime;

public class DateUtil {

    /**
     * input : 2021년 04월 03일 11시 30분 30초 + nano
     * output : 202143113030 + nano
     * @return
     */
    public static String nowToString(){
        final LocalDateTime now = LocalDateTime.now();
        return "" + now.getYear() + now.getMonth().getValue() + now.getDayOfMonth() + now.getHour() + now.getMinute() + now.getSecond() + now.getNano();
    }
}
