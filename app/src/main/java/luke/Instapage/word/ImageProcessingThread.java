package luke.Instapage.word;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by LUCASURE on 8/20/2015.
 */
public class ImageProcessingThread implements Runnable{

    @Override
    public void run() {
        // TODO Auto-generated method stub

//figure out how to run this... might need to get context of layout


            ImageView image = (ImageView)findViewById(R.id.imageView1);
            image.setImageDrawable(getResources().getDrawable(R.drawable.text1));
            ImageView image2 = (ImageView)findViewById(R.id.imageView2);
            image2.setImageDrawable(getResources().getDrawable(R.drawable.text2));
            ImageView image22 = (ImageView)findViewById(R.id.imageView3);
            image22.setImageDrawable(getResources().getDrawable(R.drawable.text3));
            ImageView image222 = (ImageView)findViewById(R.id.imageView4);
            image222.setImageDrawable(getResources().getDrawable(R.drawable.text4));
            ImageView image1 = (ImageView)findViewById(R.id.imageView5);
            image1.setImageDrawable(getResources().getDrawable(R.drawable.text5));


        System.out.println("this was executed on a separate thread by implementing the Runnable interface");
    }

}
