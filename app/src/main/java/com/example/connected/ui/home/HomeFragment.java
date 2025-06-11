package com.example.connected.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connected.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Setup click listeners for quick action cards
        setupQuickActionListeners();

        // Setup click listeners for category cards
        setupCategoryListeners();

        // Setup RecyclerView for featured courses
        setupFeaturedCoursesRecyclerView();

        return root;
    }

    private void setupQuickActionListeners() {
        // Continue Learning Card
        binding.continueLearningCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Continue Learning clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to current course or last accessed lesson
        });

        // Browse Courses Card
        binding.browseCoursesCard.setOnClickListener(v -> {
            // Navigate to courses screen
            Toast.makeText(getContext(), "Browse Courses clicked", Toast.LENGTH_SHORT).show();
            // TODO: Add navigation to courses fragment
        });
    }

    private void setupCategoryListeners() {
        // Sensors Category
        binding.sensorsCategory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sensors category selected", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to sensors courses
        });

        // Connectivity Category
        binding.connectivityCategory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Connectivity category selected", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to connectivity courses
        });

        // Programming Category
        binding.programmingCategory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Programming category selected", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to programming courses
        });

        // Projects Category
        binding.projectsCategory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Projects category selected", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to projects courses
        });
    }

    private void setupFeaturedCoursesRecyclerView() {
        RecyclerView featuredCoursesRecycler = binding.featuredCoursesRecycler;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        featuredCoursesRecycler.setLayoutManager(layoutManager);

        // TODO: Create and set adapter for featured courses
        // FeaturedCoursesAdapter adapter = new FeaturedCoursesAdapter(coursesList);
        // featuredCoursesRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}