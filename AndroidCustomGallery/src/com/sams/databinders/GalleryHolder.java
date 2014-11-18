package com.sams.databinders;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.sams.customcomponents.GalleryFrameLayout;
import com.sams.customcomponents.RecyclingImageView;

/**
 * GalleryHolder - view holder for gallery items
 * @author Sam Rajan
 *
 */
class GalleryHolder {

	//base custom framelayout
	GalleryFrameLayout baseLayout	    = null;
	
	//base framelayout
	FrameLayout 	   listBaseLayout   = null;
	
	//imageview holding thumbnail image
	RecyclingImageView folderThumbnail  = null;
	
	//textview holding folder image
	TextView           folderName       = null;
}
