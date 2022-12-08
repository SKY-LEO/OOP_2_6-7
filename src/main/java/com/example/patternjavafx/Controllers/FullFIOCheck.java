package com.example.patternjavafx.Controllers;


public class FullFIOCheck extends ChainCheck {

    public int check(String FIO, String login, String password1, String password2) {
        String[] divided_FIO = FIO.split(" ");
        if (divided_FIO.length != 3) {
            return -20;
        }
        return checkNext(FIO, login, password1, password2);
    }
}
