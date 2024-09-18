package dev.yuuki.olympiaserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dev.yuuki.olympiaserver.utils.Tools;

@SpringBootTest
public class UtilsTest {

    @Test
    public void randomLoginCodeTest() {
        System.out.println(Tools.randomLoginCode());
    }

}
