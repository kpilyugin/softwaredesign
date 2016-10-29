package ru.spbau.mit.repl.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * The cd command reads path from arguments and change current dir.
 * If path start from '/' that mean path is absolute
 * else path is relative
 */
public class Cd implements Command{
    @Override
    public void execute(List<String> arguments, InputStream input, PrintStream output) throws IOException {
        if (arguments.isEmpty()) {
            executeCd(new File(System.getProperty("user.home")), output);
        } else {
            String path = arguments.get(0);
            if (!path.isEmpty() && path.charAt(0) == '/') {
                executeCd(new File(path), output);
            } else {
                executeCd(new File(new File(System.getProperty("user.dir")), path), output);
            }
        }
    }

    private static void executeCd(File dir, PrintStream output) {
        if (!dir.exists() || !dir.isDirectory()) {
            output.println("Wrong dir name: \"" + dir.getAbsolutePath() + "\"");
            return;
        }
        System.setProperty("user.dir", dir.getAbsolutePath());
        output.println("change current dir to \"" + dir.getAbsolutePath() + "\"");
    }
}
