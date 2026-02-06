package org.example.domain.policy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolicyResultTest {

    @Test
    void approved_createsApprovedResult() {
        PolicyResult result = PolicyResult.approved();
        assertTrue(result.isApproved());
        assertFalse(result.isRejected());
    }

    @Test
    void rejected_createsRejectedResultWithReason() {
        PolicyResult result = PolicyResult.rejected("too low confidence");
        assertFalse(result.isApproved());
        assertTrue(result.isRejected());
        assertEquals("too low confidence", result.reason());
    }

    @Test
    void approved_reasonIsEmpty() {
        PolicyResult result = PolicyResult.approved();
        assertEquals("", result.reason());
    }

    @Test
    void rejected_withNullReason_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> PolicyResult.rejected(null));
    }

    @Test
    void rejected_withBlankReason_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> PolicyResult.rejected("  "));
    }
}
