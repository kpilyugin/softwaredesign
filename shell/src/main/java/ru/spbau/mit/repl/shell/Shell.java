package ru.spbau.mit.repl.shell;

import ru.spbau.mit.repl.commands.Cat;
import ru.spbau.mit.repl.commands.Command;
import ru.spbau.mit.repl.commands.Wc;
import ru.spbau.mit.repl.util.Parser;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shell is a class for managing available commands, their execution in pipeline and
 * environment variables.
 */
public class Shell {
  private final ShellVariables variables = new ShellVariables();
  private final Map<String, Command> commands = new HashMap<>();

  public Shell() {
    addCommand("echo", ((args, in, out) -> {  // print all arguments
      args.forEach(arg -> out.print(arg + " "));
      out.println();
    }));
    addCommand("exit", ((args, in, out) -> System.exit(0))); // exit shell
    addCommand("pwd", ((args, in, out) -> out.println(System.getProperty("user.dir")))); // print current directory
    addCommand("cat", new Cat());
    addCommand("wc", new Wc());
  }

  /**
   * Adds custom command to this shell.
   *
   * @param name    name of command
   * @param command object representing command to execute
   */
  public void addCommand(String name, Command command) {
    commands.put(name, command);
  }

  /**
   * Executes pipeline of commands from text line.
   *
   * @param line   text command to execute
   * @param output output stream to print result of last command
   * @throws IOException
   */
  public void execute(String line, PrintStream output) throws IOException {
    String[] pipeline = line.split("\\|");
    InputStream input = System.in;
    for (int i = 0; i < pipeline.length - 1; i++) {
      ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(byteOutput);
      executeCommand(pipeline[i].trim(), input, out);
      out.close();
      input = new ByteArrayInputStream(byteOutput.toByteArray());
    }
    executeCommand(pipeline[pipeline.length - 1].trim(), input, output);
  }

  private void executeCommand(String line, InputStream input, PrintStream output) throws IOException {
    Command command = Parser.parseCommand(commands, variables, line);
    List<String> args = Parser.parseArguments(variables, line);
    command.execute(args, input, output);
  }
}
