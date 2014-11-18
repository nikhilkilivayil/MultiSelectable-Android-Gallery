package com.sams.androidcustomgallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.sams.customcomponents.ActionBarController;
import com.sams.databinders.ChildGalleryPopulator;
import com.sams.databinders.DataHolder;
import com.sams.databinders.Data;
import com.sams.databinders.SavedIndexes;
import com.sams.workers.CacheManager;

/**
 * GalleryChildActivity - custom android gallery<br>
 * Dipalys all items in a gallery folder<br>
 * it can be either video or image
 * @author Sam Rajan
 *
 */
public class GalleryChildActivity extends ActionBarActivity  implements ActionBarController{
	
	private GridView                mGalleryView;
	
	//files and data of each gallery item
	private ArrayList<DataHolder>   mFiles;
	
	//index of selected items 
	private ArrayList<String>       mSelectedIndices;
	
	//parcelable arraylist for passing selected items to next activity 
	private ArrayList<Data>    mSelectedFiles;
	
	//parceleable arraylist for saving selected items state
	private ArrayList<SavedIndexes> mSavedSelectedIndices;
	
	//cache for thumbnail images
	private CacheManager            mCacheManager           = null;
	
	//custom adapter class for populating grid view 
	private ChildGalleryPopulator 	mGallerypopulator;
	
	//current gallery type - video or image
	private String                  mGalleryType;

	//fields needed from gallery content provider
	private static String[]         mProjection;
	
	//gallery content provider uri
	private static Uri              mGalleryUri;
	
	/*selection for content provider query
	here it will be folder name*/
	private static String           mSelection;
	
	/*extra arguments from parent activity
	it would be gallery type*/
	private static String           mFilterData;
	
	//flag for showing or hiding menu items
	private boolean                 mShowMenuItems         = false;
	
	//selected items count
	private int						mItemCounter           = 0;

	//constants, the keys for activity operations
	public static final String EXTRA_ARGS  				 = "args"; 
	public static final String ARGS        				 = "arguments";
	public static final String VIDEOS      				 = "videos";
	public static final String IMAGES      				 = "images";
	public static final String STATE_SELECTED_ITEM_COUNT = "itemcount";
	public static final String STATE_SAVED_ITEMS         = "selecteditems";
	public static final int    PREVIEW_REQUEST 			 = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_gallery_view);
	
		//retaining activity saved states
		if(savedInstanceState != null){
			
			//retaining selected items count
			mItemCounter = savedInstanceState.getInt(STATE_SELECTED_ITEM_COUNT);
			//retaining selected items
			mSavedSelectedIndices = savedInstanceState.getParcelableArrayList(STATE_SAVED_ITEMS);
			
			/*getting saved selected items from parcelable arraylist
			we are saving positions of selected grid view items*/
			mSelectedIndices = new ArrayList<String>();
			if(mSavedSelectedIndices != null && mSavedSelectedIndices.size() > 0){
				for(SavedIndexes index : mSavedSelectedIndices){
					mSelectedIndices.add(index.getPosition());
				}
			}
		}
			
		mFilterData = getIntent().getStringExtra(EXTRA_ARGS);
		mGalleryType = getIntent().getStringExtra(ARGS);
		
		mCacheManager = new CacheManager(this, getSupportFragmentManager());
		
		//assigning gallery uri, needed fields, selection for query according to gallery type
		if(mGalleryType.equals(IMAGES)){
        	mGalleryUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        	mProjection = new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Thumbnails._ID};
        	mSelection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        }
        else{
        	mGalleryUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        	mProjection = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Thumbnails._ID};
        	mSelection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " = ?";
        }
        
		//showing up button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mGalleryView = (GridView) findViewById(R.id.gv_gallery);
		
		//populating gallery
		this.populateChildGallery();
	}
	
	
	/**
	 * function for reading the gallery files and folders
	 * and setting custom adapter for gallery grid view
	 */
	private void populateChildGallery(){	
		
		//listing all gallery files
		mFiles = readGallery(this);
		//forcing garbage collection
		System.gc();
		
		mGallerypopulator = new ChildGalleryPopulator(this,
				R.layout.gridview_child_gallery_item, mGalleryType, mFiles, mCacheManager);
		mGalleryView.setAdapter(mGallerypopulator);	
		
	}
	
	
	@Override
	protected void onResume() {
	
		if(mItemCounter > 0){
			//showing action bar items(done button, cancel button and counter)
			this.toggleActionBarItems(true);
			//enabling multiselection
			mGallerypopulator.setMultiSelectionEnabled(true);
			//intended for resumed states,saved selected items 
			mGallerypopulator.setSelectedItemList(mSelectedIndices);
			mGallerypopulator.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	
	/**
	 * function for reading gallery
	 * @param activity context of current activity
	 * @return returns list of all gallery items
	 */
	public static ArrayList<DataHolder> readGallery(Activity activity) {
	    
	    Cursor cursor;  
	    int column_thumbnail_data, column_file_path;
	    //list of all gallery items
	    ArrayList<DataHolder> listOfAllImages = new ArrayList<DataHolder>();
	    //cursor holding list of all gallery items
	    cursor = activity.getContentResolver().query(mGalleryUri, mProjection, mSelection, new String[]{mFilterData}, 
    			MediaStore.Images.Media.DATE_ADDED +" DESC");

	    if(cursor == null)
	    	return listOfAllImages;
	    
	    column_thumbnail_data = cursor.getColumnIndexOrThrow(mProjection[1]);
	    column_file_path = cursor.getColumnIndexOrThrow(mProjection[0]);
	    
	    //getting data from cursor and saving it in Data holder
	    while (cursor.moveToNext()) {
	    	DataHolder holder = new DataHolder();
	    	holder.setMediapath(cursor.getString(column_file_path));
	    	holder.setThumbnailData(cursor.getString(column_thumbnail_data));
	        listOfAllImages.add(holder);
	    }
	    
	    mGalleryUri = null;
	    cursor = null;
	    column_file_path = 0;
	    column_thumbnail_data = 0;
	    mProjection = null;
	    
	    return listOfAllImages;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//go to preview page
		if(item.getItemId() == R.id.item_done){
			gotoPreview();
		}
	    //reset all selections and hides actionbar items
		else if(item.getItemId() == R.id.item_close){
			this.resetActionbar();
			mGallerypopulator.notifyDataSetInvalidated();
		}
		//finishes the current activity	
		else {
			this.resetActionbar();
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_child_gallery_activity, menu);
		super.onCreateOptionsMenu(menu);
		//hiding selected item count
		menu.getItem(0).setVisible(mShowMenuItems);
		menu.getItem(0).setTitle(String.valueOf(mItemCounter));
		//hiding done button
		menu.getItem(1).setVisible(mShowMenuItems);
		//hiding close button
		menu.getItem(2).setVisible(mShowMenuItems);
		return true;
	}


	@Override
	public void updateCounter(boolean value) {
		
		//if false, counter decrements otherwise increments
		if(value)
			mItemCounter = mItemCounter + 1;
		else
			mItemCounter = mItemCounter - 1;
		
		/*if selected item count is less than 1 ,
		then resets the action bar*/
		if(mItemCounter < 1)
			this.resetActionbar();
		else
			supportInvalidateOptionsMenu();
		
	}

	@Override
	public void toggleActionBarItems(boolean action) {
		
		/*since we are using the same itemcounter in onResume()
		we need to make it as 0 by default , and check if it is 0
		and action true, we need to make it as 1 and show it*/
		if((mItemCounter == 0) && action)
			mItemCounter = 1;
		mShowMenuItems = action;
		//hiding/showing actionbar title
		getSupportActionBar().setDisplayShowTitleEnabled(!action);
		supportInvalidateOptionsMenu();
	}
	
	
	/**
	 * function for reseting actionbar items..
	 * part of CAB type actionbar
	 *
	 */
	private void resetActionbar(){
		
		mItemCounter = 0;
		//clearing all selected items
		mGallerypopulator.clearSelectedItemList();
		//disabling multislelection
		mGallerypopulator.setMultiSelectionEnabled(false);
		//hiding action bar items
		toggleActionBarItems(false);
	}
	
	
	/**
	 * function for showing preview page 
	 */
	public void gotoPreview(){
		
		//getting all selected items 
		mSelectedIndices = mGallerypopulator.getSelectedItemList();
		
		if(mSelectedIndices.size() <= 0)
			return;
		
		//creating parcelable arraylist
		mSelectedFiles = new ArrayList<Data>();
		
		Data dataMover;
		//inserting selected items and its data in pareclable arraylist
		for(String position : mSelectedIndices){
			dataMover = new Data();
			dataMover.setMediapath(mFiles.get(Integer.valueOf(position))
					.getMediaPath());
			dataMover.setThumbnailData(mFiles.get(Integer.valueOf(position))
					.getThumbnailData());
			mSelectedFiles.add(dataMover);
		}
		
		//starting preview activity
		Intent intent = new Intent(this, PreviewActivity.class);
		intent.putParcelableArrayListExtra(PreviewActivity.PARCEL, mSelectedFiles);
		intent.putExtra(PreviewActivity.PREVIEW_TYPE, mGalleryType);
		startActivityForResult(intent, PREVIEW_REQUEST);
		
		//System.gc();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == PREVIEW_REQUEST){
			switch (resultCode) {
			// closes the current activity
			case PreviewActivity.PREVIEW_CANCEL:
				finish();
				break;
			//resets actionbar 
			case  RESULT_CANCELED:
				this.resetActionbar();
				mGallerypopulator.notifyDataSetInvalidated();
				break;
		    //repopulate the gridview at onResume()
			case PreviewActivity.ADD_MORE:
				mItemCounter = mSelectedFiles.size();
				break;
			case PreviewActivity.PREVIEW_DONE:
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra(SamsGallery.RESULT, mSelectedFiles);
				this.setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		SavedIndexes  index;
		//getting all selected items
		mSelectedIndices = mGallerypopulator.getSelectedItemList();
		if(mSelectedIndices.size() <= 0)
			return;
		
		//making parcelable arraylist for saving the current state
		mSavedSelectedIndices = new ArrayList<SavedIndexes>();
		for(String position : mSelectedIndices){
			index =  new  SavedIndexes();
			index.setPositions(position);
			mSavedSelectedIndices.add(index);
		}
		outState.putInt(STATE_SELECTED_ITEM_COUNT, mItemCounter);
		outState.putParcelableArrayList(STATE_SAVED_ITEMS, mSavedSelectedIndices);
		super.onSaveInstanceState(outState);
	}
	

}
