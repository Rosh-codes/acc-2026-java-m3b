package org.example.menu;

import org.example.config.AppConfig;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.PaymentResult;
import org.example.payment.PaymentMethod;
import org.example.payment.PaymentMethodFactory;
import org.example.payment.PaymentProcessor;

import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();

    private Order currentOrder;

    public void start(){
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to " + config.getApplicationName());

        boolean running = true;
        while(running){
            printMenu();

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
                continue;
            }

            switch (option){
                case 1 -> createOrder();
                case 2 -> addItem();
                case 3 -> viewOrder();
                case 4 -> payOrder();
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void createOrder(){
        System.out.println("Customer name:");
        String customerName = scanner.nextLine();

        currentOrder = Order.builder()
                .customerName(customerName)
                .build();
        System.out.println("Order created for " + customerName);
    }

    private void addItem(){
        if (!hasEditableOrder()) return;

        System.out.println("Item name:");
        String itemName = scanner.nextLine();

        System.out.println("Price:");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }

        System.out.println("Quantity:");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        try {
            currentOrder.addItem(new OrderItem(itemName, price, quantity));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Item added to order");
    }

    private void viewOrder(){
        if (currentOrder == null) {
            System.out.println("Create an order first.");
            return;
        }

        System.out.println("Customer: " + currentOrder.getCustomerName());
        System.out.println("Status: " +  currentOrder.getStatus());
        System.out.println("Items:");

        for (OrderItem item : currentOrder.getItems()){
            System.out.println("- " + item);
        }

        System.out.println("Total: " + currentOrder.calculateTotal());
    }

    private void payOrder(){
        if (currentOrder == null) {
            System.out.println("Create an order first.");
            return;
        }

        System.out.println("""
                Select payment method:
                1. Credit Card
                2. PayPal
                3. Gift Card
                """);
        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid payment method.");
            return;
        }

        PaymentMethod paymentMethod = switch(option){
            case 1 -> createCreditCardPayment();
            case 2 -> createPaypalPayment();
            case 3 -> createGiftCardPayment();
            default -> null;
        };

        if (paymentMethod == null) {
            System.out.println("Invalid payment method.");
            return;
        }

        PaymentResult result = paymentProcessor.process(currentOrder, paymentMethod);
        System.out.println(result.getMessage());
    }

    private PaymentMethod createCreditCardPayment(){
        System.out.println("Card number:");
        String cardNumber =  scanner.nextLine();

        System.out.println("Card holder name:");
        String cardHolderName =  scanner.nextLine();

        return PaymentMethodFactory.createCreditCardPayment(cardNumber,cardHolderName);
    }

    private  PaymentMethod createPaypalPayment(){
        System.out.println("PayPal email:");
        String email = scanner.nextLine();
        return PaymentMethodFactory.createPaypalPayment(email);
    }

    private PaymentMethod createGiftCardPayment(){
        System.out.println("Gift card code:");
        String code = scanner.nextLine();
        System.out.println("Gift card balance:");
        try {
            double balance = Double.parseDouble(scanner.nextLine());
            return PaymentMethodFactory.createGiftCardPayment(code, balance);
        } catch (NumberFormatException e) {
            System.out.println("Invalid balance.");
            return null;
        }
    }

    private boolean hasEditableOrder() {
        if (currentOrder == null) {
            System.out.println("Create an order first.");
            return false;
        }
        if (currentOrder.isPaid()) {
            System.out.println("This order has already been paid.");
            return false;
        }
        return true;
    }

    private void printMenu(){
        System.out.println("""
                1. Create order
                2. Add item to order
                3. View order
                4. Pay order
                0. Exit
                """);
    }
}
