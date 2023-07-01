package com.github.aandrosov.tkinter.toolchain;

public class Validator {

    public static boolean isPhone(String phone) {
        return phone.matches("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$");
    }

    public static boolean isPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}\\[\\]:;<>,?/~_+-=|\\s]).{8,32}$");
    }

    public static boolean isWord(String word) {
        return word.matches("^[A-Za-zа-яА-Я]{2,}$");
    }
}
