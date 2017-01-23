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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LsTest {
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private final Shell shell = new Shell();
    private String oldPwd;

    @Before
    public void before() {
        oldPwd = System.getProperty("user.dir");
        System.setProperty("user.dir", tmpFolder.getRoot().getAbsolutePath());
    }

    @After
    public void after() throws IOException {
        System.setProperty("user.dir", oldPwd);
    }

    @Test
    public void testFiles() throws IOException {
        final List<String> filenames = Arrays.asList("Ground", "Control", "to", "Major", "Tom");
        for (String fname : filenames)
            tmpFolder.newFile(fname);

        assertEquals(Arrays.asList("Control", "Ground", "Major", "Tom", "to"), executeLs());
    }

    @Test
    public void testFilesFolders() throws IOException {
        final List<String> folders = Arrays.asList("Some", "folders");
        for (String fname : folders)
            tmpFolder.newFolder(fname);
        final List<String> files = Arrays.asList("and", "Files");
        for (String fname : files)
            tmpFolder.newFolder(fname);

        assertEquals(Arrays.asList("Files", "Some", "and", "folders"), executeLs());
    }

    private List<String> executeLs() throws IOException {
        final File resultFile = File.createTempFile("result", null);

        shell.execute("ls", new PrintStream(resultFile));
        return FileUtils.readLines(resultFile);
    }
}