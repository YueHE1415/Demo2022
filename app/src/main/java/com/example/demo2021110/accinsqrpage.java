package com.example.demo2021110;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.demo2021110.databinding.ActivityAccinsqrpageBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class accinsqrpage extends AppCompatActivity {

    private ActivityAccinsqrpageBinding binding;
    String user,password,useremail;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAccinsqrpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("QRCODE");

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA )!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }


        Intent it = getIntent();
        user = it.getStringExtra("user");
        password=it.getStringExtra("password");
        useremail=it.getStringExtra("useremail");

        binding.btnaccinsqrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(accinsqrpage.this,accpage.class);
                it.putExtra("user",user);
                it.putExtra("password",password);
                it.putExtra("useremail",useremail);
                startActivity(it);
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getBaseContext(),barcodeDetector).setAutoFocusEnabled(true).build();

        binding.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)
                        !=PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode>qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    binding.textView30.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.textView30.setText(qrCodes.valueAt(0).displayValue);
                            Intent it = new Intent(accinsqrpage.this,accinsqrpage2.class);
                            it.putExtra("user",user);
                            it.putExtra("password",password);
                            it.putExtra("useremail",useremail);
                            it.putExtra("qrcode",qrCodes.valueAt(0).displayValue);
                            startActivity(it);
                        }
                    });
                }
            }
        });
    }

}