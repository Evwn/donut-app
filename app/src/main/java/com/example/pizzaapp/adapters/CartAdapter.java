package com.example.pizzaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pizzaapp.R;
import com.example.pizzaapp.models.CartItem;
import com.example.pizzaapp.models.Pizza;
import com.example.pizzaapp.utils.CartManager;

import java.util.List;

public class CartAdapter extends ArrayAdapter<CartItem> {
    private final Context context;
    private final List<CartItem> cartItems;
    private final OnCartUpdateListener listener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }


    public CartAdapter(@NonNull Context context, List<CartItem> cartItems, OnCartUpdateListener listener) {
        super(context, 0, cartItems);
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_cart, parent, false);
        }

        CartItem cartItem = getItem(position);
        if (cartItem != null) {
            TextView itemName = convertView.findViewById(R.id.cart_item_name);
            TextView itemPrice = convertView.findViewById(R.id.cart_item_price);
            TextView itemQuantity = convertView.findViewById(R.id.text_quantity);
            ImageButton btnIncrease = convertView.findViewById(R.id.button_increase);
            ImageButton btnDecrease = convertView.findViewById(R.id.button_decrease);
            ImageButton btnRemove = convertView.findViewById(R.id.button_remove);

            Pizza pizza = cartItem.getPizza();
            itemName.setText(pizza.getName());
            
            // Display size information
            String sizeInfo = String.format("(%s)", 
                pizza.getSize() == Pizza.Size.FAMILY ? "Family" : "Regular");
            
            // Display toppings (first 3 are free)
            List<String> toppings = pizza.getToppings();
            String toppingsInfo = "";
            if (!toppings.isEmpty()) {
                toppingsInfo = "\n" + String.join(", ", toppings);
                // Show which toppings are extra
                if (toppings.size() > 3) {
                    toppingsInfo += String.format("\n+%d extra topping(s) @ $1.00 each", 
                            toppings.size() - 3);
                }
            }
            
            // Display delivery info if applicable
            String deliveryInfo = "";
            if (cartItem.requiresDelivery()) {
                deliveryInfo = String.format("\nDelivery (%.1f km): $%.2f", 
                        cartItem.getDeliveryDistance(), 
                        cartItem.getDeliveryFee());
            }
            
            // Combine all info
            String itemDetails = String.format("%s %s%s%s", 
                    pizza.getName(), 
                    sizeInfo,
                    toppingsInfo,
                    deliveryInfo);
                    
            itemName.setText(itemDetails);
            itemPrice.setText(String.format("$%.2f", cartItem.getPizzaSubtotal()));
            itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

            // Set up click listeners for quantity controls
            btnIncrease.setOnClickListener(v -> {
                int newQuantity = cartItem.getQuantity() + 1;
                CartManager.getInstance().updateQuantity(cartItem, newQuantity);
                cartItem.setQuantity(newQuantity);
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onCartUpdated();
                }
                // Show a toast message
                String message = String.format("%s quantity updated to %d",
                        cartItem.getPizza().getName(), cartItem.getQuantity());
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });

            btnDecrease.setOnClickListener(v -> {
                if (cartItem.getQuantity() > 1) {
                    int newQuantity = cartItem.getQuantity() - 1;
                    CartManager.getInstance().updateQuantity(cartItem, newQuantity);
                    cartItem.setQuantity(newQuantity);
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onCartUpdated();
                    }
                    // Show a toast message
                    String message = String.format("%s quantity updated to %d",
                            cartItem.getPizza().getName(), cartItem.getQuantity());
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });

            btnRemove.setOnClickListener(v -> {
                // Remove the item from the cart using CartManager
                CartManager.getInstance().removeFromCart(cartItem);
                cartItems.remove(position);
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onCartUpdated();
                }
                // Show a toast message
                String message = String.format("%s removed from cart", cartItem.getPizza().getName());
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
