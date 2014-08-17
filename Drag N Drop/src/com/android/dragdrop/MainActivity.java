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

import com.android.dragdrop.listview.DragNDropAdapter;
import com.android.dragdrop.listview.DragNDropListView;
/**
 * Activity that shows the list view
 * @author <a href="http://sreekumar.sh" >Sreekumar SH </a> (sreekumar.sh@gmail.com)
 *
 */
public class MainActivity extends Activity {
	
	/**children items with a key and value list */
    private Map<String, ArrayList<String>> children;
    DragNDropListView eList;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getData();
        eList = (DragNDropListView) findViewById(R.id.list_view_customizer);
        eList.setDragOnLongPress(true);
        eList.setAdapter(new DragNDropAdapter(this, new int[]{R.layout.row_item}, new int[]{R.id.txt__customizer_item}, children));
    }
    
    /**
     * simple function to fill the list
     */
    
    private void getData()
    {
    	ArrayList<String> groups = new ArrayList<String>();
    	children = Collections
				.synchronizedMap(new LinkedHashMap<String, ArrayList<String>>());
        for(int i = 0;i<4 ;i++)
        {
        	groups.add("group "+i);
        	
        }
        for(String s : groups)
        { 
        	ArrayList<String> child = new ArrayList<String>();
        	
        	
        	for(int i = 0;i<4 ;i++)
            {
        		
        		child.add(s+" -value"+i);
            	
            	
            }
        	children.put(s, child);
        }
    }
    
}