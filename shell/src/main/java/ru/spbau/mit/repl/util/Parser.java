package ru.spbau.mit.repl.util;

import ru.spbau.mit.repl.commands.Command;
import ru.spbau.mit.repl.commands.Process;
import ru.spbau.mit.repl.shell.ShellVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class to parse commands and their arguments from input line.
 */
public class Parser {

  /**
   * Creates command object, that matches text command: one of named commands from map,
   * variable assignment operation, or external process.
   *
   * @param namedCommands a map of shell commands by name
   * @param variables shell environment variables to update from assignment command
   * @param command text command from input
   * @return parsed command
   */
  public static Command parseCommand(Map<String, Command> namedCommands, ShellVariables variables, String command) {
    int space = command.indexOf(' ');
    String commandName = space >= 0 ? command.substring(0, space) : command;
    Command named = namedCommands.get(commandName);
    if (named != null) {
      return named;
    }

    int equalsIndex = command.indexOf('=');
    if (equalsIndex >= 0) {
      String name = command.substring(0, equalsIndex);
      String value = command.substring(equalsIndex + 1);
      return (args, input, output) -> variables.addVariable(name, value);
    }
    return new Process(commandName);
  }

  /**
   * Parses input text command to extract command arguments.
   * Manages single and double quotation marks: text inside quotes is
   * regarded as single argument, variables are not substituted inside single quotes.
   *
   * @param variables shell environment to substitute variables
   * @param line text command to parse
   * @return list of arguments
   */
  public static List<String> parseArguments(ShellVariables variables, String line) {
    int space = line.indexOf(' ');
    line = space >= 0 ? line.substring(space) : ""; // remove command name
    int lastSpace = 0;
    boolean inQuotes = false;
    List<String> arguments = new ArrayList<>();
    for (int i = 0; i < line.length(); i++) {
      if (isQuote(line, i, '\'') || isQuote(line, i, '\"')) {
        inQuotes = !inQuotes;
      }
      boolean separate = line.charAt(i) == ' ' && !inQuotes;
      boolean end = i == line.length() - 1;
      if (separate || end) {
        String arg = line.substring(lastSpace, i + 1).trim();
        if (!arg.isEmpty()) {
          if (arg.charAt(0) != '\'') { // substitute variables if not inside single quotes
            arg = variables.substitute(arg);
          }
          if (arg.charAt(0) == '\'' || arg.charAt(0) == '\"') { // remove surrounding quotes
            arg = arg.substring(1, arg.length() - 1);
          }
          arguments.add(arg);
        }
        lastSpace = i + 1;
      }
    }
    return arguments;
  }

  private static boolean isQuote(String line, int index, char quote) {
    return line.charAt(index) == quote && (index == 0 || line.charAt(index - 1) != '\\');
  }
}
