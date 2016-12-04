package ru.spbau.mit.repl.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Represents external process.
 */
public class Process implements Command {
  private final String name;

  public Process(String name) {
    this.name = name;
  }

  /**
   * Executes external process with redirect of input and output streams.
   */
  @Override
  public void execute(List<String> arguments, InputStream input, PrintStream output) throws IOException {
    try {
      arguments.add(0, name);
      java.lang.Process process = new ProcessBuilder().command(arguments).start();
      process.waitFor();
      try (Scanner scanner = new Scanner(process.getInputStream())) {
        while (scanner.hasNextLine()) {
          output.println(scanner.nextLine());
        }
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("process was interrupted");
    }
  }
}
