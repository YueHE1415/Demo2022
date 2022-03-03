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

import com.example.demo2021110.databinding.ActivityBokkindpageBinding;
import com.example.demo2021110.databinding.ActivityBokpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class bokkindpage extends AppCompatActivity {

    private ActivityBokkindpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String user,password,useremail;
    String kind,month;
    ArrayList data_name,data_date,data_sum,data_id;
    Integer allsum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBokkindpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("記帳");
        Toast.makeText(this, "長按刪除", Toast.LENGTH_SHORT).show();
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");
        kind = it.getStringExtra("kind");
        month=it.getStringExtra("month");


        allsum=0;
        db.collection("book")
                .whereEqualTo("account",user)
                .whereEqualTo("kind",kind)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        data_name=new ArrayList<>();
                        data_date=new ArrayList<>();
                        data_sum=new ArrayList<>();
                        data_id=new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                            if(month.equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                            data_name.add(documentSnapshot.getString("name").toString());
                            data_date.add(documentSnapshot.getString("date").toString());
                            data_sum.add(documentSnapshot.getString("sum").toString());
                            data_id.add(documentSnapshot.getId().toString());
                            allsum+=Integer.valueOf(documentSnapshot.getString("sum").toString());
                            }
                        }
                        binding.txtkindkind.setText(kind);
                        binding.txtkindallnum.setText("共有"+data_date.size()+"張");
                        binding.txtkindallsum.setText("總金額"+allsum.toString()+"元");
                        MyAdapter2 adapter2 = new MyAdapter2(bokkindpage.this);
                        binding.ListViewbok.setAdapter(adapter2);
                    }
                });

        binding.btnbackbokkindpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out = new Intent(bokkindpage.this,bokpage.class);
                out.putExtra("user",user);
                out.putExtra("password",password);
                out.putExtra("useremail",useremail);
                startActivity(out);
            }
        });

        binding.ListViewbok.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.collection("book")
                        .document(data_id.get(position).toString())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(bokkindpage.this,"刪除成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                allsum=0;
                db.collection("book")
                        .whereEqualTo("account",user)
                        .whereEqualTo("kind",kind)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                data_name=new ArrayList<>();
                                data_date=new ArrayList<>();
                                data_sum=new ArrayList<>();
                                data_id=new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    if(month.equals(documentSnapshot.getString("date").toString().substring(4,6).toString())){
                                        data_name.add(documentSnapshot.getString("name").toString());
                                        data_date.add(documentSnapshot.getString("date").toString());
                                        data_sum.add(documentSnapshot.getString("sum").toString());
                                        data_id.add(documentSnapshot.getId().toString());
                                        allsum+=Integer.valueOf(documentSnapshot.getString("sum").toString());
                                    }
                                }
                                binding.txtkindkind.setText(kind);
                                binding.txtkindallnum.setText("共有"+data_date.size()+"張");
                                binding.txtkindallsum.setText("總金額"+allsum.toString()+"元");
                                MyAdapter2 adapter2 = new MyAdapter2(bokkindpage.this);
                                binding.ListViewbok.setAdapter(adapter2);
                            }
                        });
                return false;
            }
        });
    }


    public class  MyAdapter2 extends BaseAdapter{

        Context mContext;
        LayoutInflater mylayout;

        public MyAdapter2(Context mContext) {
            this.mContext = mContext;
            mylayout = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return data_name.size();
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

            TextView name,sum,date;
            convertView=mylayout.inflate(R.layout.layout2,null);

            name=convertView.findViewById(R.id.txtkindlayoutname);
            sum=convertView.findViewById(R.id.txtkindlayoutsum);
            date=convertView.findViewById(R.id.txtkindlayoutdate);

            name.setText(data_name.get(position).toString());
            sum.setText(data_sum.get(position).toString());
            date.setText(data_date.get(position).toString());

            return convertView;
        }
    }
}