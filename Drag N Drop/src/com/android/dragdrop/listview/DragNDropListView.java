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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

/**
 * Custom ExpandableListView which enables drag and drop
 * 
 * @author <a href="http://sreekumar.sh" >Sreekumar SH </a>
 *         (sreekumar.sh@gmail.com)
 * 
 */
public class DragNDropListView extends ExpandableListView {

	boolean mDragMode;

	int[] mStartPosition = new int[2];
	int[] mEndPosition = new int[2];
	int[] mMovePosition = new int[2];
	int mDragPointOffset; // Used to adjust drag view location
	int mStartFlatPosition;
	int prevY = -1;
	float screenHeight;
	float dragRatio;
	ImageView mDragView;
	GestureDetector mGestureDetector;

	DropListener mDropListener;
	RemoveListener mRemoveListener;
	DragListener mDragListener;

	public DragNDropListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenHeight = display.getWidth();
	}

	public void setDropListener(DropListener l) {
		mDropListener = l;
	}

	public void setRemoveListener(RemoveListener l) {
		mRemoveListener = l;
	}

	public void setDragListener(DragListener l) {
		mDragListener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();
		if (prevY < 0) {
			prevY = y;
		}
		long packagedPosition = 0;
		int flatPosition = 0;
		flatPosition = pointToPosition(x, y);
		dragRatio = getHeight() / screenHeight;
		packagedPosition = getExpandableListPosition(flatPosition);
		if (action == MotionEvent.ACTION_DOWN && x < this.getWidth() / 4) {
			if (getPackedPositionType(packagedPosition) == 1)
				mDragMode = true;
		}
		if (!mDragMode)
			/** when user action on other areas */
			return super.onTouchEvent(ev);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mStartFlatPosition = flatPosition;
			mStartPosition[0] = getPackedPositionGroup(packagedPosition);
			mStartPosition[1] = getPackedPositionChild(packagedPosition);

			if (packagedPosition != PACKED_POSITION_VALUE_NULL) {

				int mItemPosition = flatPosition - getFirstVisiblePosition();
				mDragPointOffset = y - getChildAt(mItemPosition).getTop();
				mDragPointOffset -= ((int) ev.getRawY()) - y;
				startDrag(mItemPosition, y);
				
				drag(x, y);// replace 0 with x if desired
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int speed = (int) ((y - prevY) * dragRatio);
			if (getLastVisiblePosition() < getCount() && speed > 0) {
				smoothScrollBy(speed, 1);
			}
			if (getFirstVisiblePosition() > 0 && speed < 0) {
				smoothScrollBy(speed, 1);
			}
			drag(0, y);// replace 0 with x if desired
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		default:

			mDragMode = false;
			if (getPackedPositionType(packagedPosition) == 0) {
				mEndPosition[0] = getPackedPositionGroup(packagedPosition);
				mEndPosition[1] = 0;
			} else {
				mEndPosition[0] = getPackedPositionGroup(packagedPosition);
				mEndPosition[1] = getPackedPositionChild(packagedPosition);
			}

			stopDrag(mStartFlatPosition);
			if (mDropListener != null
					&& packagedPosition != PACKED_POSITION_VALUE_NULL)
				mDropListener.onDrop(mStartPosition, mEndPosition);
			break;
		}
		prevY = y;
		return true;
	}

	// move the drag view
	private void drag(int x, int y) {
		if (mDragView != null) {
			WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mDragView
					.getLayoutParams();
			layoutParams.x = x;
			layoutParams.y = y - mDragPointOffset;
			WindowManager mWindowManager = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			mWindowManager.updateViewLayout(mDragView, layoutParams);

			if (mDragListener != null)
				mDragListener.onDrag(x, y, this);// change null to "this" when
													// ready to use
		}
	}

	// enable the drag view for dragging
	private void startDrag(int itemIndex, int y) {
		// stopDrag(itemIndex);

		View item = getChildAt(itemIndex);
		if (item == null)
			return;
		item.setDrawingCacheEnabled(true);
		int defaultBackgroundColor = item.getDrawingCacheBackgroundColor();
		if (mDragListener != null)
			mDragListener.onStartDrag(item, mStartPosition);

		// Create a copy of the drawing cache so that it does not get recycled
		// by the framework when the list tries to clean up memory
		Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
		item.setBackgroundColor(defaultBackgroundColor);
		

		WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP;
		mWindowParams.x = 0;
		mWindowParams.y = y - mDragPointOffset;

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		Context context = getContext();
		ImageView v = new ImageView(context);
		v.setImageBitmap(bitmap);

		WindowManager mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
	}

	/**
	 * destroy the drag view
	 * 
	 * @param itemIndex
	 *            Index of the item
	 */

	private void stopDrag(int itemIndex) {
		int firstPosition = getFirstVisiblePosition() - getHeaderViewsCount();
		int wantedChild = itemIndex - firstPosition;
		if (mDragView != null) {
			if (wantedChild < 0 || wantedChild >= getChildCount()) {
				
			}
			else if (mDragListener != null){
				mDragListener.onStopDrag(getChildAt(wantedChild));
			}
			mDragView.setVisibility(GONE);
			WindowManager wm = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
	}

}
