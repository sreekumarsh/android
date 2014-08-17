package com.example.listview;

public class Constants {
	
	/**URL strings*/
	
	/**
	 * URL to get the feed
	 */
	public static final String FEED_URL = "http://sreekumar.sh/facts.json";
	
	/**Request codes*/
	
	
	/**
	 * Code for feed request
	 */
	public static final int FEED_REQUEST_CODE = 1;
	
	
	/**HTTP status codes */
	/**
	 * status code when request is success
	 */
	public static final int HTTP_CODE_SUCCESS = 200;
	
	
	/**
	 * status code when error due to network failure
	 */
	public static final int HTTP_CODE_NETWORK_ERROR = 404;
	
	
	/**
	 * status code when error due to server exception
	 */
	public static final int HTTP_CODE_SERVER_ERROR = 500;
	
	/**
	 * status code when error due to unknown reason
	 */
	public static final int HTTP_CODE_UNKNOWN = 0;
	
	/***
	 * HTTP Message success
	 */
	public static final String HTTP_STATUS_SUCCESS = "success";

}
