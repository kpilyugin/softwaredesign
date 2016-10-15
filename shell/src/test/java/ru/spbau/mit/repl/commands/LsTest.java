package ru.spbau.mit.repl.commands;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.repl.shell.Shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LsTest {

    private final Shell shell = new Shell();
    private String initDir;

    @Rule
    public TemporaryFolder fld = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        initDir = System.getProperty("user.dir");
        fld.newFolder("a");
        File f = fld.newFile("1");
        fld.newFile("a/2");
        fld.newFile("a/3");
        System.setProperty("user.dir", f.getAbsoluteFile().getParent());
    }

    @After
    public void after(){
        System.setProperty("user.dir", initDir);
    }

    @Test
    public void testEmpty() throws IOException {
        List<String> result = executeCmd("ls");
        assertEquals(1, result.size());
        assertEquals("1 a ", result.get(0));
    }

    @Test
    public void testDot() throws IOException {
        List<String> result = executeCmd("ls .");
        assertEquals(1, result.size());
        assertEquals("1 a ", result.get(0));
    }

    @Test
    public void testDir() throws IOException {
        List<String> result = executeCmd("ls a");
        assertEquals(1, result.size());
        assertEquals("3 2 ", result.get(0));
    }

    @Test
    public void testDotDot() throws IOException {
        System.setProperty("user.dir", System.getProperty("user.dir") + "/a");
        List<String> result = executeCmd("ls ..");
        assertEquals(1, result.size());
        assertEquals("1 a ", result.get(0));
    }

    @Test
    public void testAbsol() throws IOException {
        List<String> result = executeCmd("ls " + System.getProperty("user.dir") + "/a");
        assertEquals(1, result.size());
        assertEquals("3 2 ", result.get(0));
    }

    @Test
    public void testMulti() throws IOException {
        List<String> result = executeCmd("ls a .");
        assertEquals(2, result.size());
        assertEquals("1 a ", result.get(1));
    }

    private List<String> executeCmd(String cmd) throws IOException {
        File resultFile = File.createTempFile("result", null);
        shell.execute(cmd, new PrintStream(resultFile));
        List<String> result = FileUtils.readLines(resultFile);
        resultFile.delete();
        return result;
    }

}