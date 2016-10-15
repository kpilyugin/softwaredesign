package ru.spbau.mit.repl.commands;

import java.io.*;
import java.util.List;

/**
 * Cd command goes to a directory
 */
public class Cd implements Command {
    @Override
    public void execute(List<String> args, InputStream input, PrintStream output) throws IOException {
        String location = System.getProperty("user.dir");
        if (args.isEmpty()) {
            cdImpl(System.getProperty("user.home"));
            return;
        }
        if (args.size() > 1) {
            throw new TooManyArgsException("cd too many args");
        }
        String s = args.get(0);
        if (s.equals(".")) {
            return;
        }
        if (s.equals("..")) {
            cdImpl(new File(location).getParent());
            return;
        }
        if (s.charAt(0) == '/') {
            cdImpl(s);
            return;
        }
        if (s.charAt(0) != '/') {
            cdImpl(location + "/" + s);
        }
    }

    private void cdImpl(String location) throws IOException {
        File dir = new File(location);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException();
        }
        System.setProperty("user.dir", location);
    }
}

