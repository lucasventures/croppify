package luke.Instapage.word.crop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static luke.Instapage.word.activities.MainActivity.TAG;

public class CropEngine {

    /**
     * @param context
     * @param imageUri
     * @param type     ---- this is an integer declaring the number of "levels" to a column. Only may be 1, 2, or 3.
     */
    public static ArrayList<String> createCroppedImagesWithParams(WeakReference<Context> context, String imageUri, int type) {


        ArrayList<String> stringifiedPaths = new ArrayList<>();

        InputStream image_stream;
        try {
            image_stream = context.get().getContentResolver().openInputStream(Uri.parse(imageUri));

            Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

            //TODO: this whole algorithm needs to be revised

            int chunkNumbers = 3 * type;
            int rows, cols;
            //For height and width of the small image chunks
            int chunkHeight, chunkWidth;
            //To store all the small image chunks in bitmap format in this list
            ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
            //Getting the scaled bitmap of the source image
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

            rows = type;
            cols = 3;
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

            while (loopVal < chunkNumbers) {
                Bitmap image = chunkedImages.get(loopVal);
                try {
                    stringifiedPaths.add(saveImageToDisk(context, image, loopVal));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                loopVal++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringifiedPaths;
    }


    private static String saveImageToDisk(WeakReference<Context> context, Bitmap bitmapImage, int numericIdentifier) {

        String instaCut = Environment.getExternalStorageDirectory() + "/insta_cut_pro";
        File dir = new File(instaCut);
        dir.mkdir();
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File file = new File(instaCut, timeStamp + "_" + numericIdentifier + ".png");

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException c) {
            Log.e(TAG, "saveImageToDisk: FAILURE");
        }

        notifyMediaStoreScanner(context.get(), file);

        return file.getAbsolutePath();
    }

    private static void notifyMediaStoreScanner(Context mContext, final File file) {
        try {
            MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            mContext.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
