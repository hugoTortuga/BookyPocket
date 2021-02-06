package android.cnam.bookypocket.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.cnam.bookypocket.API.API_GoogleBooks;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.R;
import android.cnam.bookypocket.Utils.Alert;
import android.cnam.bookypocket.Utils.BarCodeReader;
import android.cnam.bookypocket.Utils.ChangeActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BarCodeReaderActivity extends AppCompatActivity{

    private BarCodeReader barCodeReader;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeInfo;
    private Button stopScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);
        surfaceView = findViewById(R.id.surface_view);
        barcodeInfo = findViewById(R.id.barcode_text);
        stopScanButton = findViewById(R.id.stopScanButton);

        //Instantiate codeBarReader
        barCodeReader = new BarCodeReader();

        //Check if Barcode Detector is available on the user system
        if(barCodeReader.isBarcodeDetectorOperational(this)){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
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

        stopScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopScanAndGoRegister(v);
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

    private void stopScanAndGoRegister(View view){
        String isbn = barcodeInfo.getText().toString();
        CallGoogleBookAPI task = new CallGoogleBookAPI(this, isbn);
        task.execute();
    }

    private void GoToDetails(Book book){
        try{
            if(book == null)
                Alert.ShowDialog(this, "Mince", "Aucune correspondance n'a été trouvé sur Google Books");
            else
                ChangeActivity.GoToBookDetailActivity(this, book);
        }
        catch (Exception ex){
            Alert.ShowError(this, "Erreur", "" + ex);
        }
    }

    public void GoBack(View view) {
        this.finish();
    }

    /**
     * Classe interne qui ne sert qu'à faire des appels asynchrone à l'api google BOOK
     * obligatoire pour ne pas encombre le thread UI principal
     * voir pour externaliser cette portion de code dans un fichier ?
     */
    private class CallGoogleBookAPI extends AsyncTask<Void, Void, String> {

        private Book book;
        private BarCodeReaderActivity it;
        private String isbn;
        private ProgressDialog dialog;

        public CallGoogleBookAPI(BarCodeReaderActivity _it, String _isbn) {
            super();
            it = _it;
            isbn = _isbn;
            dialog = new ProgressDialog(_it);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Recherche en cours...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                Looper.prepare();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            try {
                List<Book> books = API_GoogleBooks.RequestISBN(isbn, it);
                if(books != null)
                    if(books.size()>0)
                        book = books.get(0);
            } catch (Exception ex) {
                Alert.ShowError(it, "Erreur lors de l'appel à l'api Google Books", "" + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            GoToDetails(book);
        }
    }


}