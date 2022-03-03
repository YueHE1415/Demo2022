package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityBokpageBinding;
import com.example.demo2021110.databinding.ActivityMainpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class bokpage extends AppCompatActivity {

    private ActivityBokpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    String user,password,useremail;
    Integer foodsum,passsum,etitsum,lifesum,othersum,allsum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBokpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("記帳");
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");

        binding.spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodsum = 0;
                etitsum = 0;
                lifesum = 0;
                passsum = 0;
                othersum=0;
                allsum=0;

                db.collection("book")
                        .whereEqualTo("account",user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                           allsum+=Integer.valueOf(documentSnapshot.getString("sum"));
                                        }
                                    }
                                    binding.txtbokallsum.setText("總金額"+allsum.toString()+"元");
                                }
                            }
                        });

                db.collection("book")
                        .whereEqualTo("account",user)
                        .whereEqualTo("kind","食品")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                            foodsum += Integer.valueOf(documentSnapshot.getString("sum"));
                                        }
                                    }
                                    binding.txtfoodsum.setText(foodsum.toString());

                                }else {
                                    Toast.makeText(bokpage.this, "er", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                db.collection("book")
                        .whereEqualTo("account",user)
                        .whereEqualTo("kind","娛樂")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                            etitsum += Integer.valueOf(documentSnapshot.getString("sum"));
                                        }
                                    }
                                    binding.txtetitsum.setText(  etitsum.toString());
                                }else {
                                    Toast.makeText(bokpage.this, "er", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                db.collection("book")
                        .whereEqualTo("account",user)
                        .whereEqualTo("kind","生活用品")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                            lifesum += Integer.valueOf(documentSnapshot.getString("sum"));
                                        }
                                    }
                                    binding.txtlifesum.setText(lifesum.toString());
                                }else {
                                    Toast.makeText(bokpage.this, "er", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                db.collection("book")
                        .whereEqualTo("account",user)
                        .whereEqualTo("kind","通行")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                            passsum += Integer.valueOf(documentSnapshot.getString("sum"));
                                        }
                                    }
                                    binding.txtpasssum.setText(passsum.toString());
                                }else {
                                    Toast.makeText(bokpage.this, "er", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                db.collection("book")
                        .whereEqualTo("account",user)
                        .whereEqualTo("kind","其他")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                            othersum += Integer.valueOf(documentSnapshot.getString("sum"));
                                        }
                                    }
                                    binding.txtothersum.setText(othersum.toString());
                                }else {
                                    Toast.makeText(bokpage.this, "er", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                binding.btninsbok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent insacc = new Intent(bokpage.this,insbokpage.class);
                        insacc.putExtra("user",binding.user2.getText().toString());
                        insacc.putExtra("useremail",useremail);
                        insacc.putExtra("password",password);
                        startActivity(insacc);
                    }
                });

                binding.btnback2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent out = new Intent(bokpage.this,Mainpage.class);
                        out.putExtra("user",user);
                        out.putExtra("password",password);
                        out.putExtra("useremail",useremail);
                        startActivity(out);
                    }
                });

                binding.txtfoodsum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bokkind = new Intent(bokpage.this,bokkindpage.class);
                        bokkind.putExtra("user",user);
                        bokkind.putExtra("password",password);
                        bokkind.putExtra("useremail",useremail);
                        bokkind.putExtra("kind","食品");
                        bokkind.putExtra("month",parent.getSelectedItem().toString());
                        startActivity(bokkind);
                    }
                });

                binding.txtpasssum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bokkind = new Intent(bokpage.this,bokkindpage.class);
                        bokkind.putExtra("user",user);
                        bokkind.putExtra("password",password);
                        bokkind.putExtra("useremail",useremail);
                        bokkind.putExtra("kind","通行");
                        bokkind.putExtra("month",parent.getSelectedItem().toString());
                        startActivity(bokkind);
                    }
                });
                binding.txtetitsum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bokkind = new Intent(bokpage.this,bokkindpage.class);
                        bokkind.putExtra("user",user);
                        bokkind.putExtra("password",password);
                        bokkind.putExtra("useremail",useremail);
                        bokkind.putExtra("kind","娛樂");
                        bokkind.putExtra("month",parent.getSelectedItem().toString());
                        startActivity(bokkind);
                    }
                });
                binding.txtlifesum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bokkind = new Intent(bokpage.this,bokkindpage.class);
                        bokkind.putExtra("user",user);
                        bokkind.putExtra("password",password);
                        bokkind.putExtra("useremail",useremail);
                        bokkind.putExtra("kind","生活用品");
                        bokkind.putExtra("month",parent.getSelectedItem().toString());
                        startActivity(bokkind);
                    }
                });
                binding.txtothersum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bokkind = new Intent(bokpage.this,bokkindpage.class);
                        bokkind.putExtra("user",user);
                        bokkind.putExtra("password",password);
                        bokkind.putExtra("useremail",useremail);
                        bokkind.putExtra("kind","其他");
                        bokkind.putExtra("month",parent.getSelectedItem().toString());
                        startActivity(bokkind);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.user2.setText(user);


    }
}