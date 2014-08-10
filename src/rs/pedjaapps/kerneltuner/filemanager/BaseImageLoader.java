package rs.pedjaapps.kerneltuner.filemanager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.os.Process;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;

import rs.pedjaapps.kerneltuner.R;

/**
 * Created by pedja on 10.8.14..
 */
public abstract class BaseImageLoader<CacheItem>
{
    private static final int DEFAULT_CACHE_THREAD_POOL_SIZE = 4;
    private static final int USER_CACHE_LIMIT = 20;

    protected Context context;
    private LinkedBlockingQueue<ImageData> queue;
    private ConcurrentSkipListMap<String, SoftReference<CacheItem>> cache;
    private ImageLoaderThread[] workerThreads;
    private Set<String> processing;
    private Handler handler;

    public BaseImageLoader(Context context)
    {
        this.context = context;
        handler = new Handler();
        queue = new LinkedBlockingQueue<>();
        cache = new ConcurrentSkipListMap<>();
        processing = new HashSet<>();
        //create threads up to the pool size
        //workerThreads = new ImageLoaderThread[DEFAULT_CACHE_THREAD_POOL_SIZE];//this gives me generic array creation error, wtf?
        @SuppressWarnings("unchecked")
        final ImageLoaderThread[] a = (ImageLoaderThread[]) Array.newInstance(ImageLoaderThread.class, DEFAULT_CACHE_THREAD_POOL_SIZE);
        this.workerThreads = a;
        //start processing on all threads
        for(int i = 0; i < workerThreads.length; i++)
        {
            workerThreads[i] = new ImageLoaderThread(queue);
            workerThreads[i].start();
        }
    }

    public void displayImage(ImageData imageData)
    {
        imageData.imageView.setTag(imageData.path);
        imageData.imageView.setImageResource(imageData.fallbackImageRes);//reset image before loading
        //check if apk is already queued
        //check if we are currently processing apk
        //check if cached image is null if its already cached
        if(!queue.contains(imageData) && !processing.contains(imageData.path)
                && (!isCached(imageData.path) || cache.get(imageData.path).get() == null))
        {
            queue.add(imageData);
        }
        else if(isCached(imageData.path))
        {
            setImageResource(imageData.imageView, getCachedItem(imageData.path));
        }
    }

    public abstract void setImageResource(ImageView imageView, CacheItem item);

    /**
     * Clear instance, clear queue and list of cached users, shutdown all worker threads
     * */
    public void cleanup()
    {
        cache.clear();
        queue.clear();
        for (ImageLoaderThread thread : workerThreads) thread.quit();
    }

    public void addToCache(String apkPath, CacheItem item)
    {
        if(cache.size() > USER_CACHE_LIMIT)
        {
            cache.remove(cache.firstKey());
        }
        cache.put(apkPath, new SoftReference<>(item));
    }

    public boolean isCached(String apkPath)
    {
        return cache.containsKey(apkPath);
    }

    public CacheItem getCachedItem(String apkPath)
    {
        SoftReference<CacheItem> item = cache.get(apkPath);
        return item == null ? null : item.get();
    }

    public void processingApk(String apkPath)
    {
        synchronized (this)
        {
            processing.add(apkPath);
        }
    }

    public void finishedProcessingApk(String apkPath)
    {
        synchronized (this)
        {
            processing.remove(apkPath);
        }
    }

    private class ImageLoaderThread extends Thread
    {
        /**
         * The queue of requests to service.
         */
        private final BlockingQueue<ImageData> mQueue;
        /**
         * Used for telling us to die.
         */
        private volatile boolean mQuit = false;

        /**
         * Creates a new cache thread.  You must call {@link #start()}
         * in order to begin processing.
         *
         * @param queue    Queue of incoming requests for triage
         */
        public ImageLoaderThread(BlockingQueue<ImageData> queue)
        {
            mQueue = queue;
        }

        /**
         * Forces this thread to quit immediately.  If any requests are still in
         * the queue, they are not guaranteed to be processed.
         */
        public void quit()
        {
            mQuit = true;
            interrupt();
        }

        @Override
        public void run()
        {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            ImageData data;
            while (true)
            {
                try
                {
                    // Take a request from the queue.
                    data = mQueue.take();
                }
                catch (InterruptedException e)
                {
                    // We may have been interrupted because it was time to quit.
                    if (mQuit)
                    {
                        return;
                    }
                    continue;
                }
                processingApk(data.path);
                final CacheItem icon;
                if(cache.containsKey(data.path) && cache.get(data.path).get() != null)
                {
                    icon = cache.get(data.path).get();
                }
                else
                {
                    icon = loadCacheItem(data);
                }
                if(icon != null)
                {
                    final ImageData finalData = data;
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(finalData.path.equals(finalData.imageView.getTag()))setImageResource(finalData.imageView, icon);
                        }
                    });
                    addToCache(data.path, icon);
                    finishedProcessingApk(data.path);
                }
                else
                {
                    final ImageData finalData = data;
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(finalData.path.equals(finalData.imageView.getTag()))finalData.imageView.setImageResource(finalData.fallbackImageRes);
                        }
                    });
                }

            }
        }
    }

    protected abstract CacheItem loadCacheItem(ImageData imageData);

    public static class ImageData
    {
        public String path;
        public ImageView imageView;
        public int fallbackImageRes;

        public ImageData(String path, ImageView imageView, int fallbackImageRes)
        {
            this.path = path;
            this.imageView = imageView;
            this.fallbackImageRes = fallbackImageRes;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ImageData imageData = (ImageData) o;

            return imageView.equals(imageData.imageView) && path.equals(imageData.path);

        }

        @Override
        public int hashCode()
        {
            int result = path.hashCode();
            result = 31 * result + imageView.hashCode();
            return result;
        }
    }
}
