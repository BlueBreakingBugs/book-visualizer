package com.joblesscoders.arbook.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.arscene.ARSceneActivity;
import com.joblesscoders.arbook.pojo.Contents;

public class DetailsActivity extends AppCompatActivity {
    private Contents content;
    private Scene mScene;
    private ModelRenderable modelRenderable;
    private Session mSession;
    private SceneView mSceneView;
    private RotatingNode node;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mSceneView = findViewById(R.id.scene_view);
        content = (Contents) getIntent().getExtras().get("content");
        if (content == null) {
            // shit
            finish();
        }

        initToolbar();
        initSceneView();

        inflateViewData();
    }

    private void initSceneView() {
        Camera camera = mSceneView.getScene().getCamera();
        camera.setLocalRotation(Quaternion.axisAngle(Vector3.right(), -30.0f));
        mScene = mSceneView.getScene();
        load3dModel();


    }
    private void load3dModel() {
       // progressDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable.builder()
                    .setSource(this, Uri.parse(content.getTitle().toLowerCase()+".sfb"))
                    .build()
                    .thenAccept(renderable ->{
                        addToScene(renderable);
                       // progressDialog.dismiss();
                        //loading_image.setVisibility(View.GONE);

                    })
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load any renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });
        }
    }

    private void addToScene(ModelRenderable renderable) {
        node = new RotatingNode(true, false, 0);
        node.setRenderable(renderable);
        node.setParent(mScene);
      //  node.setWorldScale(new Vector3(0.6f, 0.6f, 0.6f));
        node.setLocalPosition(new Vector3(0f, -0.5f, -.5f));
        mScene.addChild(node);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            mSceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSceneView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSceneView.destroy();
    }


    public void inflateViewData() {
        ((TextView)findViewById(R.id.title)).setText(content.getTitle());
        ((TextView)findViewById(R.id.description)).setText(content.getDescription());
/*
        Glide
            .with(this)
            .load(content.getThumbnail())
            .into((ImageView) findViewById(R.id.content_thumb));*/
    }
    public void initToolbar() {
        findViewById(R.id.toolbar);

        // Add this charade for adding a back button on toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // And this for title
        getSupportActionBar().setTitle("Details");
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