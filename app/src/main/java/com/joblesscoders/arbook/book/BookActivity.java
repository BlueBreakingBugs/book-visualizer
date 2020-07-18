package com.joblesscoders.arbook.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.ar.AugmentedImagesActivity;
import com.joblesscoders.arbook.pojo.Book;
import com.joblesscoders.arbook.pojo.Contents;

import java.util.List;

public class BookActivity extends AppCompatActivity {
    private Book book;
    private RecyclerView recyclerView;
    private List<Contents> contentsList;
    private RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        book = (Book) getIntent()
            .getExtras()
            .get("book");

        if (book == null) {
         Toast.makeText(this, "Unrecognized book!", Toast.LENGTH_SHORT).show();
         finish();
        }

        contentsList = book.getContents();
        contentsList.addAll(book.getContents());
        initToolbar();
        initRecyclerView();
    }

    public void initToolbar() {
        findViewById(R.id.toolbar);

        // Add this charade for adding a back button on toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // And this for title
        getSupportActionBar().setTitle(book.getTitle());
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(contentsList, getApplicationContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void startARSession(View view) {
        Intent intent = new Intent(this, AugmentedImagesActivity.class);
        intent.putExtra("book",book);
        startActivity(intent);
    }
}