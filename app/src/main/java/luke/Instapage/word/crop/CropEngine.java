package luke.Instapage.word.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static luke.Instapage.word.activities.MainActivity.TAG;

public class CropEngine {

    static void createCroppedImagesWithParams(Context context, String imageUri) {

        InputStream image_stream;
        try {
            image_stream =

                    context.getContentResolver().

                            openInputStream(Uri.parse(imageUri));


            Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

            int chunkNumbers = 9;
            //For the number of rows and columns of the grid to be displayed
            int rows, cols;
            //For height and width of the small image chunks
            int chunkHeight, chunkWidth;
            //To store all the small image chunks in bitmap format in this list
            ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
            //Getting the scaled bitmap of the source image
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            rows = cols = (int) Math.sqrt(chunkNumbers);
            chunkHeight = bitmap.getHeight() / rows;
            chunkWidth = bitmap.getWidth() / cols;

            //xCoord and yCoord are the pixel positions of the image chunks
            int yCoord = 0;
            for (int x = 0; x < rows; x++) {
                int xCoord = 0;

                for (int y = 0; y < cols; y++) {
                    chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                    xCoord += chunkWidth;
                }

                yCoord += chunkHeight;
            }

            int loopVal = 0;

            while (loopVal < 9) {
                Bitmap image = chunkedImages.get(loopVal);

                try {
                    saveImageToDisk(context, image, loopVal);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                loopVal++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void saveImageToDisk(Context context, Bitmap bitmapImage, int numericIdentifier) {
        String instaGator = new String(Environment.getExternalStorageDirectory() + "/instaGator");
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File file = new File(instaGator, ".post_order" + timeStamp + "_" + numericIdentifier + ".png");

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{".post_order" + timeStamp + "_" + numericIdentifier + ".png"}, null);
            fos.close();
        } catch (IOException c) {
            Log.e(TAG, "saveImageToDisk: FAILURE");
        }


    }

}
