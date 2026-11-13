package dev.reviewlab;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class CodeReviewLabTest {
    private static int tests = 0;

    public static void main(String[] args) throws Exception {
        testSequentialIdempotency();
        testFlawedCandidateConcurrentDoubleReservation();
        testCorrectConcurrentIdempotency();
        testSameKeyDifferentPayloadRejected();
        testInsufficientInventory();
        testFlawedCandidateMoneyOverflow();
        testCorrectMoneyArithmetic();
        testInvalidMoneyInputs();
        System.out.println("PASS: " + tests + " test cases");
    }

    private static void testSequentialIdempotency() {
        Inventory inventory = new Inventory(); inventory.setStock("BOOK", 5);
        CorrectCheckoutService service = new CorrectCheckoutService(inventory);
        Receipt a = service.checkout("req-1", "BOOK", 1);
        Receipt b = service.checkout("req-1", "BOOK", 1);
        check(a.equals(b) && inventory.available("BOOK") == 4, "duplicate sequential request reserved twice");
    }

    private static void testFlawedCandidateConcurrentDoubleReservation() throws Exception {
        Inventory inventory = new Inventory(); inventory.setStock("BOOK", 5);
        CyclicBarrier bothChecked = new CyclicBarrier(2);
        FlawedCheckoutService candidate = new FlawedCheckoutService(inventory, () -> {
            try { bothChecked.await(4, TimeUnit.SECONDS); }
            catch (Exception ex) { throw new RuntimeException(ex); }
        });
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<Receipt> a = pool.submit(() -> candidate.checkout("same-key", "BOOK", 1));
            Future<Receipt> b = pool.submit(() -> candidate.checkout("same-key", "BOOK", 1));
            Receipt left = a.get(5, TimeUnit.SECONDS);
            Receipt right = b.get(5, TimeUnit.SECONDS);
            check(inventory.available("BOOK") == 3 && !left.receiptId().equals(right.receiptId()),
                  "the deliberately flawed candidate should reproduce double reservation");
        } finally { pool.shutdownNow(); }
    }

    private static void testCorrectConcurrentIdempotency() throws Exception {
        Inventory inventory = new Inventory(); inventory.setStock("BOOK", 5);
        CorrectCheckoutService service = new CorrectCheckoutService(inventory);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<Receipt> a = pool.submit(() -> service.checkout("same-key", "BOOK", 1));
            Future<Receipt> b = pool.submit(() -> service.checkout("same-key", "BOOK", 1));
            Receipt left = a.get(5, TimeUnit.SECONDS);
            Receipt right = b.get(5, TimeUnit.SECONDS);
            check(inventory.available("BOOK") == 4 && left.equals(right),
                  "concurrent duplicates must reserve stock only once");
        } finally { pool.shutdownNow(); }
    }

    private static void testSameKeyDifferentPayloadRejected() {
        Inventory inventory = new Inventory(); inventory.setStock("BOOK", 5);
        CorrectCheckoutService service = new CorrectCheckoutService(inventory);
        service.checkout("req-1", "BOOK", 1);
        expect(IllegalArgumentException.class, () -> service.checkout("req-1", "BOOK", 2));
    }

    private static void testInsufficientInventory() {
        Inventory inventory = new Inventory(); inventory.setStock("BOOK", 1);
        CorrectCheckoutService service = new CorrectCheckoutService(inventory);
        expect(IllegalStateException.class, () -> service.checkout("req-1", "BOOK", 2));
    }

    private static void testFlawedCandidateMoneyOverflow() {
        int observed = FlawedPricing.totalCents(50_000, 50_000);
        check((long) observed != 2_500_000_000L, "candidate example should demonstrate integer overflow");
    }

    private static void testCorrectMoneyArithmetic() {
        check(CorrectPricing.totalCents(50_000, 50_000L) == 2_500_000_000L, "money must retain exact cents");
    }

    private static void testInvalidMoneyInputs() {
        expect(IllegalArgumentException.class, () -> CorrectPricing.totalCents(-1, 10L));
    }

    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        tests++;
    }

    private static void expect(Class<? extends Throwable> type, Runnable runnable) {
        try { runnable.run(); }
        catch (Throwable ex) { check(type.isInstance(ex), "unexpected exception " + ex); return; }
        throw new AssertionError("expected " + type.getSimpleName());
    }
}
