package com.example.listview.io;

public class BaseResponse {
	
	private String statusMessage;
	private int statusCode;
	private Object data;
	private int reqeustCode;
	
	public BaseResponse(int code){
		this.reqeustCode = code;
	}
	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}
	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param items the data to set
	 */
	public void setData(Object items) {
		this.data = items;
	}
	/**
	 * @return the reqeustCode
	 */
	public int getReqeustCode() {
		return reqeustCode;
	}
	/**
	 * @param reqeustCode the reqeustCode to set
	 */
	public void setReqeustCode(int reqeustCode) {
		this.reqeustCode = reqeustCode;
	}
	

}
