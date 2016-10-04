package com.schibsted.filesearch;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import static com.google.common.base.CharMatcher.BREAKING_WHITESPACE;

class SearchQuery {
    private static final int MAX_QUERY_LENGTH = 1000;
    private static final Splitter QUERY_SPLITTER = Splitter.on(BREAKING_WHITESPACE).omitEmptyStrings().trimResults();

    private final String query;
    private final ImmutableList<String> terms;

    static SearchQuery from(String query) {
        return new SearchQuery(truncate(query));
    }

    private static String truncate(String query) {
        // TODO handle word boundaries and UTF-16 surrogate pairs (e.g. emojis)
        // see http://stackoverflow.com/a/32223795
        return query.substring(0, Math.min(MAX_QUERY_LENGTH, query.length()));
    }

    private SearchQuery(String query) {
        this.query = query;
        this.terms = ImmutableList.copyOf(QUERY_SPLITTER.split(query));
    }

    String getQuery() {
        return query;
    }

    ImmutableList<String> getTerms() {
        return terms;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query, terms);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SearchQuery other = (SearchQuery) obj;
        return Objects.equal(this.query, other.query)
                && Objects.equal(this.terms, other.terms);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("query", query)
                .add("terms", terms)
                .toString();
    }
}
