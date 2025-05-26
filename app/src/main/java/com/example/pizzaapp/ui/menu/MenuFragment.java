package com.example.pizzaapp.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pizzaapp.R;
import com.example.pizzaapp.adapters.PizzaAdapter;
import com.example.pizzaapp.databinding.FragmentMenuBinding;
import com.example.pizzaapp.models.Pizza;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private List<Pizza> pizzaList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize pizza list
        initializePizzaList();

        // Set up the ListView with the pizza adapter
        PizzaAdapter adapter = new PizzaAdapter(getContext(), pizzaList);
        ListView listView = root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);

        return root;
    }

    private void initializePizzaList() {
        pizzaList = new ArrayList<>();
        pizzaList.add(new Pizza(
                "Margherita",
                "Classic pizza with tomato sauce, mozzarella, and fresh basil",
                8.99,
                R.drawable.ic_pizza
        ));
        pizzaList.add(new Pizza(
                "Pepperoni",
                "Classic pepperoni pizza with tomato sauce and mozzarella",
                10.99,
                R.drawable.ic_pizza
        ));
        pizzaList.add(new Pizza(
                "Vegetarian",
                "Loaded with fresh vegetables, tomato sauce, and mozzarella",
                9.99,
                R.drawable.ic_pizza
        ));
        pizzaList.add(new Pizza(
                "Hawaiian",
                "Ham, pineapple, tomato sauce, and mozzarella",
                11.99,
                R.drawable.ic_pizza
        ));
        pizzaList.add(new Pizza(
                "Meat Lovers",
                "Pepperoni, sausage, ham, bacon, tomato sauce, and mozzarella",
                12.99,
                R.drawable.ic_pizza
        ));
        pizzaList.add(new Pizza(
                "BBQ Chicken",
                "Grilled chicken, red onions, BBQ sauce, and mozzarella",
                11.99,
                R.drawable.ic_pizza
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
