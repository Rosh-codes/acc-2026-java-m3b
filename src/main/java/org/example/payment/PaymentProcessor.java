package org.example.payment;

import org.example.model.Order;
import org.example.model.PaymentResult;

public class PaymentProcessor {
    public PaymentResult process(Order order, PaymentMethod paymentMethod){
        if (order == null) return new PaymentResult(false, "No order to pay.");
        if (paymentMethod == null) return new PaymentResult(false, "Select a valid payment method.");
        if (order.isPaid()) return new PaymentResult(false, "Order has already been paid.");
        if (order.getItems().isEmpty()) return new PaymentResult(false, "Cannot pay for an empty order.");

        PaymentResult result = paymentMethod.pay(order.calculateTotal());

        if(result.isSuccessful()){
            order.markAsPaid();
        }

        return result;
    }
}
