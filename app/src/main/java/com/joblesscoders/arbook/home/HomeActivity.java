package com.joblesscoders.arbook.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.pojo.Book;
import com.joblesscoders.arbook.pojo.BookRequest;
import com.joblesscoders.arbook.pojo.BookResponse;
import com.joblesscoders.arbook.qrscan.QRScanActivity;
import com.joblesscoders.arbook.utils.RestApiHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private  List<String> idList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter =new RecyclerViewAdapter(bookList,getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

        idList.add("5f1084224b57b3428b42a961");
        RestApiHandler.getAPIService().getBookData(new BookRequest(idList)).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                BookResponse bookResponse = response.body();

                if(response.body().getStatusCode() == 200)
                {
                 bookList.addAll(bookResponse.getBooks());
                 //bookList.addAll(bookResponse.getBooks());
                 recyclerViewAdapter.notifyDataSetChanged();
                 //Toast.makeText(HomeActivity.this, bookList.get(0).getTitle()+"", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
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
