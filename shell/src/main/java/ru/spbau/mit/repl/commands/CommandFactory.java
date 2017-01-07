package ru.spbau.mit.repl.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for all named commands available for shell.
 */
public class CommandFactory {
  private static final CommandFactory INSTANCE = new CommandFactory();

  private final Map<String, Command> commands = new HashMap<>();

  private CommandFactory() {
    commands.put("echo", (args, in, out) -> {  // print all arguments
      out.println(args.stream().collect(Collectors.joining(" ")));
    });
    commands.put("exit", (args, in, out) -> System.exit(0));
    commands.put("pwd", (args, in, out) -> out.println(System.getProperty("user.dir")));
    commands.put("cat", new Cat());
    commands.put("wc", new Wc());
    commands.put("grep", new Grep());
  }

  /**
   * Returns command for given name.
   */
  public static Command getCommand(String name) {
    return INSTANCE.commands.get(name);
  }
}
