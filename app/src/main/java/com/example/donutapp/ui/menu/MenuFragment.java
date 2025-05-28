package com.example.donutapp.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.donutapp.R;
import com.example.donutapp.adapters.DonutAdapter;
import com.example.donutapp.databinding.FragmentMenuBinding;
import com.example.donutapp.models.Donut;
import com.example.donutapp.utils.DonutGenerator;

import java.util.List;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private List<Donut> donutList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize donut list
        donutList = DonutGenerator.getSampleDonuts();

        // Set up the ListView with the donut adapter
        DonutAdapter adapter = new DonutAdapter(getContext(), donutList);
        ListView listView = root.findViewById(R.id.menu_list);
        listView.setAdapter(adapter);

        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
