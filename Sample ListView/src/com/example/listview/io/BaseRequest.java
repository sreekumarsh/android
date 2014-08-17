package com.example.listview.io;

public class BaseRequest {
	private String url;
	private int requestCode;
	private HandleResponse callBackHandler;
	
	public BaseRequest(String url, int code, HandleResponse callBackHandler){
		this.url = url;
		this.requestCode = code;
		this.setCallBackHandler(callBackHandler);
	}
	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the requestCode
	 */
	public int getRequestCode() {
		return requestCode;
	}
	/**
	 * @param requestCode the requestCode to set
	 */
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}


	/**
	 * @return the callBackHandler
	 */
	public HandleResponse getCallBackHandler() {
		return callBackHandler;
	}


	/**
	 * @param callBackHandler the callBackHandler to set
	 */
	public void setCallBackHandler(HandleResponse callBackHandler) {
		this.callBackHandler = callBackHandler;
	}
	
}
