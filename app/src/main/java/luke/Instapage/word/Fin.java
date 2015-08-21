package luke.Instapage.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import luke.Instapage.word.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

public class Fin extends Activity implements OnClickListener {
	
	public int sayingVal;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fin);
		
			chooser();
		
			
		 
   			findViewById(R.id.top).setOnClickListener(this);
   			findViewById(R.id.mid).setOnClickListener(this);
   			findViewById(R.id.bottom).setOnClickListener(this);
   			 
   		 }

	private void chooser() {
		
		if(getIntent().getParcelableExtra("imageUri") != null)
			
			try{ Uri imUri = getIntent().getParcelableExtra("imageUri");
          	 
        	  
  			 
  			 
            InputStream image_stream;
			
				 
				 image_stream = getContentResolver().openInputStream(imUri);
					Bitmap bitmap= BitmapFactory.decodeStream(image_stream );
				 
				 
				 int chunkNumbers = 9;
					//For the number of rows and columns of the grid to be displayed
					int rows,cols;
					
					//For height and width of the small image chunks 
					int chunkHeight,chunkWidth;
					
					//To store all the small image chunks in bitmap format in this list 
					ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
					
					//Getting the scaled bitmap of the source image
					
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
					
					
					rows = cols = (int) Math.sqrt(chunkNumbers);
					chunkHeight = bitmap.getHeight()/rows;
					chunkWidth = bitmap.getWidth()/cols;
					
					//xCoord and yCoord are the pixel positions of the image chunks
					int yCoord = 0;
					for(int x=0; x<rows; x++){
						int xCoord = 0;
						for(int y=0; y<cols; y++){
							chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
							xCoord += chunkWidth;
						}
						yCoord += chunkHeight;
					}
					
					
					
					int loopVal = 0;
					int postVal = 9;
					while ( loopVal < 9) {
						Bitmap Image = chunkedImages.get(loopVal);

					try {

						String instaGator = new String(Environment.getExternalStorageDirectory()  + "/instaGator");
						String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
						File file = new File(instaGator, postVal + ".post_order" + timeStamp +  ".png");

				        FileOutputStream fos = new FileOutputStream(file);
					    Image.compress(Bitmap.CompressFormat.PNG, 100, fos);      

						MediaScannerConnection.scanFile(this, new String[] { file.getPath() }, new String[] { postVal + ".post_order" + timeStamp +  ".png" }, null);
					    fos.close();
					 
					        
					       
					} catch (Throwable e) {
					
						e.printStackTrace();

					}
					postVal--;
					loopVal++;

					}
					
					
				
		 }catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			 Toast.makeText(this, "Operation complete. Check your gallery! ", Toast.LENGTH_LONG).show();
			
		}
			
		
		
	}

	

	

	@Override
	public void onClick(View v) {
		
		
		
  	 
  	 
  	 
  	 
  	 
		
		 switch (v.getId()) {
         case R.id.top:
       
        
        startActivity(new Intent(Fin.this, InfoActivity.class));
        
       
        
        
             break;
         case R.id.mid:    	
        	 
        	 final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        	 final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        	 if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
        	 {
        		 Toast.makeText(this, "Thank you for your rating!", Toast.LENGTH_LONG).show();
        	     startActivity(rateAppIntent);
        	 }
        	 else
        	 {
        	     /* handle your error case: the device has no way to handle market urls */
        		 
        		 Toast.makeText(this, "aw, an error occurred :/", Toast.LENGTH_LONG).show();
        	 }
        	
        	 //intent to rating
     
             break;
         case R.id.bottom:
       	 // intent to my twitter
        	 
                     Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/lucasventures"));
                     startActivity(browserIntent2);
		 
         break;
     }
	}
}
	
	
	
	
	
   
	
		
		
 

		
	


	

