package android.cnam.bookypocket.Utils;

import android.content.Context;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.FocusingProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BarCodeReader {

    private static String barcodeData;

    public static String getBarcodeData() {
        return barcodeData;
    }

    private static BarcodeDetector barcodeDetector;

    public static boolean isBarcodeDetectorOperational(Context context){
        barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.EAN_13).build();

        //Check if Android system Barcode Detector available on the user system
        if(barcodeDetector.isOperational()){
            return false;
        } else {
            return true;
        }
    }

    public BarcodeDetector setUpBarcodeDetector(Context context, TextView barcodeInfo){

        barcodeDetector.setProcessor(new FocusingProcessor<Barcode>(barcodeDetector, new Tracker<Barcode>()) {

            @Override
            public void release() {
                Toast.makeText(context.getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                            BarCodeReader.barcodeData = barcodeInfo.toString();
                        }
                    });
                }
            }

            @Override
            public int selectFocus(Detector.Detections<Barcode> detections) {
                return 0;
            }

        });
        return barcodeDetector;
    }
}
