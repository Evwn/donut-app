package com.example.pizzaapp.utils;

import com.example.pizzaapp.models.CartItem;
import com.example.pizzaapp.models.Pizza;

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

    public void addToCart(Pizza pizza, int quantity, boolean requiresDelivery, double deliveryDistance) {
        // Check if similar pizza already exists in cart (same name, size, and toppings)
        for (CartItem item : cartItems) {
            if (isSamePizzaConfiguration(item.getPizza(), pizza)) {
                item.setQuantity(item.getQuantity() + quantity);
                if (requiresDelivery) {
                    item.setDelivery(true, deliveryDistance);
                }
                return;
            }
        }
        // If not, add as new item
        CartItem newItem = new CartItem(pizza, quantity);
        if (requiresDelivery) {
            newItem.setDelivery(true, deliveryDistance);
        }
        cartItems.add(newItem);
    }
    
    public void addToCart(Pizza pizza, int quantity) {
        addToCart(pizza, quantity, false, 0);
    }
    
    private boolean isSamePizzaConfiguration(Pizza p1, Pizza p2) {
        if (!p1.getName().equals(p2.getName())) return false;
        if (p1.getSize() != p2.getSize()) return false;
        if (p1.getToppings().size() != p2.getToppings().size()) return false;
        return p1.getToppings().containsAll(p2.getToppings());
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
            subtotal += item.getPizzaSubtotal();
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
