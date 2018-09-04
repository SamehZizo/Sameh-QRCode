package com.sameh.qrcode.QRScan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScanner {

    private Activity activity;
    private SurfaceView cameraPreview;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private int RequestCameraPermissionID;
    private String data = null;
    private TextView txtResult;

    private Completed completed;

    public QRScanner(Activity activity, SurfaceView cameraPreview,TextView txtResult, int requestCameraPermissionID) {
        this.activity = activity;
        this.cameraPreview = cameraPreview;
        this.RequestCameraPermissionID = requestCameraPermissionID;
        this.txtResult = txtResult;
    }

    public void Scan(Completed completed){
        this.completed = completed;
        initialize();
    }

    private void initialize(){

        barcodeDetector = new BarcodeDetector.Builder(activity)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(activity, barcodeDetector)
                .setRequestedPreviewSize(300, 300)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() != 0){
                    /*txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) activity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            txtResult.setText(barcode.valueAt(0).displayValue);
                            Completed completed = QRScanner.this.completed;
                            completed.onComplete(barcode.valueAt(0).displayValue);
                        }
                    });*/
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Vibrator vibrator = (Vibrator) activity.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                        }
                    }.start();
                    Completed completed = QRScanner.this.completed;
                    completed.onComplete(barcode.valueAt(0).displayValue);
                }
            }
        });
    }

    public void onRequestCameraPermission(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == RequestCameraPermissionID && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraSource.start(cameraPreview.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface Completed{
        void onComplete(String data);
    }
}
