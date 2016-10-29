package ru.spbau.mit.repl.commands;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * The ls command reads paths from argument list, writing list of internal files (and dirs)
 * to output stream.
 */
public class Ls implements Command {
    @Override
    public void execute(List<String> arguments, InputStream input, PrintStream output) throws IOException {
        if (arguments.isEmpty()) {
            executeLs(System.getProperty("user.dir"), output);
        } else {
            arguments.forEach(filePath -> {
                executeLs(filePath, output);
            });
        }
    }

    private static void executeLs(String filePath, PrintStream output) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            output.println("Wrong dir name: \"" + dir + "\"");
        }

        File files[] = dir.listFiles();
        if (files != null) {
            for (File file: files) {
                String prefix = file.isDirectory() ? "dir  : " : "file : ";
                output.println(prefix + file.getName());
            }
        }
    }
}
