package android.cnam.bookypocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.FocusingProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeReaderActivity extends AppCompatActivity /* implements Detector.Processor */{

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeInfo;
    private String barcodeData;
    Activity activity;
    private Barcode barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bar_code_reader);
            surfaceView = findViewById(R.id.surface_view);
            barcodeInfo = findViewById(R.id.barcode_text);

            activity = this;

            barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ISBN).build();
            cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920,1080).setAutoFocusEnabled(true).build();
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){

                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    if (ContextCompat.checkSelfPermission(BarCodeReaderActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            cameraSource.start(surfaceView.getHolder());
                        } catch (IOException ie) {
                            Log.e("CAMERA SOURCE", ie.getMessage());
                        }
                    } else {
                        //Toast.makeText(BarCodeReaderActivity.this, getResources().getString(R.string.error_permisos), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                }
            });

            barcodeDetector.setProcessor(new FocusingProcessor<Barcode>(barcodeDetector, new Tracker<Barcode>()) {

                @Override
                public void release() {
                    super.release();
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        barcodeInfo.post(new Runnable() {
                            @Override
                            public void run() {
                                final StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < barcodes.size(); ++i) {
                                    sb.append(barcodes.valueAt(i).rawValue).append("\n");
                                }
                                Intent data = new Intent();
                                data.putExtra("barcodes", sb.toString());
                                BarCodeReaderActivity.this.setResult(AppCompatActivity.RESULT_OK, data);
                                BarCodeReaderActivity.this.finish();
                                barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                            }
                        });
                    }
                }

                @Override
                public int selectFocus(Detector.Detections<Barcode> detections) {
                    return 0;
                }
            });

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).
                setRequestedPreviewSize(1024, 1024).setAutoFocusEnabled(true)
                .build();
    }

        private void checkCameraPermission() throws IOException {
        if (ActivityCompat.checkSelfPermission(BarCodeReaderActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraSource.start(surfaceView.getHolder());
        } else {

            //request permission
            ActivityCompat.requestPermissions(BarCodeReaderActivity.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

//    @Override
//    public void release() {
//        Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void receiveDetections(Detector.Detections detections) {
//        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//        if (barcodes.size() != 0) {
//            barcodeInfo.post(new Runnable() {
//                @Override
//                public void run() {
//                    final StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < barcodes.size(); ++i) {
//                        sb.append(barcodes.valueAt(i).rawValue).append("\n");
//                    }
//                    Intent data = new Intent();
//                    data.putExtra("barcodes", sb.toString());
//                    BarCodeReaderActivity.this.setResult(AppCompatActivity.RESULT_OK, data);
//                    BarCodeReaderActivity.this.finish();
//                    barcodeInfo.setText(barcodes.valueAt(0).displayValue);
//                }
//            });
//        }
//    }


//    private void set(){
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1024);
//                        return;
//                    }
//                    cameraSource.start(surfaceView.getHolder());
//                } catch (IOException ie) {
//                    Log.e("Camera start problem", ie.getMessage());
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                cameraSource.stop();
//            }
//        });
//    }
//
//    @Override
//    public void release() {
//
//    }
//
//    @Override
//    public void receiveDetections (Detector.Detections detections){
//        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//        if (barcodes.size() != 0) {
//            final StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < barcodes.size(); ++i) {
//                sb.append(barcodes.valueAt(i).rawValue).append("\n");
//            }
//            Intent data = new Intent();
//            data.putExtra("barcodes", sb.toString());
//            this.setResult(AppCompatActivity.RESULT_OK, data);
//            this.finish();
//
//        }
//
//    }

}

