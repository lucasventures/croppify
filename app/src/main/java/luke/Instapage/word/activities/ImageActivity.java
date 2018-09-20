package luke.Instapage.word.activities;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import luke.Instapage.word.CropHelper;
import luke.Instapage.word.CropParams;
import luke.Instapage.word.R;


public class ImageActivity extends BasePhotoCropActivity implements View.OnClickListener {

    public static final String TAG = "TestActivity";

    ImageView mImageView;
    CropParams mCropParams = new CropParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);


        findViewById(R.id.takePhoto).setOnClickListener(this);
        findViewById(R.id.openGallery).setOnClickListener(this);
        findViewById(R.id.extras).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.takePhoto:
                Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
                startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                break;

            case R.id.openGallery:
                startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);
                break;

            case R.id.extras:
                String merp = null;
                Intent intent3 = new Intent(ImageActivity.this, ResultDisplayActivity.class);
                intent3.putExtra("extra", merp);
                startActivity(intent3);

                Intent intent2 = new Intent(this, FinishedActivity.class);
                this.startActivity(intent2);
                this.finishActivity(0);
                break;
        }
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public void onPhotoCropped(Uri uri) {

        Log.d(TAG, "Crop Uri in path: " + uri.getPath());
        Toast.makeText(this, "Use this photo?", Toast.LENGTH_LONG).show();

        MediaScannerConnection.scanFile(this, new String[]{uri.getPath()}, new String[]{"image/png"}, null);
        // send uri to next activity woot woot 
        uriData();
    }

    private void uriData() {
        Intent intent2 = new Intent(ImageActivity.this, ResultDisplayActivity.class);
        intent2.putExtra("imageUri", mCropParams.uri);
        startActivity(intent2);
    }

    @Override
    public void onCropCancel() {
        Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCropFailed(String message) {
        Toast.makeText(this, "Crop failed:" + message, Toast.LENGTH_LONG).show();
    }
}
