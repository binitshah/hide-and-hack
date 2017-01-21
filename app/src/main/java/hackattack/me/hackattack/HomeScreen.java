package hackattack.me.hackattack;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    public Context context;
    public Activity activity;
    public FrameLayout preview;
    Intent openGame;
//    public Button joinbutton;
//    public EditText serverCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        openGame = new Intent(this, MainGameActivity.class);
        context = this;
        activity = this;

        preview = (FrameLayout) findViewById(R.id.camera_preview);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);

        Button hostbutton = (Button) findViewById(R.id.hostbutton);
        Button joinbutton = (Button) findViewById(R.id.joinbutton);
        //serverCode = (EditText) findViewById(R.id.serverCode);
        //serverCode.setVisibility(View.INVISIBLE);

        hostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                    .setTitle("Host")
                    .setMessage("Server code: ASDF")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
//                            releaseCamera();
                            startActivity(openGame);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            }
        });

        joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                serverCode.setVisibility(View.VISIBLE);
//                joinbutton.setVisibility(View.INVISIBLE);


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = activity.getLayoutInflater();

                builder.setView(inflater.inflate(R.layout.dialog_join, null))
                        // Add action buttons
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
//                                releaseCamera();
                                startActivity(openGame);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e("Camera error", "Could not open camera");
        }
        return c; // returns null if camera is unavailable
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        releaseCamera();
    }
    public void releaseCamera(){
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);

            mCamera.release();
            mCamera = null;
        }
    }
}
