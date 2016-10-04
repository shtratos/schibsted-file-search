package com.schibsted.filesearch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class FileSearcherTest {

    @Mock
    private Ranker ranker;
    @Mock
    private FileIndex index;

    private FileSearcher searcher;

    @Before
    public void setUp() throws Exception {
        searcher = new FileSearcher(index, ranker);
    }

    @Test
    public void testFileSearch() throws Exception {
        // given
        given(index.findFilesContaining("hello"))
                .willReturn(ImmutableList.of("file1", "file2"));
        given(index.findFilesContaining("world"))
                .willReturn(ImmutableList.of("file1"));
        given(ranker.rank(ImmutableListMultimap.of("file1", "world", "file1", "hello", "file2", "hello"),
                SearchQuery.from("hello world"), 10))
                .willReturn(ImmutableList.of(new SearchHit("file1", 100), new SearchHit("file2", 50)));

        // when
        final ImmutableList<SearchHit> hits = searcher.search("hello world");

        // then
        assertThat(hits, contains(new SearchHit("file1", 100), new SearchHit("file2", 50)));
    }
}
