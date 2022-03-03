package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityAccpageBinding;
import com.example.demo2021110.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class accpage extends AppCompatActivity {

    private ActivityAccpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList data_shop, data_sum, data_code, data_date,data_id;
    String user,password,useremail;
    Integer allsum ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAccpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("發票");
        Intent it = getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");

        Toast.makeText(this, "長按刪除", Toast.LENGTH_SHORT).show();
        binding.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                allsum = 0;
                db.collection("acc2")
                        .whereEqualTo("account",user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    data_shop = new ArrayList<>();
                                    data_sum = new ArrayList<>();
                                    data_code = new ArrayList<>();
                                    data_date = new ArrayList<>();
                                    data_id = new ArrayList<>();
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        if(parent.getSelectedItem().toString().equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                        account acc = documentSnapshot.toObject(com.example.demo2021110.account.class);
                                        data_shop.add(documentSnapshot.getString("shop").toString());
                                        data_sum.add(documentSnapshot.getString("sum").toString());
                                        data_code.add(documentSnapshot.getString("code").toString());
                                        data_date.add(documentSnapshot.getString("date").toString());
                                        data_id.add(documentSnapshot.getId().toString());
                                        allsum +=Integer.valueOf(documentSnapshot.getString("sum").toString());
                                         }
                                    }
                                }
                                binding.txtnum.setText("總張數"+data_code.size()+"張");
                                binding.txtsum.setText("總金額"+allsum.toString()+"元");
                                Myadapter myadapter = new Myadapter(accpage.this);
                                binding.ListView.setAdapter(myadapter);
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btninsacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(accpage.this,insacc.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        binding.btninsaccQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(accpage.this,accinsqrpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        binding.btnback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(accpage.this,Mainpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        binding.ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(accpage.this,accckpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                it.putExtra("accid",data_id.get(position).toString());
                startActivity(it);
            }
        });

        binding.ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.collection("acc2")
                        .document(data_id.get(position).toString())
                        .delete();
                allsum = 0;
                db.collection("acc2")
                        .whereEqualTo("account",user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    data_shop = new ArrayList<>();
                                    data_sum = new ArrayList<>();
                                    data_code = new ArrayList<>();
                                    data_date = new ArrayList<>();
                                    data_id = new ArrayList<>();
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                        account acc = documentSnapshot.toObject(com.example.demo2021110.account.class);
                                        data_shop.add(documentSnapshot.getString("shop").toString());
                                        data_sum.add(documentSnapshot.getString("sum").toString());
                                        data_code.add(documentSnapshot.getString("code").toString());
                                        data_date.add(documentSnapshot.getString("date").toString());
                                        data_id.add(documentSnapshot.getId().toString());
                                        allsum +=Integer.valueOf(documentSnapshot.getString("sum").toString());
                                    }
                                }
                                binding.txtnum.setText("總張數"+data_code.size()+"張");
                                binding.txtsum.setText("總金額"+allsum.toString()+"元");
                                Myadapter myadapter = new Myadapter(accpage.this);
                                binding.ListView.setAdapter(myadapter);
                            }
                        });
                return false;
            }
        });
    }



    public  class Myadapter extends BaseAdapter{

        Context mcontext;
        LayoutInflater mlayoutInflater;

        public Myadapter(Context mcontext) {
            this.mcontext = mcontext;
            mlayoutInflater = LayoutInflater.from(mcontext);
        }

        @Override
        public int getCount() {
            return data_code.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView shop,sum,code,date;

            convertView=mlayoutInflater.inflate(R.layout.layout,null);

            shop=convertView.findViewById(R.id.shop);
            sum=convertView.findViewById(R.id.sum);
            code=convertView.findViewById(R.id.code);
            date=convertView.findViewById(R.id.date);

            shop.setText(data_shop.get(position).toString());
            sum.setText(data_sum.get(position).toString());
            code.setText(data_code.get(position).toString());
            date.setText(data_date.get(position).toString());
            return convertView;
        }
    }

}