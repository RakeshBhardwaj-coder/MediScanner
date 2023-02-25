package photon.app.mediscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CheckRecycleView extends AppCompatActivity {



    private final String DIRECTORY = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_recycle_view);

//        List<String> images = new ArrayList<>();
//        String[] projection = {MediaStore.Images.Media.DATA};
//
//        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//                images.add(imagePath);
//            }
//            cursor.close();
//        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.acrvRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        List<String> mFiles =  // media file or
//        List<String> mFiles = FileUtil.findImageFileInDirectory(DIRECTORY, new String[]{"png", "jpg"}); // device file
        recyclerView.setAdapter(new GalleryAdapter(this, mFiles));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Read Permission Accepted.",Toast.LENGTH_SHORT).show();
                loadImages();
            }else {
                Toast.makeText(getApplicationContext(),"Read Permission Denied.",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        images = ImageGallery.listOfImage(this);
        myAdapter = new MyAdapter(this, images, new MyAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                Toast.makeText(getApplicationContext(),"" +path,Toast.LENGTH_SHORT).show();

            }
        });
        recyclerView.setAdapter(myAdapter);
        
    }
}