package com.qa.common;



import org.apache.commons.lang3.RandomStringUtils;
public class CommonHelper {

    /**
     * Method to return random numeric string.
     *
     * @param length
     * @return
     */
    public static String getRandomNumeric(final int length) {
        final String str = RandomStringUtils.randomNumeric(length);
        return str;
    }
}
