package ru.spbau.mit.repl.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;

/**
 * The cd command reads path from arguments and change current dir.
 */
public class Cd implements Command{
    @Override
    public void execute(List<String> arguments, InputStream input, PrintStream output) throws IOException {
        if (arguments.isEmpty()) {
            executeCd(new File(System.getProperty("user.home")), output);
        } else {
            String path = arguments.get(0);
            if (Paths.get(path).isAbsolute()) {
                executeCd(new File(path), output);
            } else {
                executeCd(new File(new File(System.getProperty("user.dir")), path), output);
            }
        }
    }

    private static void executeCd(File dir, PrintStream output) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) {
            output.println("Wrong dir name: \"" + dir.getAbsolutePath() + "\"");
            return;
        }
        System.setProperty("user.dir", dir.getCanonicalPath());
        output.println("change current dir to \"" + dir.getCanonicalPath() + "\"");
    }
}
