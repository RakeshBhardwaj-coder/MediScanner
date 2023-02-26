package photon.app.mediscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    FloatingActionButton cameraFabBtn;
    static ImageView camImage;
    public static Uri imageUri;

    TextView firstText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraFabBtn = findViewById(R.id.amFabBtn);
        camImage = findViewById(R.id.amImageView);
//        camImage.setVisibility(View.GONE);
        firstText = findViewById(R.id.amFirstText);

        cameraFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CameraActivity.class));
            }
        });
        if(imageUri !=null){
            firstText.setVisibility(View.GONE);
            camImage.setVisibility(View.VISIBLE);
            camImage.setImageURI(imageUri);
        }
        else{
            camImage.setVisibility(View.GONE);
            firstText.setVisibility(View.VISIBLE);

        }




    }
    public static Uri getImage(Uri savedUri){
        imageUri = savedUri;
        return imageUri;

    }


}