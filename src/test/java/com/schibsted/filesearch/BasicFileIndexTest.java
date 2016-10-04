package com.schibsted.filesearch;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class BasicFileIndexTest {

    private static final File RESOURCE_DIR = new File("src/test/resources");

    @Test
    public void happy_case() throws Exception {
        // given
        System.out.println(RESOURCE_DIR.toPath().toAbsolutePath());
        // when
        final BasicFileIndex index = BasicFileIndex.create(RESOURCE_DIR);

        // then
        assertThat(index.findFilesContaining("to"), contains("file2.txt"));
        assertThat(index.findFilesContaining("over"), contains("file1.txt", "file2.txt"));
        assertThat(index.findFilesContaining("characters"), contains("file2.txt"));
    }
}
