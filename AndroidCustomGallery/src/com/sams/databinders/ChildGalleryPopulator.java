package com.sams.databinders;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.sams.androidcustomgallery.GalleryChildActivity;
import com.sams.androidcustomgallery.R;
import com.sams.androidcustomgallery.SamsGallery;
import com.sams.customcomponents.ActionBarController;
import com.sams.customcomponents.GalleryFrameLayout;
import com.sams.customcomponents.RecyclingImageView;
import com.sams.workers.CacheManager;

/**
 * ChildGalleryPopulator - Custom adapter for listing all
 * items in a gallery folder
 * @author Sam Rajan
 *
 */
public class ChildGalleryPopulator extends DataAdapter {
	
	//files and data of each gallery item
	private ArrayList<DataHolder> mFiles;
	
	//index of selected items
	private ArrayList<String>     mSelections;
	
	//actionbar controller object for handiling c.a.b menus
	private ActionBarController   mController;
	
	//can select multiple files
	private boolean               isMultiSelectionEnabled = false;
	
	
	/**
	 * constructor 
	 * @param context
	 * @param resource
	 * @param galleryType
	 * @param data
	 * @param cacheManager
	 */
	public ChildGalleryPopulator(Context context, int resource, 
			String galleryType, ArrayList<DataHolder> data, CacheManager cacheManager) {
		
		super(context, resource, galleryType, cacheManager);
		this.mFiles = data;
		//creating actionbar controller with child gallery context 
		this.mController = (ActionBarController) context;
		this.mSelections =  new ArrayList<String>();
		super.setItemCount(mFiles.size());
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//view holder object
		final GalleryHolder holder;
	   
	    if (convertView == null) {
	    	LayoutInflater inflater = (LayoutInflater) getContext()
	 	    		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	convertView = inflater.inflate(R.layout.gridview_child_gallery_item, parent, false);
	        holder = new GalleryHolder();
	        holder.folderThumbnail = (RecyclingImageView) convertView.findViewById(R.id.iv_thumbnail); 
	        holder.baseLayout = (GalleryFrameLayout) convertView.findViewById(R.id.baseItemLayout);
	        convertView.setTag(holder);   
	    } else 	    	
	        holder = (GalleryHolder) convertView.getTag();
	    
	    //for retaining selected state for already selected item
	    if(mSelections.contains(String.valueOf(position)))
	    	holder.baseLayout.setChecked(true);
	    else
	    	holder.baseLayout.setChecked(false);
	    	
	    //loading thumbnail bitmap image
	   if(getGallerytype().equals(SamsGallery.IMAGES))
	        loadBitmap(SamsGallery.IMAGES, 
	        		Long.valueOf(mFiles.get(position).getThumbnailData()), holder.folderThumbnail);
	   else
	    	loadBitmap(SamsGallery.VIDEOS, 
	    			Long.valueOf(mFiles.get(position).getThumbnailData()),holder.folderThumbnail);
	   
	    
	    //Long click listener for each view item
	    holder.baseLayout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				//if multiselection is not already enabled
				if(!isMultiSelectionEnabled){
					//enabling multi selection
					setMultiSelectionEnabled(true);
					//updating view state
					updateViewState(holder.baseLayout, position);
					//showing action bar items
					mController.toggleActionBarItems(true);
				}
				else
					return false;
				
				return true;
			}
		});
	    
	    //click listener for each view item
	    holder.baseLayout.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				/*if multi selection is enabled items 
				are added to slection*/
				if(isMultiSelectionEnabled){
					//update the view state
					updateViewState(holder.baseLayout, position);
					//update the counter
					mController.updateCounter(holder.baseLayout.isChecked());
				}
				else{
					//adding the position to selected item list
					mSelections.add(String.valueOf(position));
					//going to preview
					((GalleryChildActivity)getContext()).gotoPreview();
				}
					
			}
		});
	    
	    return convertView;
	}
	
	/**
	 * function for enabling multi selection
	 * @param enable boolean value
	 */
	public void setMultiSelectionEnabled(boolean enable){
		isMultiSelectionEnabled = enable;
	}
	
	/**
	 * function for clearing selected item list
	 */
	public void clearSelectedItemList(){
		mSelections.clear();
	}
	
	/**
	 * function for getting all selected item list
	 * @return list of selected indices
	 */
	public ArrayList<String> getSelectedItemList(){
		return mSelections;
	}
	
	/**
	 * function for setting list of indices
	 * @param items list of indices
	 */
	public void setSelectedItemList(ArrayList<String> items){
		mSelections = items;
	}
	
	/**
	 * function for updating view state <br>
	 * @param frameLayout
	 * @param itemPosition
	 */
	private void updateViewState(GalleryFrameLayout frameLayout, int itemPosition){
		//if view is already checked
		if(frameLayout.isChecked()){
			//remove the position of view from selected item list
			mSelections.remove(String.valueOf(itemPosition));
			//unchecking the view 
			frameLayout.setChecked(false);
		}
		else{
			//adding the position of view to selected item list
			mSelections.add(String.valueOf(itemPosition));
			//checking the view 
			frameLayout.setChecked(true);
		}
	}
	
	

	
}
