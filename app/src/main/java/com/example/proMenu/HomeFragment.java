package com.example.proMenu;

import static android.content.ContentValues.TAG;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private LinearLayout linearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        GridLayout gridLayout = view.findViewById(R.id.homeGridLayout);
        final ConstraintLayout[] constraintLayout = {new ConstraintLayout(view.getContext())};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference stores = db.collection("stores");
        stores.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document : task.getResult()){
                    Map<String, Object> data = document.getData();
                    View inflateView = inflater.inflate(R.layout.recycler_view_item_layout, container, false);
                    TextView storeNameText = inflateView.findViewById(R.id.storeNameText);
                    storeNameText.setText((String) data.get("name"));
                    inflateView.setId(View.generateViewId());
                    ImageView imageView = inflateView.findViewById(R.id.imageView);
                    constraintLayout[0].addView(inflateView);
                    if(constraintLayout[0].getChildCount() == 2) {
                        constraintLayout[0].setId(View.generateViewId());
                        ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                        ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) constraintLayout[0].getChildAt(0).getLayoutParams();
                        ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) constraintLayout[0].getChildAt(1).getLayoutParams();
                        params1.endToStart = constraintLayout[0].getChildAt(1).getId();
                        params1.startToStart = constraintLayout[0].getId();
                        params1.topToTop = constraintLayout[0].getId();
                        params1.bottomToBottom = constraintLayout[0].getId();
                        params1.width = Constraints.LayoutParams.MATCH_CONSTRAINT;
                        params2.startToEnd = constraintLayout[0].getChildAt(0).getId();
                        params2.topToTop = constraintLayout[0].getId();
                        params2.bottomToBottom = constraintLayout[0].getId();
                        params2.endToEnd = constraintLayout[0].getId();
                        params2.width = Constraints.LayoutParams.MATCH_CONSTRAINT;
                        constraintLayout[0].getChildAt(0).setLayoutParams(params1);
                        constraintLayout[0].getChildAt(1).setLayoutParams(params2);
                        constraintLayout[0].setLayoutParams(layoutParams);
                        gridLayout.addView(constraintLayout[0]);
                        constraintLayout[0] = new ConstraintLayout(view.getContext());
                    };
                }
            }
        });
        return view;
    }
}