package android.cnam.bookypocket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.BarCodeReader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeReaderActivity extends AppCompatActivity{

    private BarCodeReader barCodeReader;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);
        surfaceView = findViewById(R.id.surface_view);
        barcodeInfo = findViewById(R.id.barcode_text);

        //Instanciate codeBarReader
        barCodeReader = new BarCodeReader();

        //Check if Barcode Detector is available on the user system
        if(barCodeReader.isBarcodeDetectorOperational(this)){
            barcodeInfo.setText("Sorry, Couldn't setup the detector");
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        } else{
            barcodeInfo.setText("Detector is operational");
            Toast.makeText(getApplicationContext(), "Detector is operational", Toast.LENGTH_LONG).show();
        }

        barcodeDetector = barCodeReader.setUpBarcodeDetector(this, barcodeInfo);

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