package ru.spbau.mit.repl.shell;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

public class ShellTest {
  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  private Shell shell;
  private ByteArrayOutputStream out;

  @Before
  public void setUp() {
    shell = new Shell();
    out = new ByteArrayOutputStream();
  }

  @Test
  public void executeCat() throws IOException {
    File tempFile = tempFolder.newFile("temp1.txt");
    String content = "a b c d";
    FileUtils.writeStringToFile(tempFile, content);
    shell.execute("cat " + tempFile.getPath(), new PrintStream(out));
    Assert.assertEquals(content + "\n", out.toString());
  }

  @Test
  public void executePipeline() throws IOException {
    File tempFile = tempFolder.newFile("temp 2.txt");
    String content = "a b";
    FileUtils.writeStringToFile(tempFile, content);
    String cmd = "fileName=\"" + tempFile.getPath() + "\" | cat $fileName | wc";
    shell.execute(cmd, new PrintStream(out));
    int bytes = content.getBytes().length;
    int words = content.split(" ").length;
    Assert.assertEquals("1 " + words + " " + bytes + "\n", out.toString());
  }

  @Test
  public void executePwd() throws IOException {
    shell.execute("pwd", new PrintStream(out));
    Assert.assertEquals(System.getProperty("user.dir") + "\n", out.toString());
  }
}