package com.schibsted.filesearch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

/**
 * Orders search results depending on what terms they contain.
 */
public interface Ranker {

    /**
     * Rank search results.
     * <p>
     * Ranking algorithm should satisfy the following conditions:
     * <ul>
     * <li>The rank score must be 100% if a file contains all the words</li>
     * <li>It must be 0% if it contains none of the words</li>
     * <li>It should be between 0 and 100 if it contains only some of the words</li>
     * </ul>
     *
     * @param filesToTerms maps each found file to the list of query terms that it contains
     * @param originalQuery original search query that produced search results
     * @param limit max number of search results to return
     * @return search results ordered by their score
     */
    ImmutableList<SearchHit> rank(Multimap<String, String> filesToTerms, SearchQuery originalQuery, int limit);
}
