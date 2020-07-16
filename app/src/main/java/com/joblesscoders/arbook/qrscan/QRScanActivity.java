package com.joblesscoders.arbook.qrscan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.pojo.Book;
import com.joblesscoders.arbook.pojo.BookRequest;
import com.joblesscoders.arbook.pojo.BookResponse;
import com.joblesscoders.arbook.utils.RestApiHandler;
import com.joblesscoders.arbook.utils.StorageUtil;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRScanActivity extends AppCompatActivity {
    final String TAG = "QRScanActivity";
    final int REQUEST_CODE = 2;
    DecoratedBarcodeView bcv;
    TextView displayData;
    Book scannedBook;
    private BarcodeCallback callback = result -> {
        Log.e(TAG, "barcodeResult: " + result.getText());
        bcv.getBarcodeView().stopDecoding();
        displayData.setText(result.getText());
        bcv.setVisibility(View.GONE);
        fetchBookData(result.getText());
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        bcv = (DecoratedBarcodeView) findViewById(R.id.decorated_barcode_view);

        // Go through the XML file to know which component it refers to
        displayData = findViewById(R.id.qr_code_data);
        displayData.setText("Nothing scanned yet");

        // Add this charade for adding a back button on toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // This function is called when scan button is pressed
    public void startScan(View v) {

        if (bcv.getVisibility() == View.VISIBLE) {

            // Scanner already being shown, no need to init it again
            return;
        }

        initScanner();
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


    public void fetchBookData(String scannedId) {
        findViewById(R.id.progress_indicator).setVisibility(View.VISIBLE);
        RestApiHandler
            .getAPIService()
            .getBookData(new BookRequest(scannedId))
            .enqueue(new Callback<BookResponse>() {
                @Override
                public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                    findViewById(R.id.progress_indicator).setVisibility(View.GONE);
                    showBookCard(response.body().getBooks().get(0));
                }

                @Override
                public void onFailure(Call<BookResponse> call, Throwable t) {
                    snackIt("Failed to fetch book information");
                }
            });
    }


    public void showBookCard(Book book) {
        scannedBook = book;
        findViewById(R.id.card_root).setVisibility(View.VISIBLE);
        displayData.setText(book.getTitle());
        Glide
                .with(QRScanActivity.this)
                .load(book.getThumbnail())
                .into((ImageView) findViewById(R.id.book_thumb));
    }

    public void addToLibrary(View v) {
        // TODO
        if (StorageUtil.addBookToList(scannedBook, this)) {
            Toast.makeText(this, "Book added to your library!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("book", scannedBook.getTitle());
            setResult(REQUEST_CODE, intent);
            finish();

        } else {
            snackIt("This book already exists in your library!");
        }
    }

    public void snackIt(String message) {
        View v = findViewById(R.id.root_layout);
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
}