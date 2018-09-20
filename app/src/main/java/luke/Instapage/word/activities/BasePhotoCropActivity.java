package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import luke.Instapage.word.CropHandler;
import luke.Instapage.word.CropHelper;
import luke.Instapage.word.CropParams;


public class BasePhotoCropActivity extends Activity implements CropHandler {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
    }

    @Override
    public void onCropCancel() {
    }

    @Override
    public void onCropFailed(String message) {
    }

    @Override
    public CropParams getCropParams() {
        return null;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (getCropParams() != null)
            CropHelper.clearCachedCropFile(getCropParams().uri);
        super.onDestroy();
    }
}
