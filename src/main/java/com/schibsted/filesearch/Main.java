package com.schibsted.filesearch;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: search <path/to/files/directory>");
            System.out.println("    Finds words in files in the specified directory.");
            System.exit(1);
        }
        try {
            final File searchDirectory = new File(args[0]);
            System.out.print("Indexing files...");
            final FileIndex fileIndex = BasicFileIndex.create(searchDirectory);
            System.out.println("Done.");
            final Ranker ranker = new BasicRanker();
            final FileSearcher searcher = new FileSearcher(fileIndex, ranker);

            enterSearchRepl(searcher);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void enterSearchRepl(FileSearcher searcher) {
        final Scanner keyboard = new Scanner(System.in);
        while (true) {
            System.out.print("search> ");

            if (!keyboard.hasNextLine()) break; // User pressed Ctrl+D
            final String query = keyboard.nextLine();
            if (":quit".equals(query)) break;

            for (SearchHit hit : searcher.search(query)) {
                System.out.println(hit.getFilename() + ":" + hit.getScore() + "%");
            }
        }
    }
}
