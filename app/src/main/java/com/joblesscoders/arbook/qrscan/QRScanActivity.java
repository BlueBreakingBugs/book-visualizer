package com.joblesscoders.arbook.qrscan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joblesscoders.arbook.R;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QRScanActivity extends AppCompatActivity {
final String TAG = "QRScanActivity";
DecoratedBarcodeView bcv;
TextView displayData;

private BarcodeCallback callback = result -> {
    Toast.makeText(QRScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
    Log.e(TAG, "barcodeResult: " + result.getText());
    bcv.getBarcodeView().stopDecoding();
    displayData.setText(result.getText());
    bcv.setVisibility(View.GONE);
};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        bcv = (DecoratedBarcodeView) findViewById(R.id.decorated_barcode_view);

        // Go through the XML file to know which component it refers to
        displayData = findViewById(R.id.qr_code_data);
        displayData.setText("Nothing scanned yet");
    }

    public void initScanner() {
        bcv.setVisibility(View.VISIBLE);

        // In case we want to limit the type of barcodes that can be scanned, we can use the following
//        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.UPC_A);

        // List of decoder types, sending no params make it open to app valid code types
        bcv.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory());

        // God knows what it does, I copied this because it wasn't working before
        bcv.initializeFromIntent(getIntent());

        // This starts the final scanning action, until this is called, the camera may show contents, but won't scan anything
        bcv.decodeSingle(callback);
    }
    @Override
    protected void onResume() {
        super.onResume();
        bcv.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bcv.pause();
    }

    // This function is called when scan button is pressed
    public void startScan(View v) {

        // This is the default full screen activity, better to not use it
//        new IntentIntegrator(this)
//                .setOrientationLocked(true)
//                .initiateScan();

        if (bcv.getVisibility() == View.VISIBLE) {

            // Scanner already being shown, no need to init it again
            return;
        }

        initScanner();
    }

}