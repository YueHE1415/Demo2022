package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityInsaccBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class insacc extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityInsaccBinding binding;
    String user,password,useremail;
    String shop,sum,date,buy,kind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsaccBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent it = getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");
        setTitle("發票");
        binding.btninsaccback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(insacc.this,accpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        binding.btninsaccck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                account account = new account();
                String[] data_sum = binding.editinsaccsum.getText().toString().split("[,]");
                Integer allaccsum = 0;
                for (int i = 0; i<=(data_sum.length-1);i++){
                    allaccsum+=Integer.valueOf(data_sum[i]);
                }
                account.shop = binding.editinsaccshop.getText().toString();
                account.code = binding.editinsacccode.getText().toString();
                account.sum = allaccsum.toString();
                account.date = binding.editinsaccdate.getText().toString();
                account.buy = binding.editinsaccbuy.getText().toString();
                account.account = user;

                shop = binding.editinsaccshop.getText().toString();
                sum = binding.editinsaccsum.getText().toString();
                date = binding.editinsaccdate.getText().toString();
                buy = binding.editinsaccbuy.getText().toString();

                String[] data_buy = buy.split("[,]");

                if (account.shop.isEmpty()||account.code.isEmpty()||account.sum.isEmpty()||account.date.isEmpty()||account.buy.isEmpty()){
                    Toast.makeText(insacc.this, "欄位不可空白", Toast.LENGTH_SHORT).show();
                }else {
                if(data_buy.length != data_sum.length){
                    Toast.makeText(insacc.this, "購買品項與金額數不相符", Toast.LENGTH_SHORT).show();
                }else {
                    db.collection("acc2")
                            .document(account.account + nowDate)
                            .set(account)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                  //  Toast.makeText(insacc.this, "新增成功", Toast.LENGTH_SHORT).show();
                                }
                            });


                    for (int i = 0; i <= (data_buy.length - 1); i++) {
                        int k = i;
                        binding.textView50.setText("");
                        db.collection("Kind")
                                .whereEqualTo("name", data_buy[i])
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            bok bk = new bok();
                                            bk.shop = binding.editinsaccshop.getText().toString();
                                            bk.date = binding.editinsaccdate.getText().toString();
                                            bk.sum = data_sum[k];
                                            bk.kind = documentSnapshot.getString("kind");
                                            binding.textView50.setText(documentSnapshot.getString("kind"));
                                            bk.name = data_buy[k];
                                            bk.account = user;
                                            db.collection("book")
                                                    .document(bk.account + nowDate + k)
                                                    .set(bk)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(insacc.this, documentSnapshot.getString("kind"), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                        if (binding.textView50.getText().equals("")){
                            bok bk = new bok();
                            bk.shop = binding.editinsaccshop.getText().toString();
                            bk.date = binding.editinsaccdate.getText().toString();
                            bk.sum = data_sum[k];
                            bk.name = data_buy[k];
                            bk.kind = "其他";
                            bk.account = user;
                            db.collection("book")
                                    .document(bk.account + nowDate + k)
                                    .set(bk)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(insacc.this, "其他", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                 }
                }

            }
        });
    }
}