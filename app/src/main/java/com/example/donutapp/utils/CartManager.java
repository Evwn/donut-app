package com.example.donutapp.utils;

import com.example.donutapp.models.CartItem;
import com.example.donutapp.models.Donut;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Donut donut, int quantity, boolean requiresDelivery, double deliveryDistance) {
        // Check if similar donut already exists in cart (same name, size, and customizations)
        for (CartItem item : cartItems) {
            if (isSameDonutConfiguration(item.getDonut(), donut)) {
                item.setQuantity(item.getQuantity() + quantity);
                if (requiresDelivery) {
                    item.setDelivery(true, deliveryDistance);
                }
                return;
            }
        }
        // If not, add as new item
        CartItem newItem = new CartItem(donut, quantity);
        if (requiresDelivery) {
            newItem.setDelivery(true, deliveryDistance);
        }
        cartItems.add(newItem);
    }
    
    public void addToCart(Donut donut, int quantity) {
        addToCart(donut, quantity, false, 0);
    }
    
    private boolean isSameDonutConfiguration(Donut d1, Donut d2) {
        if (!d1.getName().equals(d2.getName())) return false;
        if (d1.getSize() != d2.getSize()) return false;
        if (d1.getToppings().size() != d2.getToppings().size()) return false;
        if (d1.getGlazes().size() != d2.getGlazes().size()) return false;
        if (d1.getSprinkles().size() != d2.getSprinkles().size()) return false;
        
        return d1.getToppings().containsAll(d2.getToppings()) &&
               d1.getGlazes().containsAll(d2.getGlazes()) &&
               d1.getSprinkles().containsAll(d2.getSprinkles());
    }

    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void updateQuantity(CartItem cartItem, int newQuantity) {
        if (newQuantity <= 0) {
            cartItems.remove(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getDonutSubtotal();
        }
        return subtotal;
    }
    
    public double getDeliveryFee() {
        double totalFee = 0;
        for (CartItem item : cartItems) {
            if (item.requiresDelivery()) {
                // Only charge delivery fee for the first item that requires delivery
                totalFee = item.getDeliveryFee();
                break;
            }
        }
        return totalFee;
    }
    
    public double getTotal() {
        return getSubtotal() + getDeliveryFee();
    }
}
