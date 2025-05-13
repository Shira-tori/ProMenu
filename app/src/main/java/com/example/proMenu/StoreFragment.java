package com.example.proMenu;

import static android.content.ContentValues.TAG;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        String[] menu;
        try{
            menu = getArguments().getStringArray("menu");
        } catch (Exception e){
            menu = null;
        }
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        if(menu != null){
            for(String menuItem : menu){
                View storeMenuLayout = inflater.inflate(R.layout.store_menu_layout, container, false);
                TextView titleTextView = storeMenuLayout.findViewById(R.id.titleTextView);
                CardView cardView = storeMenuLayout.findViewById(R.id.cardView);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("stores").document(getArguments().getString("documentId"))
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Map<String, Object> items = task.getResult().getData();
                                ArrayList<String> itemList = (ArrayList<String>) items.get(menuItem);
                                ItemsFragment itemsFragment = new ItemsFragment();
                                Bundle dataBundle = new Bundle();
                                if(itemList != null){
                                    String[] itemListString = new String[itemList.size()];
                                    itemList.toArray(itemListString);
                                    dataBundle.putStringArray("items", itemListString);
                                } else {

                                }
                                FragmentManager fragmentManager = getParentFragmentManager();
                                itemsFragment.setArguments(dataBundle);
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, itemsFragment, null).addToBackStack(null).commit();
                            }
                        });
                    }
                });
                titleTextView.setText(menuItem);
                linearLayout.addView(storeMenuLayout);
            }
        } else {
            TextView noMenu = new TextView(view.getContext());
            noMenu.setText("NO MENU AVAILABLE");
            noMenu.setGravity(Gravity.CENTER);
            noMenu.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            noMenu.setLayoutParams(layoutParams);
            linearLayout.addView(noMenu);
        }
        return view;
    }
}