package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import luke.Instapage.word.CropHelper;
import luke.Instapage.word.R;


public class ResultDisplayActivity extends Activity implements OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        ImageButton keep = findViewById(R.id.Continue);
        ImageButton back = findViewById(R.id.goback);
        Uri imUri = getIntent().getParcelableExtra("imageUri");
        ImageView mImageView = findViewById(R.id.cropDisplay);
        mImageView.setImageBitmap(CropHelper.decodeUriAsBitmap(this, imUri));

        keep.setOnClickListener(this);
        back.setOnClickListener(this);


        // sends parcelable to
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:

                startActivity(new Intent(ResultDisplayActivity.this,
                        ImageActivity.class));


                break;
            case R.id.Continue:


                // this needs to be executed from a separate thread


                Uri imUri = getIntent().getParcelableExtra("imageUri");
                Intent intent2 = new Intent(ResultDisplayActivity.this, FinishedActivity.class);
                intent2.putExtra("imageUri", imUri);

                // Thread myRunnableThread = new Thread(new ImageProcessingThread());
                // myRunnableThread.start();


                startActivity(intent2);
                Toast.makeText(this, "Performing Operations...", Toast.LENGTH_SHORT).show();
                // pull up progress dialog that receives updates from thread


                break;
        }
    }
}
	


	
	
	
			

	


		
		
		
	



