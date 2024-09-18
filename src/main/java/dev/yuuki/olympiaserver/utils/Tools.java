package dev.yuuki.olympiaserver.utils;

import java.util.Random;

public class Tools {

    public static String randomLoginCode() {
        String result = new String();
        Random random = new Random();
        for (int i = 0; i < 6; i++) result += random.nextInt(10);
        return result;
    }

}
