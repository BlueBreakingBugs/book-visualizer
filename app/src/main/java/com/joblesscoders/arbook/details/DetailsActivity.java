package com.joblesscoders.arbook.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.ar.ARActivity;
import com.joblesscoders.arbook.arscene.ARSceneActivity;
import com.joblesscoders.arbook.pojo.Contents;

public class DetailsActivity extends AppCompatActivity {
    private Contents content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        content = (Contents) getIntent().getExtras().get("content");
        if (content == null) {
            // shit
            finish();
        }

        initToolbar();

        inflateViewData();
    }

    public void inflateViewData() {
        ((TextView)findViewById(R.id.title)).setText(content.getTitle());
        ((TextView)findViewById(R.id.description)).setText(content.getDescription());

        Glide
            .with(this)
            .load(content.getThumbnail())
            .into((ImageView) findViewById(R.id.content_thumb));
    }
    public void initToolbar() {
        findViewById(R.id.toolbar);

        // Add this charade for adding a back button on toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // And this for title
        getSupportActionBar().setTitle(content.getTitle());
    }

    public void startARSession(View view) {
        Intent intent = new Intent(this, ARSceneActivity.class);
       // intent.addFlags("")
        intent.putExtra("content",content);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}