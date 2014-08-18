package com.example.listview.io;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.os.Handler;

import com.example.listview.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DataExchangerTask implements Runnable {

	private Handler handler;
	private BaseRequest request;
	private Class<?> respDataClass;

	public DataExchangerTask(BaseRequest request, Class<?> respDataClass,
			Handler handler) {
		this.request = request;
		this.handler = handler;
		this.respDataClass = respDataClass;
	}

	/**
	 * Send the response to UI thread
	 * 
	 * @param response
	 */
	private void doResultCallback(final BaseResponse response) {
		try {
			handler.post(new Runnable() {
				@Override
				public void run() {
					request.getCallBackHandler().handleResponse(response);
				}
			});

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BaseResponse responseData = new BaseResponse(request.getRequestCode());
		try {
			DataExchanger dataExchanger = new DataExchanger();
			String data = dataExchanger.exchangeStringGET(request.getUrl());
			Gson gson = new Gson();
			Object items = gson.fromJson(data, respDataClass);
			responseData.setStatusCode(Constants.HTTP_CODE_SUCCESS);
			responseData.setStatusMessage(Constants.HTTP_STATUS_SUCCESS);
			responseData.setData(items);
		} catch (ClientProtocolException e) {
			// connection failed
			responseData.setStatusCode(Constants.HTTP_CODE_NETWORK_ERROR);
			responseData.setStatusMessage(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// jsonparse exception
			responseData.setStatusCode(Constants.HTTP_CODE_SERVER_ERROR);
			responseData.setStatusMessage(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// connection failed
			responseData.setStatusCode(Constants.HTTP_CODE_NETWORK_ERROR);
			responseData.setStatusMessage(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			// unknown error
			responseData.setStatusCode(Constants.HTTP_CODE_UNKNOWN);
			responseData.setStatusMessage(e.getLocalizedMessage());
			e.printStackTrace();

		} catch (IllegalArgumentException e) {
			// unknown error
			responseData.setStatusCode(Constants.HTTP_CODE_UNKNOWN);
			responseData.setStatusMessage(e.getLocalizedMessage());
			e.printStackTrace();

		}
		doResultCallback(responseData);

	}

}
