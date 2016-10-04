package com.schibsted.filesearch;

import com.google.common.collect.ImmutableList;

/**
 * Keeps files indexed by the terms they contain.
 * <p>
 * The index should be optimised for retrieval of file names that contain a given term.
 */
public interface FileIndex {

    /**
     * Find files that contain a given term.
     *
     * @param term search term
     * @return list of files containing the term
     */
    ImmutableList<String> findFilesContaining(String term);
}
