package photon.app.mediscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

public class ScanningActivity extends AppCompatActivity  {


    Task<Text> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        ImageView imageView = findViewById(R.id.asImageView);
        TextView textView = findViewById(R.id.asTextView);
        TextView textView2 = findViewById(R.id.asTextView2);
        TextView textView3 = findViewById(R.id.asTextView3);

        View myView = findViewById(R.id.myView);


        TextRecognizer recognizer = TextRecognition.getClient();

        InputImage image;

        image = InputImage.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.text),0);
        result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    // ...

                                    Toast.makeText(getApplicationContext(),"Process Successfully.",Toast.LENGTH_SHORT).show();
                                    for (Text.TextBlock block : result.getResult().getTextBlocks()) {

                                        String blockText = block.getText();
                                        textView.setText(blockText);
                                        Point[] blockCornerPoints = block.getCornerPoints();
                                        Rect blockFrame = block.getBoundingBox();
                                        for (Text.Line line : block.getLines()) {
                                            String lineText = line.getText();
                                            Point[] lineCornerPoints = line.getCornerPoints();
                                            textView2.setText(lineText);
                                            Rect lineFrame = line.getBoundingBox();
                                            for (Text.Element element : line.getElements()) {
                                                String elementText = element.getText();
                                                textView3.setText(elementText);
                                                Point[] elementCornerPoints = element.getCornerPoints();
                                                Rect elementFrame = element.getBoundingBox();

                                            }
                                        }
                                    }

                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Toast.makeText(getApplicationContext(),"Process UnSuccessfully "+e,Toast.LENGTH_SHORT).show();
                                        }
                                    });

//        String resultText = result.getResult().getText();



//        try {
//            image = InputImage.fromFilePath(this, Uri.parse("drawable/text.jpg"));
//
//            Task<Text> result =
//                    recognizer.process(image)
//                            .addOnSuccessListener(new OnSuccessListener<Text>() {
//                                @Override
//                                public void onSuccess(Text visionText) {
//                                    // Task completed successfully
//                                    // ...
//
//                                    Toast.makeText(getApplicationContext(),"Process Successfully.",Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(
//                                    new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Task failed with an exception
//                                            // ...
//                                            Toast.makeText(getApplicationContext(),"Process UnSuccessfully "+e,Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//
//                    String resultText = String.valueOf(result.getResult());
//            Toast.makeText(getApplicationContext(),"Process UnSuccessfully "+resultText,Toast.LENGTH_SHORT).show();
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Error to get image:  "+e,Toast.LENGTH_SHORT).show();
//
//        }
    }
}

@ExperimentalGetImage
class YourAnalyzer implements ImageAnalysis.Analyzer {

    @Override
    public void analyze(ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            // Pass image to an ML Kit Vision API
            // ...
        }
    }
}