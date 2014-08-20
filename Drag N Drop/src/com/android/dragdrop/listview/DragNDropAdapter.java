/*
 * Copyright (C) 2012 Sreekumar SH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.dragdrop.listview;

import java.util.ArrayList;
import java.util.Map;

import com.android.dragdrop.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for the drag and drop listview
 * 
 * @author <a href="http://sreekumar.sh" >Sreekumar SH </a>
 *         (sreekumar.sh@gmail.com)
 * 
 */
public final class DragNDropAdapter extends BaseExpandableListAdapter{

	private int[] mIds;
	private int[] mLayouts;
	private int selectedGroup;
	private int selectedChild;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<String> groups;
	private Map<String, ArrayList<String>> children;

	public DragNDropAdapter(Context context, Map<String, ArrayList<String>> child) {
		init(context, new int[] { android.R.layout.simple_list_item_1 },
				new int[] { android.R.id.text1 },child);
	}

	public DragNDropAdapter(Context context, int[] itemLayouts, int[] itemIDs,
			Map<String, ArrayList<String>> child) {
		init(context, itemLayouts, itemIDs, child);
	}

	private void init(Context context, int[] layouts, int[] ids,
			 Map<String, ArrayList<String>> child) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);
		mIds = ids;
		mLayouts = layouts;
		groups = new ArrayList<String>();
		groups.addAll(child.keySet());
		mContext = context;
		children = child;

	}

	public void onPick(int[] position) {
		selectedGroup = position[0];
		selectedChild = position[1];
	}

	static class ViewHolder {
		TextView text;
	}

	public void onDrop(int[] from, int[] to) {
		if (to[0] > children.size() || to[0] < 0 || to[1] < 0)
			return;
		String tValue = getValue(from);
		children.get(children.keySet().toArray()[from[0]]).remove(tValue);
		children.get(children.keySet().toArray()[to[0]]).add(to[1], tValue);
		selectedGroup = -1;
		selectedChild = -1;
		notifyDataSetChanged();
	}

	private String getValue(int[] id) {
		return (String) children.get(children.keySet().toArray()[id[0]]).get(id[1]);
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return children.get(children.keySet().toArray()[groupPosition]).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is no
		// need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(mLayouts[0], null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(mIds[0]);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		holder.text.setText((String) (getChild(groupPosition, childPosition)));
		if (groupPosition != selectedGroup && childPosition != selectedChild) {
			convertView.setVisibility(View.VISIBLE);
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.move_icon_customizer_item);
			iv.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return children.get(children.keySet().toArray()[groupPosition]).size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv = getGenericView();
		tv.setText(groups.get(groupPosition));

		return tv;
	}

	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 64);

		TextView textView = new TextView(mContext);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(36, 0, 0, 0);
		return textView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
}