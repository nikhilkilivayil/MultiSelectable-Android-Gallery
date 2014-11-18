package com.sams.databinders;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sams.androidcustomgallery.R;
import com.sams.androidcustomgallery.SamsGallery;
import com.sams.customcomponents.RecyclingImageView;
import com.sams.workers.CacheManager;

/**
 * HorizontalListViewPopulator - custom adapter for populating
 * horizontal list view in preview activiy
 * @author Sam Rajan
 *
 */
public class HorizontalListViewPopulator extends DataAdapter {

	//files and data of each gallery item
	private ArrayList<DataHolder> mFiles;
	
	/**
	 * Constructor
	 * @param context
	 * @param resource
	 * @param galleryType
	 * @param data
	 * @param cacheManager
	 */
	public HorizontalListViewPopulator(Context context, int resource,
			String galleryType, ArrayList<DataHolder> data, CacheManager cacheManager) {
		super(context, resource, galleryType, cacheManager);
		this.mFiles = data;
		super.setItemCount(mFiles.size());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final GalleryHolder holder;
		
		 if (convertView == null) {
		    	LayoutInflater inflater = (LayoutInflater) getContext()
		 	    		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	convertView = inflater.inflate(R.layout.horizontal_listview_item, parent, false);
		        holder = new GalleryHolder();
		        holder.folderThumbnail = (RecyclingImageView) convertView.findViewById(R.id.iv_thumbnail); 
		        holder.listBaseLayout = (FrameLayout) convertView.findViewById(R.id.baseItemLayout);
		        convertView.setTag(holder);   
	    } else 	    	
	        holder = (GalleryHolder) convertView.getTag();
		 
		  //loading thumbnail bitmap image
		 if(getGallerytype().equals(SamsGallery.IMAGES))
		        loadBitmap(SamsGallery.IMAGES, 
		        		Long.valueOf(mFiles.get(position).getThumbnailData()), holder.folderThumbnail);
		 else
		    	loadBitmap(SamsGallery.VIDEOS, 
		    			Long.valueOf(mFiles.get(position).getThumbnailData()),holder.folderThumbnail); 		 
		 
		 
		return convertView;
	}

	
}
