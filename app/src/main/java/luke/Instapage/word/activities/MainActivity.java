package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import luke.Instapage.word.CropParams;
import luke.Instapage.word.R;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "CropEngine";
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

            case R.id.openGallery:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;

            case R.id.extras:

                Intent intent2 = new Intent(this, FinishedActivity.class);
                this.startActivity(intent2);
                this.finishActivity(0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //this is the cropped image, so I'm gonna have to do some funkiness with the image cropping
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
