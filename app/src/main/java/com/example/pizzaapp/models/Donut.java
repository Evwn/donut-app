package com.example.pizzaapp.models;

import java.util.ArrayList;
import java.util.List;

public class Donut {
    public enum Size {
        SINGLE(2.5),
        HALF_DOZEN(12.0),
        DOZEN(20.0);
        
        private final double basePrice;
        
        Size(double basePrice) {
            this.basePrice = basePrice;
        }
        
        public double getBasePrice() {
            return basePrice;
        }
        
        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
        }
    }
    
    private String name;
    private String description;
    private int imageResource;
    private Size size;
    private List<String> toppings;
    private List<String> glazes;
    private List<String> sprinkles;
    private int maxFreeToppings = 2;
    private double extraToppingPrice = 0.5;

    public Donut(String name, String description, int imageResource) {
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
        this.size = Size.SINGLE;
        this.toppings = new ArrayList<>();
        this.glazes = new ArrayList<>();
        this.sprinkles = new ArrayList<>();
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
        // Add extra toppings cost (first 2 are free)
        int extraToppings = Math.max(0, (toppings.size() + glazes.size() + sprinkles.size()) - maxFreeToppings);
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
    
    public void addGlaze(String glaze) {
        if (!glazes.contains(glaze)) {
            glazes.add(glaze);
        }
    }
    
    public void addSprinkles(String sprinkle) {
        if (!sprinkles.contains(sprinkle)) {
            sprinkles.add(sprinkle);
        }
    }
    
    public void removeTopping(String topping) {
        toppings.remove(topping);
    }
    
    public List<String> getToppings() {
        return new ArrayList<>(toppings);
    }
    
    public List<String> getGlazes() {
        return new ArrayList<>(glazes);
    }
    
    public List<String> getSprinkles() {
        return new ArrayList<>(sprinkles);
    }
    
    public String getCustomizationSummary() {
        StringBuilder summary = new StringBuilder();
        if (!toppings.isEmpty()) {
            summary.append("Toppings: ").append(String.join(", ", toppings)).append("\n");
        }
        if (!glazes.isEmpty()) {
            summary.append("Glazes: ").append(String.join(", ", glazes)).append("\n");
        }
        if (!sprinkles.isEmpty()) {
            summary.append("Sprinkles: ").append(String.join(", ", sprinkles));
        }
        return summary.toString().trim();
    }

    public int getImageResource() {
        return imageResource;
    }
}
