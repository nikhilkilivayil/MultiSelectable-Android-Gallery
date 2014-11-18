package com.example.com.sams.customgallerypickersample;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sams.androidcustomgallery.SamsGallery;
import com.sams.databinders.Data;

public class MainActivity extends Activity {
	
	private ListView mListView ;
	 
	public static final int REQUEST_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get ListView object from xml
        mListView = (ListView) findViewById(R.id.list);

    }
    
    public void buttonClick(View view){	
    	Intent intent = new Intent(this, SamsGallery.class);
    	startActivityForResult(intent, REQUEST_GALLERY);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK ){
			
			if(requestCode == REQUEST_GALLERY){
				
				if(data ==null)
					return;
				
				ArrayList<Data> datalist = data.getParcelableArrayListExtra(SamsGallery.RESULT);
				final String values[] = new String[datalist.size()];
				int index = 0;
				for(Data imageData: datalist){
					values[index] = imageData.getMediaPath();
					index++;
				}
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, values);
	    
	            // Assign adapter to ListView
	            mListView.setAdapter(adapter); 
	            
	            final LayoutInflater inflater = getLayoutInflater();
	            final View layout = inflater.inflate(R.layout.layout_toast,
	                    (ViewGroup) findViewById(R.id.root));

	            final ImageView image = (ImageView) layout.findViewById(R.id.img_image);
	            
	            
	            mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
			            image.setImageURI(Uri.fromFile(new File(values[position])));
			            Toast toast = new Toast(getApplicationContext());
			            toast.setDuration(Toast.LENGTH_LONG);
			            toast.setView(layout);
			            toast.show();
			            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.FILL_VERTICAL, 0, 0);
						
					}
				});
				
			}
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    
}
