package com.joblesscoders.arbook.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.ArFragment;
import com.joblesscoders.arbook.R;
import com.joblesscoders.arbook.pojo.Book;
import com.joblesscoders.arbook.pojo.Contents;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AugmentedImagesActivity extends AppCompatActivity {
    private List<Contents> contents;
    private ArFragment arFragment;
    private ImageView fitToScanView;
    private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        Book book= getIntent().getParcelableExtra("book");
        contents = book.getContents();
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        fitToScanView = findViewById(R.id.image_view_fit_to_scan);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (augmentedImageMap.isEmpty()) {
            fitToScanView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        arFragment.onPause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        arFragment.onDestroy();
        super.onDestroy();
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        // If there is no frame or ARCore is not tracking yet, just return.
        if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
            return;
        }

        Collection<AugmentedImage> updatedAugmentedImages =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image " + augmentedImage.getName();
                   // Toast.makeText(this, text+"", Toast.LENGTH_SHORT).show();
                    break;

                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    fitToScanView.setVisibility(View.GONE);

                    // Create a new anchor for newly found images.
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        String name = augmentedImage.getName().toLowerCase().substring(0,augmentedImage.getName().lastIndexOf('.'));
                        //Toast.makeText(this, name+"", Toast.LENGTH_SHORT).show();
                        Log.e("hello",name);

                        AugmentedImageNode node = new AugmentedImageNode(this,arFragment, name+".sfb");
                        Log.e("model","image found");
                        node.setImage(augmentedImage);
                        augmentedImageMap.put(augmentedImage, node);
                        arFragment.getArSceneView().getScene().addChild(node);
                    }
                    break;

                case STOPPED:
                    augmentedImageMap.remove(augmentedImage);
                    break;
            }
        }

    }
}