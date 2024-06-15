package com.example.foodstore.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodstore.MainActivity;
import com.example.foodstore.R;
import com.example.foodstore.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<CartModel> cartModelList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_order);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        List<CartModel> list = (ArrayList<CartModel>) getIntent().getSerializableExtra("itemList");

        if (list != null && list.size() > 0){
            for (CartModel model : list){
                final HashMap<String, Object> orderMap = new HashMap<>();

                orderMap.put("productName", model.getProductName());
                orderMap.put("productPrice", model.getProductPrice());
                orderMap.put("currentDate", model.getCurrentDate());
                orderMap.put("currentTime", model.getCurrentTime());
                orderMap.put("totalQuantity", model.getTotalQuantity());
                orderMap.put("totalPrice", model.getTotalPrice());


                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("MyOrders").add(orderMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PlaceOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PlaceOrderActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
