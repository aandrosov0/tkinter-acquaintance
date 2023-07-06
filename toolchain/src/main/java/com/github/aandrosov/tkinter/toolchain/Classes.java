package com.github.aandrosov.tkinter.toolchain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Classes {

    public static List<Field> getAllFields(Class<?> classType) {
        List<Field> fields = new ArrayList<>(Arrays.asList(classType.getDeclaredFields()));
        Class<?> superClass = classType.getSuperclass();

        if(superClass != null) {
            fields.addAll(getAllFields(superClass));
        }

        return fields;
    }

    public static List<Field> getAnnotatedFields(Class<?> classType, Class<? extends Annotation> annotationTypes) {
        return getAllFields(classType).stream()
                .map(field -> field.isAnnotationPresent(annotationTypes) ? field : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
