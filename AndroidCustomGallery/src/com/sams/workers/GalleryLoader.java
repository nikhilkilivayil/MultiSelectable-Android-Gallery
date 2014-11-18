package com.sams.workers;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.sams.androidcustomgallery.SamsGallery;

/**
 * GalleryLoader - Async class for loading gallery thumbnails
 * @author Sam Rajan
 *
 */
public class GalleryLoader extends AsyncTask<Long, Object, Bitmap> {
	
	protected WeakReference<ImageView> mImageReference;
	protected static BitmapDrawable    mBitmapDrawable;
	protected CacheManager             mCacheManager = null;
	
	protected String                   mImageType;
	protected Context                  mContext;
	protected Long                     mResourceId = (long) 0;

	/**
	 * Constructor
	 * @param context
	 * @param imageView
	 * @param type
	 * @param cacheManager
	 */
	public GalleryLoader(Context context, ImageView imageView, String type, 
			CacheManager cacheManager) {
		this.mCacheManager = cacheManager;
		this.mImageType = type;
		this.mContext = context;
		mImageReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(Long... params) {
		mResourceId = params[0];
		 
		boolean bitmapNotInDisk = false;  
		//getting bitmap from disk cache
		mBitmapDrawable = mCacheManager.getBitmapFromDiskCache(String.valueOf(mResourceId));
		
		//if bitmap not in disk cache
		if(mBitmapDrawable == null){
			Bitmap resultBitmap;
			bitmapNotInDisk = true;
			if(mImageType.equals(SamsGallery.IMAGES))
				resultBitmap = MediaStore.Images.Thumbnails.getThumbnail(
		        		mContext.getContentResolver(), mResourceId,
		        		MediaStore.Images.Thumbnails.MINI_KIND, null);
			else
				resultBitmap = MediaStore.Video.Thumbnails.getThumbnail(
		        		mContext.getContentResolver(), mResourceId,
		        		MediaStore.Video.Thumbnails.MINI_KIND, null);
			
			mBitmapDrawable = new BitmapDrawable(mContext.getResources(), resultBitmap);
		}	

		//adding created bitmap in cache
		mCacheManager.addBitmapToCache(String.valueOf(params[0]), mBitmapDrawable, bitmapNotInDisk);
		if(mBitmapDrawable.getBitmap() != null)
			return mBitmapDrawable.getBitmap();
		else
			return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		
		if (isCancelled()) {
            result = null;
        }
		
		if (mImageReference != null && result != null) {
            final ImageView imageView = mImageReference.get();
            final GalleryLoader galleryLoader =  getGalleryLoader(imageView);
            if (this == galleryLoader && imageView != null) {
                imageView.setImageBitmap(result);
            }
        }
		super.onPostExecute(result);
	}
	
	
	public static boolean cancelPotentialWork(Long resourceId, ImageView imageView) {
	    final GalleryLoader galleryLoader = getGalleryLoader(imageView);

	    if (galleryLoader != null) {
	        final Long bitmapData = galleryLoader.mResourceId;
	        // If bitmapData is not yet set or it differs from the new data
	        if (bitmapData == 0 || bitmapData != resourceId) {
	            // Cancel previous task
	        	galleryLoader.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
		
	
	private static GalleryLoader getGalleryLoader(ImageView imageView) {
	   if (imageView != null) {
	       final Drawable drawable = imageView.getDrawable();
	       if (drawable instanceof AsyncDrawable) {
	           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
	           return asyncDrawable.getGalleryLoader();
	       }
	    }
	    return null;
	}
	
	
	
	public static class AsyncDrawable extends BitmapDrawable{
		private final WeakReference<GalleryLoader> galleryLoaderReference;
	
		public AsyncDrawable(Resources res, Bitmap bitmap,
				GalleryLoader galleryLoader) {
			super(res, bitmap);
			galleryLoaderReference =
		            new WeakReference<GalleryLoader>(galleryLoader);
		}
			
		public GalleryLoader getGalleryLoader() {
	        return galleryLoaderReference.get();
	    }
		
	}

}


