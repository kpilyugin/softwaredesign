package ru.spbau.mit.repl.commands;

import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * The grep utility searches any given input files, selecting lines that
 * match pattern.
 * Available arguments:
 * -i: make pattern case insensitive
 * -w: search only for full words in a line
 * -A n: print n lines after every matching line
 */
public class Grep implements Command {
  private static final Options OPTIONS = new Options();

  static {
    OPTIONS.addOption("i", false, "case insensitive");
    OPTIONS.addOption("w", false, "search for full words");
    OPTIONS.addOption("A", true, "print n lines after matching");
  }

  /**
   * Evaluates command arguments using CommandLineParser, then executes
   * command choosing right matching pattern depending on arguments.
   * If no input files are given, reads from input stream.
   */
  @Override
  public void execute(List<String> args, InputStream input, PrintStream output) throws IOException {
    CommandLineParser parser = new BasicParser();
    try {
      CommandLine cmd = parser.parse(OPTIONS, args.toArray(new String[args.size()]));
      String[] cmdArgs = cmd.getArgs();

      String regex = cmdArgs[0];
      boolean caseInsensitive = cmd.hasOption("i");
      Pattern pattern = Pattern.compile(regex, caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);

      boolean fullWords = cmd.hasOption("w");
      // choose testing predicate using option
      Predicate<String> predicate = fullWords ?
          s -> Arrays.stream(s.split(" ")).anyMatch(regex::equals) :
          s -> pattern.matcher(s).find();

      int numLinesAfter = cmd.hasOption("A") ? Integer.parseInt(cmd.getOptionValue("A")) : 0;
      if (cmdArgs.length == 1) { // no files: read from given input stream
        executeGrep(predicate, input, output, numLinesAfter);
        return;
      }
      for (int i = 1; i < cmdArgs.length; i++) {      // execute on all given files
        executeGrep(predicate, new FileInputStream(cmdArgs[i]), output, numLinesAfter);
      }
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private static void executeGrep(Predicate<String> matcher, InputStream input, PrintStream output, int numPrintLinesAfterMatched) {
    int numAfterMatched = 0;
    try (Scanner scanner = new Scanner(input)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        boolean test = matcher.test(line);
        if (test) {
          numAfterMatched = 0;
        } else {
          numAfterMatched++;
        }
        if (numAfterMatched <= numPrintLinesAfterMatched) {
          output.println(line);
        }
      }
    }
  }
}
