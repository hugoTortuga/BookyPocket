package android.cnam.bookypocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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

public class BarCodeReaderActivity extends AppCompatActivity{

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeInfo;
    private String barcodeData;

    private Barcode barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);
        surfaceView = findViewById(R.id.surface_view);
        barcodeInfo = findViewById(R.id.barcode_text);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.EAN_13).build();

        if(!barcodeDetector.isOperational()){
            barcodeInfo.setText("Sorry, Couldn't setup the detector");
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            barcodeInfo.setText("Detector is operational");
            Toast.makeText(getApplicationContext(), "Detector is operational", Toast.LENGTH_LONG).show();
        }

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK).
                setRequestedPreviewSize(1024,1024).setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){

                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    try {
                        checkCameraPermission();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("CAMERA SOURCE", e.getMessage());
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

        barcodeDetector.setProcessor(new FocusingProcessor<Barcode>(barcodeDetector, new Tracker<Barcode>()) {

            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            barcodeInfo.setText(    // Update the TextView
                                    barcodes.valueAt(0).displayValue
                            );
                        }
                    });
                }

                //TEST 2
//                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                if (barcodes.size() > 0) {
//                    barcodeInfo.setText(barcodes.valueAt(0).displayValue);
//                }
                //TEST 3
//                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                if (barcodes.size() != 0) {
//                    barcodeInfo.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            barcodeInfo.setText(barcodes.valueAt(0).displayValue);
//                        }
//                    });
//                } else {
//                    barcodeInfo.setText("No data available");
//
//                }
            }

            @Override
            public int selectFocus(Detector.Detections<Barcode> detections) {
                return 0;
            }

        });


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

}