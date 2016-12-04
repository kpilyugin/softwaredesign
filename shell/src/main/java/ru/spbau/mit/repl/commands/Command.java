package ru.spbau.mit.repl.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Represents command, which can be executed in pipeline.
 */
public interface Command {
  /**
   * Executes current command in pipeline.
   *
   * @param arguments list of arguments of executed command
   * @param input input stream to read from
   * @param output stream to redirect output
   * @throws IOException
   */
  void execute(List<String> arguments, InputStream input, PrintStream output) throws IOException;
}
