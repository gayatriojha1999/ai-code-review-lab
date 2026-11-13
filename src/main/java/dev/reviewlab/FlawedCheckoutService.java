package dev.reviewlab;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Deliberately flawed candidate: check-then-act is not atomic. */
public final class FlawedCheckoutService {
    private final Inventory inventory;
    private final Map<String, Receipt> receipts = new ConcurrentHashMap<>();
    private final Runnable afterCheck;

    public FlawedCheckoutService(Inventory inventory, Runnable afterCheck) {
        this.inventory = inventory;
        this.afterCheck = afterCheck;
    }

    public Receipt checkout(String requestId, String sku, int quantity) {
        Receipt existing = receipts.get(requestId);
        if (existing != null) return existing;
        afterCheck.run(); // Test seam to reliably reproduce a concurrent request race.
        inventory.reserve(sku, quantity);
        Receipt receipt = new Receipt(UUID.randomUUID().toString(), requestId, sku, quantity);
        receipts.put(requestId, receipt);
        return receipt;
    }
}
