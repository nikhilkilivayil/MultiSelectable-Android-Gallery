package com.sams.androidcustomgallery;

import java.util.TreeMap;

import com.sams.databinders.GalleryPopulator;
import com.sams.workers.CacheManager;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * GallerySwitcherFragment - fragment holding seperated video 
 * and images in parent activity<br>
 * Used with Actionbar Tabs
 * @author Sam Rajan
 *
 */
public class GallerySwitcherFragment extends Fragment {
	
	//list of all gallery folders 
	private TreeMap<String, String> mFolders;
	private GridView                mGalleryView;
	
	//fields needed from gallery content provider
	private static String[]         mProjection;
	
	//gallery content provider uri
	private static Uri              mGalleryUri;
	
	//current gallery type - video or image
	private String 					mGalleryType;
	
	//cache for thumbnail images
	private CacheManager            mCacheManager = null;  

	//constants, the keys for activity operations
	public static final String ARGS   = "arguments";
	
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
   
        View rootView = inflater.inflate(R.layout.fragment_gallery_view, container, false);
        Bundle args = getArguments();
        mGalleryType = args.get(ARGS).toString();
        mCacheManager = new CacheManager(getActivity(), 
        		getActivity().getSupportFragmentManager());
      //assigning gallery uri, needed fields, selection for query according to gallery type
        if(mGalleryType.equals(SamsGallery.IMAGES)){
        	mGalleryUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        	mProjection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME , MediaStore.Images.Thumbnails._ID};
        }
        else{
        	mGalleryUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        	mProjection = new String[]{MediaStore.Video.Media.BUCKET_DISPLAY_NAME , MediaStore.Video.Thumbnails._ID};
        }
        	
        
        
        mGalleryView = (GridView) rootView.findViewById(R.id.gv_gallery);
        //populating gallery
		this.populateGallery();
		
        return rootView;
    }
	
	
	private void populateGallery(){
		//listing all gallery folders
		mFolders = readGallery(getActivity());
		//forcing garbage collection
		System.gc();
		GalleryPopulator populator = new GalleryPopulator(getActivity()
				, R.layout.gridview_gallery_item, mFolders, mGalleryType, mCacheManager);
		mGalleryView.setAdapter(populator);
    
	}
	

	/**
	 * function for reading gallery folders
	 * @param activity current activity context
	 * @return Treemap containing folder name and folder image
	 */
	public static TreeMap<String, String> readGallery(Activity activity) {
	    
	    Cursor cursor;  
	    int column_thumbnail_data, column_index_folder_name;
	    //list of all gallery folders
	    TreeMap<String, String> listOfAllImages = new TreeMap<String, String>();
	    //cursor holding list of all gallery items
	    cursor = activity.getContentResolver().query(mGalleryUri, mProjection, null, null, null);

	    if(cursor == null)
	    	return listOfAllImages;
	    
	    column_thumbnail_data = cursor.getColumnIndexOrThrow(mProjection[1]);
	    column_index_folder_name = cursor.getColumnIndexOrThrow(mProjection[0]);
	    //getting data from cursor and saving it in treemap
	    while (cursor.moveToNext()) {
	        listOfAllImages.put(cursor.getString(column_index_folder_name), cursor.getString(column_thumbnail_data));
	    }
	    
	    mGalleryUri = null;
	    cursor = null;
	    column_index_folder_name = 0;
	    column_thumbnail_data = 0;
	    mProjection = null;
	    
	    return listOfAllImages;
	}

}
