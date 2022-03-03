package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("登入畫面");
        firebaseAuth = FirebaseAuth.getInstance();

        binding.log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = binding.account.getText().toString();
                String password = binding.password.getText().toString();

                if (acc.isEmpty()||password.isEmpty()){
                    Toast.makeText(MainActivity.this,"帳號密碼不可空白",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.signInWithEmailAndPassword(acc,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent it = new Intent(MainActivity.this,Mainpage.class);
                                        it.putExtra("user",acc);
                                        it.putExtra("password",password);
                                        it.putExtra("useremail",acc);
                                        startActivity(it);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "帳號密碼錯誤", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        binding.ns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this,nspage.class);
                startActivity(it);
            }
        });
    }
}