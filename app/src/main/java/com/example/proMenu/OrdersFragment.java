package com.example.proMenu;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ArrayList<OrderStructure> ordersList = new ArrayList<OrderStructure>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference orders = db.collection("orders");
        orders.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult()){
                    String customerId = (String) document.get("customerId");
                    if(customerId.equals(user.getUid())){
                        OrderStructure order = new OrderStructure();
                        order.customerId = customerId;
                        order.completed = (Boolean) document.get("completed");
                        order.totalPrice = (Long) document.get("totalPrice");
                        order.items = (ArrayList<String>) document.get("items");
                        ordersList.add(order);
                    };
                }
                LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
                for(OrderStructure ordersItem : ordersList){
                    View orderView = inflater.inflate(R.layout.order_layout, container, false);
                    TextView itemText = orderView.findViewById(R.id.itemsText);
                    TextView totalPrice = orderView.findViewById(R.id.totalPrice);
                    TextView status = orderView.findViewById(R.id.statusText);
                    totalPrice.setText("₱" + Long.toString(ordersItem.totalPrice));
                    itemText.setText(ordersItem.items.toString());
                    if ((boolean) ordersItem.completed) {
                        status.setText("STATUS: COMPLETED");
                    } else {
                        status.setText("STATUS: PREPARING");
                    }
                    linearLayout.addView(orderView);
                }
            }
        });

        return view;
    }
}