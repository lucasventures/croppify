package luke.Instapage.word.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.ref.WeakReference;

import luke.Instapage.word.R;
import luke.Instapage.word.crop.CropEngine;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "CropEngine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        findViewById(R.id.openGallery).setOnClickListener(this);
        //remove cache just in case
        CropEngine.getCachedImages().clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.openGallery:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.THREE_BY_THREE)
                        .start(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);

            final int cropType = data.getIntExtra("type", 1);
            if (resultCode == RESULT_OK && cropType != 0) {

                final Handler handler = new Handler();

                final WeakReference<Context> contextWeakReference = new WeakReference<Context>(this);

                final AlertDialog dialog = new AlertDialog.Builder(contextWeakReference.get())
                        .setView(R.layout.loading)
                        .setCancelable(false)
                        .create();
                dialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean successful = CropEngine
                                .createCroppedImagesWithParams(contextWeakReference, result.getUri().toString(), cropType);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (successful) {
                                    final Intent intent = new Intent(MainActivity.this, ResultDisplayActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                }).start();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e(TAG, "onActivityResult: CROPPING ERROR");
                Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show();
        }
    }

}