package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityAccinsqrpage2Binding;
import com.example.demo2021110.databinding.ActivityInsaccqrcodepageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;

public class insaccqrcodepage extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityInsaccqrcodepageBinding binding;
    String user,password,useremail,qrcode,qrcode2;
    String code,date,buy,sum,allsum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityInsaccqrcodepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("發票");
        Intent it = getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");
        qrcode=it.getStringExtra("qrcode");
        qrcode2=it.getStringExtra("qrcode2");
        String buyqrcode=qrcode.substring(89);

        if (Integer.valueOf(buyqrcode.substring(0,1))>1){
            buyqrcode += qrcode2.substring(2);
            String[] buyarr = buyqrcode.split(":");
            for(int i = 1;i<=Integer.valueOf(buyarr[0]);i++){
                for (int j = 1 ; j<=Integer.valueOf(buyarr[i*3+1]);j++){
                    buy+=buyarr[i*3]+",";
                    sum+=buyarr[i*3+2]+",";
                }
            }
        }else {
            String[] buyarr = buyqrcode.split(":");
            for (int i = 1 ; i<= Integer.valueOf(buyarr[4]);i++){
                buy+=buyarr[3]+",";
                sum+=buyarr[5]+",";
            }
        }

        code=qrcode.substring(0,10);
        String year =String.valueOf((Integer.valueOf(qrcode.substring(10,13))+1911));
        date= year+qrcode.substring(13,17);
        allsum=String.valueOf(Integer.parseInt(qrcode.substring(29,37),16));

        binding.editinsaccqrcodebuy.setText(buy.substring(4));
        binding.editinsaccqrcodecode.setText(code);
        binding.editinsaccqrcodedate.setText(date);
        binding.editinsaccqrcodesum.setText(sum.substring(4));

        binding.btninsaccqrcodeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(insaccqrcodepage.this,accpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        binding.btninsaccqrcodech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                account account = new account();
                String[] data_sum = binding.editinsaccqrcodesum.getText().toString().split("[,]");
                String[] data_buy = binding.editinsaccqrcodebuy.getText().toString().split("[,]");
                String eshop = binding.editinsaccqrcodeshop.getText().toString();
                String edate = binding.editinsaccqrcodedate.getText().toString();
                String ecode = binding.editinsaccqrcodecode.getText().toString();
                String ebuy = binding.editinsaccqrcodebuy.getText().toString();

                account.account=user;
                account.shop=eshop;
                account.buy=ebuy;
                account.code=ecode;
                account.date=edate;
                account.sum=String.valueOf(allsum);
                if(ecode.isEmpty()||ebuy.isEmpty()||edate.isEmpty()||eshop.isEmpty()||edate.isEmpty()){
                    Toast.makeText(insaccqrcodepage.this, "欄位不可空白", Toast.LENGTH_SHORT).show();
                }else {
                    if(data_buy.length != data_sum.length){
                        Toast.makeText(insaccqrcodepage.this, "購買品項與金額數不相符", Toast.LENGTH_SHORT).show();
                    }else {
                        db.collection("acc2")
                                .document(account.account + nowDate)
                                .set(account)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(insaccqrcodepage.this, "新增成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                for (int i = 0; i <= (data_buy.length - 1); i++) {
                    int k = i;
                    binding.textView3.setText("");
                    db.collection("Kind")
                            .whereEqualTo("name", data_buy[i])
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        bok bk = new bok();
                                        bk.shop = eshop;
                                        bk.date = edate;
                                        bk.sum = data_sum[k];
                                        bk.kind = documentSnapshot.getString("kind");
                                        binding.textView3.setText(documentSnapshot.getString("kind"));
                                        bk.name = data_buy[k];
                                        bk.account = user;
                                        db.collection("book")
                                                .document(bk.account + nowDate + k)
                                                .set(bk)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(insaccqrcodepage.this, documentSnapshot.getString("kind"), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                    if(binding.textView3.getText().equals("")){
                        bok bk = new bok();
                        bk.shop = eshop;
                        bk.date = edate;
                        bk.sum = data_sum[k];
                        bk.kind = "其他";
                        bk.name = data_buy[k];
                        bk.account = user;
                        db.collection("book")
                                .document(bk.account + nowDate + k)
                                .set(bk)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(insaccqrcodepage.this, "其他", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }
}