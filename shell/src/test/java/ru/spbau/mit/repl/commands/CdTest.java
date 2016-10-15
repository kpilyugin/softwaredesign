package ru.spbau.mit.repl.commands;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.repl.shell.Shell;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CdTest {

    private final Shell shell = new Shell();
    private String initDir;

    @Rule
    public TemporaryFolder fld = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        initDir = System.getProperty("user.dir");
        fld.newFolder("b");
        File f = fld.newFile("1");
        fld.newFile("b/2");
        fld.newFile("b/3");
        System.setProperty("user.dir", f.getAbsoluteFile().getParent());
    }

    @After
    public void after() {
        System.setProperty("user.dir", initDir);
    }

    @Test
    public void testEmpty() throws IOException {
        executeCmd("cd");
        assertEquals(System.getProperty("user.home"), System.getProperty("user.dir"));
    }

    @Test
    public void testDot() throws IOException {
        String before = System.getProperty("user.dir");
        executeCmd("cd .");
        assertEquals(before, System.getProperty("user.dir"));
    }

    @Test
    public void testDir() throws IOException {
        String before = System.getProperty("user.dir");
        executeCmd("cd b");
        assertEquals(before + "/b", System.getProperty("user.dir"));
    }

    @Test
    public void testDotDot() throws IOException {
        String before = System.getProperty("user.dir");
        System.setProperty("user.dir", before + "/b");
        executeCmd("cd ..");
        assertEquals(before, System.getProperty("user.dir"));
    }

    @Test
    public void testAbsol() throws IOException {
        String before = System.getProperty("user.dir");
        executeCmd("cd " + before + "/b");
        assertEquals(before + "/b", System.getProperty("user.dir"));
    }

    @Test(expected = TooManyArgsException.class)
    public void testMulti() throws IOException {
        executeCmd("cd a b");
    }

    private List<String> executeCmd(String cmd) throws IOException {
        File resultFile = File.createTempFile("result", null);
        shell.execute(cmd, new PrintStream(resultFile));
        List<String> result = FileUtils.readLines(resultFile);
        resultFile.delete();
        return result;
    }

}