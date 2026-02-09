package com.yulgnier.common.utils;

import java.util.Random;

public final class CodeUtil {
    public static String getRandomCode(Integer length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
