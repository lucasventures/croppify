package luke.Instapage.word;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import luke.Instapage.word.R;



public class Splash extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 1000; // time to display the splash screen in ms
	
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.splash);

    
    
    



    Thread splashTread = new Thread() {
        @Override
        public void run() {
            try {
                int waited = 0;
                while (_active && (waited < _splashTime)) {
                    sleep(100);
                    if (_active) {
                        waited += 100;
                    }
                }
            } catch (Exception e) {

            } finally {

                startActivity(new Intent(Splash.this,
                        ImageActivity.class));

            }
        };
             };
    splashTread.start();
}
}



   