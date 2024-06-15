package com.example.foodstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.foodstore.models.ImageModel;
import com.example.foodstore.utils.ImageUtils;
import com.example.foodstore.utils.JsonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUsFragment extends Fragment {

    EditText contactName, contactEmail, contactPhone, contactMessage;
    Button contactSubmit;
    FirebaseFirestore db;
    FirebaseAuth auth;

    ImageView imageView, imageView2, imageView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        imageView = view.findViewById(R.id.imageView);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);

        contactName = view.findViewById(R.id.contact_name);
        contactEmail = view.findViewById(R.id.contact_email);
        contactPhone = view.findViewById(R.id.contact_phone);
        contactMessage = view.findViewById(R.id.contact_message);
        contactSubmit = view.findViewById(R.id.contact_submit);

        loadImages();

        contactSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = contactName.getText().toString().trim();
                String email = contactEmail.getText().toString().trim();
                String phone = contactPhone.getText().toString().trim();
                String message = contactMessage.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the form submission
                    Toast.makeText(getActivity(), "Thank you for contacting us!", Toast.LENGTH_SHORT).show();

                    // Add to Firestore
                    Map<String, Object> contactInfo = new HashMap<>();
                    contactInfo.put("name", name);
                    contactInfo.put("email", email);
                    contactInfo.put("phone", phone);
                    contactInfo.put("message", message);

                    db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("ContactUs").add(contactInfo)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Message sent successfully.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Error sending message: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    // Optionally, clear the form fields
                    contactName.setText("");
                    contactEmail.setText("");
                    contactPhone.setText("");
                    contactMessage.setText("");
                }
            }
        });

        return view;
    }

    private void loadImages() {
        String json = JsonUtils.loadJSONFromAsset(getContext(), "images.json");
        List<ImageModel> images = ImageUtils.getImagesFromJson(json);

        if (images != null && images.size() >= 3) {
            Glide.with(getContext()).load(images.get(0).getUrl()).into(imageView);
            Glide.with(getContext()).load(images.get(1).getUrl()).into(imageView2);
            Glide.with(getContext()).load(images.get(2).getUrl()).into(imageView3);
        } else {
            Toast.makeText(getContext(), "Failed to load images", Toast.LENGTH_SHORT).show();
        }
    }

}
