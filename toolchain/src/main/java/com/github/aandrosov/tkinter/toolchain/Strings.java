package com.github.aandrosov.tkinter.toolchain;

public class Strings {

    public static String arrayToStringSequence(Object[] values, String delimiter, String endDelimiter) {
        StringBuilder sqlBuilder = new StringBuilder();

        for(Object value : values) {
            if(sqlBuilder.length() > 0) {
                sqlBuilder.append(delimiter);
            }

            sqlBuilder.append(value).append(endDelimiter);
        }

        return sqlBuilder.toString();
    }
}
