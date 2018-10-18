package lucas.ventures.ninecrop.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
    private boolean rated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        rated = hasRated();

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
                if (postNumber == 1 && !rated) {
                    //This is the last post!
                    new AlertDialog.Builder(this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("Hope you are enjoying 9Crop! Please take a moment to rate the application, it'd really mean a lot to hear your feedback. Thanks, and best wishes! -@LucasVentures")
                            .setPositiveButton("Rate :)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //take off to rate, preserve place in post order
                                    rated = true;
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ResultDisplayActivity.this);
                                    preferences.edit().putBoolean("hasRated", true).apply();
                                    dialog.dismiss();
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
                                }
                            })
                            .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    createInstagramIntent(CropEngine.getCachedImages().get(postNumber));
                                }
                            })
                            .create()
                            .show();
                } else {
                    if (postNumber >= 0) {
                        createInstagramIntent(CropEngine.getCachedImages().get(postNumber));
                    }
                }

            } else {
                Toast.makeText(this, "Please install Instagram before proceeding.", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.save) {
            CropEngine.saveAllImages(new WeakReference<Context>(this));
        }

    }

    private boolean hasRated() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("hasRated", false);
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
	


	
	
	
			

	


		
		
		
	



