package ru.spbau.mit.repl.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Lists directory content
 */
public class Ls implements Command {
    @Override
    public void execute(List<String> arguments, InputStream input, PrintStream output) throws IOException {
        final Path pwd = Paths.get(System.getProperty("user.dir"));

        final File[] files = pwd.toFile().listFiles();
        if (null == files)
            return;
        Arrays.stream(files).sorted().forEach(file -> output.println(file.getName()));
    }
}
