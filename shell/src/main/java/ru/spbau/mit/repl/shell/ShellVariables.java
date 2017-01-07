package ru.spbau.mit.repl.shell;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents shell environmental variables
 */
public class ShellVariables {
  private static final ShellVariables INSTANCE = new ShellVariables();

  public static ShellVariables getInstance() {
    return INSTANCE;
  }

  private final Map<String, String> variables = new HashMap<>();

  /**
   * Adds variable to a map, called from assignment operation
   *
   * @param name variable's name
   * @param value variable's value
   */
  public void addVariable(String name, String value) {
    variables.put(name, value);
  }

  /**
   * Substitutes all entries of all known variables of an argument
   *
   * @param arg argument string
   * @return argument after substitution
   */
  public String substitute(String arg) {
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      arg = arg.replace("$" + entry.getKey(), entry.getValue());
    }
    return arg;
  }

  /**
   * Clears all variables in environment
   */
  public void clear() {
    variables.clear();
  }
}
