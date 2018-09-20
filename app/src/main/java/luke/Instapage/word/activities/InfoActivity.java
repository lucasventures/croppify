package luke.Instapage.word.activities;

import android.app.Activity;
import android.os.Bundle;

import luke.Instapage.word.ImageProcessingThread;
import luke.Instapage.word.R;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        Thread rThread = new Thread(new ImageProcessingThread());
        rThread.start();
    }


}
