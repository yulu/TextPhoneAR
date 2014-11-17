package com.research.textphonear;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.materials.textures.ATexture;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.math.vector.Vector3;
import rajawali.primitives.Plane;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.research.phonearlib.camera.TrackData;
import com.research.phonearlib.renderer.BaseRenderer;

public class TextRenderer extends BaseRenderer{
	private TextView tv;
	private Object3D mPlane;
	private Material mMaterial;

	private String text;
	private boolean changed = true;
	
	public TextRenderer(Context context, TrackData td, String text) {
		super(context, td);
		tv = new TextView(context);
		this.text = text;
	}
	
	
	protected void initScene() {
		//set material
		//setMaterial("Start");
	}

	@Override
	protected void drawModel(boolean tracked) {
		if(mPlane != null)
			mPlane.setVisible(false);
		
		if(tracked){	
			if(changed){
				//getCurrentScene().removeChild(mPlane);
				setMaterial(text);
				changed=false;
			}
			mPlane.setVisible(true);			
		}
		
	}
	
	public void setText(String s){
		this.text = s;
	}
	
	private void setMaterial(String s){
		mMaterial = new Material();
		try {
			if(text != null && text.length() > 0)
				mMaterial.addTexture(TextToTexture(s));
			else
				mMaterial.addTexture(TextToTexture(s));
			mMaterial.setColorInfluence(0);
		} catch (TextureException e) {
			e.printStackTrace();
		}
		
		mPlane = new Plane(8, 1, 1, 1);
		mPlane.setScale(0.2);
		mPlane.rotateAround(new Vector3(0,1,0), 180.0);
		mPlane.setDoubleSided(true);
		mPlane.setMaterial(mMaterial);
		
		getCurrentScene().addChild(mPlane);
	}
	
	private ATexture TextToTexture(String text){
		//set textview (from input text)
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 50);
	    tv.setLayoutParams(layoutParams);
	    tv.setText(text);
	    tv.setTextSize(10);
	    tv.setTextColor(Color.BLACK);
	    tv.setBackgroundColor(Color.WHITE);

	    //set bitmap (from textview)
	    Bitmap bitmap;

	    bitmap = Bitmap.createBitmap(400, 50, Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(bitmap);
	    tv.layout(0, 0, 400, 50);
	    tv.draw(c);
	    
	    //set texture (from bitmap)
	    ATexture texture = new Texture("image", bitmap);
		texture.setMipmap(true);
		texture.shouldRecycle(true);
		
		return mTextureManager.addTexture(texture);
	}

}
