package ru.spbau.mit.repl.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Ls command lists all the files in the currecnt or given directory
 */
public class Ls implements Command {
    @Override
    public void execute(List<String> args, InputStream input, PrintStream output) throws IOException {
        String location = System.getProperty("user.dir");
        if (args.isEmpty()) {
            listImpl(location, output);
            return;
        }
        for (String s : args) {
            if (s.equals(".")) {
                listImpl(location, output);
                return;
            }
            if (s.equals("..")) {
                listImpl(new File(location).getParent(), output);
                return;
            }
            if (s.charAt(0) == '/') {
                listImpl(s, output);
                return;
            }
            if (s.charAt(0) != '/') {
                listImpl(location + "/" + s, output);
            }
            output.println("");
        }
    }

    private void listImpl(String location, PrintStream out) {
        File dir = new File(location);
        // If it's a file
        if (!dir.isDirectory()) {
            out.print(dir.toString());
            return;
        }
        for (File f : dir.listFiles()) {
            out.print(f.toString() + " ");
        }
    }
}

