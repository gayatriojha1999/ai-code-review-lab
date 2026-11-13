package dev.reviewlab;

/** Deliberately flawed candidate: multiplication overflows signed 32-bit int. */
public final class FlawedPricing {
    private FlawedPricing() { }
    public static int totalCents(int units, int unitPriceCents) {
        return units * unitPriceCents;
    }
}
