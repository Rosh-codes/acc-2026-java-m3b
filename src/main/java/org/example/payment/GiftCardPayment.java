package org.example.payment;

import org.example.model.PaymentResult;

public class GiftCardPayment extends PaymentMethod{
    private final String code;
    private double balance;

    public GiftCardPayment(String code, double balance) {
        super("Gift Card");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Gift card code is required.");
        if (!Double.isFinite(balance) || balance < 0) throw new IllegalArgumentException("Gift card balance cannot be negative.");
        this.code = code;
        this.balance = balance;
    }

    @Override
    public PaymentResult processPayment(double amount){
        if (code.isBlank()) return new PaymentResult(false, "Gift card code is required.");
        if (balance < amount) return new PaymentResult(false, "Insufficient gift card balance.");
        balance -= amount;
        return new PaymentResult(true, "Paid " + amount + " using gift card");
    }
}
