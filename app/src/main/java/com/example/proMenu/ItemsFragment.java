package com.example.proMenu;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ItemsFragment() {
    }
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        String[] items = getArguments().getStringArray("items");
        FragmentManager fragmentManager = getParentFragmentManager();
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        ConstraintLayout constraintLayout = new ConstraintLayout(view.getContext());
        constraintLayout.setId(View.generateViewId());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, -1);
        constraintLayout.setLayoutParams(layoutParams);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                fragmentManager.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);
        if(items != null){
            for(String menuItems : items){
                View itemCards = inflater.inflate(R.layout.menu_item_layout, container, false);
                TextView itemName = itemCards.findViewById(R.id.itemNameTitle);
                itemName.setText(menuItems.split("-")[0]);
                TextView price = itemCards.findViewById(R.id.priceTitle);
                price.setText("₱"+ menuItems.split("-")[1]);
                Button addToCartButton = itemCards.findViewById(R.id.addToCart);
                addToCartButton.setText(R.string.add_to_cart);
                constraintLayout.addView(itemCards);
                CardView cards = itemCards.findViewById(R.id.cardView);
                cards.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                if(constraintLayout.getChildCount() == 2){
                    float scale = getContext().getResources().getDisplayMetrics().density;
                    ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayout.getChildAt(0).getLayoutParams();
                    ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) constraintLayout.getChildAt(1).getLayoutParams();
                    params1.endToStart = constraintLayout.getChildAt(1).getId();
                    params1.startToStart = constraintLayout.getId();
                    params1.topToTop = constraintLayout.getId();
                    params1.bottomToBottom = constraintLayout.getId();
                    params1.width = Constraints.LayoutParams.MATCH_CONSTRAINT;
                    params2.startToEnd = constraintLayout.getChildAt(0).getId();
                    params2.topToTop = constraintLayout.getId();
                    params2.bottomToBottom = constraintLayout.getId();
                    params2.endToEnd = constraintLayout.getId();
                    params2.width = Constraints.LayoutParams.MATCH_CONSTRAINT;
                    layoutParams.bottomMargin = (int) (20 * scale + 0.5f);
                    gridLayout.addView(constraintLayout);
                    constraintLayout = new ConstraintLayout(view.getContext());
                    constraintLayout.setId(View.generateViewId());
                    ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(-1, -1);
                    constraintLayout.setLayoutParams(layoutParams1);
                }
            }
            if(constraintLayout.getChildCount() == 1){
                float scale = getContext().getResources().getDisplayMetrics().density;
                layoutParams.topMargin = (int) (20 * scale + 0.5f);
                constraintLayout.setLayoutParams(layoutParams);
                gridLayout.addView(constraintLayout);
            }
        } else {
            TextView noItems = new TextView(view.getContext());
            noItems.setText(R.string.no_items);
            noItems.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            gridLayout.addView(noItems);
        }
        return view;
    }
}