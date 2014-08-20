package com.example.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class TogglePreference extends Preference implements
		OnCheckedChangeListener {

	private String textOn;
	private String textOff;
	private boolean defaultValue;
	private ToggleButton toggleBtn;
	private Drawable icon;
	private TogglePreferenceChangedListener listener;

	public TogglePreference(Context context) {
		super(context);
	}

	public TogglePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		int[] attrsArray = new int[] { android.R.attr.textOn, // 0
				android.R.attr.textOff, // 1
				android.R.attr.defaultValue, // 2
				android.R.attr.icon // 3
		};
		TypedArray tArray = context.obtainStyledAttributes(attrs, attrsArray);
		textOff = tArray.getString(1);
		textOn = tArray.getString(0);
		icon = tArray.getDrawable(3);
		defaultValue = tArray.getBoolean(2, true);
		tArray.recycle();
	}

	public TogglePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnPreferrenceChangedListener(
			TogglePreferenceChangedListener checkListener) {
		listener = checkListener;
	}

	/**
	 * Inflates a custom layout for this preference, taking advantage of views
	 * with ids that are already being used in the Preference base class.
	 */
	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = inflater.inflate(R.layout.item_toggle_preference,
				parent, false);
		toggleBtn = (ToggleButton) convertView
				.findViewById(R.id.toggleBtnTogglePreference);
		toggleBtn.setOnCheckedChangeListener(this);
		if (icon != null) {
			convertView.findViewById(android.R.id.icon).setVisibility(
					View.VISIBLE);
		}

		if (textOff != null && textOff.length() > 0) {
			toggleBtn.setTextOff(textOff);
		}
		if (textOn != null && textOn.length() > 0) {
			toggleBtn.setTextOn(textOn);
		}
		toggleBtn.setSelected(defaultValue);
		return convertView;
	}

	/**
	 * set the toggle button
	 */
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		toggleBtn = (ToggleButton) view
				.findViewById(R.id.toggleBtnTogglePreference);
		toggleBtn.setChecked(defaultValue);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.onToggleChanged(isChecked ? textOn : textOff);
			listener.onToggleChanged(isChecked);
		}
	}

}
