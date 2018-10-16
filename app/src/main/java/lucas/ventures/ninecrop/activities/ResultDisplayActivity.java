package lucas.ventures.ninecrop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import lucas.ventures.ninecrop.R;
import lucas.ventures.ninecrop.cropper.CropEngine;


public class ResultDisplayActivity extends AppCompatActivity implements OnClickListener {

    private ImageView one, two, three, four, five, six, seven, eight, nine;
    private ArrayList<ImageView> imageViews;

    private int postNumber;
    private boolean triggeredByInstagramIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        one = findViewById(R.id.oneone);
        two = findViewById(R.id.onetwo);
        three = findViewById(R.id.onethree);

        four = findViewById(R.id.twoone);
        five = findViewById(R.id.twotwo);
        six = findViewById(R.id.twothree);

        seven = findViewById(R.id.threeone);
        eight = findViewById(R.id.threetwo);
        nine = findViewById(R.id.threethree);

        postNumber = CropEngine.getCachedImages().size();

        imageViews = new ArrayList<>();
        imageViews.add(one);
        imageViews.add(two);
        imageViews.add(three);
        imageViews.add(four);
        imageViews.add(five);
        imageViews.add(six);
        imageViews.add(seven);
        imageViews.add(eight);
        imageViews.add(nine);

        setImages(imageViews);

        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.post).setOnClickListener(this);
    }

    private void setImages(ArrayList<ImageView> imageViews) {
        ArrayList<Bitmap> images = CropEngine.getCachedImages();
        for (int i = 0; i < images.size(); i++) {
            imageViews.get(i).setImageBitmap(images.get(i));
        }
        postNumber = images.size() - 1;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.post) {
            PackageManager pm = getPackageManager();
            boolean isInstalled = isPackageInstalled("com.instagram.android", pm);
            if (isInstalled) {
                if (postNumber >= 0) {
                    createInstagramIntent(CropEngine.getCachedImages().get(postNumber));
                } else {
                    //create dialog for rating app as well as finally giving an option to save.

                    //new CompletionDialog().show();
                }
            } else {
                Toast.makeText(this, "Please install Instagram before proceeding.", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.save) {
            CropEngine.saveAllImages(new WeakReference<Context>(this));
        }
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //increment number for post
        if (triggeredByInstagramIntent) {
            if (postNumber >= 0) {
                imageViews.get(postNumber).setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.OVERLAY);
                postNumber--;
                triggeredByInstagramIntent = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when the application is interrupted, save the values of the current page in case there is an issue? not this release, but the next :]
    }

    String type = "image/*";

    private void createInstagramIntent(Bitmap bmp) {

        String path = CropEngine.saveImageToDisk(new WeakReference<Context>(this), bmp, postNumber, false);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.instagram.android");
        try {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), path, "", "")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        shareIntent.setType("image/jpeg");
        triggeredByInstagramIntent = true;
        startActivity(shareIntent);
    }

    @Override
    public void onBackPressed() {
        CropEngine.getCachedImages().clear();
        super.onBackPressed();
    }
}
	


	
	
	
			

	


		
		
		
	



