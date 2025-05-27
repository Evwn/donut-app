package com.example.pizzaapp;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.pizzaapp.models.Donut;
import com.example.pizzaapp.models.Purchase;
import com.example.pizzaapp.utils.PurchaseManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.appBarMain.toolbar);
        
        // Enable ActionBar app icon to behave as action to toggle nav drawer
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set up FAB
        binding.appBarMain.fabPurchases.setImageResource(R.drawable.ic_shopping_bag);
        binding.appBarMain.fabPurchases.setOnClickListener(view -> showPurchasedItemsDialog());
        
        // Set up navigation
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        
        // Set up navigation header
        View headerView = navigationView.getHeaderView(0);
        
        // Set up navigation menu items
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        
        // Set up navigation controller
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, 
                R.id.nav_menu, 
                R.id.nav_cart)
                .setOpenableLayout(drawer)
                .build();
                
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        
        // Set up action bar with nav controller
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        
        // Set up navigation view with nav controller
        NavigationUI.setupWithNavController(navigationView, navController);
        
        // Set up navigation item selection listener
        navigationView.setNavigationItemSelectedListener(item -> 
            NavigationUI.onNavDestinationSelected(item, navController) || 
            super.onOptionsItemSelected(item)
        );
        
        // Set status bar color to transparent
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        
        // Set navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
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
                    .setMessage("You haven't purchased any donuts yet.")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(R.drawable.ic_shopping_bag)
                    .show();
            return;
        }
        
        // Create a formatted string with all purchased items
        StringBuilder purchasedItems = new StringBuilder();
        double subtotal = 0;
        double deliveryTotal = 0;
        
        // Add header
        purchasedItems.append("Your Donut Orders\n\n");
        
        for (int i = 0; i < purchases.size(); i++) {
            Purchase purchase = purchases.get(i);
            Donut donut = purchase.getDonut();
            
            // Add order number
            purchasedItems.append("Order ").append(i + 1).append(":\n");
            
            // Add donut details
            purchasedItems.append(String.format("%s (%s) x%d\n",
                    donut.getName(),
                    donut.getSize().toString(),
                    purchase.getQuantity()));
            
            // Add customizations if any
            String customizations = donut.getCustomizationSummary();
            if (!customizations.isEmpty()) {
                purchasedItems.append(customizations).append("\n");
            }
            
            // Calculate prices
            double itemSubtotal = purchase.getDonutSubtotal();
            double itemDelivery = purchase.isDelivery() ? purchase.getDeliveryFee() : 0;
            
            purchasedItems.append(String.format("Subtotal: $%.2f\n", itemSubtotal));
            if (purchase.isDelivery()) {
                purchasedItems.append(String.format("Delivery: $%.2f\n", itemDelivery));
            }
            purchasedItems.append(String.format("Total: $%.2f\n\n", itemSubtotal + itemDelivery));
            
            subtotal += itemSubtotal;
            deliveryTotal += itemDelivery;
        }
        
        // Add summary
        double total = subtotal + deliveryTotal;
        purchasedItems.append("\n");
        purchasedItems.append(String.format("Subtotal: $%.2f\n", subtotal));
        if (deliveryTotal > 0) {
            purchasedItems.append(String.format("Delivery Total: $%.2f\n", deliveryTotal));
        }
        purchasedItems.append(String.format("Grand Total: $%.2f", total));
        
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