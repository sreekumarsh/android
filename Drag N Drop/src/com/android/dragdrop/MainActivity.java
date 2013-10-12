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


package com.android.dragdrop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.android.dragdrop.listview.DragListener;
import com.android.dragdrop.listview.DragNDropAdapter;
import com.android.dragdrop.listview.DragNDropListView;
import com.android.dragdrop.listview.DropListener;
import com.android.dragdrop.listview.RemoveListener;
/**
 * Activity that shows the list view
 * @author <a href="http://sreekumar.sh" >Sreekumar SH </a> (sreekumar.sh@gmail.com)
 *
 */
public class MainActivity extends Activity {
	
	/**group items*/
	private ArrayList<String> groups;
	/**children items with a key and value */
    private ArrayList<Map<String,String>> children;
    DragNDropListView eList;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getData();
        eList = (DragNDropListView) findViewById(R.id.list_view_customizer);
        eList.setAdapter(new DragNDropAdapter(this, new int[]{R.layout.row_item}, new int[]{R.id.txt__customizer_item},  groups, children));
        
        if (eList instanceof DragNDropListView) {
        	((DragNDropListView) eList).setDropListener(mDropListener);
        	((DragNDropListView) eList).setRemoveListener(mRemoveListener);
        	((DragNDropListView) eList).setDragListener(mDragListener);
        }
        
        
        
    }
    /**implementing the drop listener*/
    private DropListener mDropListener = 
    		new DropListener() {
            public void onDrop(int[] from, int[] to) {
            	ExpandableListAdapter adapter = eList.getExpandableListAdapter();
            	if (adapter instanceof DragNDropAdapter) {
            		((DragNDropAdapter)adapter).onDrop(from, to); // send the event to adapter and manipulate items in the arraylists
            		eList.invalidateViews(); //redraw the list view
            	}
            }
        };
        /**remove item from the list*/
        private RemoveListener mRemoveListener =
            new RemoveListener() {
            public void onRemove(int which[]) {
            	ExpandableListAdapter adapter = eList.getExpandableListAdapter();
            	if (adapter instanceof DragNDropAdapter) {
            		((DragNDropAdapter)adapter).onRemove(which);
            		eList.invalidateViews();
            	}
            }
        };
        
        private DragListener mDragListener =
        	new DragListener() {

        	int backgroundColor = 0xe0103010; // different color to identify
        	int defaultBackgroundColor;
        	
    			public void onDrag(int x, int y, ExpandableListView listView) {
    				// TODO Auto-generated method stub
    			}

    			public void onStartDrag(View itemView) {
    				itemView.setVisibility(View.INVISIBLE); // make the item invisible as we have picked it 
    				defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor(); // fill the empty space with default color
    				itemView.setBackgroundColor(backgroundColor);
    				ImageView iv = (ImageView)itemView.findViewById(R.id.move_icon_customizer_item);
    				if (iv != null) iv.setVisibility(View.INVISIBLE);
    			}

    			public void onStopDrag(View itemView) {
    				itemView.setVisibility(View.VISIBLE);
    				itemView.setBackgroundColor(defaultBackgroundColor);
    				ImageView iv = (ImageView)itemView.findViewById(R.id.move_icon_customizer_item);
    				if (iv != null) iv.setVisibility(View.VISIBLE);
    			}
        	
        };
    
    
    
    /**
     * simple function to fill the list
     */
    
    private void getData()
    {
    	groups = new ArrayList<String>();
    	children = new ArrayList<Map<String,String>>();
        for(int i = 0;i<4 ;i++)
        {
        	groups.add("group"+i);
        	
        }
        for(String s : groups)
        { 
        	Map<String, String> chList = Collections
    				.synchronizedMap(new LinkedHashMap<String, String>());
        	for(int i = 0;i<4 ;i++)
            {
        		
        		chList.put(s+"item"+i,s+"value"+i);
            	
            	
            }
        	children.add(chList);
        }
    }
    
}