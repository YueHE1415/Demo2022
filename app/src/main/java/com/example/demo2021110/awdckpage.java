package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityAwdckpageBinding;
import com.example.demo2021110.databinding.ActivityAwdpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class awdckpage extends AppCompatActivity {

    private ActivityAwdckpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user,password,useremail,awdid,awd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAwdckpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("中獎發票");
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");
        awdid = it.getStringExtra("awdid");
        awd =it.getStringExtra("awdsum");

        binding.btnawdckback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out = new Intent(awdckpage.this,awdpage.class);
                out.putExtra("user",user);
                out.putExtra("password",password);
                out.putExtra("useremail",useremail);
                startActivity(out);
            }
        });

        db.collection("acc2")
                .document(awdid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        binding.txtawdckawdsum.setText(awd);
                        binding.txtawdckcode.setText(documentSnapshot.getString("code"));
                        binding.txtawdckbuy.setText(documentSnapshot.getString("buy"));
                        binding.txtawdckshop.setText(documentSnapshot.getString("shop"));
                        binding.txtawdckdate.setText(documentSnapshot.getString("date"));
                        binding.txtawdcksum.setText(documentSnapshot.getString("sum"));
                    }
                });
    }
}