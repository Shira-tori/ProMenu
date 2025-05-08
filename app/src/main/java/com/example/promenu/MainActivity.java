package com.example.promenu;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.promenu.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private LinearLayout linearLayout;
    private ActivityMainBinding binding;
    private LinkedList<String> store_names = new LinkedList<String>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        db.collection("stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> result = document.getData();
                                store_names.add((String) result.get("name"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.home){
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment.class, null).commit();
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(new RecyclerViewAdapter());
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            } else if (itemId == R.id.orders) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, OrdersFragment.class, null).commit();
            } else if (itemId == R.id.account) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AccountFragment.class, null).commit();
            }
            return true;
        });
    }
}