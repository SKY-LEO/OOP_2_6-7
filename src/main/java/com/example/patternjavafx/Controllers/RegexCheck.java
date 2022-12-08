package com.example.patternjavafx.Controllers;

import java.util.regex.Pattern;

public class RegexCheck extends ChainCheck{
    private Pattern password_pattern;
    private Pattern fio_pattern;
    private Pattern login_pattern;

    public RegexCheck(Pattern password_pattern, Pattern fio_pattern, Pattern login_pattern){
        this.fio_pattern = fio_pattern;
        this.login_pattern = login_pattern;
        this.password_pattern = password_pattern;
    }

    public int check(String FIO, String login, String password1, String password2) {
        if (!fio_pattern.matcher(FIO).matches()) {
            return -10;
        } else if (!login_pattern.matcher(login).matches()) {
            return -11;
        } else if (!password_pattern.matcher(password1).matches()) {
            return -12;
        } else if (!password_pattern.matcher(password2).matches()) {
            return -13;
        }
        return checkNext(FIO, login, password1, password2);
    }
}
