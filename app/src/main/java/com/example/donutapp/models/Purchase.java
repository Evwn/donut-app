package com.example.donutapp.models;

import java.util.Date;

public class Purchase {
    private Donut donut;
    private int quantity;
    private Date purchaseDate;
    private double totalPrice;
    private boolean isDelivery;
    private double deliveryFee;

    public Purchase(Donut donut, int quantity) {
        this.donut = donut;
        this.quantity = quantity;
        this.purchaseDate = new Date();
        this.totalPrice = donut.getTotalPrice() * quantity;
        this.isDelivery = false;
        this.deliveryFee = 0.0;
    }

    public Purchase(CartItem cartItem) {
        this.donut = cartItem.getDonut();
        this.quantity = cartItem.getQuantity();
        this.purchaseDate = new Date();
        this.totalPrice = cartItem.getTotalPrice();
        this.isDelivery = cartItem.requiresDelivery();
        this.deliveryFee = cartItem.getDeliveryFee();
    }

    public Donut getDonut() {
        return donut;
    }
    
    // For backward compatibility
    public Donut getPizza() {
        return donut;
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
    
    /**
     * Calculates the subtotal of the donut purchase (price * quantity)
     * @return The subtotal amount
     */
    public double getDonutSubtotal() {
        return donut.getTotalPrice() * quantity;
    }
}
