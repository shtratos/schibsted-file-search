package com.schibsted.filesearch;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

class SearchHit {
    private final String filename;
    private final int score;

    SearchHit(String filename, int score) {
        this.filename = filename;
        this.score = score;
    }

    String getFilename() {
        return filename;
    }

    int getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(filename, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SearchHit other = (SearchHit) obj;
        return Objects.equal(this.filename, other.filename)
                && Objects.equal(this.score, other.score);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("filename", filename)
                .add("score", score)
                .toString();
    }
}
