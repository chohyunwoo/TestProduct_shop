package org.example.domain.policy;

public final class PolicyResult {

    private final boolean approved;
    private final String reason;

    private PolicyResult(boolean approved, String reason) {
        this.approved = approved;
        this.reason = reason;
    }

    public static PolicyResult approved() {
        return new PolicyResult(true, "");
    }

    public static PolicyResult rejected(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Rejection reason must not be null or blank");
        }
        return new PolicyResult(false, reason);
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isRejected() {
        return !approved;
    }

    public String reason() {
        return reason;
    }

    @Override
    public String toString() {
        return approved ? "PolicyResult{APPROVED}" : "PolicyResult{REJECTED: " + reason + "}";
    }
}
