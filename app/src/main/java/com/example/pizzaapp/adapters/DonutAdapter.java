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
import com.example.pizzaapp.models.Donut;
import com.example.pizzaapp.utils.CartManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DonutAdapter extends ArrayAdapter<Donut> {
    private final Context context;
    private final List<Donut> donuts;

    public DonutAdapter(@NonNull Context context, List<Donut> donuts) {
        super(context, 0, donuts);
        this.context = context;
        this.donuts = donuts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_donut, parent, false);
        }

        Donut donut = getItem(position);
        if (donut != null) {
            ImageView donutImage = convertView.findViewById(R.id.donut_image);
            TextView donutName = convertView.findViewById(R.id.donut_name);
            TextView donutDescription = convertView.findViewById(R.id.donut_description);
            TextView donutPrice = convertView.findViewById(R.id.donut_price);

            donutImage.setImageResource(donut.getImageResource());
            donutName.setText(donut.getName());
            donutDescription.setText(donut.getDescription());
            donutPrice.setText(String.format("From $%.2f", donut.getBasePrice()));

            // Set click listener for the add to cart button
            convertView.findViewById(R.id.button_add_to_cart).setOnClickListener(v -> 
                showDonutCustomizationDialog(donut)
            );
        }

        return convertView;
    }
    
    private void showDonutCustomizationDialog(Donut donut) {
        // Create a copy of the donut to modify
        Donut customizedDonut = new Donut(donut.getName(), donut.getDescription(), donut.getImageResource());
        
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_donut_customization, null);
        
        // Setup size selection
        RadioGroup sizeGroup = dialogView.findViewById(R.id.size_group);
        RadioButton singleSize = dialogView.findViewById(R.id.radio_single);
        RadioButton halfDozenSize = dialogView.findViewById(R.id.radio_half_dozen);
        RadioButton dozenSize = dialogView.findViewById(R.id.radio_dozen);
        
        // Setup toppings checkboxes
        CheckBox[] toppingCheckboxes = {
            dialogView.findViewById(R.id.topping_chocolate_chips),
            dialogView.findViewById(R.id.topping_peanuts),
            dialogView.findViewById(R.id.topping_almonds),
            dialogView.findViewById(R.id.topping_coconut)
        };
        
        // Setup glazes checkboxes
        CheckBox[] glazeCheckboxes = {
            dialogView.findViewById(R.id.glaze_chocolate),
            dialogView.findViewById(R.id.glaze_vanilla),
            dialogView.findViewById(R.id.glaze_strawberry),
            dialogView.findViewById(R.id.glaze_maple)
        };
        
        // Setup sprinkles checkboxes
        CheckBox[] sprinkleCheckboxes = {
            dialogView.findViewById(R.id.sprinkle_rainbow),
            dialogView.findViewById(R.id.sprinkle_chocolate),
            dialogView.findViewById(R.id.sprinkle_caramel)
        };
        
        // Setup delivery options
        ViewGroup deliverySection = dialogView.findViewById(R.id.delivery_section);
        TextView deliveryDistanceText = dialogView.findViewById(R.id.delivery_distance);
        
        // Show/hide delivery section based on delivery option
        ((RadioGroup)dialogView.findViewById(R.id.delivery_option_group)).setOnCheckedChangeListener((group, checkedId) -> {
            boolean isDelivery = checkedId == R.id.radio_delivery_yes;
            deliverySection.setVisibility(isDelivery ? View.VISIBLE : View.GONE);
        });
        
        // Create and show the dialog
        new AlertDialog.Builder(context)
            .setTitle("Customize Your Donut")
            .setView(dialogView)
            .setPositiveButton("Add to Cart", (dialog, which) -> {
                // Set the selected size
                int selectedSizeId = sizeGroup.getCheckedRadioButtonId();
                if (selectedSizeId == R.id.radio_single) {
                    customizedDonut.setSize(Donut.Size.SINGLE);
                } else if (selectedSizeId == R.id.radio_half_dozen) {
                    customizedDonut.setSize(Donut.Size.HALF_DOZEN);
                } else {
                    customizedDonut.setSize(Donut.Size.DOZEN);
                }
                
                // Add selected toppings
                for (CheckBox checkbox : toppingCheckboxes) {
                    if (checkbox.isChecked()) {
                        customizedDonut.addTopping(checkbox.getText().toString());
                    }
                }
                
                // Add selected glazes
                for (CheckBox checkbox : glazeCheckboxes) {
                    if (checkbox.isChecked()) {
                        customizedDonut.addGlaze(checkbox.getText().toString());
                    }
                }
                
                // Add selected sprinkles
                for (CheckBox checkbox : sprinkleCheckboxes) {
                    if (checkbox.isChecked()) {
                        customizedDonut.addSprinkles(checkbox.getText().toString());
                    }
                }
                
                // Check if delivery is selected
                boolean isDelivery = ((RadioGroup)dialogView.findViewById(R.id.delivery_option_group))
                    .getCheckedRadioButtonId() == R.id.radio_delivery_yes;
                
                double distance = 0;
                if (isDelivery) {
                    try {
                        distance = Double.parseDouble(deliveryDistanceText.getText().toString());
                    } catch (NumberFormatException e) {
                        distance = 0;
                    }
                }
                
                // Add to cart
                CartManager.getInstance().addToCart(customizedDonut, 1, isDelivery, distance);
                Toast.makeText(context, "Added to cart: " + customizedDonut.getName(), Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
