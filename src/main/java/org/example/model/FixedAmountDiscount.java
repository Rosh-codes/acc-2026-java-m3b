package org.example.model;

public class FixedAmountDiscount extends Discount {
    private final double amount;

    public FixedAmountDiscount(String code, double amount) {
        super(code);
        if (!Double.isFinite(amount) || amount < 0) throw new IllegalArgumentException("Discount amount cannot be negative.");
        this.amount = amount;
    }

    @Override
    public double apply(double originalAmount) {
        return Math.max(0, originalAmount - amount);
    }
}
