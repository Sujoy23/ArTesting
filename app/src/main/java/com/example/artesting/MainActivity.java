package com.example.artesting;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // object of ArFragment Class
    private ArFragment arCam;

    // helps to render the 3d model
    // only once when we tap the screen
    private int clickNo = 0;
    private TransformableNode currentModelNode;
    private Button whiteBtn;
    private Button yellowBtn;
    private Button greenBtn;
    private Button redBtn;
    private Button blueBtn;
    private Button blackBtn;
    private HitResult tempHitResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whiteBtn = (Button) findViewById(R.id.white);
        yellowBtn = (Button) findViewById(R.id.yellow);
        greenBtn = (Button) findViewById(R.id.green);
        redBtn = (Button) findViewById(R.id.red);
        blueBtn = (Button) findViewById(R.id.blue);
        blackBtn = (Button) findViewById(R.id.black);

        whiteBtn.setBackgroundColor(getResources().getColor(R.color.white));
        yellowBtn.setBackgroundColor(getResources().getColor(R.color.yellow));
        greenBtn.setBackgroundColor(getResources().getColor(R.color.green));
        redBtn.setBackgroundColor(getResources().getColor(R.color.red));
        blueBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        blackBtn.setBackgroundColor(getResources().getColor(R.color.black));

        whiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempHitResult == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Create a sphere on the screen first by clicking on the plane once it is created!").show();
                } else {
                    Anchor anchor = tempHitResult.createAnchor();
                    loadModel(R.raw.s_white, anchor);
                }
            }
        });
        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempHitResult == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Create a sphere on the screen first by clicking on the plane once it is created!").show();
                } else {
                    Anchor anchor = tempHitResult.createAnchor();
                    loadModel(R.raw.s_yellow, anchor);
                }
            }
        });
        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempHitResult == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Create a sphere on the screen first by clicking on the plane once it is created!").show();
                } else {
                    Anchor anchor = tempHitResult.createAnchor();
                    loadModel(R.raw.s_green, anchor);
                }
            }
        });
        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempHitResult == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Create a sphere on the screen first by clicking on the plane once it is created!").show();
                } else {
                    Anchor anchor = tempHitResult.createAnchor();
                    loadModel(R.raw.s_red, anchor);
                }
            }
        });
        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempHitResult == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Create a sphere on the screen first by clicking on the plane once it is created!").show();
                } else {
                    Anchor anchor = tempHitResult.createAnchor();
                    loadModel(R.raw.s_blue, anchor);
                }
            }
        });
        blackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempHitResult == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Create a sphere on the screen first by clicking on the plane once it is created!").show();
                } else {
                    Anchor anchor = tempHitResult.createAnchor();
                    loadModel(R.raw.s_black, anchor);
                }
            }
        });

        if (checkSystemSupport(this)) {

            // ArFragment is linked up with its respective id used in the activity_main.xml
            arCam = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arCameraArea);
            arCam.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                // the 3d model comes to the scene only
                // when clickNo is one that means once
                tempHitResult= hitResult;
                Anchor anchor = hitResult.createAnchor();
                loadModel(R.raw.s_white,anchor);
            });
        } else {
            return;
        }
    }

    private void loadModel(int modelResourceId, Anchor anchor) {
        ModelRenderable.builder()
                .setSource(this, modelResourceId)
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(modelRenderable -> {
                    // check if model is created or not if yes then update it or else create a new one.
                    if (currentModelNode != null) {
                        currentModelNode.setRenderable(modelRenderable);
                    } else {
                        addModel(anchor, modelRenderable);
                    }
                })
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Something went wrong!" + throwable.getMessage()).show();
                    return null;
                });
    }

    public static boolean checkSystemSupport(Activity activity) {

        // checking whether the API version of the running Android >= 24
        // that means Android Nougat 7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String openGlVersion = ((ActivityManager) Objects.requireNonNull(activity.getSystemService(Context.ACTIVITY_SERVICE))).getDeviceConfigurationInfo().getGlEsVersion();

            // checking whether the OpenGL version >= 3.0
            if (Double.parseDouble(openGlVersion) >= 3.0) {
                return true;
            } else {
                Toast.makeText(activity, "App needs OpenGl Version 3.0 or later", Toast.LENGTH_SHORT).show();
                activity.finish();
                return false;
            }
        } else {
            Toast.makeText(activity, "App does not support required Build Version", Toast.LENGTH_SHORT).show();
            activity.finish();
            return false;
        }
    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {

        // Creating a AnchorNode with a specific anchor
        AnchorNode anchorNode = new AnchorNode(anchor);

        // attaching the anchorNode with the ArFragment
        anchorNode.setParent(arCam.getArSceneView().getScene());

        // attaching the anchorNode with the TransformableNode
        TransformableNode model = new TransformableNode(arCam.getTransformationSystem());
        model.setParent(anchorNode);

        // attaching the 3d model with the TransformableNode
        // that is already attached with the node
        model.setRenderable(modelRenderable);
        model.select();

        currentModelNode = model; // set the created model as current model to modify the color
    }
}
