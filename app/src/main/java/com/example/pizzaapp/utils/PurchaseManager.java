package com.example.pizzaapp.utils;

import com.example.pizzaapp.models.CartItem;
import com.example.pizzaapp.models.Purchase;
import com.example.pizzaapp.models.Donut;

import java.util.ArrayList;
import java.util.List;

public class PurchaseManager {
    private static PurchaseManager instance;
    private final List<Purchase> purchases = new ArrayList<>();

    private PurchaseManager() {}

    public static synchronized PurchaseManager getInstance() {
        if (instance == null) {
            instance = new PurchaseManager();
        }
        return instance;
    }

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

    public List<Purchase> getPurchases() {
        return new ArrayList<>(purchases);
    }

    public void clearPurchases() {
        purchases.clear();
    }
}
