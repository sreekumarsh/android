package com.example.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.listview.imageload.ImageLoader;
import com.example.listview.models.ListItemModel;

public class ListAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;
	private ListItemModel[] items;

	public ListAdapter(Context context, ListItemModel[] items) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.items = items;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (items != null) {
			return items.length;
		}
		return 0;
	}

	@Override
	public ListItemModel getItem(int position) {
		// TODO Auto-generated method stub
		if (items != null && position < items.length) {
			return items[position];
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;

		if (convertView == null) {

			view = inflater.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.txtTitle);
			holder.description = (TextView) view
					.findViewById(R.id.txtDescription);
			holder.image = (ImageView) view.findViewById(R.id.imgPhoto);
			holder.thumbnail = (LinearLayout) view.findViewById(R.id.thumbnail);

			/************ Set holder with LayoutInflater ************/
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText(items[position].getTitle());
		holder.description.setText(items[position].getDescription());
		ImageView image = holder.image;
		image.setTag(holder);
		if (items[position].getImageHref() != null
				&& items[position].getImageHref().length() > 0) {
			// DisplayImage function from ImageLoader Class
			imageLoader.DisplayImage(items[position].getImageHref(), image);
		} else {
			// hide the layout if image not available
			holder.thumbnail.setVisibility(View.GONE);
		}
		return view;
	}
	
	/**
	 * Hide the thumbnail layout if image not found
	 * @author Sreekumar
	 *
	 */
	public interface InvalidUrlHandler{
		public void hideThumbnail();
	}

	/**
	 * Holder Class to contain inflated xml file elements
	 * 
	 * 
	 */
	public static class ViewHolder implements InvalidUrlHandler{

		public TextView title;
		public TextView description;
		public ImageView image;
		public LinearLayout thumbnail;
		@Override
		public void hideThumbnail() {
			// TODO Auto-generated method stub
			thumbnail.setVisibility(View.GONE);
		}

	}

}
