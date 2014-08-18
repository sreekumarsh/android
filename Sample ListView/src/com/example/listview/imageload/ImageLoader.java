package com.example.listview.imageload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.example.listview.ListAdapter.InvalidUrlHandler;
import com.example.listview.MainActivity;

public class ImageLoader {

	private BitmapCache memoryCache;
	private FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	// handler to display images in UI thread
	private static Handler handler = new Handler();

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		// use 25% of available heap
		memoryCache = new BitmapCache(Runtime.getRuntime().maxMemory() / 4);
		executorService = MainActivity.getExecutor();
	}

	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			((InvalidUrlHandler) imageView.getTag()).hideThumbnail();
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad photoLoad = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(photoLoad));
	}

	private Bitmap getBitmap(String url) {
		File file = fileCache.getFile(url);

		// from SD cache
		Bitmap bitmapFile = decodeFile(file);
		if (bitmapFile != null)
			return bitmapFile;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(file);
			copyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(file);
			return bitmap;
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			memoryCache.clear();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * copy stream to file
	 * 
	 * @param in
	 *            - InputStream
	 * @param out
	 *            - OutputStream
	 */
	private void copyStream(InputStream in, OutputStream out) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = in.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				out.write(bytes, 0, count);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 *  decodes image file to {@code Bitmap} and scales it to reduce memory consumption
	 * @param file - File
	 * @return - Bitmap
	 */
	private Bitmap decodeFile(File file) {
		try {
			// decode image size
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(file);
			BitmapFactory.decodeStream(stream1, null, opts);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = opts.outWidth, height_tmp = opts.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		private String url;
		private ImageView imageView;

		public PhotoToLoad(String url, ImageView img) {
			this.url = url;
			imageView = img;
		}
	}

	private class PhotosLoader implements Runnable {
		private PhotoToLoad photoToLoad;

		public PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}catch (OutOfMemoryError ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Check if the imageView is reused;
	 * No need to update if imageView is reused
	 * */
	private boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	/**
	 * Used to display bitmap in the UI thread
	 * 
	 * @author Sreekumar
	 * 
	 */
	private class BitmapDisplayer implements Runnable {
		private Bitmap bitmap;
		private PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				((InvalidUrlHandler) photoToLoad.imageView.getTag())
						.showThumbnail();
				photoToLoad.imageView.setImageBitmap(bitmap);
			} else {
				// hide the parent thumbnail if url cannot be reached
				((InvalidUrlHandler) photoToLoad.imageView.getTag())
						.hideThumbnail();
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}
