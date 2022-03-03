package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityAnapageBinding;
import com.example.demo2021110.databinding.ActivityBokkindpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class anapage extends AppCompatActivity {

    private ActivityAnapageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user,password,useremail;
    Integer allsum,foodhigher,lifehigher,passhigher,etithigher,otherhigher = 0;
    String foodhigherid,lifehigherid,passhigherid,etithigherid,otherhigherid="";
    Integer allfoodsum,alllifesum,allpasssum,alletitsum,allothersum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnapageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("消費分析");
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");

        binding.anapagenack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back =new Intent(anapage.this,Mainpage.class);
                back.putExtra("user",user);
                back.putExtra("password",password);
                back.putExtra("useremail",useremail);
                startActivity(back);
            }
        });

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                allsum=0;
                foodhigher=0;
                lifehigher=0;
                passhigher=0;
                etithigher=0;
                otherhigher = 0;

                foodhigherid="";
                lifehigherid="";
                passhigherid="";
                etithigherid="";
                otherhigherid="";

                allfoodsum=0;
                alllifesum=0;
                allpasssum=0;
                alletitsum=0;
                allothersum=0;

                db.collection("book")
                        .whereEqualTo("account",user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for(DocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                            allsum +=Integer.valueOf(documentSnapshot.getString("sum").toString());

                                            if(documentSnapshot.getString("kind").toString().equals("食品")){
                                                Integer ft = Integer.valueOf(documentSnapshot.getString("sum").toString());
                                                allfoodsum+=ft;
                                                if (ft>foodhigher){
                                                   foodhigher=ft;
                                                   foodhigherid=documentSnapshot.getId().toString();
                                                }
                                            }

                                            if(documentSnapshot.getString("kind").toString().equals("生活用品")){
                                                Integer lt = Integer.valueOf(documentSnapshot.getString("sum").toString());
                                                alllifesum+=lt;
                                                if (lt>lifehigher){
                                                    lifehigher=lt;
                                                    lifehigherid=documentSnapshot.getId().toString();
                                                }
                                            }

                                            if(documentSnapshot.getString("kind").toString().equals("通行")){
                                                Integer pt = Integer.valueOf(documentSnapshot.getString("sum").toString());
                                                allpasssum+=pt;
                                                if (pt>passhigher){
                                                    passhigher=pt;
                                                    passhigherid=documentSnapshot.getId().toString();
                                                }
                                            }

                                            if(documentSnapshot.getString("kind").toString().equals("娛樂")){
                                                Integer et = Integer.valueOf(documentSnapshot.getString("sum").toString());
                                                alletitsum+=et;
                                                if (et>etithigher){
                                                    etithigher=et;
                                                    etithigherid=documentSnapshot.getId().toString();
                                                }
                                            }

                                            if(documentSnapshot.getString("kind").toString().equals("其他")){
                                                Integer ot = Integer.valueOf(documentSnapshot.getString("sum").toString());
                                                allothersum+=ot;
                                                if (ot>otherhigher){
                                                    otherhigher=ot;
                                                    otherhigherid=documentSnapshot.getId().toString();
                                                }
                                            }
                                        }
                                    }
                                    if(allsum!=0){
                                        binding.txtfoodcolor.setWidth(allfoodsum*1000/allsum);
                                        binding.txtlifecolor.setWidth(alllifesum*1000/allsum);
                                        binding.txtpasscolor.setWidth(allpasssum*1000/allsum);
                                        binding.txtetitcolor.setWidth(alletitsum*1000/allsum);
                                        binding.txtothercolor.setWidth(allothersum*1000/allsum);

                                        binding.txtfoodpt.setText("食品"+String.valueOf(allfoodsum*100/allsum)+"%");
                                        binding.txtlifept.setText("生活用品"+String.valueOf(alllifesum*100/allsum)+"%");
                                        binding.txtpasspt.setText("通行"+String.valueOf(allpasssum*100/allsum)+"%");
                                        binding.txtetitpt.setText("娛樂"+String.valueOf(alletitsum*100/allsum)+"%");
                                        binding.txtotherpt.setText("其他"+String.valueOf(allothersum*100/allsum)+"%");
                                    }else {
                                        binding.txtfoodpt.setText("食品0%");
                                        binding.txtlifept.setText("生活用品0%");
                                        binding.txtpasspt.setText("通行0%");
                                        binding.txtetitpt.setText("娛樂0%");
                                        binding.txtotherpt.setText("其他0%");
                                    }
                                }else {
                                    Toast.makeText(anapage.this, "er", Toast.LENGTH_SHORT).show();
                                }
                                binding.txtfoodhighsum.setText(String.valueOf(foodhigher));
                                binding.txtotherhighsum.setText(String.valueOf(otherhigher));
                                binding.txtetithighsum.setText(String.valueOf(etithigher));
                                binding.txtpasshighsum.setText(String.valueOf(passhigher));
                                binding.txtlifehighsum.setText(String.valueOf(lifehigher));
                                binding.txtallmonthallsum.setText(allsum.toString());
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtfoodhighsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foodhigherid.equals("")){
                    Toast.makeText(anapage.this, "無資料", Toast.LENGTH_SHORT).show();
                }else {
                    Intent anack =new Intent(anapage.this,anackpage.class);
                    anack .putExtra("user",user);
                    anack .putExtra("password",password);
                    anack .putExtra("useremail",useremail);
                    anack.putExtra("ckanaid",foodhigherid);
                    startActivity(anack);
                }
            }
        });

        binding.txtlifehighsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lifehigherid.equals("")){
                    Toast.makeText(anapage.this, "無資料", Toast.LENGTH_SHORT).show();
                }else {
                    Intent anack =new Intent(anapage.this,anackpage.class);
                    anack .putExtra("user",user);
                    anack .putExtra("password",password);
                    anack .putExtra("useremail",useremail);
                    anack.putExtra("ckanaid",lifehigherid);
                    startActivity(anack);
                }
            }
        });

        binding.txtpasshighsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passhigherid.equals("")){
                    Toast.makeText(anapage.this, "無資料", Toast.LENGTH_SHORT).show();
                }else {
                    Intent anack =new Intent(anapage.this,anackpage.class);
                    anack .putExtra("user",user);
                    anack .putExtra("password",password);
                    anack .putExtra("useremail",useremail);
                    anack.putExtra("ckanaid",passhigherid);
                    startActivity(anack);
                }
            }
        });

        binding.txtetithighsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etithigherid.equals("")){
                    Toast.makeText(anapage.this, "無資料", Toast.LENGTH_SHORT).show();
                }else {
                    Intent anack =new Intent(anapage.this,anackpage.class);
                    anack .putExtra("user",user);
                    anack .putExtra("password",password);
                    anack .putExtra("useremail",useremail);
                    anack.putExtra("ckanaid",etithigherid);
                    startActivity(anack);
                }
            }
        });

        binding.txtotherhighsum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otherhigherid.equals("")){
                    Toast.makeText(anapage.this, "無資料", Toast.LENGTH_SHORT).show();
                }else {
                    Intent anack =new Intent(anapage.this,anackpage.class);
                    anack .putExtra("user",user);
                    anack .putExtra("password",password);
                    anack .putExtra("useremail",useremail);
                    anack.putExtra("ckanaid",otherhigherid);
                    startActivity(anack);
                }
            }
        });
    }
}