package com.ekincare.ui.custom;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.ekincare.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyHorizontalLayout extends LinearLayout {

	ImageLoader imageLoader1;
	Context myContext;
	ArrayList<String> itemList = new ArrayList<String>();

	public void initializeImageLoader(){
		imageLoader1 = ImageLoader.getInstance();
		imageLoader1.init(ImageLoaderConfiguration.createDefault(myContext));
	}

	public MyHorizontalLayout(Context context) {
		super(context);
		myContext = context;

	}

	public MyHorizontalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		myContext = context;
	}

	public MyHorizontalLayout(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		myContext = context;
	}

	public void add(String path){
		int newIdx = itemList.size();
		itemList.add(path);

		addView(getImageView(newIdx));
	}

	ImageView getImageView(final int i){
		//		Bitmap bm = null;
		//		if (i < itemList.size()){
		//			bm = decodeSampledBitmapFromUri(itemList.get(i), 220, 220);
		//		}

		final ImageView imageView = new ImageView(myContext);
		imageView.setLayoutParams(new LayoutParams(getResources().getInteger(R.integer.int_200), getResources().getInteger(R.integer.int_200)));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//		imageView.setImageBitmap(bm);
		imageView.setPadding(getResources().getInteger(R.integer.int_10),getResources().getInteger(R.integer.int_10),
				getResources().getInteger(R.integer.int_10),getResources().getInteger(R.integer.int_10));

		DisplayImageOptions options1 = new DisplayImageOptions.Builder()
		.bitmapConfig(Bitmap.Config.ARGB_8888)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.resetViewBeforeLoading(true)
		.build();


		//        animateFirstListener1 = new Constant.AnimateFirstDisplayListener(new ProgressBar(Act_Comm.this));

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		final int DeviceTotalWidth = metrics.widthPixels;
		int DeviceTotalHeight = metrics.heightPixels;

		//		Display display = myContext.getWindowManager().getDefaultDisplay();
		//		Point size = new Point();
		//		display.getSize(size);
		//		final int width = size.x;

		imageView.setTag(i);
		imageLoader1.displayImage(itemList.get(i), imageView, options1);
		imageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
//				Toast.makeText(myContext, "Clicked - " + itemList.get(i), Toast.LENGTH_LONG).show();
				int scrollX = (imageView.getLeft() - (DeviceTotalWidth / 2)) + (imageView.getWidth() / 2);
				//				int scrollX = (DeviceTotalWidth / 2) - (imageView.getWidth() / 2);
				((HorizontalScrollView)getParent()).smoothScrollTo(scrollX, 0);
			}
		});


		return imageView;
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//		BitmapFactory.decodeFile(path, options);

		//		InputStream iss;
		//		try {
		//			iss = new URL(path).openStream();
		//			BitmapFactory.decodeStream(iss);
		//		} catch (MalformedURLException e) {
		//			e.printStackTrace();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		//		bm = BitmapFactory.decodeFile(path, options);

		InputStream is;
		try {
			is = new URL(path).openStream();
			bm = BitmapFactory.decodeStream(is,null,options);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bm;
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}

		return inSampleSize;
	}

}