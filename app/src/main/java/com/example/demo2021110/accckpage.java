package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityAccckpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class accckpage extends AppCompatActivity {

    private ActivityAccckpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user,password,useremail,accid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccckpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("發票");
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");
        accid = it.getStringExtra("accid");


        binding.btnaccckback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(accckpage.this,accpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        db.collection("acc2")
                .document(accid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        binding.txtaccckcode.setText(documentSnapshot.getString("code").toString());
                        binding.txtaccckbuy.setText(documentSnapshot.getString("buy").toString());
                        binding.txtaccckdate.setText(documentSnapshot.getString("date").toString());
                        binding.txtacccksum.setText(documentSnapshot.getString("sum").toString());
                        binding.txtaccckshop.setText(documentSnapshot.getString("shop").toString());
                    }
                });
    }
}