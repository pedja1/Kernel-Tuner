package rs.pedjaapps.kerneltuner.filemanager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.widget.ImageView;

public class VideoThumbLoader extends BaseImageLoader<Bitmap>
{
    private static VideoThumbLoader instance = null;

    private VideoThumbLoader(Context context)
    {
        super(context);
    }

    public static VideoThumbLoader getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new VideoThumbLoader(context);
        }
        return instance;
    }

    @Override
    public void setImageResource(ImageView imageView, Bitmap bitmap)
    {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected Bitmap loadCacheItem(ImageData imageData)
    {
        return ThumbnailUtils.createVideoThumbnail(imageData.path,
                MediaStore.Images.Thumbnails.MICRO_KIND);
    }


}