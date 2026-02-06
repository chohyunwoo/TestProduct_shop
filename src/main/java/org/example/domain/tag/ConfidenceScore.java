package org.example.domain.tag;

import java.util.Objects;

public final class ConfidenceScore {

    private final double value;

    private ConfidenceScore(double value) {
        this.value = value;
    }

    public static ConfidenceScore of(double value) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("ConfidenceScore must be between 0.0 and 1.0, got: " + value);
        }
        return new ConfidenceScore(value);
    }

    public double value() {
        return value;
    }

    public boolean isAbove(double threshold) {
        return value > threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfidenceScore that)) return false;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ConfidenceScore{" + value + "}";
    }
}
