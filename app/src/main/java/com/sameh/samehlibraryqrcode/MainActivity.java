package com.sameh.samehlibraryqrcode;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sameh.qrcode.QRCreate.QRCreator;
import com.sameh.qrcode.QRScan.QRScanner;

public class MainActivity extends AppCompatActivity {

    /*QRCreator qrCreator;
    ImageView imageView;*/

    QRScanner qrScanner;
    SurfaceView surfaceView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*qrCreator = new QRCreator(this);
        imageView = findViewById(R.id.image);*/

        surfaceView = findViewById(R.id.scan);
        textView = findViewById(R.id.text);
        qrScanner = new QRScanner(this,surfaceView,5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrScanner.Scan(new QRScanner.Completed() {
            @Override
            public void onComplete(String data) {
                textView.setText(data);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        qrScanner.onRequestCameraPermission(requestCode, permissions, grantResults);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        qrCreator.Create("Sameh", new QRCreator.Completed() {
            @Override
            public void onComplete(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
    }*/
}
