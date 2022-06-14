package com.boot.security.service.Util;

import java.util.Random;

public class CodeUtil {
    public static String getCodeWithNoChar(int length)
    {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(String.valueOf(random.nextInt(10)));

        }
        return builder.toString();
    }

}
