package tv.spideo.test.util;

import java.util.UUID;

public class CommonUtils {

    CommonUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
