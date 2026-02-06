package org.example.domain.tag;

import java.util.Objects;
import java.util.UUID;

public final class TagId {

    private final String value;

    private TagId(String value) {
        this.value = value;
    }

    public static TagId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TagId must not be null or blank");
        }
        return new TagId(value);
    }

    public static TagId generate() {
        return new TagId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagId that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TagId{" + value + "}";
    }
}
