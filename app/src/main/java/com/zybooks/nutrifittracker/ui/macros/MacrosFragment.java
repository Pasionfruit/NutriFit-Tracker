package com.zybooks.nutrifittracker.ui.macros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.zybooks.nutrifittracker.databinding.FragmentMacrosBinding;

public class MacrosFragment extends Fragment {

    private FragmentMacrosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MacrosViewModel homeViewModel =
                new ViewModelProvider(this).get(MacrosViewModel.class);

        binding = FragmentMacrosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMacros;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
