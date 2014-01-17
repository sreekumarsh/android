package com.example.momentage.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.momentage.MainActivity;
import com.example.momentage.R;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

	Activity activity;
	
	@Before
	public void setup() {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
	}

	/**
	 * Check if the upload button is not visible to the users before login
	 * 
	 * @throws Exception
	 */
	@Test
	public void UploadButtonIsInvisible() throws Exception {
		Button uploadBtn = (Button) activity
				.findViewById(R.id.btnUpload);
		assertTrue(uploadBtn.getVisibility() == View.INVISIBLE);
	}
	
	/**
	 * Check if the photo list is not visible to the users before login
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListIsInvisible() throws Exception {
		ListView listView = (ListView) activity
				.findViewById(R.id.imgGallery);
		assertTrue(listView.getVisibility() == View.INVISIBLE);
	}
	
	/**
	 * Check for facebook login button is visible
	 * 
	 * @throws Exception
	 */
	@Test
	public void FacebookLoginVisible() throws Exception {
		Button facebookLogin = (Button) activity
				.findViewById(R.id.login_button);
		assertTrue(facebookLogin.getVisibility() == View.VISIBLE);
	}

}