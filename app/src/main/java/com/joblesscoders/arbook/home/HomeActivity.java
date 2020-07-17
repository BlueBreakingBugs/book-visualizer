package com.joblesscoders.arbook.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.pojo.Book;
import com.joblesscoders.arbook.pojo.BookRequest;
import com.joblesscoders.arbook.pojo.BookResponse;
import com.joblesscoders.arbook.qrscan.QRScanActivity;
import com.joblesscoders.arbook.utils.RestApiHandler;
import com.joblesscoders.arbook.utils.StorageUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private final int ADD_BOOK_REQUEST_CODE = 2;
    private  List<String> idList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressBar = findViewById(R.id.progress_bar);
        initRecyclerView();

        fetchBookData();
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(bookList, getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    public ArrayList<String> getRegisteredBooks() {
        // load list of books from SharedPreference
        return StorageUtil.getBookList(this);
    }

    public void fetchBookData() {
        // load the list of books registered by the user
        idList = StorageUtil.getBookList(this);

        progressBar.setVisibility(View.VISIBLE);
        // call api and get details of these books
        RestApiHandler.getAPIService().getBookData(new BookRequest(idList)).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                BookResponse bookResponse = response.body();
                progressBar.setVisibility(View.GONE);
                if(response.body().getStatusCode() == 200)
                {
                    bookList.clear();
                    bookList.addAll(bookResponse.getBooks());
                    //bookList.addAll(bookResponse.getBooks());
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goToScannerActivity(View v) {
        // go to qr code scanner activity to add new book

        // Intent object takes reference of current activity, and class of the destination activity
        Intent intent = new Intent(this, QRScanActivity.class);

        // Start calling startActivity with the intent loads the new activity on top of current stack
        startActivityForResult(intent, ADD_BOOK_REQUEST_CODE);

        // also in case you need to close an activity, you can call finish()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BOOK_REQUEST_CODE) {
            Log.e("Home", resultCode + "");
            if (resultCode == ADD_BOOK_REQUEST_CODE) {
                fetchBookData();
            }
        }
    }
}
