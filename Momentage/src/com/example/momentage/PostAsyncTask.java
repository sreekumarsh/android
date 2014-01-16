package com.example.momentage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class PostAsyncTask extends AsyncTask<String, String, String> {
	private HashMap<String, String> mData = null;// post data

	private MainActivity activity;

	private int code;

	private ProgressDialog mProgress;

	/**
	 * constructor
	 */
	public PostAsyncTask(HashMap<String, String> data, MainActivity callback,
			int code) {
		mData = data;
		activity = callback;
		this.code = code;

	}

	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		ShowProgress();
	}


	/**
	 * background
	 */
	@Override
	protected String doInBackground(String... params) {
		byte[] result = null;
		String str = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(params[0]);// in this case, params[0] is
												// URL
		try {
			// set up post data
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			Iterator<String> it = mData.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
			HttpResponse response = client.execute(post);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toByteArray(response.getEntity());
				str = new String(result, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * on getting result
	 */
	@Override
	protected void onPostExecute(String result) {
		hideProgress();
		activity.onResponse(result, code);
	}

	private void ShowProgress() {
		mProgress = new ProgressDialog(activity);
		mProgress.setCancelable(false);
		mProgress.setMessage(activity.getString(R.string.loading));
		mProgress.show();
	}
	private void hideProgress(){
		mProgress.hide();
	}
	
}