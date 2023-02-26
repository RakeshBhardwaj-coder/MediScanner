package photon.app.mediscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    FloatingActionButton cameraFabBtn;
    static ImageView camImage;
    public static Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraFabBtn = findViewById(R.id.amFabBtn);
        camImage = findViewById(R.id.amImageView);

        cameraFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CameraActivity.class));
            }
        });
        if(imageUri !=null)
            camImage.setImageURI(imageUri);




    }
    public static Uri getImage(Uri savedUri){
        imageUri = savedUri;

        return imageUri;

    }


}