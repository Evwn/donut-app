package com.example.pizzaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pizzaapp.R;
import com.example.pizzaapp.models.Pizza;
import com.example.pizzaapp.utils.CartManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PizzaAdapter extends ArrayAdapter<Pizza> {
    private final Context context;
    private final List<Pizza> pizzas;

    public PizzaAdapter(@NonNull Context context, List<Pizza> pizzas) {
        super(context, 0, pizzas);
        this.context = context;
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_pizza, parent, false);
        }

        Pizza pizza = getItem(position);
        if (pizza != null) {
            ImageView pizzaImage = convertView.findViewById(R.id.pizza_image);
            TextView pizzaName = convertView.findViewById(R.id.pizza_name);
            TextView pizzaDescription = convertView.findViewById(R.id.pizza_description);
            TextView pizzaPrice = convertView.findViewById(R.id.pizza_price);

            pizzaImage.setImageResource(pizza.getImageResource());
            pizzaName.setText(pizza.getName());
            pizzaDescription.setText(pizza.getDescription());
            pizzaPrice.setText(String.format("From $%.2f", pizza.getBasePrice()));

            // Set click listener for the add to cart button
            convertView.findViewById(R.id.button_add_to_cart).setOnClickListener(v -> showPizzaCustomizationDialog(pizza));
        }

        return convertView;
    }
    
    private void showPizzaCustomizationDialog(Pizza pizza) {
        // Create a copy of the pizza to modify
        Pizza customizedPizza = new Pizza(pizza.getName(), pizza.getDescription(), 
                pizza.getBasePrice(), pizza.getImageResource());
        
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_pizza_customization, null);
        
        // Setup size selection
        RadioGroup sizeGroup = dialogView.findViewById(R.id.size_group);
        RadioButton regularSize = dialogView.findViewById(R.id.radio_regular);
        RadioButton familySize = dialogView.findViewById(R.id.radio_family);
        
        // Setup toppings checkboxes
        CheckBox[] toppingCheckboxes = {
            dialogView.findViewById(R.id.topping_pepperoni),
            dialogView.findViewById(R.id.topping_mushrooms),
            dialogView.findViewById(R.id.topping_olives),
            dialogView.findViewById(R.id.topping_onions),
            dialogView.findViewById(R.id.topping_peppers),
            dialogView.findViewById(R.id.topping_extra_cheese)
        };
        
        // Setup delivery options
        ViewGroup deliverySection = dialogView.findViewById(R.id.delivery_section);
        TextView deliveryDistanceText = dialogView.findViewById(R.id.delivery_distance);
        
        // Show/hide delivery section based on delivery option
        ((RadioGroup)dialogView.findViewById(R.id.delivery_option_group)).setOnCheckedChangeListener((group, checkedId) -> {
            boolean isDelivery = checkedId == R.id.radio_delivery_yes;
            deliverySection.setVisibility(isDelivery ? View.VISIBLE : View.GONE);
        });
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Customize Your " + pizza.getName())
               .setView(dialogView)
               .setPositiveButton("Add to Cart", (dialog, which) -> {
                   // Set pizza size
                   customizedPizza.setSize(familySize.isChecked() ? 
                           Pizza.Size.FAMILY : Pizza.Size.REGULAR);
                   
                   // Add selected toppings
                   List<String> availableToppings = Arrays.asList(
                           "Pepperoni", "Mushrooms", "Olives", 
                           "Onions", "Peppers", "Extra Cheese");
                   
                   for (int i = 0; i < toppingCheckboxes.length; i++) {
                       if (toppingCheckboxes[i].isChecked()) {
                           customizedPizza.addTopping(availableToppings.get(i));
                       }
                   }
                   
                   // Check if delivery is selected
                   boolean isDelivery = ((RadioGroup)dialogView.findViewById(R.id.delivery_option_group))
                           .getCheckedRadioButtonId() == R.id.radio_delivery_yes;
                   
                   double deliveryDistance = 0;
                   if (isDelivery) {
                       try {
                           deliveryDistance = Double.parseDouble(
                                   deliveryDistanceText.getText().toString());
                       } catch (NumberFormatException e) {
                           deliveryDistance = 0;
                       }
                   }
                   
                   // Add to cart
                   CartManager.getInstance().addToCart(customizedPizza, 1, isDelivery, deliveryDistance);
                   
                   // Show confirmation message
                   String message = customizedPizza.getName() + " added to cart!";
                   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancel", null);
               
        builder.create().show();
    }
}
