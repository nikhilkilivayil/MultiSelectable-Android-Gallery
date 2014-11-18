package com.sams.databinders;

import java.util.concurrent.RejectedExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sams.workers.CacheManager;
import com.sams.workers.GalleryLoader;
import com.sams.workers.GalleryLoader.AsyncDrawable;

/**'
 * DataAdapter - custom BaseAdapter for populating 
 * gallery
 * @author Sam Rajan
 *
 */
public class DataAdapter extends BaseAdapter {
	
	//current context
	private Context       mContext;
	
	//placeholder bitmap
	private static Bitmap mPlaceHolderBitmap;
	
	//cache for thumbnail images
	private CacheManager  mCacheManager; 
	
	//gallery type
	private String        mGalleryType;
	
	//total number of items in view
	private int           ITEM_COUNT = 0;

	
	/**
	 * constructor
	 * @param context
	 * @param resource
	 * @param galleryType
	 * @param cacheManager
	 */
	public DataAdapter(Context context, int resource, 
			String galleryType, CacheManager cacheManager) {
		
		this.mContext = context;
		this.mGalleryType = galleryType;
		this.mCacheManager = cacheManager;
	}

	@Override
	public int getCount() {
		return ITEM_COUNT;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * function for getting current context
	 * @return current context
	 */
	public Context getContext(){
		return mContext;
	}
	
	/**
	 * setting total items available in view
	 * @param itemCount 
	 */
	public void setItemCount(int itemCount){
		ITEM_COUNT = itemCount;
	}
	
	/**
	 * function for getting gallery type
	 * @return gallery type
	 */
	public String getGallerytype(){
		return mGalleryType;
	}
	
	
	/**
	 * Load bitmaps from the cache if it is available in cache
	 * otherwise process the bitmap and save it in cache
	 * @param type
	 * @param resourceId
	 * @param imageView
	 */
	public void loadBitmap(String type, Long resourceId, ImageView imageView){
		
		//unique key for thumbnail
		final String imageKey = String.valueOf(resourceId);
		
		//getting image from memory cache
	    BitmapDrawable bitmap = mCacheManager.getBitmapFromMemCache(imageKey);
	    
	    if(bitmap != null){
	    	imageView.setImageDrawable(bitmap);
	    	Log.e("cached copy", "cached copy");
	    	return;
	    }
	    
	    //processing bitmaps in separated thread	    	
		if (GalleryLoader.cancelPotentialWork(resourceId, imageView)) {
			GalleryLoader galleryLoader = new GalleryLoader(mContext, imageView, type, mCacheManager);
			AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(),
					mPlaceHolderBitmap, galleryLoader);
			imageView.setImageDrawable(asyncDrawable);
			try{
				galleryLoader.execute(resourceId);
			}
			catch(RejectedExecutionException exception){
				exception.printStackTrace();
			}
		}
		
	}
}
