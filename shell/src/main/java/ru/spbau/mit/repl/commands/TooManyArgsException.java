package ru.spbau.mit.repl.commands;


import java.io.IOException;

public class TooManyArgsException extends IOException {
    public TooManyArgsException(String s) {
        super(s);
    }
}
