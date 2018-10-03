package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        ImageView one = findViewById(R.id.oneone);
        ImageView two = findViewById(R.id.onetwo);
        ImageView three = findViewById(R.id.onethree);

        ImageView four = findViewById(R.id.twoone);
        ImageView five = findViewById(R.id.twotwo);
        ImageView six = findViewById(R.id.twothree);

        ImageView seven = findViewById(R.id.threeone);
        ImageView eight = findViewById(R.id.threetwo);
        ImageView nine = findViewById(R.id.threethree);

        ImageView[] imageViews = {
                one, two, three, four, five, six, seven, eight, nine
        };

        setImages(imageViews);

        findViewById(R.id.button4).setOnClickListener(this);
    }

    private void setImages(ImageView[] imageViews) {
        ArrayList<Bitmap> images = CropEngine.getCachedImages();
        for (int i = 0; i < images.size(); i++) {
            imageViews[i].setImageBitmap(images.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        createInstagramIntent(type, CropEngine.getCachedImages().get(0));
    }


    String type = "image/*";

    private void createInstagramIntent(String type, Bitmap bmp) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);
        String path = CropEngine.saveImageToDisk(new WeakReference<Context>(this), bmp, 0);

        // Create the URI from the media
        File media = new File(path);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
// create a content provider for instagram package name 
}
	


	
	
	
			

	


		
		
		
	



