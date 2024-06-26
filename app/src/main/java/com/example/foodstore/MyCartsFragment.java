package com.example.foodstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodstore.activities.PlaceOrderActivity;
import com.example.foodstore.adapters.CartAdapter;
import com.example.foodstore.models.CartModel;
import com.example.foodstore.utils.NetworkUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;

    TextView overTotal;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<CartModel> cartModelList;
    ConstraintLayout constraint1, constraint2;

    Button buyNow;
    int totalBill;
    ProgressBar progressBar;

    public MyCartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        if (!NetworkUtil.isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Internet Connection Stable", Toast.LENGTH_SHORT).show();
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBar = root.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        buyNow = root.findViewById(R.id.buy_now);

        overTotal = root.findViewById(R.id.overTotal);
        constraint1 = root.findViewById(R.id.constraint1);
        constraint2 = root.findViewById(R.id.constraint2);

        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(),cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                String documentId = documentSnapshot.getId();

                                CartModel cartModel = documentSnapshot.toObject(CartModel.class);

                                cartModel.setDocumentId(documentId);

                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            if (cartModelList.isEmpty()) {
                                constraint1.setVisibility(View.VISIBLE);
                                constraint2.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                constraint1.setVisibility(View.GONE);
                                constraint2.setVisibility(View.VISIBLE);
                            }

                            calculateTotalAmount(cartModelList);
                        }
                    }
                });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlaceOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("CartEmpty"));

        return root;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            constraint1.setVisibility(View.VISIBLE);
            constraint2.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    };

    private void calculateTotalAmount(List<CartModel> cartModelList) {
        double totalAmount = 0.0;
        for (CartModel cartModel : cartModelList){
            totalAmount += cartModel.getTotalPrice();
        }

        overTotal.setText("Total Amount : "+String.valueOf(totalAmount));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

}