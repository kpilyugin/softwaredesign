package ru.spbau.mit.repl.commands;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * The cat command reads files from argument list, writing them to output stream.
 * If no arguments given, it reads from input stream.
 */
public class Cat implements Command {

  @Override
  public void execute(List<String> args, InputStream input, PrintStream output) throws IOException {
    if (args.isEmpty()) {
      executeCat(input, output);
      return;
    }
    for (String fileName : args) {
      executeCat(new FileInputStream(fileName), output);
    }
  }

  private static void executeCat(InputStream input, PrintStream output) {
    try (Scanner scanner = new Scanner(input)) {
      while (scanner.hasNextLine()) {
        output.println(scanner.nextLine());
      }
    }
  }
}
