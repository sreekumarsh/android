package com.example.momentage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.momentage.models.Photo;
import com.example.momentage.models.PhotoResponse;
import com.example.momentage.util.Constants;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

public class MainActivity extends FragmentActivity implements Callback {

	private final String TAG = MainActivity.class.getName();

	private LoginButton loginButton;
	private GraphUser user;
	private UiLifecycleHelper uiHelper;
	private Button uploadBtn;
	private PendingAction pendingAction = PendingAction.NONE;
	private ListView imageList;
	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		uploadBtn = (Button) findViewById(R.id.btnUpload);
		imageList = (ListView) findViewById(R.id.imgGallery);
		
		uploadBtn.setVisibility(View.INVISIBLE);
		imageList.setVisibility(View.INVISIBLE);

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						MainActivity.this.user = user;
						Session session = Session.getActiveSession();
						if (session != null && session.isOpened()) {
							uploadBtn.setVisibility(View.VISIBLE);
							imageList.setVisibility(View.VISIBLE);
							HashMap<String, String> data = new HashMap<String, String>();
							data.put(Constants.USERNAME_KEY, user.getUsername());
							PostAsyncTask getPhotos = new PostAsyncTask(data,
									MainActivity.this,
									Constants.DOWNLOAD_HTTP_CODE);
							getPhotos.execute(Constants.DOWNLOAD_URL);
							
						} else {
							uploadBtn.setVisibility(View.INVISIBLE);
							imageList.setVisibility(View.INVISIBLE);
						}
					}
				});
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						Constants.BROWSE_CODE);
			}
		});
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
			Log.d(TAG, String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
			Log.d(TAG, "Success!");
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.BROWSE_CODE) {
			if (resultCode == RESULT_OK) {

				Uri _uri = data.getData();
				Cursor cursor = getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				final String imageFilePath = cursor.getString(0);
				cursor.close();
				Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 12, baos);
				byte[] byteArrayImage = baos.toByteArray();
				String encodedImage = Base64.encodeToString(byteArrayImage,
						Base64.DEFAULT);
				Photo photo = new Photo();
				photo.setData(encodedImage);
				((ImageAdapter) imageList.getAdapter()).addPhoto(photo);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(Constants.USERNAME_KEY, user.getUsername());
				params.put(Constants.PHOTO_KEY, user.getUsername() + "-"
						+ Constants.PHOTO_KEY);
				params.put(Constants.DATA_KEY, encodedImage);
				PostAsyncTask getPhotos = new PostAsyncTask(params,
						MainActivity.this, Constants.UPLOAD_HTTP_CODE);
				getPhotos.execute(Constants.UPLOAD_URL);
			}

		} else {
			uiHelper.onActivityResult(requestCode, resultCode, data,
					dialogCallback);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();

		// Call the 'activateApp' method to log an app event for use in
		// analytics and advertising reporting. Do so in
		// the onResume methods of the primary Activities that an app may be
		// launched into.
		AppEventsLogger.activateApp(this);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {

		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {

		}
	}

	private void populateGallery(String data) {
		PhotoResponse response = getResponse(data);
		if (response == null || response.getResult() == null
				|| response.getResult().getPhotos() == null) {
			ArrayList<Photo> photos = new ArrayList<Photo>();
			imageList.setAdapter(new ImageAdapter(this, photos));
			return;
		}

		imageList.setAdapter(new ImageAdapter(this, response.getResult()
				.getPhotos()));
	}

	private PhotoResponse getResponse(String data) {
		PhotoResponse response = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper
				.configure(
						DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);
		try {
			response = objectMapper.readValue(data, PhotoResponse.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public void onResponse(String data, int code) {
		// TODO Auto-generated method stub
		switch (code) {
		case Constants.UPLOAD_HTTP_CODE:
			Log.d(TAG, data);
			break;
		case Constants.DOWNLOAD_HTTP_CODE:
			Log.d(TAG, data);
			populateGallery(data);
			break;
		}

	}

}
