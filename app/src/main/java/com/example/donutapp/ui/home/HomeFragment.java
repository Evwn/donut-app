package com.example.donutapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.donutapp.R;
import com.example.donutapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up the welcome message
        TextView welcomeText = root.findViewById(R.id.text_home);
        welcomeText.setText("Welcome to Donut Delight!");
        welcomeText.setTextSize(24);
        welcomeText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Set up the donut image
        ImageView donutImage = root.findViewById(R.id.donut_image);
        donutImage.setImageResource(R.drawable.ic_donut);
        donutImage.setContentDescription("Delicious Donuts");

        // Set up the order now button
        Button orderNowButton = root.findViewById(R.id.order_now_button);
        orderNowButton.setText("Order Donuts Now!");
        orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the menu
                Navigation.findNavController(v).navigate(R.id.nav_menu);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}