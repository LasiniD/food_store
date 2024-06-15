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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {

    TextView totalAmount;
    Button placeOrder;
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

        Intent intent = getIntent();
        cartModelList = (List<CartModel>) intent.getSerializableExtra("itemList");

        double totalBill = 0.0;
        for (CartModel cartModel : cartModelList) {
            totalBill += cartModel.getTotalPrice();
        }

        totalAmount.setText("Total Amount: $" + totalBill);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void placeOrder() {
        progressBar.setVisibility(View.VISIBLE);

        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("orderDate", saveCurrentDate);
        orderMap.put("orderTime", saveCurrentTime);
        orderMap.put("totalPrice", totalAmount.getText().toString());
        orderMap.put("items", cartModelList);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrders").add(orderMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                    .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                                        .collection("AddToCart").document(doc.getId()).delete();
                                            }
                                            Toast.makeText(PlaceOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(PlaceOrderActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                        } else {
                            Toast.makeText(PlaceOrderActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
