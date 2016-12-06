package ru.spbau.mit.repl.shell;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

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
    Assert.assertEquals(content, getResultWithoutNewline());
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
    Assert.assertEquals("1 " + words + " " + bytes, getResultWithoutNewline());
  }

  @Test
  public void executeEchoPipeline() throws IOException {
    shell.execute("x=echo | $x 1", new PrintStream(out));
    Assert.assertEquals("1", getResultWithoutNewline());
  }

  @Test
  public void executePwd() throws IOException {
    shell.execute("pwd", new PrintStream(out));
    String result = getResultWithoutNewline();
    Assert.assertEquals(System.getProperty("user.dir"), result);
  }

  private String getResultWithoutNewline() {
    return out.toString().replaceAll(System.getProperty("line.separator"), "");
  }

  @Test
  public void executeLs() throws IOException {
    System.setProperty("user.dir", tempFolder.getRoot().getAbsolutePath());
    File file = tempFolder.newFile("file.txt");
    File dir = tempFolder.newFolder("folder");
    String res_file = "file : " + file.getName();
    String res_dir = "dir  : " + dir.getName();
    shell.execute("ls", new PrintStream(out));
    List<String> lines = Arrays.asList(out.toString().split(System.getProperty("line.separator")));
    Assert.assertEquals(2, lines.size());
    Assert.assertTrue(lines.contains(res_file));
    Assert.assertTrue(lines.contains(res_dir));
  }

  @Test
  public void executeCd() throws IOException {
    shell.execute("cd " + tempFolder.getRoot().getAbsolutePath(), new PrintStream(out));
    Assert.assertEquals(tempFolder.getRoot().getAbsolutePath(), System.getProperty("user.dir"));
  }
}