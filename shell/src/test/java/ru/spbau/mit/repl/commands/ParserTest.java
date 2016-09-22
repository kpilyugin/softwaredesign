package ru.spbau.mit.repl.commands;

import org.junit.Test;
import ru.spbau.mit.repl.shell.ShellVariables;
import ru.spbau.mit.repl.util.Parser;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class ParserTest {
  @Test
  public void parseAssignment() throws Exception {
    ShellVariables vars = new ShellVariables();
    Parser.parseCommand(Collections.emptyMap(), vars, "a=100").execute(Collections.emptyList(), null, null);
    Parser.parseCommand(Collections.emptyMap(), vars, "b=20").execute(Collections.emptyList(), null, null);
    String cmd = "$a $b";
    assertEquals("100 20", vars.substitute(cmd));
  }

  @Test
  public void parseDoubleQuote() throws Exception {
    ShellVariables vars = new ShellVariables();
    vars.addVariable("a", "100");
    String line = "cat \"$a $a\" b b";
    List<String> args = Parser.parseArguments(vars, line);
    assertEquals(3, args.size());
    assertEquals("100 100", args.get(0));
  }

  @Test
  public void parseSingleQuote() throws Exception {
    ShellVariables vars = new ShellVariables();
    vars.addVariable("a", "100");
    String line = "cat \'$a $a\' b";
    List<String> args = Parser.parseArguments(vars, line);
    assertEquals(2, args.size());
    assertEquals("$a $a", args.get(0));
  }
}