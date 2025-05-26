package com.example.pizzaapp.models;

import java.util.Date;

public class Purchase {
    private Pizza pizza;
    private int quantity;
    private Date purchaseDate;
    private double totalPrice;
    private boolean isDelivery;
    private double deliveryFee;

    public Purchase(Pizza pizza, int quantity) {
        this.pizza = pizza;
        this.quantity = quantity;
        this.purchaseDate = new Date();
        this.totalPrice = pizza.getTotalPrice() * quantity;
        this.isDelivery = false;
        this.deliveryFee = 0.0;
    }

    public Purchase(CartItem cartItem) {
        this.pizza = cartItem.getPizza();
        this.quantity = cartItem.getQuantity();
        this.purchaseDate = new Date();
        this.totalPrice = cartItem.getTotalPrice();
        this.isDelivery = cartItem.requiresDelivery();
        this.deliveryFee = cartItem.getDeliveryFee();
    }

    public Pizza getPizza() {
        return pizza;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public boolean isDelivery() {
        return isDelivery;
    }
    
    public double getDeliveryFee() {
        return deliveryFee;
    }
}
