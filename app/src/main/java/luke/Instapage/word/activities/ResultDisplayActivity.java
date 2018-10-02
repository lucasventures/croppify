package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import luke.Instapage.word.CropHelper;
import luke.Instapage.word.R;


public class ResultDisplayActivity extends Activity implements OnClickListener {

    private ImageView one;
    private ImageView two;
    private ImageView three;
    private ImageView four;
    private ImageView five;
    private ImageView six;
    private ImageView seven;
    private ImageView eight;
    private ImageView nine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        ArrayList<String> paths = getIntent().getExtras().getStringArrayList("paths");

        if (paths != null && !paths.isEmpty()) {

            one = findViewById(R.id.oneone);
            two = findViewById(R.id.onetwo);
            three = findViewById(R.id.onethree);

            four = findViewById(R.id.twoone);
            five = findViewById(R.id.twotwo);
            six = findViewById(R.id.twothree);

            seven = findViewById(R.id.threeone);
            eight = findViewById(R.id.threetwo);
            nine = findViewById(R.id.threethree);
//
//            if (paths.size() == 3) {
//                grid.findViewById(R.id.rowtwo).setVisibility(View.GONE);
//                grid.findViewById(R.id.rowthree).setVisibility(View.GONE);
//            } else if (paths.size() == 6) {
//                grid.findViewById(R.id.rowthree).setVisibility(View.GONE);
//            }

            ImageView[] imageViews = {
                    one, two, three, four, five, six, seven, eight, nine
            };

            setImages(imageViews, paths);

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setImages(ImageView[] imageViews, ArrayList<String> paths) {

        for (int i = 0; i < paths.size(); i++) {
            Bitmap bm = CropHelper.decodeUriAsBitmap(this, Uri.parse("file://" + paths.get(i)));
            imageViews[i].setImageBitmap(bm);
        }

        //get container view
//        if(paths.size()==9){
//            for (int i = 0; i < paths.size(); i++) {
//                imageViews[i].setImageBitmap(CropHelper.decodeUriAsBitmap(this, Uri.parse(paths.get(i))));
//            }
//        }else if(paths.size() ==6){
//            for (int i = 2; i < paths.size(); i++) {
//                imageViews[i].setImageBitmap(CropHelper.decodeUriAsBitmap(this, Uri.parse(paths.get(i))));
//            }
//        }else if(paths.size() == 3){
//            for (int i = 5; i < paths.size(); i++) {
//                imageViews[i].setImageBitmap(CropHelper.decodeUriAsBitmap(this, Uri.parse(paths.get(i))));
//            }
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:


            case R.id.Continue:

                break;
        }
    }
}
	


	
	
	
			

	


		
		
		
	



