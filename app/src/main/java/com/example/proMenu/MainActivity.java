package com.example.proMenu;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proMenu.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private String[] stringArray;
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
        stringArray = new String[store_names.size()];
        store_names.toArray(stringArray);
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment.class, null).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.home){
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, homeFragment, null).commit();
            } else if (itemId == R.id.orders) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, OrdersFragment.class, null).commit();
            } else if (itemId == R.id.account) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AccountFragment.class, null).commit();
            }
            return true;
        });
    }
}