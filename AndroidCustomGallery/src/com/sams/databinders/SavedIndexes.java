package com.sams.databinders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for holding position if selected views
 *  while saving the activity state
 * @author Sam Rajan
 *
 */
public class SavedIndexes implements Parcelable {
	
	//position
	private String position;
	
	
	public SavedIndexes(){}
	
	public static final Parcelable.Creator<SavedIndexes> CREATOR = new Creator<SavedIndexes>() {
		
		@Override
		public SavedIndexes[] newArray(int size) {
			return new SavedIndexes[size];
		}
		
		@Override
		public SavedIndexes createFromParcel(Parcel source) {
			return new SavedIndexes(source);
		}
	};

	public SavedIndexes(Parcel source) {
		readFromParcel(source);
	}

	private void readFromParcel(Parcel source) {
		position = source.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(position);
	}
	
	
	/**
	 * function for getting position
	 * @return position
	 */
	public String getPosition() {
		return position;
	}
	
	
	/**
	 * function for setting position
	 * @param position
	 */
	public void setPositions(String positions) {
		this.position = positions;
	}

	
	
}
