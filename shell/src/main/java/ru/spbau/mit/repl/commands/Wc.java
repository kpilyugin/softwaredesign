package ru.spbau.mit.repl.commands;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * The wc command displays the number of lines, words, and bytes contained
 * in each input file, or input from previous command (if no file is specified)
 */
public class Wc implements Command {

  @Override
  public void execute(List<String> args, InputStream input, PrintStream output) throws IOException {
    if (args.isEmpty()) {
      executeWc(input, output);
      return;
    }
    for (String fileName : args) {
      executeWc(new FileInputStream(fileName), output);
    }
  }

  private static void executeWc(InputStream input, PrintStream output) {
    int numLines = 0;
    int numWords = 0;
    int numBytes = 0;
    try (Scanner scanner = new Scanner(input)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        numLines++;
        numWords += line.split(" ").length;
        numBytes += line.getBytes().length;
      }
    }
    output.format("%d %d %d", numLines, numWords, numBytes);
    output.println();
  }
}
