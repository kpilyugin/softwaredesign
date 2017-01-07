package ru.spbau.mit.repl.shell;

import ru.spbau.mit.repl.commands.Command;
import ru.spbau.mit.repl.util.Parser;

import java.io.*;
import java.util.List;

/**
 * Shell is a class for managing available commands, their execution in pipeline and
 * environment variables.
 */
public class Shell {
  /**
   * Executes pipeline of commands from text line.
   *
   * @param line text command to execute
   * @param output output stream to print result of last command
   * @throws IOException if error occurred during command execution
   */
  public static void execute(String line, PrintStream output) throws IOException {
    String[] pipeline = line.split("\\|");
    InputStream input = System.in;
    for (int i = 0; i < pipeline.length - 1; i++) {
      ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(byteOutput);
      String command = ShellVariables.getInstance().substitute(pipeline[i].trim());
      executeCommand(command, input, out);
      out.close();
      input = new ByteArrayInputStream(byteOutput.toByteArray());
    }
    String lastCommand = ShellVariables.getInstance().substitute(pipeline[pipeline.length - 1].trim());
    executeCommand(lastCommand, input, output);
  }

  private static void executeCommand(String line, InputStream input, PrintStream output) throws IOException {
    Command command = Parser.parseCommand(line);
    List<String> args = Parser.parseArguments(line);
    command.execute(args, input, output);
  }
}
