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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo2021110.databinding.ActivityAccckpageBinding;
import com.example.demo2021110.databinding.ActivityAwdpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class awdpage extends AppCompatActivity {

    private ActivityAwdpageBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user,password,useremail;
    String[] awdid,awd,awdmo,awddate,awdcode,insawd;
    int ti = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAwdpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("中獎發票");
        Intent it =getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");

        find();

        binding.btnawdback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent out = new Intent(awdpage.this,Mainpage.class);
                out.putExtra("user",user);
                out.putExtra("password",password);
                out.putExtra("useremail",useremail);
                startActivity(out);
            }
        });

        binding.spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ti == 0){
                    ti+=1;
                }else {
                    String tmo = binding.txtmo.getText().toString();
                    String acid = binding.txthidid.getText().toString();
                    String awdsum = binding.txthidawdsum.getText().toString();
                    String date = binding.txthiddate.getText().toString();
                    String code = binding.txthidcode.getText().toString();
                    Integer ist = 0;
                    Integer allsum =0;
                    Integer allnum = 0;

                    awdid = acid.split(",");
                    awdmo = tmo.split(",");
                    awd=awdsum.split(",");
                    awddate = date.split(",");
                    awdcode = code.split(",");



                    if(awdid.length>1){
                        for (int i = 1; i<=awdid.length-1;i++){
                            if (awdmo[i].equals(String.valueOf(position))){
                                ist+=1;
                            }
                        }
                        insawd = new String[ist];
                        ist=0;
                        for (int i = 1; i<=awdid.length-1;i++){
                            if (awdmo[i].equals(String.valueOf(position))){
                                insawd[ist]=awdcode[i]+"    日期:"+awddate[i]+"   中獎金額:"+awd[i];
                                allsum+=Integer.valueOf(awd[i]);
                                allnum+=1;
                                ist+=1;
                            }
                        }
                        binding.txtawdsum.setText(String.valueOf(allsum)+"元");
                        binding.txtawdnum.setText(String.valueOf(allnum)+"張");
                        ListAdapter adapter = new ArrayAdapter<String>(awdpage.this , android.R.layout.simple_list_item_1 ,insawd);
                        binding.awdListView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(awdpage.this, "er", Toast.LENGTH_SHORT).show();
            }
        });

        binding.awdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent out = new Intent(awdpage.this,awdckpage.class);
                out.putExtra("user",user);
                out.putExtra("password",password);
                out.putExtra("useremail",useremail);
                out.putExtra("awdid",awdid[(position+1)]);
                out.putExtra("awdsum",awd[(position+1)]);
                startActivity(out);
            }
        });
    }

    private void find(){
        db.collection("acc2")
                .whereEqualTo("account",user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot:task.getResult()){
                                Integer mo = (Integer.valueOf(documentSnapshot.getString("date").substring(4,6))+1)/2;
                                db.collection("awd")
                                        .whereEqualTo("month",String.valueOf(mo)).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    String tmo = binding.txtmo.getText().toString();
                                                    String acid = binding.txthidid.getText().toString();
                                                    String awdsum = binding.txthidawdsum.getText().toString();
                                                    String date = binding.txthiddate.getText().toString();
                                                    String code = binding.txthidcode.getText().toString();

                                                    for (DocumentSnapshot documentSnapshot1:task.getResult()){
                                                        if (documentSnapshot.getString("code").toString().substring(2).equals(documentSnapshot1.getString("awdnum"))) {
                                                            if (documentSnapshot1.getString("awdkind").equals("special")) {
                                                                binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                                binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                                binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                                binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                                binding.txthidawdsum.setText(awdsum+","+"2000000");
                                                                break;
                                                            } else if (documentSnapshot1.getString("awdkind").equals("maximum")) {
                                                                binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                                binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                                binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                                binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                                binding.txthidawdsum.setText(awdsum+","+"10000000");
                                                                break;
                                                            } else {
                                                                binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                                binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                                binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                                binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                                binding.txthidawdsum.setText(awdsum+","+"200000");
                                                                break;
                                                            }
                                                        }
                                                        if (documentSnapshot.getString("code").toString().substring(3).equals(documentSnapshot1.getString("awdnum").substring(1)) && documentSnapshot1.getString("awdkind").equals("common")) {
                                                            binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                            binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                            binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                            binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                            binding.txthidawdsum.setText(awdsum+","+"40000");
                                                            break;
                                                        }
                                                        if (documentSnapshot.getString("code").toString().substring(4).equals(documentSnapshot1.getString("awdnum").substring(2)) && documentSnapshot1.getString("awdkind").equals("common")) {
                                                            binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                            binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                            binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                            binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                            binding.txthidawdsum.setText(awdsum+","+"10000");
                                                            break;
                                                        }
                                                        if (documentSnapshot.getString("code").toString().substring(5).equals(documentSnapshot1.getString("awdnum").substring(3)) && documentSnapshot1.getString("awdkind").equals("common")) {
                                                            binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                            binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                            binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                            binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                            binding.txthidawdsum.setText(awdsum+","+"4000");
                                                            break;
                                                        }
                                                        if (documentSnapshot.getString("code").toString().substring(6).equals(documentSnapshot1.getString("awdnum").substring(4)) && documentSnapshot1.getString("awdkind").equals("common")) {
                                                            binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                            binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                            binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                            binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                            binding.txthidawdsum.setText(awdsum+","+"1000");
                                                            break;
                                                        }
                                                        if (documentSnapshot.getString("code").toString().substring(7).equals(documentSnapshot1.getString("awdnum").substring(5))) {
                                                            binding.txtmo.setText(tmo+","+String.valueOf(mo));
                                                            binding.txthidid.setText(acid+","+documentSnapshot.getId().toString());
                                                            binding.txthiddate.setText(date+","+documentSnapshot.getString("date"));
                                                            binding.txthidcode.setText(code+","+documentSnapshot.getString("code"));
                                                            binding.txthidawdsum.setText(awdsum+","+"200");
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }
}