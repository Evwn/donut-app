package com.example.pizzaapp;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.pizzaapp.models.Purchase;
import com.example.pizzaapp.utils.PurchaseManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fabPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPurchasedItemsDialog();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_menu, R.id.nav_cart)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void showPurchasedItemsDialog() {
        List<Purchase> purchases = PurchaseManager.getInstance().getPurchases();
        
        if (purchases.isEmpty()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(R.string.view_purchases)
                    .setMessage(R.string.purchased_items_placeholder)
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(R.drawable.ic_shopping_bag)
                    .show();
            return;
        }
        
        // Create a formatted string with all purchased items
        StringBuilder purchasedItems = new StringBuilder();
        double total = 0;
        
        for (Purchase purchase : purchases) {
            String itemLine = String.format("%s x%d - $%.2f",
                    purchase.getPizza().getName(),
                    purchase.getQuantity(),
                    purchase.getTotalPrice());
                    
            if (purchase.isDelivery()) {
                itemLine += String.format(" (Delivery: $%.2f)", purchase.getDeliveryFee());
            }
            
            purchasedItems.append(itemLine).append("\n");
            total += purchase.getTotalPrice();
        }
        
        // Add total
        purchasedItems.append("\n").append(getString(R.string.total)).append(" ").append(String.format("$%.2f", total));
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.view_purchases)
                .setMessage(purchasedItems.toString())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(R.drawable.ic_shopping_bag)
                .show();
    }

    public void showPurchasesFab() {
        if (binding != null && binding.appBarMain != null && binding.appBarMain.fabPurchases != null) {
            binding.appBarMain.fabPurchases.show();
        }
    }

    private void hidePurchasesFab() {
        if (binding != null && binding.appBarMain != null && binding.appBarMain.fabPurchases != null) {
            binding.appBarMain.fabPurchases.hide();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}