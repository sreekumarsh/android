package com.example.momentage;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momentage.models.Photo;

public class ImageAdapter extends BaseAdapter {

	ArrayList<Photo> mPhotos;
	Context mContext;

	public ImageAdapter(Context context, ArrayList<Photo> photos) {
		mPhotos = photos;
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return mPhotos == null ? 0 : mPhotos.size();
	}
	public void addPhoto(Photo photo){
		mPhotos.add(photo);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.row_photo, null);
		}
		ImageView photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
		
		byte[] decodedImage = Base64.decode(mPhotos.get(position).getData(),
				Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0,
				decodedImage.length);
		photo.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 120, 120, false));
		TextView name = (TextView) convertView.findViewById(R.id.tvName);
		name.setText(mPhotos.get(position).getName());
		return convertView;
	}
}
