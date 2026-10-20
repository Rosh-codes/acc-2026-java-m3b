package org.example.payment;

import org.example.model.PaymentResult;

public class CreditCardPayment extends PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        super("CreditCard");
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public PaymentResult processPayment(double amount) {
        String normalizedCardNumber = cardNumber == null ? "" : cardNumber.replaceAll("\\s", "");

        if (normalizedCardNumber.length() < 12 || normalizedCardNumber.length() > 19) {
            return new PaymentResult(false, "Invalid card number.");
        }

        for (char c : normalizedCardNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return new PaymentResult(false, "Invalid card number.");
            }
        }

        if (cardHolderName == null || cardHolderName.isBlank()) {
            return new PaymentResult(false, "Card holder name is required.");
        }

        return new PaymentResult(true, "Paid " + amount + " using credit card ending with " + normalizedCardNumber.substring(normalizedCardNumber.length() - 4));
    }
}
