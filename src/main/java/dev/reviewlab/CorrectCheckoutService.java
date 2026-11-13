package dev.reviewlab;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Single-instance teaching fix; does not provide multi-instance durability. */
public final class CorrectCheckoutService {
    private final Inventory inventory;
    private final Map<String, Receipt> receipts = new HashMap<>();

    public CorrectCheckoutService(Inventory inventory) {
        this.inventory = inventory;
    }

    public synchronized Receipt checkout(String requestId, String sku, int quantity) {
        if (requestId == null || requestId.isBlank())
            throw new IllegalArgumentException("requestId is required");
        if (sku == null || sku.isBlank())
            throw new IllegalArgumentException("sku is required");
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity must be positive");
        Receipt existing = receipts.get(requestId);
        if (existing != null) {
            if (!existing.sku().equals(sku) || existing.quantity() != quantity)
                throw new IllegalArgumentException("idempotency key reused for different request");
            return existing;
        }
        inventory.reserve(sku, quantity);
        Receipt receipt = new Receipt(UUID.randomUUID().toString(), requestId, sku, quantity);
        receipts.put(requestId, receipt);
        return receipt;
    }
}
