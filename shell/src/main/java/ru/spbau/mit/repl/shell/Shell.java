package ru.spbau.mit.repl.shell;

import ru.spbau.mit.repl.commands.*;
import ru.spbau.mit.repl.util.Parser;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Shell is a class for managing available commands, their execution in pipeline and
 * environment variables.
 */
public class Shell {
  private final ShellVariables variables = new ShellVariables();
  private final Map<String, Command> commands = new HashMap<>();

  public Shell() {
    addCommand("echo", (args, in, out) -> {  // print all arguments
      out.println(args.stream().collect(Collectors.joining(" ")));
    });
    addCommand("exit", ((args, in, out) -> System.exit(0))); // exit shell
    addCommand("pwd", ((args, in, out) -> out.println(System.getProperty("user.dir")))); // print current directory
    addCommand("cat", new Cat());
    addCommand("wc", new Wc());
    addCommand("grep", new Grep());
    addCommand("cd", new Cd());
    addCommand("ls", new Ls());
  }

  /**
   * Adds custom command to this shell.
   *
   * @param name name of command
   * @param command object representing command to execute
   */
  public void addCommand(String name, Command command) {
    commands.put(name, command);
  }

  /**
   * Executes pipeline of commands from text line.
   *
   * @param line text command to execute
   * @param output output stream to print result of last command
   * @throws IOException
   */
  public void execute(String line, PrintStream output) throws IOException {
    String[] pipeline = line.split("\\|");
    InputStream input = System.in;
    for (int i = 0; i < pipeline.length - 1; i++) {
      ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(byteOutput);
      String command = variables.substitute(pipeline[i].trim());
      executeCommand(command, input, out);
      out.close();
      input = new ByteArrayInputStream(byteOutput.toByteArray());
    }
    String lastCommand = variables.substitute(pipeline[pipeline.length - 1].trim());
    executeCommand(lastCommand, input, output);
  }

  private void executeCommand(String line, InputStream input, PrintStream output) throws IOException {
    Command command = Parser.parseCommand(commands, variables, line);
    List<String> args = Parser.parseArguments(variables, line);
    command.execute(args, input, output);
  }
}
