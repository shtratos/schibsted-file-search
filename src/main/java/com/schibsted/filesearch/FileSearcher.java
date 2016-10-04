package com.schibsted.filesearch;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

class FileSearcher {
    private static final int MAX_RESULTS = 10;

    private final FileIndex index;
    private final Ranker ranker;

    FileSearcher(FileIndex index, Ranker ranker) {
        this.index = index;
        this.ranker = ranker;
    }

    ImmutableList<SearchHit> search(String queryString) {
        final SearchQuery query = SearchQuery.from(queryString);

        Multimap<String, String> termToFiles = ArrayListMultimap.create();
        for (String term : query.getTerms()) {
            termToFiles.putAll(term, index.findFilesContaining(term));
        }
        final ImmutableListMultimap<String, String> filesToTerms = ImmutableListMultimap.copyOf(termToFiles).inverse();

        return ranker.rank(filesToTerms, query, MAX_RESULTS);
    }
}
