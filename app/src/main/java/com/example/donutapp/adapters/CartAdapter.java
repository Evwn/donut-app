package com.example.donutapp.adapters;

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

import com.example.donutapp.R;
import com.example.donutapp.models.CartItem;
import com.example.donutapp.models.Donut;
import com.example.donutapp.utils.CartManager;

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

            Donut donut = cartItem.getDonut();
            
            // Display size information
            String sizeInfo = String.format("(%s)", donut.getSize().toString());
            
            // Get all customizations
            List<String> toppings = donut.getToppings();
            List<String> glazes = donut.getGlazes();
            List<String> sprinkles = donut.getSprinkles();
            
            // Build customization info
            StringBuilder customizations = new StringBuilder();
            
            if (!toppings.isEmpty()) {
                customizations.append("\nToppings: ").append(String.join(", ", toppings));
            }
            if (!glazes.isEmpty()) {
                customizations.append("\nGlazes: ").append(String.join(", ", glazes));
            }
            if (!sprinkles.isEmpty()) {
                customizations.append("\nSprinkles: ").append(String.join(", ", sprinkles));
            }
            
            // Show extra charges info if any
            int totalExtras = toppings.size() + glazes.size() + sprinkles.size() - 2; // First 2 are free
            if (totalExtras > 0) {
                customizations.append(String.format("\n+%d extra item(s) @ $0.50 each", totalExtras));
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
                    donut.getName(), 
                    sizeInfo,
                    customizations.toString(),
                    deliveryInfo);
                    
            itemName.setText(itemDetails);
            itemPrice.setText(String.format("$%.2f", cartItem.getDonutSubtotal()));
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
                        cartItem.getDonut().getName(), cartItem.getQuantity());
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
                            cartItem.getDonut().getName(), cartItem.getQuantity());
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
                String message = String.format("%s removed from cart", cartItem.getDonut().getName());
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
