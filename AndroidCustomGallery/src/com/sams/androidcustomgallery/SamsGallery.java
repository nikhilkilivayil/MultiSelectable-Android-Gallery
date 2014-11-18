package com.sams.androidcustomgallery;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * SamsGallery - Custom Android Gallery<br>
 * Entry point of the app<br>
 * Displays all gallery folders<br>
 * It can be either video or image folder
 * @author Sam Rajan
 *
 */
public class SamsGallery extends ActionBarActivity {
	
	
	// request code for starting child activity
	private static final int  CHILD_REQUEST = 1;
	
	//Gallery Type
	public static final String GALLERY_TYPE = "gallery_type";
	//Multi-selectable
	//public static final String MUTLISELECTABLE = "multiselectable";
	
	//Result item
	public static final String RESULT = "result";
	public static final String VIDEOS = "videos";
	public static final String IMAGES = "images";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		try{
			super.onCreate(savedInstanceState);
		}
		catch(Exception exceptions){
			exceptions.printStackTrace();
		}
		
		setContentView(R.layout.activity_sams_gallery);
		
		String galleryType;
		
		if(getIntent().getExtras() != null)
			galleryType = getIntent().getStringExtra(GALLERY_TYPE);
		else
			galleryType = IMAGES; //default gallery is images
		
		
			    	
		Fragment fragment = new GallerySwitcherFragment();
	    Bundle args = new Bundle();
    	args.putString(GallerySwitcherFragment.ARGS, galleryType);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
        	.replace(R.id.ll_gridHolder, fragment).commit();
        
        fragment = null;
        args = null;
	        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	}
	
	
       
	/**
	 * Force actionbar tabs to display on action bar
	 * @param actionbarObject activities actionbar object
	 *//*
	public void forceTabs(Object actionbarObject) {
        try {
           
        	Java Reflection Method
        	Checking whether there is a function called 'setHasEmbeddedTabs'
        	if not there, an exception would occur
            final Method setHasEmbeddedTabsMethod = actionbarObject.getClass()
                .getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            
            //making 'setHasEmbeddedTabs' method accessible
            setHasEmbeddedTabsMethod.setAccessible(true);
            //invoking 'setHasEmbeddedTabs' method
            setHasEmbeddedTabsMethod.invoke(actionbarObject, true);
        }
        catch(final Exception e) {
            // Handle issues as needed: log, warn user, fallback etc
            // This error is safe to ignore, standard tabs will appear.
        }
    }
	*/
	
    public void startChildActivity(Intent intent){
    	//starting the child activity
    	startActivityForResult(intent, CHILD_REQUEST);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == CHILD_REQUEST){
			switch (resultCode) {
			 
			case  RESULT_CANCELED:
				finish();
				break;
			case RESULT_OK:
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra(SamsGallery.RESULT, 
						data.getParcelableArrayListExtra(RESULT));
				this.setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    
}
