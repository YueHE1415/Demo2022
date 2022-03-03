package com.example.demo2021110;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityInsbokpageBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class insbokpage extends AppCompatActivity {

    private ActivityInsbokpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String user,password,useremail;
    String selitem,shop,sum,name,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInsbokpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("記帳");
        Intent it = getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selitem= parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                bok bk=new bok();
                bk.shop=binding.editshopname.getText().toString();
                bk.sum=binding.editsum.getText().toString();
                bk.name=binding.editname.getText().toString();
                bk.date=binding.editTextDate.getText().toString();
                bk.account=user;
                bk.kind=selitem;
                if (bk.shop.isEmpty()||bk.sum.isEmpty()||bk.name.isEmpty()||bk.date.isEmpty()){
                    Toast.makeText(insbokpage.this, "欄位不可空白", Toast.LENGTH_SHORT).show();
                }else {
                    db.collection("book")
                            .document(bk.account+nowDate)
                            .set(bk);
                    Toast.makeText(insbokpage.this, "新增成功", Toast.LENGTH_SHORT).show();
                    Intent out = new Intent(insbokpage.this,bokpage.class);
                    out.putExtra("user",user);
                    out.putExtra("password",password);
                    out.putExtra("useremail",useremail);
                    startActivity(out);
                }
            }
        });

        binding.btnback3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out = new Intent(insbokpage.this,bokpage.class);
                out.putExtra("user",user);
                out.putExtra("password",password);
                out.putExtra("useremail",useremail);
                startActivity(out);
            }
        });
    }
}