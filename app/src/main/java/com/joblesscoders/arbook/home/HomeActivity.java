package com.joblesscoders.arbook.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.qrscan.QRScanActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fetchBookData();
    }

    public String[] getRegisteredBooks() {
        // load list of books from SharedPreference
        // TODO, parse the data from SharedPreference
        return null;
    }
    public void fetchBookData() {
        // load the list of books registered by the user
        String books[] = getRegisteredBooks();

        // call api and get details of these books
        // TODO, do it.
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchBookData();
    }

    public void goToScannerActivity(View v) {
        // go to qr code scanner activity to add new book

        // Intent object takes reference of current activity, and class of the destination activity
        Intent intent = new Intent(this, QRScanActivity.class);

        // Start calling startActivity with the intent loads the new activity on top of current stack
        startActivity(intent);

        // also in case you need to close an activity, you can call finish()
    }
}
