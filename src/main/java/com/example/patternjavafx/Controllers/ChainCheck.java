package com.example.patternjavafx.Controllers;

abstract public class ChainCheck {
    private ChainCheck next;

    public static ChainCheck link(ChainCheck first, ChainCheck... chain) {
        ChainCheck head = first;
        for (ChainCheck nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract int check(String FIO, String login, String password1, String password2);

    protected int checkNext(String FIO, String login, String password1, String password2) {
        if (next == null) {
            return 0;
        }
        return next.check(FIO, login, password1, password2);
    }
}
