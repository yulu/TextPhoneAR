package com.research.textphonear;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.research.phonearlib.NativeTracking;
import com.research.phonearlib.PhoneARActivity;
import com.research.phonearlib.camera.CameraFrame;
import com.research.phonearlib.camera.CvFrame.FrameAvailableListener;

public class MainActivity extends PhoneARActivity {

	private static final int			DISPLAY = 0;
	private static final int			TRAIN = 1;
	private static final int			TRACK = 2;
	
	private MenuItem			mItemTrain;
	private int					mMode = DISPLAY;
	private String 				mDisplay="";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCvFrame.resigsterFrameAvailableListener(new FrameAvailableListener(){

			@Override
			public void OnFrameAvailable(CameraFrame frame) {
				Mat resizeImage = new Mat(360, 640, CvType.CV_8UC1);
				Imgproc.resize(frame.gray(), resizeImage, resizeImage.size());
				
				switch(mMode){
				case TRAIN:
					NativeTracking.train(resizeImage.getNativeObjAddr());
					mMode = TRACK;
					break;
				case TRACK:
					float[] trans = new float[3];
					float[] rots = new float[4];
					boolean tracked = NativeTracking.track(resizeImage.getNativeObjAddr(), trans, rots);
					mTrackData.updateRots(rots);
					mTrackData.updateTrans(trans);
					mTrackData.setTracked(tracked);
					break;
				default:
					mMode = DISPLAY;
					break;
						
				}		
				
				resizeImage.release();
			}
			
		});
		
		requestInput();

        setRenderer();

	}
	
	private void setRenderer(){
		mRenderer = new TextRenderer(this, mTrackData, "Utown");

		mRenderer.registerRendererStartListener(mCvFrame);
		mRenderer.setSurfaceView(mSurfaceView);
		
		setRenderer(mRenderer);
	}
	
	private void requestInput(){
		LinearLayout layout = new LinearLayout(this);
		final EditText text_1 = new EditText(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        
		layout.setOrientation(LinearLayout.VERTICAL);
	        
        text_1.setHint("New Password");
        text_1.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(text_1);
        
        builder.setTitle("Input String to Display").setView(layout);
        
        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                final String result_1 = text_1.getText().toString();
                if (TextUtils.isEmpty(result_1)) {
                	
        		}else{
        			//http request
        			((TextRenderer) mRenderer).setText(result_1);
        			
        		}
                

            }
        });
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface di, int i) {
	            	//do nothing
	            }
	        });
	        
	        builder.create().show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		mItemTrain = menu.add("Train");
		
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item){
		if(item == mItemTrain) {
			mMode = TRAIN;
		}
		
		return true;
	}

}
