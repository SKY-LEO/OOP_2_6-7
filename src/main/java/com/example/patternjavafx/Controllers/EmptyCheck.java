package com.example.patternjavafx.Controllers;

public class EmptyCheck extends ChainCheck {

    public int check(String FIO, String login, String password1, String password2) {
        if (FIO.isEmpty()) {
            return -1;
        } else if (login.isEmpty()) {
            return -2;
        } else if (password1.isEmpty()) {
            return -3;
        } else if (password2.isEmpty()) {
            return -4;
        } else if (!password1.equals(password2)){
            return -5;
        }
        return checkNext(FIO, login, password1, password2);
    }
}
