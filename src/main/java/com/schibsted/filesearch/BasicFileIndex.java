package com.schibsted.filesearch;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.google.common.io.LineProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.base.CharMatcher.BREAKING_WHITESPACE;

/**
 * Indexes plain-text files in the directory.
 * <p>
 * Files are expected to be UTF-8 encoded and each line to fit in memory.
 * Only files at the first level of the directory are indexed.
 */
class BasicFileIndex implements FileIndex {
    private static final Splitter WORD_SPLITTER = Splitter.on(BREAKING_WHITESPACE).omitEmptyStrings().trimResults();

    private final ImmutableListMultimap<String, String> termsToFiles;

    private BasicFileIndex(Multimap<String, String> termsToFiles) {
        this.termsToFiles = ImmutableListMultimap.copyOf(termsToFiles);
    }

    /**
     * Create an index from files in a given directory.
     *
     * @param indexableDirectory directory containing files
     * @return built index
     * @throws IOException if there's a problem reading the directory or files in it
     */
    static BasicFileIndex create(File indexableDirectory) throws IOException {
        return new BasicFileIndex(indexDirectory(indexableDirectory.toPath()));
    }

    private static SetMultimap<String, String> indexDirectory(Path indexableDirectory) throws IOException {
        SetMultimap<String, String> termsToFiles = HashMultimap.create();
        try (DirectoryStream<Path> files = Files.newDirectoryStream(indexableDirectory, file -> file.toFile().isFile())) {
            for (Path file : files) {
                indexFile(file, termsToFiles);
            }
        }
        return termsToFiles;
    }

    private static void indexFile(Path file, SetMultimap<String, String> termsToFiles) throws IOException {
        final String filename = file.getFileName().toString();
        com.google.common.io.Files.readLines(file.toFile(), StandardCharsets.UTF_8, new LineProcessor<Void>() {
            @Override
            public boolean processLine(String line) throws IOException {
                for (String word : WORD_SPLITTER.split(line)) {
                    termsToFiles.put(word, filename);
                }
                return true;
            }

            @Override
            public Void getResult() {
                return null;
            }
        });
    }

    public ImmutableList<String> findFilesContaining(String term) {
        return termsToFiles.get(term);
    }
}
