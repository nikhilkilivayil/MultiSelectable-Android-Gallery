package com.sams.databinders;

import java.util.TreeMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sams.androidcustomgallery.GalleryChildActivity;
import com.sams.androidcustomgallery.R;
import com.sams.androidcustomgallery.SamsGallery;
import com.sams.customcomponents.RecyclingImageView;
import com.sams.workers.CacheManager;

public class GalleryPopulator extends DataAdapter {
	
	//list of all gallery folders 
	private TreeMap<String, String> mObjects;
	
	//list of all folder names
	private String                  mFolderNames[];
	
	/**
	 * constructor
	 * @param context
	 * @param resource
	 * @param objects
	 * @param galleryType
	 * @param cacheManager
	 */
	public GalleryPopulator(Context context, int resource, TreeMap<String, 
			String> objects, String galleryType, CacheManager cacheManager) {
		super(context, resource, galleryType, cacheManager);
		this.mFolderNames = objects.keySet().toArray(new String[0]);
		this.mObjects = objects;
		super.setItemCount(mFolderNames.length);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		GalleryHolder holder;
	    if (convertView == null) {
	     
	    	LayoutInflater inflater = (LayoutInflater) getContext()
	 	    		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	convertView = inflater.inflate(R.layout.gridview_gallery_item, parent, false);
	        holder = new GalleryHolder();
	        holder.folderName = (TextView) convertView.findViewById(R.id.tv_folder);
	        holder.folderThumbnail = (RecyclingImageView) convertView.findViewById(R.id.iv_thumbnail);
	        convertView.setTag(holder);
	        
	    } else 	    	
	        holder = (GalleryHolder) convertView.getTag();
	        
	    //setting folder name
	    holder.folderName.setText(mFolderNames[position]);
	    
	    //loading thumbnail bitmap image
	    if(getGallerytype().equals(SamsGallery.IMAGES))
	        loadBitmap(SamsGallery.IMAGES, 
	        		Long.valueOf(mObjects.get(mFolderNames[position]).toString()), holder.folderThumbnail);
	    else
	    	loadBitmap(SamsGallery.VIDEOS, 
	    			Long.valueOf(mObjects.get(mFolderNames[position]).toString()),holder.folderThumbnail);
	    
	   
	    //click listener for gallery item
	    OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//starts child gallery
				Intent intent = new Intent(getContext(), GalleryChildActivity.class);
				intent.putExtra(GalleryChildActivity.EXTRA_ARGS, mFolderNames[position]);
				intent.putExtra(GalleryChildActivity.ARGS, getGallerytype());
				((SamsGallery) getContext()).startChildActivity(intent);
			}
		};
		
		holder.folderThumbnail.setOnClickListener(clickListener);
		holder.folderName.setOnClickListener(clickListener);
	    
	    return convertView;
	}
	
	

}
