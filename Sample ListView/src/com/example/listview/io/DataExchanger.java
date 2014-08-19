package com.example.listview.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Defines various methods to exchange data with server; Eg: GET, POST etc
 * 
 * @author Sreekumar
 * 
 */
public class DataExchanger {

	/**
	 * Perform a GET request and returns the string response
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws Exception
	 */
	public String exchangeStringGET(String url) throws ClientProtocolException,
			IOException {

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
		Reader in = new BufferedReader(new InputStreamReader(inputStream,
				"ISO-8859-1"));
		int ch;
		while ((ch = in.read()) > -1) {
			stringBuffer.append((char) ch);
		}
		in.close();
		inputStream.close();
		return stringBuffer.toString();
	}

}
