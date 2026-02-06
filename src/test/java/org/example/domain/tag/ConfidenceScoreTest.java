package org.example.domain.tag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfidenceScoreTest {

    @Test
    void create_withValidScore_succeeds() {
        ConfidenceScore score = ConfidenceScore.of(0.85);
        assertEquals(0.85, score.value());
    }

    @Test
    void create_withZero_succeeds() {
        ConfidenceScore score = ConfidenceScore.of(0.0);
        assertEquals(0.0, score.value());
    }

    @Test
    void create_withOne_succeeds() {
        ConfidenceScore score = ConfidenceScore.of(1.0);
        assertEquals(1.0, score.value());
    }

    @Test
    void create_belowZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ConfidenceScore.of(-0.1));
    }

    @Test
    void create_aboveOne_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ConfidenceScore.of(1.1));
    }

    @Test
    void isAbove_returnsCorrectly() {
        ConfidenceScore score = ConfidenceScore.of(0.7);
        assertTrue(score.isAbove(0.5));
        assertFalse(score.isAbove(0.8));
        assertFalse(score.isAbove(0.7));
    }

    @Test
    void equality_sameValue_areEqual() {
        ConfidenceScore s1 = ConfidenceScore.of(0.5);
        ConfidenceScore s2 = ConfidenceScore.of(0.5);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
