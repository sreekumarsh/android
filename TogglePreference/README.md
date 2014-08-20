TogglePreference
================

Custom preference component with a toggle button, toggle button and text can be changed from the resource and code.

Usage in XML
------------

```
<com.example.preference.TogglePreference
            android:defaultValue="true"
            android:icon="@drawable/camera"
            android:key="@string/selected_camera_key"
            android:summary="@string/select_camera"
            android:textOn="@string/front"
            android:textOff="@string/rear"
            android:title="@string/camera" />
```

Usage in JAVA
-------------

```
addPreferencesFromResource(R.xml.preferences);
		TogglePreference togglePreference = (TogglePreference) findPreference(getResources()
				.getString(R.string.selected_camera_key));
togglePreference.setOnPreferrenceChangedListener(this);
```
<br><br>
<img style="float: center" src="http://sreekumar.sh/share/images/togglePreference.png" />
