package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityMainBinding;
import com.example.demo2021110.databinding.ActivityMainpageBinding;
import com.example.demo2021110.databinding.ActivityNspageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Mainpage extends AppCompatActivity {

    private ActivityMainpageBinding binding;
    private FirebaseAuth firebaseAuth;

    String user,useremail;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("選單");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Intent it = getIntent();
        user = it.getStringExtra("user");
        password = it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");

        firebaseAuth.signInWithEmailAndPassword(useremail,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (firebaseUser.getDisplayName().isEmpty())
                            binding.Name.setText(firebaseUser.getEmail());
                        else
                            binding.Name.setText(firebaseUser.getDisplayName());
                    }
                });

        binding.out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();;
                Intent out = new Intent(Mainpage.this,MainActivity.class);
                startActivity(out);
            }
        });

        binding.btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acc = new Intent(Mainpage.this,accpage.class);
                acc.putExtra("user",binding.Name.getText().toString());
                acc.putExtra("password",password);
                acc.putExtra("useremail",useremail);
                startActivity(acc);
            }
        });

        binding.btnbok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bok = new Intent(Mainpage.this,bokpage.class);
                bok.putExtra("user",binding.Name.getText().toString());
                bok.putExtra("password",password);
                bok.putExtra("useremail",useremail);
                startActivity(bok);
            }
        });

        binding.btnana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ana = new Intent(Mainpage.this,anapage.class);
                ana.putExtra("user",binding.Name.getText().toString());
                ana.putExtra("password",password);
                ana.putExtra("useremail",useremail);
                startActivity(ana);
            }
        });

        binding.btnawd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent awd = new Intent(Mainpage.this,awdpage.class);
                awd.putExtra("user",binding.Name.getText().toString());
                awd.putExtra("password",password);
                awd.putExtra("useremail",useremail);
                startActivity(awd);
            }
        });
    }
}