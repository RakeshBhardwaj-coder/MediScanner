package photon.app.mediscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;

public class PickImageContract extends ActivityResultContract<Void, Uri> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        // Create an Intent to start the image picker activity
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    @Override
    public Uri parseResult(int resultCode, @NonNull Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return null;
        }
        return intent.getData();
    }
}
