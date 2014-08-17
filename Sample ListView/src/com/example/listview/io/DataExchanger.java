package com.example.listview.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DataExchanger {

	
	public String exchangeStringGET(String url)
			throws ClientProtocolException, Exception {

		InputStream inputStream = null;
		StringBuilder stringBuffer = new StringBuilder();
		DefaultHttpClient httpClient = null;
		httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		HttpResponse httpResponse = httpClient.execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new IOException(httpResponse.getStatusLine().getStatusCode()
					+ " : " + httpResponse.getStatusLine().getReasonPhrase());
		}
		HttpEntity httpEntity = httpResponse.getEntity();
		inputStream = httpEntity.getContent();
		if (inputStream == null)
			return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
				"UTF-8"), 8);

		String line = null;
		while ((line = reader.readLine()) != null) {
			stringBuffer.append(line + "\n");
		}
		inputStream.close();
		return stringBuffer.toString();
	}

}
