package com.sams.databinders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for holding data while parceling
 * from one activity to another
 * @author Sam Rajan
 *
 */
public class Data implements Parcelable {

	//file path of the media
	private String mMediaPath;
	//thumbnail id
	private String mThumbnailData;
	
	public static final Parcelable.Creator<Data> CREATOR = new Creator<Data>() {
		
		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
		
		@Override
		public Data createFromParcel(Parcel source) {
			return new Data(source);
		}
	};
	
	public Data(Parcel source) {
		readFromParcel(source);
	}

	public Data() {}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMediaPath);
		dest.writeString(mThumbnailData);
	}
	
	
	public void readFromParcel(Parcel source) {
		mMediaPath = source.readString();
		mThumbnailData = source.readString();
	}
	
	/**
	 * function for getting media path
	 * @return media path
	 */
	public String getMediaPath(){
		return mMediaPath;
	}
	
	/**
	 * function for getting media id
	 * @return thumbnail id
	 */
	public String getThumbnailData(){
		return mThumbnailData;
	}
	
	
	/**
	 * function for setting media path
	 * @param mediaPath
	 */
	public void setMediapath(String mediaPath){
		mMediaPath = mediaPath; 
	}
	
	
	/**
	 * function for setting thumbnail id
	 * @param thumbnailData
	 */
	public void setThumbnailData(String thumbnailData){
		mThumbnailData = thumbnailData;
	}
	

}
