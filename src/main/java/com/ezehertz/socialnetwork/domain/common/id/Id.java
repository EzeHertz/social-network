package com.ezehertz.socialnetwork.domain.common.id;

import java.util.Objects;

public class Id<T> implements Comparable<Id<T>> {
    private final String rawId;

    private Id(String rawId) {
        this.rawId = rawId;
    }

    public static <T> Id<T> of(String rawId) {
        return new Id<>(rawId);
    }

    public String rawId() { return rawId; }

    @Override
    public int compareTo(Id<T> other) {
        return this.rawId.compareTo(other.rawId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id<?> id)) return false;
        return this.getClass().equals(o.getClass()) && Objects.equals(rawId, id.rawId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), rawId);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + rawId + ")";
    }
}
