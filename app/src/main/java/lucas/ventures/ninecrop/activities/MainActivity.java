package lucas.ventures.ninecrop.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;

import lucas.ventures.ninecrop.R;
import lucas.ventures.ninecrop.cropper.CropEngine;
import lucas.ventures.ninecrop.cropper.CropImage;
import lucas.ventures.ninecrop.cropper.CropImageView;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "CropEngine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(getResources().getColor(R.color.gray));
        setContentView(R.layout.main);
        findViewById(R.id.openGallery).setOnClickListener(this);
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //remove cache just in case
        CropEngine.getCachedImages().clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openGallery:
                if (isStoragePermissionGranted()) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.THREE_BY_THREE)
                            .start(this);
                }
                break;
            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.connect:
                Uri link = Uri.parse("http://instagram.com/_u/9croppro");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, link);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/9croppro")));
                }
                break;

        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            findViewById(R.id.openGallery).callOnClick();
        } else if (grantResults[0] == -1) {
            Toast.makeText(this, "Please enable Storage permissions in application settings to continue.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (data == null) {
                //cancelled out of crop process
                return;
            }

            final int cropType = data.getIntExtra("type", 0);
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
            } else {
                Toast.makeText(this, "An error has occurred. value type 0", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show();
        }
    }

}