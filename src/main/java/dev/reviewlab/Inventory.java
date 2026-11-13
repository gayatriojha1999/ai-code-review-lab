package dev.reviewlab;

import java.util.HashMap;
import java.util.Map;

/** Simple in-memory stock store; synchronized for deterministic reservation. */
public final class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();

    public synchronized void setStock(String sku, int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("negative inventory");
        stock.put(sku, quantity);
    }

    public synchronized int available(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public synchronized void reserve(String sku, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        int current = stock.getOrDefault(sku, 0);
        if (current < quantity) throw new IllegalStateException("insufficient inventory");
        stock.put(sku, current - quantity);
    }
}
