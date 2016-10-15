package ru.spbau.mit.repl.commands;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import ru.spbau.mit.repl.shell.Shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LsTest {

  private final Shell shell = new Shell();
  private final File file;

  public LsTest() throws IOException {
    file = createTempFileWithContent("grep",
        "a b c d",
        "100 200 300",
        "AA BB CC DD");
  }

  @Test
  public void testUsual() throws IOException {
    List<String> result = executeGrep("grep a ");
    assertEquals(1, result.size());
    assertEquals("a b c d", result.get(0));
  }

  @Test
  public void testCaseInsensitive() throws IOException {
    List<String> result = executeGrep("grep -i a");
    assertEquals(2, result.size());
    assertEquals("AA BB CC DD", result.get(1));
  }

  @Test
  public void testWholeWords() throws IOException {
    List<String> result = executeGrep("grep -w A");
    assertEquals(0, result.size());

    List<String> result2 = executeGrep("grep -w AA");
    assertEquals(1, result2.size());
  }

  @Test
  public void testLinesAfter() throws IOException {
    List<String> result = executeGrep("grep -A 2 a");
    assertEquals(3, result.size());
  }

  private List<String> executeGrep(String cmd) throws IOException {
    File resultFile = File.createTempFile("result", null);
    shell.execute(cmd + " " + file.getPath(), new PrintStream(resultFile));
    List<String> result = FileUtils.readLines(resultFile);
    resultFile.delete();
    return result;
  }

  private static File createTempFileWithContent(String name, String... content) throws IOException {
    File file = File.createTempFile(name, null);
    file.deleteOnExit();
    try (PrintWriter writer = new PrintWriter(file)) {
      for (String s : content) {
        writer.println(s);
      }
    }
    return file;
  }
}