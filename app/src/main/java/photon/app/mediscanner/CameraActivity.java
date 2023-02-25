package photon.app.mediscanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.internal.annotation.CameraExecutor;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import kotlinx.coroutines.Dispatchers;

public class CameraActivity extends AppCompatActivity implements LifecycleOwner {

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = { Manifest.permission.CAMERA };

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;

    ProcessCameraProvider processCameraProvider;

    private FloatingActionButton fabBtnImageCapture;


    ImageView torchIconOff,torchIconOn;

    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.acPreviewView);
        fabBtnImageCapture = findViewById(R.id.acCaptureImageBtn);

        torchIconOff = findViewById(R.id.acTorchIconOff);
        torchIconOn = findViewById(R.id.acTorchIconOn);

        torchIconOff.setVisibility(View.VISIBLE);
        torchIconOn.setVisibility(View.GONE);

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }


        torchIconOff.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"jhgjhg",Toast.LENGTH_SHORT).show();

                    }
                }
        );
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

        // For performing operations that affect all outputs.
        CameraControl cameraControl = camera.getCameraControl();
// For querying information and states.
        CameraInfo cameraInfo = camera.getCameraInfo();
//        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.getCameraInfo()));
        preview.setSurfaceProvider(previewView.createSurfaceProvider(cameraInfo));


        torchIconOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cameraInfo.hasFlashUnit()){
                    cameraControl.enableTorch(true);
                    torchIconOff.setVisibility(View.GONE);
                    torchIconOn.setVisibility(View.VISIBLE);

                }

            }
        });

        torchIconOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraControl.enableTorch(false);
                torchIconOff.setVisibility(View.VISIBLE);
                torchIconOn.setVisibility(View.GONE);

            }
        });

        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview);
      
        fabBtnImageCapture.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
               capturePhoto();
            }
        });






    }

    private void startCamera() {
        previewView.post(() -> {
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }, ContextCompat.getMainExecutor(this));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void capturePhoto(){
        long timeStamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build(), getMainExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(getApplicationContext(),"Saving...",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(getApplicationContext(),"Error:"+exception.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}