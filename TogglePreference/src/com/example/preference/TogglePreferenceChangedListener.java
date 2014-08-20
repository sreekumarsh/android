package com.example.preference;

public interface TogglePreferenceChangedListener {

	/**
	 * Calls when toggle button is clicked
	 * 
	 * @param text
	 */
	public void onToggleChanged(String text);

	/**
	 * Calls when toggle button is clicked
	 * 
	 * @param checked
	 */
	public void onToggleChanged(boolean checked);

}
