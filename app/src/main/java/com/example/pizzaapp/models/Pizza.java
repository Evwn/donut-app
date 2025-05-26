package com.example.pizzaapp.models;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    public enum Size {
        REGULAR(5.0),
        FAMILY(8.0);
        
        private final double basePrice;
        
        Size(double basePrice) {
            this.basePrice = basePrice;
        }
        
        public double getBasePrice() {
            return basePrice;
        }
    }
    
    private String name;
    private String description;
    private double basePrice;
    private int imageResource;
    private Size size;
    private List<String> toppings;
    private int maxFreeToppings = 3;
    private double extraToppingPrice = 1.0;

    public Pizza(String name, String description, double basePrice, int imageResource) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.imageResource = imageResource;
        this.size = Size.REGULAR;
        this.toppings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getBasePrice() {
        return size.getBasePrice();
    }
    
    public double getTotalPrice() {
        double total = getBasePrice();
        // Add extra toppings cost
        int extraToppings = Math.max(0, toppings.size() - maxFreeToppings);
        total += extraToppings * extraToppingPrice;
        return total;
    }
    
    public void setSize(Size size) {
        this.size = size;
    }
    
    public Size getSize() {
        return size;
    }
    
    public void addTopping(String topping) {
        if (!toppings.contains(topping)) {
            toppings.add(topping);
        }
    }
    
    public void removeTopping(String topping) {
        toppings.remove(topping);
    }
    
    public List<String> getToppings() {
        return new ArrayList<>(toppings);
    }

    public int getImageResource() {
        return imageResource;
    }
}
