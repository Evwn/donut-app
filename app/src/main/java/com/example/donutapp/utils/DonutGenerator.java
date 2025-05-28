package com.example.donutapp.utils;

import com.example.donutapp.R;
import com.example.donutapp.models.Donut;

import java.util.ArrayList;
import java.util.List;

public class DonutGenerator {
    public static List<Donut> getSampleDonuts() {
        List<Donut> donuts = new ArrayList<>();
        
        // Classic Donuts
        donuts.add(createDonut("Glazed", "Classic ring donut with sweet glaze", R.drawable.donut_placeholder));
        donuts.add(createDonut("Chocolate Frosted", "Fluffy donut with rich chocolate frosting", R.drawable.donut_placeholder));
        donuts.add(createDonut("Strawberry Frosted", "Classic donut with sweet strawberry frosting", R.drawable.donut_placeholder));
        
        // Filled Donuts
        donuts.add(createDonut("Boston Cream", "Cream-filled donut with chocolate glaze", R.drawable.donut_placeholder));
        donuts.add(createDonut("Jelly Filled", "Soft donut filled with fruit jelly", R.drawable.donut_placeholder));
        donuts.add(createDonut("Custard Filled", "Creamy vanilla custard filled donut", R.drawable.donut_placeholder));
        
        // Specialty Donuts
        donuts.add(createDonut("Maple Bacon", "Maple glazed donut with crispy bacon", R.drawable.donut_placeholder));
        donuts.add(createDonut("Matcha Glazed", "Green tea flavored donut with matcha glaze", R.drawable.donut_placeholder));
        donuts.add(createDonut("Red Velvet", "Rich red velvet donut with cream cheese glaze", R.drawable.donut_placeholder));
        
        return donuts;
    }
    
    private static Donut createDonut(String name, String description, int imageResId) {
        return new Donut(name, description, imageResId);
    }
}
