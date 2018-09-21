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

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        ImageButton keep = findViewById(R.id.Continue);
        ImageButton back = findViewById(R.id.goback);
        path = getIntent().getExtras().getString("imageUri");
        ImageView mImageView = findViewById(R.id.cropDisplay);
        if (path != null) {
            mImageView.setImageBitmap(CropHelper.decodeUriAsBitmap(this, Uri.parse(path)));
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show();
        }

        keep.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

            case R.id.Continue:

                if (path == null) {
                    Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent2 = new Intent(ResultDisplayActivity.this, FinishedActivity.class);
                    intent2.putExtra("imageUri", path);
                    startActivity(intent2);
                }

                break;
        }
    }
}
	


	
	
	
			

	


		
		
		
	



