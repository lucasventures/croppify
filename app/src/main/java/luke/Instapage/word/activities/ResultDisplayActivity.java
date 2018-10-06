package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import luke.Instapage.word.R;
import luke.Instapage.word.crop.CropEngine;


public class ResultDisplayActivity extends Activity implements OnClickListener {

    private ImageView one, two, three, four, five, six, seven, eight, nine;
    private ArrayList<ImageView> imageViews;

    private int postNumber;
    private boolean triggeredByInstagramIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        one = findViewById(R.id.oneone);
        two = findViewById(R.id.onetwo);
        three = findViewById(R.id.onethree);

        four = findViewById(R.id.twoone);
        five = findViewById(R.id.twotwo);
        six = findViewById(R.id.twothree);

        seven = findViewById(R.id.threeone);
        eight = findViewById(R.id.threetwo);
        nine = findViewById(R.id.threethree);

        postNumber = 0;

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
        if (postNumber >= 0) {
            createInstagramIntent(type, CropEngine.getCachedImages().get(postNumber));
        } else {
            //create dialog for rating app as well as finally giving an option to save.
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //increment number for post
        if (triggeredByInstagramIntent) {
            if (postNumber >= 0) {
                imageViews.get(postNumber).setColorFilter(ContextCompat.getColor(this, R.color.lblue), PorterDuff.Mode.OVERLAY);
                postNumber--;
                triggeredByInstagramIntent = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //when the application is interrupted, save the values of the current page in case there is an issue? not this release, but the next :)
//        if()
    }

    String type = "image/*";

    private void createInstagramIntent(String type, Bitmap bmp) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);
        String path = CropEngine.saveImageToDisk(new WeakReference<Context>(this), bmp, postNumber);

        // Create the URI from the media
        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".luke.instapage.word", new File(path));

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, photoURI);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        triggeredByInstagramIntent = true;
        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
}
	


	
	
	
			

	


		
		
		
	



