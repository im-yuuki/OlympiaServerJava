package dev.yuuki.olympiaserver.utils;

import java.util.Random;

public class Tools {

    public static String randomLoginCode() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) result.append(random.nextInt(10));
        return result.toString();
    }

}
