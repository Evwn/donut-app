package com.example.pizzaapp.models;

public class CartItem {
    private Donut donut;
    private int quantity;
    private boolean requiresDelivery;
    private double deliveryDistance; // in kilometers
    
    // Delivery pricing constants
    private static final double BASE_DELIVERY_FEE = 10.0;
    private static final double MID_DISTANCE_RATE = 2.5; // per km after 5km up to 10km
    private static final double FAR_DISTANCE_RATE = 4.0; // per km after 10km
    private static final int FIRST_THRESHOLD = 5; // km
    private static final int SECOND_THRESHOLD = 10; // km

    public CartItem(Donut donut, int quantity) {
        this.donut = donut;
        this.quantity = quantity;
        this.requiresDelivery = false;
        this.deliveryDistance = 0;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setDelivery(boolean requiresDelivery, double distance) {
        this.requiresDelivery = requiresDelivery;
        this.deliveryDistance = Math.max(0, distance);
    }
    
    public boolean requiresDelivery() {
        return requiresDelivery;
    }
    
    public double getDeliveryDistance() {
        return deliveryDistance;
    }
    
    public double getDeliveryFee() {
        if (!requiresDelivery) return 0;
        
        if (deliveryDistance <= FIRST_THRESHOLD) {
            return BASE_DELIVERY_FEE;
        } else if (deliveryDistance <= SECOND_THRESHOLD) {
            return BASE_DELIVERY_FEE + 
                  (deliveryDistance - FIRST_THRESHOLD) * MID_DISTANCE_RATE;
        } else {
            return 25.0 + // $10 base + $15 for 5-10km
                  (deliveryDistance - SECOND_THRESHOLD) * FAR_DISTANCE_RATE;
        }
    }
    
    public double getDonutSubtotal() {
        return donut.getTotalPrice() * quantity;
    }
    
    // For backward compatibility
    public double getPizzaSubtotal() {
        return getDonutSubtotal();
    }
    
    public double getTotalPrice() {
        return getPizzaSubtotal() + getDeliveryFee();
    }
}
