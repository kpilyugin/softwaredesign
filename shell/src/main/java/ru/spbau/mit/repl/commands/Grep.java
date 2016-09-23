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

public class Grep implements Command {
  private static final Options OPTIONS = new Options();

  static {
    OPTIONS.addOption("i", false, "case insensitive");
    OPTIONS.addOption("w", false, "search for full words");
    OPTIONS.addOption("A", true, "print n lines after matching");
  }

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
      Predicate<String> predicate = fullWords ?
          s -> Arrays.stream(s.split(" ")).anyMatch(regex::equals) :
          s -> pattern.matcher(s).find();

      int numLinesAfter = cmd.hasOption("A") ? Integer.parseInt(cmd.getOptionValue("A")) : 0;
      if (cmdArgs.length == 1) {
        executeGrep(predicate, input, output, numLinesAfter);
        return;
      }
      for (int i = 1; i < cmdArgs.length; i++) {
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
