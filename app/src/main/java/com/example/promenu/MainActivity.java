package com.example.promenu;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.promenu.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private LinearLayout linearLayout;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.home){
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment.class, null).commit();
            } else if (itemId == R.id.orders) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, OrdersFragment.class, null).commit();
            } else if (itemId == R.id.account) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AccountFragment.class, null).commit();
            }
            return true;
        });


    }
}