package ru.spbau.mit.repl.commands;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.repl.shell.ShellVariables;
import ru.spbau.mit.repl.util.Parser;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class ParserTest {
  private ShellVariables vars;

  @Before
  public void setUp() {
    vars = ShellVariables.getInstance();
    vars.clear();
  }

  @Test
  public void parseAssignment() throws Exception {
    Parser.parseCommand("a=100").execute(Collections.emptyList(), null, null);
    Parser.parseCommand("b=20").execute(Collections.emptyList(), null, null);
    String cmd = "$a $b";
    assertEquals("100 20", vars.substitute(cmd));
  }

  @Test
  public void parseDoubleQuote() throws Exception {
    vars.addVariable("a", "100");
    String line = "cat \"$a $a\" b b";
    List<String> args = Parser.parseArguments(line);
    assertEquals(3, args.size());
    assertEquals("100 100", args.get(0));
  }

  @Test
  public void parseSingleQuote() throws Exception {
    vars.addVariable("a", "100");
    String line = "cat \'$a $a\' b";
    List<String> args = Parser.parseArguments(line);
    assertEquals(2, args.size());
    assertEquals("$a $a", args.get(0));
  }
}