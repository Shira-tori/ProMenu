package com.example.proMenu;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<String> stores = new ArrayList<>();
    HashMap<String, ArrayList<String>> items = new HashMap<>();
    int totalPrice = 0;

    public CartFragment() {
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ExpandableListView expandableListView = view.findViewById(R.id.expandableListView);
        TextView totalPriceText = view.findViewById(R.id.totalPriceCart);
        db.collection("accounts").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Map<String, Object> cart = (HashMap<String, Object>) document.get("cart");
                if(cart == null){

                } else{
                    for(Map.Entry<String, Object> entry : cart.entrySet()){
                        String storeId = entry.getKey();
                        String storeName;
                        Map<String, Object> storeMap = (HashMap<String, Object>) entry.getValue();
                        stores.add(storeId);
                        ArrayList<String> itemsList = (ArrayList<String>) storeMap.get("items");
                        ArrayList<Long> priceList = (ArrayList<Long>) storeMap.get("prices");
                        for(long price : priceList){
                            totalPrice += price;
                        }
                        items.put(storeId, itemsList);
                    }
                    CartExpandableListViewAdapter adapter = new CartExpandableListViewAdapter(stores, items);
                    expandableListView.setAdapter(adapter);
                }

                totalPriceText.setText("₱" + Integer.toString(totalPrice));
            }
        });
        return view;
    }
}