package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.demo2021110.databinding.ActivityAnackpageBinding;
import com.example.demo2021110.databinding.ActivityAnapageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class anackpage extends AppCompatActivity {

    private ActivityAnackpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user,password,useremail;
    String ckanaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnackpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("消費分析");
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");
        ckanaid=it.getStringExtra("ckanaid");

        binding.btnanackback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out = new Intent(anackpage.this,anapage.class);
                out.putExtra("user",user);
                out.putExtra("password",password);
                out.putExtra("useremail",useremail);
                startActivity(out);
            }
        });

        db.collection("book")
                .document(ckanaid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            binding.txtanackdate.setText(documentSnapshot.getString("date").toString());
                            binding.txtanackname.setText(documentSnapshot.getString("name").toString());
                            binding.txtanackshop.setText(documentSnapshot.getString("shop").toString());
                            binding.txtanacksum.setText(documentSnapshot.getString("sum").toString());
                        }
                    }
                });
    }
}