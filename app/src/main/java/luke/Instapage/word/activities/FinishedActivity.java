package luke.Instapage.word.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import luke.Instapage.word.R;

public class FinishedActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin);

        findViewById(R.id.top).setOnClickListener(this);
        findViewById(R.id.mid).setOnClickListener(this);
        findViewById(R.id.bottom).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.top:
                startActivity(new Intent(FinishedActivity.this, InfoActivity.class));
                break;

            case R.id.mid:
                final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

                if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
                    Toast.makeText(this, "Thank you for your rating!", Toast.LENGTH_LONG).show();
                    startActivity(rateAppIntent);
                } else {
                    /* handle your error case: the device has no way to handle market urls */
                    Toast.makeText(this, "aw, an error occurred :/", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.bottom:
                // intent to my twitter
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/lucasventures"));
                startActivity(browserIntent2);
                break;
        }
    }
}
	
	
	
	
	
   
	
		
		
 

		
	


	

