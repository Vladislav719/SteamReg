package com.github.vladislav719.help;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by vladislav on 02.06.2015.
 */
public class GeneratorApi {

    public static void main(String[] args) {
        System.out.println(loginGenerator(8));
        System.out.println(passwordGenerator(8));
        System.out.println(emailGenerator("@yopmail.com"));
    }

    public static String loginGenerator(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String passwordGenerator(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * @param suffix smth like this @yopmail.com
     * @return
     */
    public static String emailGenerator(String suffix) {
        return RandomStringUtils.randomAlphanumeric(6) + suffix;
    }

}
