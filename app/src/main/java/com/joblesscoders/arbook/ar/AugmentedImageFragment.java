package com.joblesscoders.arbook.ar;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;
import java.io.IOException;
import java.io.InputStream;

/**
 * Extend the ArFragment to customize the ARCore session configuration to include Augmented Images.
 */
public class AugmentedImageFragment extends ArFragment {
  private static final String TAG = "AugmentedImageFragment";

  // This is the name of the image in the sample database.  A copy of the image is in the assets
  // directory.  Opening this image on your computer is a good quick way to test the augmented image
  // matching.
  private static final String DEFAULT_IMAGE_NAME = "default.jpg";

  // This is a pre-created database containing the sample image.
  private  String IMAGE_DATABASE = "database.imgdb" ;
  //private  String HEART_DATABASE = "heartdb.imgdb" ;

  // Augmented image configuration and rendering.
  // Load a single image (true) or a pre-generated image database (false).
  private static final boolean USE_SINGLE_IMAGE = false;

  // Do a runtime check for the OpenGL level available at runtime to avoid Sceneform crashing the
  // application.
  private static final double MIN_OPENGL_VERSION = 3.0;


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // Check for Sceneform being supported on this device.  This check will be integrated into
    // Sceneform eventually.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      Log.e(TAG, "Sceneform requires Android N or later");
      Toast.makeText(context, "Sceneform requires Android N or later", Toast.LENGTH_SHORT).show();
    }

    String openGlVersionString =
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
            .getDeviceConfigurationInfo()
            .getGlEsVersion();
    if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
      Log.e(TAG, "Sceneform requires OpenGL ES 3.0 or later");
      Toast.makeText(context, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public View onCreateView(
          LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    // Turn off the plane discovery since we're only looking for images
    getPlaneDiscoveryController().hide();
    getPlaneDiscoveryController().setInstructionView(null);
    getArSceneView().getPlaneRenderer().setEnabled(false);
    return view;
  }

  @Override
  protected Config getSessionConfiguration(Session session) {
   // Config config = super.getSessionConfiguration(session);

    /*
    config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
    config.setFocusMode(Config.FocusMode.AUTO);
    config.setDepthMode(Config.DepthMode.AUTOMATIC);
    session.configure(config);
    this.getArSceneView().setupSession(session);
     */
    //getPlaneDiscoveryController().setInstructionView(null);
    Config config = new Config(session);
    config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
    session.configure(config);
    getArSceneView().setupSession(session);

    if (!setupAugmentedImageDatabase(config, session)) {
      //TODO : replace with snackbar
      Toast.makeText(getContext(), "Could not setup augmented image database", Toast.LENGTH_SHORT).show();
    }
    return config;
  }

  private boolean setupAugmentedImageDatabase(Config config, Session session) {
    AugmentedImageDatabase augmentedImageDatabase;

    AssetManager assetManager = getContext() != null ? getContext().getAssets() : null;
    if (assetManager == null) {
      Log.e(TAG, "Context is null, cannot intitialize image database.");
      return false;
    }

    // There are two ways to configure an AugmentedImageDatabase:
    // 1. Add Bitmap to DB directly
    // 2. Load a pre-built AugmentedImageDatabase
    // Option 2) has
    // * shorter setup time
    // * doesn't require images to be packaged in apk.
   /* if (USE_SINGLE_IMAGE) {
      Bitmap augmentedImageBitmap = loadAugmentedImageBitmap(assetManager);
      if (augmentedImageBitmap == null) {
        return false;
      }

      augmentedImageDatabase = new AugmentedImageDatabase(session);
      augmentedImageDatabase.addImage(DEFAULT_IMAGE_NAME, augmentedImageBitmap);
      // If the physical size of the image is known, you can instead use:
      //     augmentedImageDatabase.addImage("image_name", augmentedImageBitmap, widthInMeters);
      // This will improve the initial detection speed. ARCore will still actively estimate the
      // physical size of the image as it is viewed from multiple viewpoints.
    } else*/
      // This is an alternative way to initialize an AugmentedImageDatabase instance,
      // load a pre-existing augmented image database.
      try (InputStream is = getContext().getAssets().open(IMAGE_DATABASE)) {
        augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, is);
      } catch (IOException e) {
        Log.e(TAG, "IO exception loading augmented image database.", e);
        return false;
      }


    config.setAugmentedImageDatabase(augmentedImageDatabase);
    return true;
  }

  private Bitmap loadAugmentedImageBitmap(AssetManager assetManager) {
    try (InputStream is = assetManager.open(DEFAULT_IMAGE_NAME)) {
      return BitmapFactory.decodeStream(is);
    } catch (IOException e) {
      Log.e(TAG, "IO exception loading augmented image bitmap.", e);
    }
    return null;
  }
}
