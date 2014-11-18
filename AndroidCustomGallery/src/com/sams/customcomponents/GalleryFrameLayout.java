package com.sams.customcomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sams.androidcustomgallery.R;

/**
 * GalleryFrameLayout - custom framelayout for handiling
 * gallery item click and resizing according to gridview cell size 
 * @author Sam Rajan
 *
 */
public class GalleryFrameLayout extends FrameLayout {

	//selected state
	private boolean mChecked = false;
	
	public GalleryFrameLayout(Context context) {
		super(context);
	}
	
	public GalleryFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GalleryFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//getting  default width and height
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);      
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    
	    //getting device width and height
	    double density = getResources().getDisplayMetrics().density;
	    
	    //taking 159px as reference value and converting it into dp
	    double referenceWidth = 159 * density;
	    //change in size
	    double ratio =  (widthSize/referenceWidth);
	    
	    //multiplying height  with ratio 
	    heightSize = (int) (heightSize * ratio);
	    heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
	    
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}


	/**
	 * function for setting selected state of view
	 * @param checked boolean value
	 */
	public void setChecked(boolean checked) {
		
		 mChecked = checked;
		 if(checked)
			this.setForeground(getResources().getDrawable(R.drawable.abc_list_longpressed_holo));
		 else
			 this.setForeground(getResources().getDrawable(R.drawable.gridview_selector));
		
	}

	/**
	 * @return boolean value indicating checked state
	 */
	public boolean isChecked() {
		return mChecked;
	}

}
