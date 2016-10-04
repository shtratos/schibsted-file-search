package com.schibsted.filesearch;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Basic ranker that ranks files based on how many query terms are present in them.
 */
class BasicRanker implements Ranker {

    private static final Comparator<SearchHit> SEARCH_HIT_COMPARATOR =
            (hit1, hit2) -> ComparisonChain.start()
                    .compare(hit2.getScore(), hit1.getScore())
                    .compare(hit2.getFilename(), hit1.getFilename())
                    .result();

    public ImmutableList<SearchHit> rank(Multimap<String, String> filesToTerms, SearchQuery originalQuery, int limit) {
        final long uniqueTerms = originalQuery.getTerms().stream().distinct().count();

        final Iterator<SearchHit> rankedHits = filesToTerms.asMap().entrySet().stream()
                .map(entry -> {
                    final String filename = entry.getKey();
                    final long matchingTerms = entry.getValue().stream().distinct().count();
                    final Long score = Math.round(100.0 * matchingTerms / uniqueTerms);
                    return new SearchHit(filename, score.intValue());
                })
                .sorted(SEARCH_HIT_COMPARATOR)
                .limit(limit)
                .iterator();

        return ImmutableList.copyOf(rankedHits);
    }

}
