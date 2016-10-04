package com.schibsted.filesearch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class BasicRankerTest {

    private BasicRanker basicRanker;


    @Before
    public void setUp() throws Exception {
        basicRanker = new BasicRanker();
    }

    @Test
    public void when_all_terms_present_rank_is_100() throws Exception {
        // given
        final SearchQuery query = SearchQuery.from("hello world");
        final Multimap<String, String> filesToTerms = ImmutableListMultimap.of(
                "file1", "hello",
                "file1", "world"
        );
        // when
        final ImmutableList<SearchHit> rankedHits = basicRanker.rank(filesToTerms, query, 10);
        // then
        assertThat(rankedHits, contains(new SearchHit("file1", 100)));
    }

    @Test
    public void when_half_of_terms_present_rank_is_50() throws Exception {
        final SearchQuery query = SearchQuery.from("hello world");
        final Multimap<String, String> filesToTerms = ImmutableListMultimap.of(
                "file1", "hello"
        );
        // when
        final ImmutableList<SearchHit> rankedHits = basicRanker.rank(filesToTerms, query, 10);
        // then
        assertThat(rankedHits, contains(new SearchHit("file1", 50)));
    }

    @Test
    public void only_top_results_are_returned() throws Exception {
        final SearchQuery query = SearchQuery.from("hello there world");
        final Multimap<String, String> filesToTerms = ImmutableListMultimap.<String, String>builder()
                .put("file1", "hello")
                .put("file1", "there")
                .put("file1", "world")
                .put("file2", "hello")
                .put("file2", "world")
                .put("file3", "hello")
                .build();
        // when
        final ImmutableList<SearchHit> rankedHits = basicRanker.rank(filesToTerms, query, 2);
        // then
        assertThat(rankedHits, contains(new SearchHit("file1", 100), new SearchHit("file2", 67)));
    }
}
