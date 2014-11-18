package com.sams.androidcustomgallery;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.VideoView;

import com.sams.customcomponents.HorizontalListView;
import com.sams.databinders.Data;
import com.sams.databinders.DataHolder;
import com.sams.databinders.HorizontalListViewPopulator;
import com.sams.workers.CacheManager;

/**
 * PreviewActivity - Preview page for selected image<br>
 * if more than one image is selected, it would appear in 
 * a horizontal listview which facilitates preview of each
 * @author Sam Rajan
 *
 */
public class PreviewActivity extends ActionBarActivity {

	//selected files and data
	private ArrayList<DataHolder>		mFilesList;
	
	//parcelable array list for getting selected files from parent
	private ArrayList<Data>  		mSelectedFileList;
	
	private HorizontalListView    		mListView;
	
	//horizontal listview custom adapter
	private HorizontalListViewPopulator mListViewPopulator;
	
	private ImageView				    mImageView;
	private VideoView                   mVideoView;
	
	//cache for thumbnail in horizontal listview
	private CacheManager            	mCacheManager = null;
	
	//current gallery type
	private String 						mGalleryType;
	
	//index of image selected in horizontal list
	private int 						mPreviewImageIndex = 0;
	
	public static final String PARCEL          = "parcel";
	public static final String PREVIEW_TYPE    = "type";
	public static final int    PREVIEW_DONE    = 1;
	public static final int    PREVIEW_CANCEL  = -1;
	public static final int    ADD_MORE        = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		
		mSelectedFileList = getIntent().getExtras().getParcelableArrayList(PARCEL);
		mGalleryType = getIntent().getExtras().getString(PREVIEW_TYPE);
		mFilesList = new ArrayList<DataHolder>();
		mCacheManager = new CacheManager(this, getSupportFragmentManager());
		mListView = (HorizontalListView) findViewById(R.id.HorizontalListView);
		mImageView = (ImageView) findViewById(R.id.iv_imagePreview);
		mVideoView = (VideoView) findViewById(R.id.vv_videoPreview);
		
		
		Log.e("czxcxz", mGalleryType);
		
		//getting files and data from parcel
		DataHolder holder;
		for(Data mover : mSelectedFileList){
			holder = new DataHolder();
			holder.setMediapath(mover.getMediaPath());
			holder.setThumbnailData(mover.getThumbnailData());
			mFilesList.add(holder);
		}
		
		
		this.changePreview(0);
		
		//setting adapter for listview
		mListViewPopulator = new HorizontalListViewPopulator(this, R.layout.horizontal_listview_item,
				mGalleryType, mFilesList, mCacheManager);
		mListView.setAdapter(mListViewPopulator);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				changePreview(position);
			}
		});
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*closes the current activity and offers addition
		more images*/
		if(item.getItemId() == R.id.item_add){
			this.setResult(ADD_MORE);
			finish();
		}
		else if(item.getItemId() == android.R.id.home){
			finish();
		}
	
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_preview_activity, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}
	
	/**
	 * function for getting uri of selected image from list 
	 * @return Uri of selected image
	 */
	private Uri getPreviewUri(){
		return Uri.parse(mFilesList.get(mPreviewImageIndex).getMediaPath());
	}
	
	
	/**
	 * function for changing the preview image
	 * @param position
	 */
	public void changePreview(int position) {
		mPreviewImageIndex = position;
		//if image, hides the video view and shows the imageview
		if(mGalleryType.equals(SamsGallery.IMAGES)){
			mImageView.setImageURI(getPreviewUri());
			mImageView.setVisibility(View.VISIBLE);
			mVideoView.setVisibility(View.GONE);
		}else{
			mVideoView.setVideoURI(getPreviewUri());
			//for getting a preview in video view 
			mVideoView.seekTo(100);
			mVideoView.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
		}
	}
	
	
	public void onButtonClick(View v){
		
		if(v.getId() == R.id.btn_done){
			this.setResult(PREVIEW_DONE);
			finish();
		}
		else if(v.getId() ==  R.id.btn_cancel){
			this.setResult(PREVIEW_CANCEL);
			finish();
		}
		
	}
}
