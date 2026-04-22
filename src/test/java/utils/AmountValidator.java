package utils;

public class AmountValidator {

    private static ThreadLocal<Double> previousBalance = new ThreadLocal<>();
    private static ThreadLocal<Double> currentBalance = new ThreadLocal<>();

    // Store previous balance
    public static void setPreviousBalance(double balance) {
        previousBalance.set(balance);
        System.out.println("💰 Previous Balance: " + balance);
    }

    // Store current balance
    public static void setCurrentBalance(double balance) {
        currentBalance.set(balance);
        System.out.println("💰 Current Balance: " + balance);
    }

    // Validate Debit Transaction
    public static void validateDebit(double amount) {

        double prev = previousBalance.get();
        double curr = currentBalance.get();

        double expected = prev - amount;

        if (Math.abs(expected - curr) < 0.5) {
            System.out.println("✅ Debit Transaction Validated");
        } else {
            throw new AssertionError(
                "❌ Debit Failed | Expected: " + expected + " | Actual: " + curr
            );
        }

        // ✅ update for next transaction
        previousBalance.set(curr);
    }

    // Validate Credit Transaction (optional)
    public static void validateCredit(double amount) {

        double prev = previousBalance.get();
        double curr = currentBalance.get();

        double expected = prev + amount;

        if (Math.abs(expected - curr) < 0.5) {
            System.out.println("✅ Credit Transaction Validated");
        } else {
            throw new AssertionError(
                "❌ Credit Failed | Expected: " + expected + " | Actual: " + curr
            );
        }

        previousBalance.set(curr);
    }
}