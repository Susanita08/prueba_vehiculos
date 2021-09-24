package com.ing.interview.utilsTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {

    public static LocalDateTime getFormatTime(LocalDateTime localDateTimeNow){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String lastUpdateString = localDateTimeNow.format(formatter);
        return LocalDateTime.parse(lastUpdateString, formatter);
    }
}

