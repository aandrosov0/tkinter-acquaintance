package com.github.aandrosov.tkinter.toolchain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Classes {

    public static List<Field> getAllFields(Class<?> tClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(tClass.getDeclaredFields()));
        Class<?> superClass = tClass.getSuperclass();

        if(superClass != null) {
            fields.addAll(getAllFields(superClass));
        }

        return fields;
    }
}
