package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityMainBinding;
import com.example.demo2021110.databinding.ActivityNspageBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class nspage extends AppCompatActivity {

    private ActivityNspageBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNspageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("新增使用者");
        firebaseAuth = FirebaseAuth.getInstance();

        binding.Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(nspage.this,MainActivity.class);
                startActivity(it);
            }
        });

        binding.InNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.TextID.getText().toString();
                String email = binding.TextEmail.getText().toString();
                String password = binding.TextPassword.getText().toString();

                if (password.length()>5){
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();
                                    firebaseUser.updateProfile(userProfileChangeRequest)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(nspage.this,"註冊成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(nspage.this,"註冊失敗",Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(nspage.this,"密碼太短",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}