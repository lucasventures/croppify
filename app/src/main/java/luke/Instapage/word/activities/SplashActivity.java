package luke.Instapage.word.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import luke.Instapage.word.R;


public class SplashActivity extends Activity {

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
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

        };
        splashTread.start();
    }
}



   