package dev.reviewlab;

public final class CorrectPricing {
    private CorrectPricing() { }
    public static long totalCents(int units, long unitPriceCents) {
        if (units < 0 || unitPriceCents < 0)
            throw new IllegalArgumentException("negative values not allowed");
        return Math.multiplyExact((long) units, unitPriceCents);
    }
}
