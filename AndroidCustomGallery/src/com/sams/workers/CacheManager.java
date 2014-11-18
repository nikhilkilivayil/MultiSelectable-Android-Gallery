package com.sams.workers;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

public class CacheManager {
	
	//lrucache object for memory caching
	private LruCache<String, BitmapDrawable> mMemCache;
	
	//object for disk caching
	private DiskLruCache                     mDiskLruCache;
	
	//context
	private Context 						 mContext;
	
	//Disk caching parameters
	private final Object                     mDiskCacheLock = new Object();
	private final String                     DISK_CACHE_SUBDIR = "thumbnails";
	private boolean                          mDiskCacheStarting = true;
	private final int                        DISK_CACHE_SIZE = 1024 * 1024 * 90; // 90MB
	
	
	public CacheManager(Context context, FragmentManager fragmentManager){
		
		this.mContext = context;
		
		//creating disk cache
		File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
	    new InitDiskCacheTask().execute(cacheDir);
		
		// Get max available VM memory, exceeding this amount will throw an
	    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
	    // int in its constructor.
	    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

	    // Use 1/5th of the available memory for this memory cache.
	    final int cacheSize = maxMemory/5;
	    
	    //getting cache memory state from saved state
	    CacheRetainer retainFragment =
	    		CacheRetainer.findOrCreateRetainFragment(fragmentManager);
	    mMemCache = retainFragment.mRetainedCache;
	    
	    //creating memory cache
	    if (mMemCache == null){
		    mMemCache = new LruCache<String, BitmapDrawable>(cacheSize) {
		        @Override
		        protected int sizeOf(String key, BitmapDrawable bitmap) {
		            // The cache size will be measured in kilobytes rather than
		            // number of items.
		            return (int) (getSizeInBytes(bitmap.getBitmap()) / 1024);
		        }

				@Override
				protected void entryRemoved(boolean evicted, String key,
						BitmapDrawable oldValue, BitmapDrawable newValue) {
					Log.e("Asas", "Removed"+(Runtime.getRuntime().maxMemory()/1024));
					super.entryRemoved(evicted, key, oldValue, newValue);					
				}  
		        
		    };
		    retainFragment.mRetainedCache = mMemCache;
	    }
	    
	}	
	
	
	 /**
	 * function  for adding bitmap to cache
	 * @param data
	 * @param value
	 * @param addBitmaptoDisk
	 */
	public void addBitmapToCache(String data, BitmapDrawable value, boolean addBitmaptoDisk) {
		 
		 if ((data == null) ||(value == null))
				return;
	    // Add to memory cache as before
	    if (getBitmapFromMemCache(data) == null) {
	    	mMemCache.put(data, value);
	    }
	    
	    if(!addBitmaptoDisk)
	    	return;
	    
	    synchronized (mDiskCacheLock) {
	        if (mDiskLruCache != null && mDiskLruCache.get(data) == null) {
	        	
	            mDiskLruCache.put(data, value.getBitmap());
	        }
	    }
	 }
	
	
	/**
	 * function for getting bitmap from memory cache
	 * @param key
	 * @return bitmap drawable
	 */
	public BitmapDrawable getBitmapFromMemCache(String key) {
		return mMemCache.get(key);
	}
	
	
	/**
	 * function for getting bitmap from disk cache
	 * @param data
	 * @return BitmapDrawable
	 */
	public BitmapDrawable getBitmapFromDiskCache(String data) {

		Bitmap bitmap = null;
		synchronized (mDiskCacheLock) {
	        // Wait while disk cache is started from background thread
	        while (mDiskCacheStarting) {
	            try {
	                mDiskCacheLock.wait();
	            } catch (InterruptedException e) {}
	        }
	        if (mDiskLruCache != null) {
	        	bitmap =  mDiskLruCache.get(data);
	        }
	    }
       
        if(bitmap != null)
        	return new BitmapDrawable(mContext.getResources(), bitmap);
        else
        	return null;
        
        //END_INCLUDE(get_bitmap_from_disk_cache)
    }
  
	    
	 

	
	/**
	 * function for getting bitmap size in bites
	 * @param bitmap
	 * @return bytes 
	 */
	@SuppressLint("NewApi")
	private long getSizeInBytes(Bitmap bitmap) {
		
		if(bitmap == null)
			return 0;
		
	    if(android.os.Build.VERSION.SDK_INT >= 12) {
	        return bitmap.getByteCount();
	    } else {
	        return bitmap.getRowBytes() * bitmap.getHeight();
	    }
	}
	
	 
	// Creates a unique subdirectory of the designated app cache directory. Tries to use external
	// but if not mounted, falls back on internal storage.
	public static File getDiskCacheDir(Context context, String uniqueName) {
	    // Check if media is mounted or storage is built-in, if so, try and use external cache dir
	    // otherwise use internal cache dir
	    final String cachePath =
	            Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
	                    !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
	                            context.getCacheDir().getPath();

	    return new File(cachePath + File.separator + uniqueName);
	}
	
	
	class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
	    @Override
	    protected Void doInBackground(File... params) {
	        synchronized (mDiskCacheLock) {
	            File cacheDir = params[0];
	            mDiskLruCache = DiskLruCache.openCache(mContext, cacheDir, DISK_CACHE_SIZE);
	            mDiskCacheStarting = false; // Finished initialization
	            mDiskCacheLock.notifyAll(); // Wake any waiting threads
	        }
	        return null;
	    }
	}
	
}


