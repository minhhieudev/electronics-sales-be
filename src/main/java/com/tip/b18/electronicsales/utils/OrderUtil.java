package com.tip.b18.electronicsales.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderUtil {
    private OrderUtil(){}

    private static final AtomicInteger count = new AtomicInteger(0);

    public static String generateOrderCode(){
        return "HD-" + generateCreationDate() + "-" + count.incrementAndGet();
    }

    private static String generateCreationDate(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmss"));
    }
}
