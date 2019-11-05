package com.gloomyer.auto.utils;

import java.text.MessageFormat;

public class LG {
    public static void e(String format, Object... args) {
        if (args.length == 0) {
            System.err.println(format);
        } else {
            System.err.println(MessageFormat.format(format, args));
        }
    }

    public static void i(String format, Object... args) {
        if (args.length == 0) {
            System.out.println(format);
        } else {
            System.out.println(MessageFormat.format(format, args));
        }
    }
}
