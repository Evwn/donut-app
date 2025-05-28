package com.example.donutapp.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.donutapp.MainActivity;
import com.example.donutapp.R;
import com.example.donutapp.adapters.CartAdapter;
import com.example.donutapp.databinding.FragmentCartBinding;
import com.example.donutapp.models.CartItem;
import com.example.donutapp.models.Donut;
import com.example.donutapp.models.Purchase;
import com.example.donutapp.utils.CartManager;
import com.example.donutapp.utils.PurchaseManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartUpdateListener {

    private FragmentCartBinding binding;
    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private TextView subtotalView;
    private TextView deliveryFeeView;
    private View deliveryFeeContainer;
    private TextView totalView;
    private View emptyStateView;
    private View cartContent;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        // Get NavController
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

        // Initialize views
        subtotalView = root.findViewById(R.id.cart_subtotal);
        deliveryFeeView = root.findViewById(R.id.delivery_fee);
        deliveryFeeContainer = root.findViewById(R.id.delivery_fee_container);
        totalView = root.findViewById(R.id.cart_total);
        MaterialButton checkoutButton = root.findViewById(R.id.button_checkout);
        ListView listView = root.findViewById(R.id.cart_list);
        
        // Set up empty state view
        emptyStateView = root.findViewById(R.id.empty_state);
        cartContent = root.findViewById(R.id.cart_content);
        
        // Set up the ListView with the cart adapter
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(getContext(), cartItems, this);
        listView.setAdapter(cartAdapter);
        
        // Set up checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Snackbar.make(requireView(), R.string.empty_cart, Snackbar.LENGTH_SHORT).show();
                return;
            }
            
            // Show a confirmation message
            Snackbar.make(requireView(), R.string.order_placed, Snackbar.LENGTH_LONG)
                .setAction(R.string.view_menu, v1 -> navigateToMenu())
                .show();
            
            // Move items to purchased list
            for (CartItem item : cartItems) {
                Purchase purchase = new Purchase(item);
                PurchaseManager.getInstance().addPurchase(purchase);
            }
            
            // Clear the cart
            CartManager.getInstance().clearCart();
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            updateCartState();
            
            // Show the FAB to view purchases
            if (getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showPurchasesFab();
            }
        });
        
        // Set up browse menu button in empty state
        MaterialButton browseMenuButton = emptyStateView.findViewById(R.id.button_browse_menu);
        browseMenuButton.setOnClickListener(v -> navigateToMenu());
        
        // Update the initial cart state
        updateCartState();
        updateTotal();

        return root;
    }
    
    private void navigateToMenu() {
        // Navigate to the menu fragment
        navController.navigate(R.id.nav_menu);
    }
    
    // Removed getSampleCartItems as we're now using CartManager
    
    private void updateTotal() {
        CartManager cartManager = CartManager.getInstance();
        
        // Update subtotal
        double subtotal = cartManager.getSubtotal();
        subtotalView.setText(String.format("$%.2f", subtotal));
        
        // Update delivery fee
        double deliveryFee = cartManager.getDeliveryFee();
        if (deliveryFee > 0) {
            deliveryFeeContainer.setVisibility(View.VISIBLE);
            deliveryFeeView.setText(String.format("$%.2f", deliveryFee));
        } else {
            deliveryFeeContainer.setVisibility(View.GONE);
        }
        
        // Update total
        double total = cartManager.getTotal();
        totalView.setText(String.format("$%.2f", total));
    }
    
    private void updateCartState() {
        if (cartItems.isEmpty()) {
            cartContent.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            cartContent.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onCartUpdated() {
        updateTotal();
        updateCartState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
