package com.example.preference;

import java.util.Locale;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends PreferenceActivity implements
		TogglePreferenceChangedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		TogglePreference togglePreference = (TogglePreference) findPreference(getResources()
				.getString(R.string.selected_camera_key));
		togglePreference.setOnPreferrenceChangedListener(this);
	}

	@Override
	public void onToggleChanged(String text) {
		// TODO Auto-generated method stub
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onToggleChanged(boolean checked) {
		// TODO Auto-generated method stub

	}
}
