package android.cnam.bookypocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.vision.CameraSource;
//import com.google.android.gms.vision.Detector;
//import com.google.android.gms.vision.barcode.Barcode;
//import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeReaderActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    //private BarcodeDetector barcodeDetector;
    //private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeInfo;
    private String barcodeData;

    //private Barcode barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);
        surfaceView = findViewById(R.id.surface_view);
        barcodeInfo = findViewById(R.id.barcode_text);

        initialize();
    }

    private void checkCameraPermission() throws IOException {
        if (ActivityCompat.checkSelfPermission(BarCodeReaderActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
           // cameraSource.start(surfaceView.getHolder());
        } else {

            //request permission
            ActivityCompat.requestPermissions(BarCodeReaderActivity.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }
    private void scan(){
        /*barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {

                        @Override
                        public void run() {
                            barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                }
            };

        });*/
    }

    private void initialize(){
        //check if scnner activ
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        // barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ISBN).build();
        // cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920,1080).setAutoFocusEnabled(true).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    checkCameraPermission();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

        scan();
    }


}